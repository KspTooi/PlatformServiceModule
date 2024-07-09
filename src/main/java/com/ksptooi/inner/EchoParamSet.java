package com.ksptooi.inner;

import com.ksptooi.uac.core.annatatiotion.Param;

@ParamSet
public class EchoParamSet {

    @Require
    @Param("userName")
    private String name;

}
