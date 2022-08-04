package com.ksptooi.asf.core.cli;

import com.ksptooi.asf.core.entities.CliCommandDefine;
import com.ksptooi.asf.core.entities.CliParam;

public class CliBuilder {

    private final CliCommandDefine define;

    public static CliBuilder newDefine(String name){
        return new CliBuilder(new CliCommandDefine(name));
    }

    private CliBuilder(CliCommandDefine cliCommandDefine){
        this.define = cliCommandDefine;
    }

    public CliBuilder withParam(String name){
        return this.withParam(name,false);
    }

    public CliBuilder withParam(String name,String desc){
        return this.withParam(name,desc,false);
    }

    public CliBuilder withParam(String name,boolean require){
        return this.withParam(name,null,require);
    }

    public CliBuilder withParam(String name,boolean require,String desc){
        return this.withParam(name,desc,require);
    }

    public CliBuilder withParam(String name, String desc,boolean require){

        CliParam[] params = this.define.getParams();

        CliParam[] extend = null;

        if(desc==null){
            extend = this.extend(params, new CliParam(name,require));
        }
        if(desc!=null){
            extend = this.extend(params, new CliParam(name,desc,require));
        }

        this.define.setParams(extend);

        return this;
    }

    public CliCommandDefine build(){
        return this.define;
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
