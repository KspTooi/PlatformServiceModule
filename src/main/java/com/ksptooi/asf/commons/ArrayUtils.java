package com.ksptooi.asf.commons;

import com.ksptooi.asf.core.entities.CliParam;

public class ArrayUtils {



    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] param, T insert){

        T[] retParam = (T[]) new Object[param.length + 1];

        for (int i = 0; i < param.length; i++) {
            retParam[i] = param[i];
        }

        retParam[retParam.length-1] = insert;
        return retParam;
    }

}
