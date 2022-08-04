package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.mapper.CommandMapper;
import com.ksptooi.asf.core.service.CommandService;
import com.ksptooi.asf.extendsbuildin.processor.ApplicationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommandOperateService {

    private final Logger logger = LoggerFactory.getLogger(ServiceFrame.class);

    @Inject
    private CommandMapper mapper;


    public void listAll(){

        List<Command> commandList = mapper.getCommandList(new Command());

        CommandLineTable clTable = new CommandLineTable();

        clTable.setHeaders("Name","Processor","metadata");
        clTable.setShowVerticalLines(true);

        commandList.forEach(item -> {
            clTable.addRow(item.getName(),item.getExecutorName(),"N/A");
        });

        clTable.print();

    }


}
