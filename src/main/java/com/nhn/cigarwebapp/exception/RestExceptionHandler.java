package com.nhn.cigarwebapp.exception;

import com.nhn.cigarwebapp.common.ErrorResponseObject;
import com.nhn.cigarwebapp.common.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponseObject> exceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(ErrorResponseObject.builder()
                        .message(ex.getMessage())
                        .errorCode("500")
                        .time(new Date())
                        .build());

    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseObject> illegalArgumentExceptionHandler(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(ErrorResponseObject.builder()
                        .message(ex.getMessage())
                        .errorCode("400")
                        .time(new Date())
                        .build());

    }

}
