package com.ksptooi.asf.extendsbuildin.entities;

public class ApplicationData {

    private final String metaVersion = "V2";

    private String path;

    private String fileName;

    private Boolean isDirectory;

    private Long length;

    private String md5;

    public String getMetaVersion() {
        return metaVersion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


}
