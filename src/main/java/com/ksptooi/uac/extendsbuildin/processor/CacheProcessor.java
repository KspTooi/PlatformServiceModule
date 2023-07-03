package com.ksptooi.uac.extendsbuildin.processor;

import com.ksptooi.uac.core.annatatiotion.CommandMapping;
import com.ksptooi.uac.core.annatatiotion.Param;
import com.ksptooi.uac.core.annatatiotion.Processor;
import com.ksptooi.uac.core.entities.CliCommand;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.processor.ProcessorAdapter;

@Processor("CacheProcessor")
public class CacheProcessor extends ProcessorAdapter {

    @Override
    public String[] defaultCommand() {
        return new String[]{
                "cache",
                "cache list",
                "cache get"
        };
    }


    @CommandMapping("cache")
    public void cache(@Param("C1") String c1){
        System.out.println("参数为:"+c1);
    }




    @Override
    public void onCommand(CliCommand preparedCommand, Command command) {


    }

}
