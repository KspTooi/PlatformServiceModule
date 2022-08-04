package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.mapper.CommandMapper;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CommandOperateService {

    private final Logger logger = LoggerFactory.getLogger(ServiceFrame.class);

    @Inject
    private CommandMapper mapper;

    @Inject
    private ProcessorDispatcher processorDispatcher;


    public void listAll(){

        List<Command> commandList = mapper.getCommandList(new Command());

/*        Comparator<Object> comparator = Collator.getInstance(Locale.US);

        commandList.sort((u1, u2) -> {
            return comparator.compare(u1.getName(), u2.getName());
        });*/

        CommandLineTable clTable = new CommandLineTable();

        clTable.setHeaders("Name","Processor","description","metadata");
        clTable.setShowVerticalLines(true);

        commandList.forEach(item -> {
            clTable.addRow(item.getName(),item.getExecutorName(),item.getDescription(),"N/A");
        });

        clTable.print();
    }

    public void refreshCommand(){

        //获取所有已注册的处理器
        List<String> registeredProcessor = processorDispatcher.getRegisteredProcessor();

        //获取所有已注册的命令
        List<Command> commandList = mapper.getCommandList(new Command());

        for(Command item : commandList){

            boolean existsProcessor = false;

            for(String procName : registeredProcessor){

                if(procName.equals(item.getProcessorName())){
                    existsProcessor = true;
                    break;
                }

            }

            if(!existsProcessor){
                logger.info("清除无效的指令:{}->{}",item.getName(),item.getProcessorName());
                mapper.removeById(item.getCmdId()+"");
            }

        }

        logger.info("刷新指令数据完成");

    }


}
