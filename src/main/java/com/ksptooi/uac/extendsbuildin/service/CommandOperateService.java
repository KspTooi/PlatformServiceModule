package com.ksptooi.uac.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.uac.ApplicationOld;
import com.ksptooi.uac.commons.CommandLineTable;
import com.ksptooi.uac.core.entities.Command;
import com.ksptooi.uac.core.mapper.CommandMapper;
import com.ksptooi.uac.core.processor.ProcessorDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommandOperateService {

    private final Logger logger = LoggerFactory.getLogger(ApplicationOld.class);

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

        clTable.setHeaders("Name","Processor","description","metadata","createTime");
        clTable.setShowVerticalLines(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        commandList.forEach(item -> {
            clTable.addRow(item.getName(),item.getExecutorName(),item.getDescription(),"N/A",sdf.format(item.getCreateTime()));
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
