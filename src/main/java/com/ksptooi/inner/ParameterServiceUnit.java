package com.ksptooi.inner;

import com.ksptooi.psm.processor.*;
import com.ksptooi.psm.utils.RefTools;
import com.ksptooi.psm.utils.aio.ConnectMode;
import com.ksptooi.psm.utils.aio.color.GreenDye;
import com.ksptooi.uac.core.annatatiotion.Param;

import java.util.List;

@ServiceUnit("bundled::test::ParameterServiceUnit")
public class ParameterServiceUnit {


    @OnActivated
    public void activated(){

    }

    public static void main(String[] args) throws Exception {

        var clazz = ParameterServiceUnit.class;
        var instance = clazz.getConstructor().newInstance();

        for(var item : clazz.getMethods()){
            if(item.getName().equals("echo1")){
                var parameterType = RefTools.getParameterActualType(item,1);
                System.out.println(parameterType.getFirst().equals(Integer.class));
            }
        }
    }

    @RequestHandler("echoSet")
    public List<String> echoSet(ShellRequest req, @ParamSet EchoParamSet set){
        //String s = names.get(0);
        System.out.println(set);
        return null;
    }

    @RequestHandler("echo1")
    public List<String> echo1(ShellRequest req, @Param("names")boolean names){
        //String s = names.get(0);
        System.out.println(names);
        return null;
    }


    @RequestHandler("echo")
    public void echo(ShellRequest request,@Param("fileName") String fileName,@Param("size") Double size){
        var cable = request.getCable().connect(ConnectMode.OUTPUT).dye(GreenDye.pickUp);
        cable.w("输入参数 ").w("FileName:").w(fileName);
        cable.w(" Size:").w(size);
    }

}
