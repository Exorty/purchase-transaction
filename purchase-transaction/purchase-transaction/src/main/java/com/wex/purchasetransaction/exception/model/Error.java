package com.wex.purchasetransaction.exception.model;

import lombok.Data;

import java.util.List;

@Data
public class Error {

    private String application;
    private String code;
    private String date;
    private String message;
    private String rootCause;
    private List<StackTrace> stackTrace;

}
