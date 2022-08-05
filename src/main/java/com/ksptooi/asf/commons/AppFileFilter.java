package com.ksptooi.asf.commons;

import java.io.File;
import java.io.FileFilter;

public class AppFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {

        if(pathname.getName().endsWith(".exe")){
            return true;
        }

        if(pathname.getName().endsWith(".bat")){
            return true;
        }

        if(pathname.getName().endsWith(".txt")){
            return true;
        }

        if(pathname.getName().endsWith(".jpg")){
            return true;
        }

        if(pathname.getName().endsWith(".png")){
            return true;
        }

        if(pathname.getName().endsWith(".xlsx")){
            return true;
        }

        if(pathname.getName().endsWith(".xlsm")){
            return true;
        }

        if(pathname.getName().endsWith(".xls")){
            return true;
        }

        if(pathname.isDirectory()){
            return true;
        }

        return false;
    }

}
