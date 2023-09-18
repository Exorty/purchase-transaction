package com.wex.purchasetransaction.exception.model;

import lombok.Data;

@Data
public class StackTrace {

    private String className;
    private Integer lineNumber;
    private String methodName;

}
