package com.nhn.cigarwebapp.exception;

import com.nhn.cigarwebapp.common.ErrorResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponseObject errorHandler(IllegalArgumentException ex, WebRequest request) {
        String responseString = "IllegalArgumentException";
        return ErrorResponseObject.builder()
                .message(responseString)
                .errorCode("400")
                .time(new Date())
                .build();
    }

}
