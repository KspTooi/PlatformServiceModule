package com.ksptooi.asf.core.cli;

import com.google.inject.Inject;
import com.ksptooi.asf.core.processor.ProcessorDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OperateCli implements CommandLine{


    private final Logger logger = LoggerFactory.getLogger(OperateCli.class);

    @Inject
    private ProcessorDispatcher scheduler;


    @Override
    public void run() {

        logger.info("Cli已就绪");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("@:");

        while (true){

            try {


                String commandString = br.readLine();

                if(commandString.replace(" ","").equals("")){
                    System.out.print("@:");
                    continue;
                }

                scheduler.publish(CommandParser.parse(commandString));
                System.out.print("@:");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void stop() {

    }

}
