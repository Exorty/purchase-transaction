package com.wex.purchasetransaction.exception;

import com.wex.purchasetransaction.exception.model.Error;
import com.wex.purchasetransaction.exception.model.StackTrace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    private final String API_NAME = "WEX-Transactions";

    private Error buildError(Exception e, HttpStatus status) {
        Error error = new Error();

        Throwable throwableRootCause = e.getCause();

        error.setApplication(API_NAME);
        error.setCode(Integer.toString(status.value()));
        error.setDate(OffsetDateTime.now().toString());
        error.setMessage(e.getMessage());

        if (throwableRootCause == null) {
            error.setRootCause(e.toString());
            error.setStackTrace(getStackTraceElement(e.getStackTrace()));
        }

        return error;
    }

    private List<StackTrace> getStackTraceElement(StackTraceElement[] stackTraceElement) {
        List<StackTrace> stackList = new ArrayList<StackTrace>();
        if (stackTraceElement != null && stackTraceElement.length != 0) {
            for (StackTraceElement s : stackTraceElement) {
                StackTrace stackTrace = new StackTrace();
                stackTrace.setClassName(s.getClassName());
                stackTrace.setLineNumber(s.getLineNumber());
                stackTrace.setMethodName(s.getMethodName());
                stackList.add(stackTrace);
            }
        }
        return stackList;
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<?> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        Error error = buildError(ex, ex.getHttpStatus());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(ExchangeRateNotFound.class)
    public ResponseEntity<?> handleExchangeRateNotFound(ExchangeRateNotFound ex) {
        Error error = buildError(ex, ex.getHttpStatus());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        Error error = buildError(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        Error error = buildError(ex, ex.getHttpStatus());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

}