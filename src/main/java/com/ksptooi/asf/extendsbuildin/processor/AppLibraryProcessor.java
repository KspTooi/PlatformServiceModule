package com.ksptooi.asf.extendsbuildin.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Processor;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.processor.AbstractProcessor;
import com.ksptooi.asf.extendsbuildin.service.AppLibraryService;
import com.ksptooi.asf.extendsbuildin.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Processor("build-in-AppLibraryProcessor")
public class AppLibraryProcessor extends AbstractProcessor {


    private final Logger logger = LoggerFactory.getLogger(AppLibraryProcessor.class);

    @Inject
    private AppLibraryService service;


    @Override
    public String[] defaultCommand() {
        return new String[]
                {
                 "lib add",
                 "lib a",
                 "lib remove",
                 "lib rm",
                 "lib scan",
                 "lib s",
                 "lib fix",
                 "lib f",
                };
    }

    @CommandMapping({"lib add","lib a"})
    public void addAppLibrary(){

    }

    @CommandMapping({"lib remove","lib rm"})
    public void removeAppLibrary(){

    }

    @CommandMapping({"lib scan","lib s"})
    public void scanAppLibrary(){

    }

    @CommandMapping({"lib fix","lib f"})
    public void fixAppLibrary(){

    }


    @Override
    public void onCommand(CliCommand pCommand, Command command) {
    }




}
