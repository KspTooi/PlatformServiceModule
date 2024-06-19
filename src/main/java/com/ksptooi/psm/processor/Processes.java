package com.ksptooi.psm.processor;

import com.ksptooi.psm.processor.entity.Process;

public class Processes {

    public static boolean isFinished(Process p){
        if(p == null){
            return true;
        }
        return !p.getInstance().isAlive() || p.getStage() != Process.STAGE_RUNNING;
    }

}
