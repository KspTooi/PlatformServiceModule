package com.ksptooi.asf.core.processor;

import com.google.inject.Inject;
import com.ksptooi.asf.commons.ArrayUtils;
import com.ksptooi.asf.commons.ReflectUtils;
import com.ksptooi.asf.core.annatatiotion.CommandMapping;
import com.ksptooi.asf.core.annatatiotion.Param;
import com.ksptooi.asf.core.entities.CliCommand;
import com.ksptooi.asf.core.entities.CliParam;
import com.ksptooi.asf.core.entities.Command;
import com.ksptooi.asf.core.service.CommandService;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CmdAnnotationDispatcher extends CmdProcessRegisterWrapper{

    private final Logger logger = LoggerFactory.getLogger(ProcessorDispatcher.class);

    @Inject
    private CommandService service;


    //查找类上带有指定注解与value的方法
    private Method getMethodByCommandMapping(Class<?> clazz,String inCommand){

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

    private Object[] assemblyParams(Method method, Object[] innerParam, List<String> stringParam){

        if(stringParam==null){
            stringParam = new ArrayList<>();
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        //参数不足
        if((parameterTypes.length - innerParam.length) > stringParam.size()){
            return null;
        }

        Object[] params = new Object[method.getParameterCount()];

        int paramCount = 0;

        for (int i = 0; i < parameterTypes.length; i++) {

            boolean isInnerParam = false;

            //判断当前参数类型是否为innerParam中的类型
            for(Object item : innerParam){
                if(parameterTypes[i].isInstance(item)){
                    params[i] = item;
                    isInnerParam = true;
                }
            }

            if(!isInnerParam){
                params[i] = stringParam.get(paramCount);
                paramCount ++;
            }

        }

        return params;
    }

    private String[] getParamNameByAnnotation(Method method){

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        List<String> retString = new ArrayList<>();

        for (Annotation[] anno : parameterAnnotations){

            for(Annotation item:anno){

                if(item instanceof Param){
                    retString.add(((Param) item).value());
                }

            }

        }

        return retString.toArray(new String[0]);
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
        Object[] params = this.assemblyParams(targetMethod, new Object[]{inVo, commandByName}, inVo.getParameter());

        if(params==null){
            logger.info("参数不足>{}", Arrays.toString(this.getParamNameByAnnotation(targetMethod)));
            return;
        }

/*
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
*/

        try {

            targetMethod.invoke(processor,params);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
