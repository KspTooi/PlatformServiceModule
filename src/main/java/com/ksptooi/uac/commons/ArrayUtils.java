package com.ksptooi.uac.commons;

public class ArrayUtils {



    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] param, T insert){

        Object[] retParam = new Object[param.length + 1];

        for (int i = 0; i < param.length; i++) {
            retParam[i] = param[i];
        }

        retParam[retParam.length-1] = insert;
        return (T[]) retParam;
    }

}
