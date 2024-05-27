package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.ProcDefine;

import java.util.List;

public class DefineTools {

    public static ProcDefine getHook(String hookName, List<ProcDefine> defines){
        for(ProcDefine def : defines){
            if(def.getDefType().equals(hookName)){
                return def;
            }
        }
        return null;
    }

    /**
     * 根据pattern和参数数量查找define
     */
    public static ProcDefine getDefine(String pattern,int paramsCount,List<ProcDefine> defines){
        for(ProcDefine def : defines){
            if(def.getDefType().equals(ProcDefType.REQ_HANDLER)){
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
    public static ProcDefine getDefaultDefine(List<ProcDefine> defines){
        for(ProcDefine def : defines){
            if(def.getDefType().equals(ProcDefType.REQ_HANDLER)){
                if(def.getPattern().equals("*") && def.getParamCount() == 0){
                    return def;
                }
            }
        }
        return null;
    }



}
