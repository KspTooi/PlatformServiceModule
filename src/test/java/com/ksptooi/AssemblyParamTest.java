package com.ksptooi;

import com.ksptooi.psm.processor.AssemblingException;
import com.ksptooi.psm.processor.ServiceUnits;
import com.ksptooi.uac.core.annatatiotion.Param;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AssemblyParamTest {


    public static void main(String[] args) throws AssemblingException {

        var userParam = new HashMap<String, List<String>>();
        userParam.put("key1", List.of());
        userParam.put("key2", List.of("2"));

        for(var m : AssemblyParamTest.class.getMethods()){
            if(m.getName().equals("target")){

                //组装参数
                var objects = ServiceUnits.assemblyArgumentWithType(m, userParam);
                System.out.println(Arrays.toString(objects));

            }
        }

    }


    public void target(@Param("key1")boolean key,@Param("key2") Integer key2){
        System.out.println(key);
    }


}
