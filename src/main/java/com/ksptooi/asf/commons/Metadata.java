package com.ksptooi.asf.commons;

import com.alibaba.fastjson.JSON;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.extendsbuildin.entities.ApplicationData;
import org.checkerframework.checker.units.qual.A;

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
