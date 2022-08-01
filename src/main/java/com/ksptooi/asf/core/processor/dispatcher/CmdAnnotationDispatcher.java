package com.ksptooi.asf.core.processor.dispatcher;

import com.google.inject.Inject;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.processor.Processor;
import com.ksptooi.asf.core.service.CommandService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CmdAnnotationDispatcher extends CmdProcessRegisterWrapper{

    @Inject
    private CommandService service;

    @Override
    public void publish(CliCommand inVo) {

        HashMap<String, Processor> processorMap = this.getProcessorMap();

        Command commandByName = service.getCommandByName(inVo.getName());

        if(commandByName == null){
            super.publish(inVo);
            return;
        }

        Processor processor = processorMap.get(commandByName.getExecutorName());

        if(processor==null){
            super.publish(inVo);
            return;
        }

        //获取类上所有方法
        Method[] methods = processor.getClass().getMethods();

        Method targetMethod = null;

        //查找带有注解的方法
        for(Method item:methods){

            CommandMapping annotation = item.getAnnotation(CommandMapping.class);

            if(annotation == null){
                continue;
            }
            if(annotation.value().equals(inVo.getName())){
                targetMethod = item;
                break;
            }

        }

        //类上没有带有注解的方法
        if(targetMethod == null){
            super.publish(inVo);
            return;
        }

        //类上存在带有注解的方法 准备参数
        Object[] params = new Object[targetMethod.getParameterCount()];

        Class<?>[] paramsTypes = targetMethod.getParameterTypes();

        for (int i = 0; i < paramsTypes.length; i++) {

            if(paramsTypes[i].isInstance(inVo)){
                params[i] = inVo;
                continue;
            }
            if(paramsTypes[i].isInstance(commandByName)){
                params[i] = commandByName;
                continue;
            }

            params[i] = null;
        }

        try {

            targetMethod.invoke(processor,params);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
