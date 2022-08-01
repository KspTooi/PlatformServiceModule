package com.ksptooi.asf.core.command;

import com.ksptooi.asf.core.annatatiotion.Component;
import com.ksptooi.asf.core.entities.PreparedCommand;

import java.util.ArrayList;
import java.util.List;

@Component
public class InnerCommandParser implements CommandParser{


    @Override
    public PreparedCommand parse(String inCommand) {

        PreparedCommand pCommand = new PreparedCommand();

        String commandString = null;

        //预处理
        commandString = inCommand.trim();


        //解析参数
        String[] params = commandString.split(">");

        //无参数
        if(params.length <= 1){
            pCommand.setName(commandString);
            pCommand.setParameter(new ArrayList<>());
            return pCommand;
        }

        List<String> paramList = new ArrayList<>();

        //有参数
        String param = params[1];

        String[] split = param.split(",");

        for(String item:split){

            if(item.trim().equals("")){
                continue;
            }

            paramList.add(item.trim());
        }

        pCommand.setName(params[0]);
        pCommand.setParameter(paramList);
        return pCommand;
    }

}
