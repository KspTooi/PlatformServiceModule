package com.ksptooi.asf.core.entities;

import java.math.BigInteger;
import java.sql.Blob;
import java.util.Arrays;
import java.util.Date;

public class Document {

    private Long docId;

    private String name;

    private String domType;

    private String metadata;

    private byte[] binaryData;

    private String description;

    private Date createTime;


    public void appendBinaryData(byte[] binary,int length){

        byte[] target = null;

        if(this.binaryData != null){
            target = new byte[this.binaryData.length + length];
            System.arraycopy(binaryData, 0, target, 0, this.binaryData.length);
            System.arraycopy(binary, 0, target, this.binaryData.length, length);
            this.binaryData = target;
        }

        if(this.binaryData == null){
            target = new byte[length];
            System.arraycopy(binary, 0, target, 0, length);

            this.binaryData = target;
        }

    }



    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDomType() {
        return domType;
    }

    public void setDomType(String domType) {
        this.domType = domType;
    }
}
