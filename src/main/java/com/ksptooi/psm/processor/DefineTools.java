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



}
