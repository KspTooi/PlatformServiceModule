package com.ksptooi.psm.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementResolver {

    private static final int STATE_INIT = 0;   //初始化
    private static final int STATE_P_NAME = 1; //解析参数名
    private static final int STATE_P_VAL = 2;  //解析参数值
    private static final int STATE_V_SEP = 3;  //解析参数分隔
    private static final int STATE_V_ESC = 4;  //解析转义符


    public static void main(String[] args) throws StatementParsingException {

        var parser = new StatementResolver();

        parser.resolve("command -param1 -param2");
        parser.resolve("echo -param1=p1 -param1=p1 -param2=p2 -param3=p3");
        parser.resolve("echo -param1 = p1 -param2 = p2 -param3 = p3");
        parser.resolve("echo -param1=p1    -param2=p2-param3=p3");
        parser.resolve("echo -param1=p1-param2=p2-param3=p3");
        //parser.resolve("echo -param1=");
        parser.resolve("echo -param1=value -with -@ -character -param2=\"quoted value\"");
        parser.resolve("echo");
        parser.resolve("echo -param1=value");
        //parser.resolve("echo -param1=value1=value2");
    }

    public ParsedStatements resolve(String statement) throws StatementParsingException {

        var ret = new ParsedStatements();

        var parameters = new HashMap<String,List<String>>();
        var cs = statement.toCharArray();

        var state = STATE_INIT;
        var pattern = new StringBuilder();
        var name = new StringBuilder();
        var val = new StringBuilder();

        for(var i = 0; i<cs.length; i++){
            var cur = cs[i];

            if(cur == '-'){
                if(pattern.isEmpty()){
                    throw new StatementParsingException("cannot parsing pattern in statement",statement,i);
                }
                if(state != STATE_INIT && state != STATE_P_VAL && state != STATE_V_SEP){
                    if(name.isEmpty()){
                        throw new StatementParsingException("Invaild symbol",statement,i);
                    }
                }

                state = STATE_P_NAME;

                if(!name.isEmpty()){

                    var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());

                    if(!val.isEmpty()){
                        kind.add(fmt(val));
                        val.setLength(0);
                    }
                    name.setLength(0);
                }

                continue;
            }
            if(cur == '='){
                if(name.isEmpty()){
                    throw new StatementParsingException("cannot parsing parameterName in statement",statement,i);
                }
                state = STATE_P_VAL;
                continue;
            }
            if(cur == ','){
                if(val.isEmpty()){
                    throw new StatementParsingException("val separator was found but without parameter value in previous token",statement,i);
                }
                if(name.isEmpty()){
                    throw new StatementParsingException("val separator was found but without parameter name in previous token",statement,i);
                }
                var kind = parameters.get(fmt(name));
                kind.add(fmt(val));
                val.setLength(0);
                state = STATE_V_SEP;
                continue;
            }


            if(state == STATE_INIT){
                pattern.append(cur);
            }
            if(state == STATE_P_NAME){
                name.append(cur);
            }
            if(state == STATE_P_VAL){
                val.append(cur);
            }
            if(state == STATE_V_SEP){
                val.append(cur);
            }

        }

        if(state == STATE_P_NAME && name.isEmpty()){
            throw new StatementParsingException("param symbol was found but without parameter name",statement,cs.length);
        }
        if(state == STATE_P_VAL && (val.isEmpty() || name.isEmpty())){
            throw new StatementParsingException("val symbol was found but without values",statement,cs.length);
        }

        //追加参数
        if(state == STATE_P_NAME){
            parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
        }
        if(state == STATE_P_VAL){
            var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
            kind.add(fmt(val));
        }

        //parameters.put(pName.toString(),pVal.toString());

        ret.setPattern(pattern.toString().trim());
        ret.setParameter(parameters);
        System.out.println(ret);
        return ret;
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
                    parameters.put(fmt(pName), fmt(pVal));
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
        if(state == 2 && pVal.isEmpty()){
            throw new StatementParsingException("[values symbol start] was found but without values",statement,cs.length);
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

    public String fmt(StringBuilder b){
        return b.toString().trim();
    }

}
