package com.ksptooi.uac;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFolderExample {
    public static void main(String[] args) {

        String sourceFolder = "E:\\Services";
        String zipFile = "E:\\zipfile.zip";
        
        try {
            zipFolder(sourceFolder, zipFile);
            System.out.println("文件夹已成功压缩为ZIP文件！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void zipFolder(String sourceFolder, String zipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        
        addFolderToZip(sourceFolder, sourceFolder, zos);
        
        zos.close();
        fos.close();
    }
    
    private static void addFolderToZip(String folderPath, String sourceFolder, ZipOutputStream zos) throws IOException {
        File folder = new File(folderPath);
        
        for (String fileName : folder.list()) {
            String filePath = folderPath + File.separator + fileName;
            if (new File(filePath).isDirectory()) {
                addFolderToZip(filePath, sourceFolder, zos);
            } else {
                String entryPath = filePath.replace(sourceFolder + File.separator, "");
                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);
                
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024*1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                
                fis.close();
                zos.closeEntry();
            }
        }
    }
}
