package com.ksptooi.asf.core.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.commons.ArrayUtils;
import com.ksptooi.asf.commons.ReflectUtils;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.service.CommandService;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CmdAnnotationDispatcher extends CmdProcessRegisterWrapper{

    @Inject
    private CommandService service;


    //查找类上带有指定注解与value的方法
    public Method getMethodByCommandMapping(Class<?> clazz,String inCommand){

        Method[] methodByAnnotation = ReflectUtils.getMethodByAnnotation(clazz, CommandMapping.class);

        if(methodByAnnotation.length < 1){
            return null;
        }

        for(Method item:methodByAnnotation){

            String[] values = item.getAnnotation(CommandMapping.class).value();

            for(String value : values){
                if(value.equals(inCommand)){
                    return item;
                }
            }

        }

        return null;
    }


    @Override
    public void publish(CliCommand inVo) {

        Map<String, Processor> processorMap = this.getProcessorMap();

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

        //获取类上带注解的方法
        Method targetMethod = this.getMethodByCommandMapping(processor.getClass(), inVo.getName());

        
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
