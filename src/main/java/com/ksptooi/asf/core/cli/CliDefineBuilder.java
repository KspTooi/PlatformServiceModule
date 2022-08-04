package com.ksptooi.asf.core.cli;

import com.ksptooi.asf.core.entities.CliCommandDefine;
import com.ksptooi.asf.core.entities.CliParam;

public class CliDefineBuilder {

    private CliCommandDefine define;

    public CliDefineBuilder newDefine(String name){
        this.define = new CliCommandDefine(name);;
        return this;
    }

    public CliDefineBuilder withParam(String name, String desc){

        CliParam[] params = this.define.getParams();

        CliParam[] extend = this.extend(params, new CliParam(name, desc));

        this.define.setParams(extend);

        return this;
    }




    private CliParam[] extend(CliParam[] param,CliParam insert){

        CliParam[] retParam = new CliParam[param.length + 1];

        for (int i = 0; i < param.length; i++) {
            retParam[i] = param[i];
        }

        retParam[retParam.length-1] = insert;
        return retParam;
    }


}
