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

        //拿到该Class里面带注解的方法列表
        Method[] methodByAnnotation = ReflectUtils.getMethodByAnnotation(clazz, CommandMapping.class);

        if(methodByAnnotation.length < 1){
            return null;
        }

        //遍历class的方法
        for(Method item:methodByAnnotation){

            //拿到方法上CommandMapping注解的值
            String[] values = item.getAnnotation(CommandMapping.class).value();

            //拿入参匹配注解上的值
            for(String value : values){
                if(value.equals(inCommand)){
                    return item;
                }
            }

        }

        return null;
    }

    /**
     * 组装方法参数
     * @param method 需要组装参数的方法
     * @param innerParam 需要注入的内部组件
     * @param stringParam 外部入参
     * @return 返回已组装好的参数组
     */
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

            try{
                if(!isInnerParam){
                    params[i] = stringParam.get(paramCount);
                    paramCount ++;
                }
            }catch (IndexOutOfBoundsException e){
                return null;
            }

        }

        return params;
    }

    /**
     * 获取该方法上的所有全部注解参数名
     * @param method
     * @return
     */
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


    /**
     * 命令调度推送命令
     * @param inVo
     */
    @Override
    public void publish(CliCommand inVo) {

        //获取已注册的处理器组
        Map<String, Processor> processorMap = this.getProcessorMap();

        //查询数据库命令
        Command commandByName = service.getCommandByName(inVo.getName());

        if(commandByName == null){
            super.publish(inVo);
            return;
        }

        //根据数据库中处理器名称获取当前注册的处理器
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
            logger.info("指令参数不足>{}", Arrays.toString(this.getParamNameByAnnotation(targetMethod)));
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

            //反射注入方法入参
            targetMethod.invoke(processor,params);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
