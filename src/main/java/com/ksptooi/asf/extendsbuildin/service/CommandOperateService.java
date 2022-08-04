package com.ksptooi.asf.extendsbuildin.service;

import com.google.inject.Inject;
import com.ksptooi.asf.ServiceFrame;
import com.ksptooi.asf.commons.CommandLineTable;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.mapper.CommandMapper;
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


    public void listAll(){

        List<Command> commandList = mapper.getCommandList(new Command());

/*        Comparator<Object> comparator = Collator.getInstance(Locale.US);

        commandList.sort((u1, u2) -> {
            return comparator.compare(u1.getName(), u2.getName());
        });*/

        CommandLineTable clTable = new CommandLineTable();

        clTable.setHeaders("Name","Processor","metadata");
        clTable.setShowVerticalLines(true);

        commandList.forEach(item -> {
            clTable.addRow(item.getName(),item.getExecutorName(),"N/A");
        });

        clTable.print();

    }


}
