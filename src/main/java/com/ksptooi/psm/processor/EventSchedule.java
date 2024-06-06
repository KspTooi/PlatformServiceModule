package com.ksptooi.psm.processor;

import com.ksptooi.guice.annotations.Unit;
import com.ksptooi.psm.processor.entity.ActiveProcessor;
import com.ksptooi.psm.processor.entity.ProcDefine;
import com.ksptooi.psm.processor.event.generic.ProcEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Unit
public class EventSchedule {

    private static final Logger log = LoggerFactory.getLogger(EventSchedule.class);

    private final Map<String, List<ProcDefine>> eventMap = new ConcurrentHashMap<>();

    /**
     * 向事件调度注册一个事件
     */
    public void register(ProcDefine def){

        if(def == null){
            log.error("无法注册事件处理器,内部错误(E1)");
            return;
        }
        if(!def.getDefType().equals(ProcDefType.EVENT_HANDLER)){
            log.error("无法注册事件处理器,Define类型错误. 位于Proc:{}.{}",def.getProcName(),def.getMethod().getName());
            return;
        }

        List<ProcDefine> emList = eventMap.computeIfAbsent(def.getEventHandlerType(), k -> new ArrayList<>());
        emList.add(def);
        Collections.sort(emList);
        log.info("注册事件处理器 {}:{}({})..{}",def.getProcName(),def.getEventName(),def.getEventHandlerOrder(),def.getMethod().getName());
    }

    /**
     * 向调度器发布一个事件
     */
    public ProcEvent forward(ProcEvent event){

        var eventType = event.getClass().getName();

        if(! eventMap.containsKey(eventType)){
            return null;
        }

        var defines = eventMap.get(eventType);

        for(var def : defines){

            try {

                //获得处理器实例
                var processor = ProcessorManager.getProcessor(def.getProcName());
                def.getMethod().invoke(processor.getProc(),event);

                if(event.isIntercepted()){
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.warn("执行事件时出现错误. 处理器:{} 事件名:{} 事件处理器:{}",def.getProcName(),def.getEventName(),def.getMethod().getName());
                continue;
            }

        }

        return event;
    }


}
