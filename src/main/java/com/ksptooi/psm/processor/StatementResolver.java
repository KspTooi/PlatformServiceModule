package com.ksptooi.psm.processor;

import java.util.ArrayList;
import java.util.List;

public class StatementResolver {


    public static void main(String[] args) {

        var sr = new StatementResolver();

        var reqSeqStyle = new ShellRequest("echo>p1,p2,p3");
        var reqGNUStyle = new ShellRequest("echo -param1=p1 -param2=p2 -param3=p3");

        sr.resolve(reqSeqStyle);
        sr.resolve(reqGNUStyle);

        System.out.println(reqSeqStyle);
        System.out.println(reqGNUStyle);
    }



    public void resolve(ShellRequest request){

        var statement = request.getStatement();



    }

    public void resolveAsSequentialStyle(ShellRequest req){

        String statement = req.getStatement();
        String requestName = null;

        //预处理
        requestName = statement.trim();

        //解析参数
        String[] params = statement.split(">");

        //无参数
        if(params.length <= 1){
            req.setPattern(requestName);
            req.setParams(new ArrayList<>());
            return;
        }

        //有参数
        List<String> paramList = new ArrayList<>();
        String param = params[1];

        String[] split = param.split(",");

        for(String item:split){

            if(item.trim().equals("")){
                continue;
            }

            paramList.add(item.trim());
        }

        req.setPattern(params[0]);
        req.setParams(paramList);
    }
}
