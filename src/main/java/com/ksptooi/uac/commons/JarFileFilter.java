package com.ksptooi.uac.commons;

import java.io.File;
import java.io.FileFilter;

public class JarFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {

        if(pathname.getName().endsWith(".jar")){
            return true;
        }

        return false;
    }

}
