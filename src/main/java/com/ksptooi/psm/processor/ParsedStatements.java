package com.ksptooi.psm.processor;

import lombok.Data;

import java.util.Map;

@Data
public class ParsedStatements {
    private String pattern;
    private Map<String,String> parameter;
}
