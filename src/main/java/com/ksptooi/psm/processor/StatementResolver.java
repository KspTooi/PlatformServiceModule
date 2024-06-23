package com.ksptooi.psm.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementResolver {


    public static void main(String[] args) throws StatementParsingException {

        var sr = new StatementResolver();

        //var reqSeqStyle = new ShellRequest("echo>p1,p2,p3");
        var reqGNUStyle = new ShellRequest("echo -abc");

        //sr.resolve(reqSeqStyle);
        sr.resolve(reqGNUStyle);

        //System.out.println(reqSeqStyle);
        //System.out.println(reqGNUStyle);
    }



    public void resolve(ShellRequest request) throws StatementParsingException{

        var statement = request.getStatement();

        if(statement.contains(">")){
            resolveAsSequentialStyle(request);
            return;
        }

        var parameters = new HashMap<String,String>();

        var cs = statement.toCharArray();

        var pattern = new StringBuilder();
        var pName = new StringBuilder();
        var pVal = new StringBuilder();

        //echo -param1=p1 -param2=p2 -param3=p3
        //状态机代码 0:pattern 1:paramName 2:val 3:val_separator
        var state = 0;

        for(var i = 0; i<cs.length; i++){

            var cur = cs[i];

            if(cur == '-'){
                if(pattern.isEmpty()){
                    throw new StatementParsingException("No Pattern Input",statement,i);
                }
                if(!pName.isEmpty() || !pVal.isEmpty()){
                    parameters.put(pName.toString().trim(), pVal.toString().trim());
                    pName.setLength(0);
                    pVal.setLength(0);
                }
                state = 1;
                continue;
            }
            if(cur == '='){
                if(state != 1 || pName.isEmpty()){
                    throw new StatementParsingException("[value symbol start] was found but without parameter name",statement,i);
                }
                state = 2;
                continue;
            }
            if(state == 0){
                if(cur != ' '){
                    pattern.append(cur);
                    continue;
                }
            }
            if(state == 1){
                if(cur != ' '){
                    pName.append(cur);
                    continue;
                }
            }
            if(state == 2){
                pVal.append(cur);
            }

        }

        if(state == 1 && pName.isEmpty()){
            throw new StatementParsingException("[param symbol start] was found but without parameter name",statement,cs.length);
        }

        parameters.put(pName.toString(),pVal.toString());

        System.out.println(parameters);
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
