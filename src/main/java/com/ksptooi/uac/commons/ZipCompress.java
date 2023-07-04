package com.ksptooi.uac.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    private Logger logger = LoggerFactory.getLogger(ZipCompress.class);

    private DecimalFormat decFmt = new DecimalFormat("0.0");

    private Path source = null;

    private long sourceSize = 0L;

    private long readSize = 0L;

    public ZipCompress(Path path){
        this.source = path;
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
                byte[] buffer = new byte[1024*1024];

                while (true){

                    int read = is.read(buffer);
                    readSize = readSize + read;

                    if(read < 1){
                        break;
                    }

                    zos.write(buffer,0,read);
                }

                is.close();
                zos.closeEntry();


                //logger.info("{}MB of {}MB",toMb(readSize),toMb(sourceSize));
            }


            CliProgressBar.updateProgressBar("进度",readSize/1024/1024,  sourceSize/1024/1024);
        }

    }

    private String toMb(long bytes){
        return decFmt.format((double) bytes/1024/1024);
    }


}
