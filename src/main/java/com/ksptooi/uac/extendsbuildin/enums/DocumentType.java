package com.ksptooi.uac.extendsbuildin.enums;

public enum DocumentType {

    APP_LIB("document_app_lib"),
    APP_ARCHIVE("app_archive");


    private String name;

    DocumentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
