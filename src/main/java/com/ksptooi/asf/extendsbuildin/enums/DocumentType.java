package com.ksptooi.asf.extendsbuildin.enums;

public enum DocumentType {

    APP_LIB("document_app_lib");


    private String name;

    DocumentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
