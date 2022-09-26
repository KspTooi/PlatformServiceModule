package com.ksptooi.uac.commons;

import com.alibaba.fastjson.JSON;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.extendsbuildin.entities.ApplicationData;

public class Metadata {

    private Metadata(){

    }


    public static ApplicationData asAppdata(Command command){

        String metadata = command.getMetadata();

        try{
            return JSON.parseObject(metadata, ApplicationData.class);
        }catch (Exception e){
            return new ApplicationData();
        }

    }


}
