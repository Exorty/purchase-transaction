package com.wex.purchasetransaction.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TransactionNotFoundException(String message) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
