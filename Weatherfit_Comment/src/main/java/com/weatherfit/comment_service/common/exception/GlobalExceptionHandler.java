package com.weatherfit.comment_service.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        HttpStatus httpStatus;
        if(e instanceof IllegalArgumentException)
            httpStatus = HttpStatus.BAD_REQUEST;
        else if(e instanceof NoSuchFieldException)
            httpStatus = HttpStatus.NOT_FOUND;
        else
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(e.getMessage(), httpStatus);
    }
}
