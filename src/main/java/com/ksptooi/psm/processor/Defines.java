package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.ActivatedSrvUnit;
import com.ksptooi.psm.processor.entity.SrvDefine;
import com.ksptooi.psm.processor.event.generic.ServiceUnitEvent;

import java.util.ArrayList;
import java.util.List;

public class Defines {


    //查找处理器内部的*非全局*可用事件处理器
    public static void findAvailableEventHandler(ServiceUnitEvent event, List<SrvDefine> defines, List<SrvDefine> handler){
        for(var def : defines){
            if(!def.getDefType().equals(SrvDefType.EVENT_HANDLER)){
                continue;
            }
            if(def.isGlobalEventHandler()){
                continue;
            }
            if(def.getEventHandlerType().equals(event.getClass().getName())){
                handler.add(def);
            }
        }
    }

    public static SrvDefine getHook(String hookName, List<SrvDefine> defines){
        for(SrvDefine def : defines){
            if(def.getDefType().equals(hookName)){
                return def;
            }
        }
        return null;
    }


    /**
     * 根据pattern和参数数量查找define
     */
    public static List<SrvDefine> getDefines(String pattern, int paramsCount, ActivatedSrvUnit unit){
        var ret = new ArrayList<SrvDefine>();
        for(SrvDefine def : unit.getSrvDefines()){
            if(def.getDefType().equals(SrvDefType.REQ_HANDLER)){
                if(def.getPattern().equals(pattern) && def.getParamCount() == paramsCount){
                    ret.add(def);
                }
            }
        }
        return ret;
    }


    /**
     * 根据pattern和参数数量查找define
     */
    public static SrvDefine getDefine(String pattern, int paramsCount, List<SrvDefine> defines){
        for(SrvDefine def : defines){
            if(def.getDefType().equals(SrvDefType.REQ_HANDLER)){
                if(def.getPattern().equals(pattern) && def.getParamCount() == paramsCount){
                    return def;
                }
            }
        }
        return null;
    }

    /**
     * 查找具有通配符*的Define
     */
    public static SrvDefine getDefaultDefine(List<SrvDefine> defines){
        for(SrvDefine def : defines){
            if(def.getDefType().equals(SrvDefType.REQ_HANDLER)){
                if(def.getPattern().equals("*") && def.getParamCount() == 0){
                    return def;
                }
            }
        }
        return null;
    }



}
