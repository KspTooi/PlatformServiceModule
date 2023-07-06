package com.ksptooi.uac.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    private Logger logger = LoggerFactory.getLogger(ZipCompress.class);

    private DecimalFormat decFmt = new DecimalFormat("0.0");

    private Path source = null;

    private long sourceSize = 0L;

    private long readSize = 0L;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final AtomicBoolean streamIsReady = new AtomicBoolean(false);

    private final int buffSize = (1024*512);

    public ZipCompress(Path path){
        this.source = path;
    }


    public InputStream getInputStream(){

        if(!Files.isDirectory(source)){
            throw new RuntimeException("打包操作失败,目标不是文件夹! source:"+source.toString());
        }

        sourceSize = FileUtils.sizeOfDirectory(source.toFile());

        PipedInputStream pis = new PipedInputStream(buffSize);

        executorService.submit(()->{

            try {

                PipedOutputStream os = new PipedOutputStream(pis);
                ZipOutputStream zos = new ZipOutputStream(os);

                this.addFolderToZip(source,source,zos);
                System.out.print("\r\n");

                zos.close();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件夹转换为二进制流失败!");
            }

        });

        while (true){
            if(streamIsReady.get()){
                break;
            }
        }

        return pis;
    }

    public byte[] compress(){

        if(!Files.isDirectory(source)){
            throw new RuntimeException("打包操作失败,目标不是文件夹! source:"+source.toString());
        }

        sourceSize = FileUtils.sizeOfDirectory(source.toFile());

        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(os);

            this.addFolderToZip(source,source,zos);
            System.out.print("\r\n");

            zos.close();
            os.close();
            return os.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件夹转换为二进制流失败!");
        }

    }

    //将文件夹递归压缩为二进制流
    private void addFolderToZip(Path path,Path sourceFolder, ZipOutputStream zos) throws IOException {

        File folder = path.toFile();
        File[] subFolder = folder.listFiles();

        if(subFolder == null){
            return;
        }

        for(File item : subFolder){

            String filePath = path + File.separator + item.getName();

            if(item.isDirectory()){
                addFolderToZip(Paths.get(filePath),sourceFolder,zos);
            }

            if(!item.isDirectory()){

                String entryPath = filePath.replace(sourceFolder + File.separator, "");

                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);
                FileInputStream is = new FileInputStream(item);
                byte[] buffer = new byte[buffSize];

                while (true){

                    int read = is.read(buffer);
                    readSize = readSize + read;

                    if(read < 1){
                        break;
                    }

                    zos.write(buffer,0,read);

                    //将流置为准备状态
                    if(!this.streamIsReady.get()){
                        this.streamIsReady.set(true);
                    }

                    CliProgressBar.updateProgressBar("处理中",readSize/1024/1024,  sourceSize/1024/1024);

                }

                is.close();
                zos.closeEntry();

                //logger.info("{}MB of {}MB",toMb(readSize),toMb(sourceSize));
            }


        }

    }

    private String toMb(long bytes){
        return decFmt.format((double) bytes/1024/1024);
    }


}
