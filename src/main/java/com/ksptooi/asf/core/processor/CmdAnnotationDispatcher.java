package com.ksptooi.asf.core.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.commons.ArrayUtils;
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
    public Method[] getMethodByAnnotation(Class<?> clazz,Class<? extends Annotation> inAnno,String value){

        Method[] methods = clazz.getMethods();

        Method[] retMethod = new Method[0];

        for(Method item:methods){

            Annotation annotation = item.getAnnotation(inAnno);

            if(annotation!=null){
                retMethod = ArrayUtils.append(retMethod,item);
            }

        }

        return retMethod;
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

        //获取类上所有方法
        Method[] methods = processor.getClass().getMethods();

        Method targetMethod = null;

        //查找带有注解的方法
        for(Method item:methods){

            CommandMapping annotation = item.getAnnotation(CommandMapping.class);

            if(annotation == null){
                continue;
            }

            String[] value = annotation.value();

            boolean hasTargetMethod = false;

            for(String valueItem:value){

                if(valueItem.equals(inVo.getName())){
                    targetMethod = item;
                    hasTargetMethod = true;
                    break;
                }

            }

            if(hasTargetMethod){
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
