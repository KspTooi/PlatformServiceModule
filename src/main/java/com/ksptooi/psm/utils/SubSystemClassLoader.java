package com.ksptooi.psm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Objects;

public class SubSystemClassLoader extends URLClassLoader {

    public SubSystemClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public SubSystemClassLoader(URL[] urls) {
        super(urls);
    }

    public SubSystemClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public SubSystemClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(name, urls, parent);
    }

    public SubSystemClassLoader(String name, URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(name, urls, parent, factory);
    }

    @Override
    public InputStream getResourceAsStream(String name) {

        var resource = getResource(name);

        if(resource == null){
            //throw new RuntimeException("public InputStream getResourceAsStream(String name)");
            return null;
        }

        try {
            return resource.openStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public URL getResource(String name) {
        Objects.requireNonNull(name);
        return findResource(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
