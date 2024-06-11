package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.SrvDefine;

import java.util.List;

public class DefineTools {

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
