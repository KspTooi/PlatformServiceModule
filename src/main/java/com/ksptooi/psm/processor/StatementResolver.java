package com.ksptooi.psm.processor;

import com.ksptooi.guice.annotations.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Unit
public class StatementResolver {

    private static final int STATE_INIT = 0;   //初始化
    private static final int STATE_P_NAME = 1; //解析参数名
    private static final int STATE_P_VAL = 2;  //解析参数值
    private static final int STATE_V_SEP = 3;  //解析参数分隔
    private static final int STATE_V_ESC = 4;  //解析转义符
    private static final int STATE_V_IDLE = 5;  //惰性状态

    //顺序风格的状态机
    private static final int SEQ_STATE_INIT = 101;
    private static final int SEQ_STATE_VAL = 102;
    private static final int SEQ_STATE_ESC = 103;

    public static void main(String[] args) throws StatementParsingException {

        var parser = new StatementResolver();


        var resolve = parser.resolve("echo -name");

        System.out.println(resolve);
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

            if(cur == '"'){
                //退出转义
                if(state == STATE_V_ESC){
                    state = STATE_V_IDLE;
                    var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
                    kind.add(fmt(val));
                    val.setLength(0);
                    continue;
                }
                //转义只能在参数段中开始
                if(state != STATE_P_VAL && state!= STATE_V_SEP){
                    throw new StatementParsingException("Invalid symbol \" escape only using in value block",statement,i);
                }
                if(name.isEmpty()){
                    throw new StatementParsingException("Invalid symbol \" escape only using in value block",statement,i);
                }
                state = STATE_V_ESC;
                continue;
            }

            if(state == STATE_V_ESC){
                val.append(cur);
                continue;
            }

            if(cur == '-'){

                if(pattern.isEmpty()){
                    throw new StatementParsingException("Cannot parsing pattern in statement",statement,i);
                }
                if(state != STATE_INIT && state != STATE_P_VAL && state != STATE_V_SEP){
                    if(name.isEmpty()){
                        throw new StatementParsingException("Invalid symbol",statement,i);
                    }
                }

                state = STATE_P_NAME;

                if(!name.isEmpty()){

                    var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());

                    //当前已经有Kind 但是依然试图添加boolean值
                    if(!kind.isEmpty() && val.isEmpty()){
                        throw new StatementParsingException("Invalid symbol",statement,i);
                    }

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
                    throw new StatementParsingException("Cannot parsing parameterName in statement",statement,i);
                }
                if(state != STATE_P_NAME){
                    throw new StatementParsingException("Invalid symbol",statement,i);
                }
                state = STATE_P_VAL;
                continue;
            }

            if(cur == ','){
                if(state == STATE_V_IDLE){
                    state = STATE_V_SEP;
                    continue;
                }
                if(val.isEmpty()){
                    throw new StatementParsingException("Value separator was found but without parameter value in previous token",statement,i);
                }
                if(name.isEmpty()){
                    throw new StatementParsingException("Value separator was found but without parameter name in previous token",statement,i);
                }
                var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
                kind.add(fmt(val));
                val.setLength(0);
                state = STATE_V_SEP;
                continue;
            }

            if(state == STATE_INIT){
                //if(cur == ' '){
                //    continue;
                //}
                pattern.append(cur);
            }
            if(state == STATE_P_NAME){
                if(cur == ' '){
                    continue;
                }
                name.append(cur);
            }
            if(state == STATE_P_VAL){
                val.append(cur);
            }
            if(state == STATE_V_SEP){
                val.append(cur);
            }

        }

        if(state == STATE_V_ESC){
            throw new StatementParsingException("Missing escaped closing symbol",statement,cs.length - 1);
        }
        if(state == STATE_P_NAME){
            if(name.isEmpty()){
                throw new StatementParsingException("Param symbol was found but without parameter name",statement,cs.length - 1);
            }
            var v = parameters.get(fmt(name));
            if(v != null && !v.isEmpty()){
                throw new StatementParsingException("Invalid symbol",statement,cs.length - 1);
            }
        }
        if(state == STATE_P_VAL && (val.isEmpty() || name.isEmpty())){
            throw new StatementParsingException("Value symbol was found but without values",statement,cs.length - 1);
        }
        if(state == STATE_V_SEP){
            if(val.isEmpty()){
                throw new StatementParsingException("Separator was found but without value",statement,cs.length - 1);
            }
            var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
            kind.add(fmt(val));
        }

        //追加参数
        if(state == STATE_P_NAME){
            parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
        }
        if(state == STATE_P_VAL){
            var kind = parameters.computeIfAbsent(fmt(name), k -> new ArrayList<>());
            kind.add(fmt(val));
        }

        //没有解析到任何值
        if(state == STATE_INIT && pattern.isEmpty()){
            throw new StatementParsingException("parsing exception",statement,cs.length - 1);
        }

        ret.setPattern(fmt(pattern));
        ret.setParameter(parameters);
        //System.out.println(ret);
        return ret;
    }



    public void resolve(ShellRequest request) throws StatementParsingException{

        var statement = request.getStatement();

        //按顺序风格解析
        if(isSequenceStyleStatement(statement)){
            resolveAsSequentialStyle(request);
            return;
        }


        var parsed = resolve(statement);
        request.setPattern(parsed.getPattern());

        var origin = parsed.getParameter();
        var params = new HashMap<String,List<String>>();

        //将参数名称全部转换为小写并合并
        for(var entry : origin.entrySet()){
            var pNameLC = entry.getKey().toLowerCase();
            var kind = params.computeIfAbsent(pNameLC, k -> new ArrayList<>());
            kind.addAll(entry.getValue());
        }

        request.setArgumentMap(params);
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
            req.setSeqArgument(new ArrayList<>());
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
        req.setSeqArgument(paramList);
        req.setSeqStyleArgument(true);
    }

    /**
     * 判断一段statement是否为"顺序风格"
     * @param statement
     * @return
     */
    public boolean isSequenceStyleStatement(String statement){

        var cs = statement.toCharArray();

        for (int i = 0; i < cs.length; i++) {
            if(cs[i] == '"' || cs[i] == '-'){
                return false;
            }
            if(cs[i] == '>'){
                return true;
            }
        }

        return false;
    }

    public String fmt(StringBuilder b){
        return b.toString().trim();
    }


}
