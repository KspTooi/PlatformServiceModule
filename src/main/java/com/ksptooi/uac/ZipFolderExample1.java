package com.ksptooi.uac;

import com.ksptooi.uac.commons.ZipCompress;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFolderExample1 {
    public static void main(String[] args) throws IOException {

        String sourceFolder = "E:\\Services\\";
        String zipFile = "E:\\zipfile.zip";


        ZipCompress zip = new ZipCompress(Paths.get(sourceFolder));

        byte[] compress = zip.compress();

        //File file = new File(zipFile);
        //file.createNewFile();
        //OutputStream os = Files.newOutputStream(file.toPath());

        //os.write(compress);
        //os.close();

    }
    

}
