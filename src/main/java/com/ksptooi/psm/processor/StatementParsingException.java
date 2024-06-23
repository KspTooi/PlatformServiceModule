package com.ksptooi.psm.processor;

import lombok.Data;
import lombok.Getter;

import java.io.PrintStream;
import java.io.PrintWriter;

@Getter
public class StatementParsingException extends Exception{

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


}
