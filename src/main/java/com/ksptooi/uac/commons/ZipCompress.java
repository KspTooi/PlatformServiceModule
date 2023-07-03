package com.ksptooi.uac.commons;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    private Path source = null;

    public ZipCompress(Path path){
        this.source = path;
    }

    public byte[] compress(){

        if(!Files.isDirectory(source)){
            throw new RuntimeException("执行压缩失败,目标不是文件夹! source:"+source.toString());
        }

        //ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            new File("E:\\zipfile.zip").createNewFile();

            OutputStream os = Files.newOutputStream(Paths.get("E:\\zipfile.zip"));
            ZipOutputStream zos = new ZipOutputStream(os);

            this.addFolderToZip(source,source,zos);
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("压缩失败!");
        }

    }

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

                System.out.println(entryPath);

                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);

                FileInputStream is = new FileInputStream(item);
                byte[] buffer = new byte[1024*1024];

                while (true){

                    int read = is.read(buffer);

                    if(read < 1){
                        break;
                    }

                    zos.write(buffer,0,read);
                }

                is.close();
                zos.closeEntry();
            }

        }

    }


}
