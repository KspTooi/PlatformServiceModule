package com.ksptooi.inner;

import com.ksptooi.psm.processor.*;
import com.ksptooi.uac.core.annatatiotion.Param;

@ParamSet
public class EchoParamSet {

    @Require
    @Param("userName")
    @DefaultVal("account")
    @Hint("?")
    @Introduction("用户账户")
    private String account;

}
