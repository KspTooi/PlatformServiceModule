package com.ksptooi.psm.processor;

import lombok.Data;
import lombok.Getter;

import java.io.PrintStream;
import java.io.PrintWriter;

@Getter
public class StatementParsingException extends Exception{

    private String message;
    private String statement;
    private int index;

    public StatementParsingException() {
        super();
    }

    public StatementParsingException(String message) {
        super(message);
    }

    public StatementParsingException(String message,String statement,int index) {
        super(message);
        this.message = message;
        this.statement = statement;
        this.index = index;
    }

    public StatementParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatementParsingException(Throwable cause) {
        super(cause);
    }

    protected StatementParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public String toString() {

        var msg = new StringBuilder();
        msg.append("\r\n").append("StatementParsingException Cause:").append(message).append("\r\n");
        msg.append("\r\n").append(statement).append("\r\n");

        var cs = statement.toCharArray();

        for(var i =0;i < index; i++){

            if(isDoubleWidth(cs[i])){
                msg.append("。");
                continue;
            }
            msg.append(".");
        }
        msg.append("^").append("\r\n");

        return msg.toString();
    }

    //判断字符是否是双宽字符
    private boolean isDoubleWidth(char c) {

        if ((int) c <= 127) {
            return false;
        } else {
            return true;
        }

    }
}
