package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.dtos.ErrorResponse;
import com.datastax.oss.driver.api.mapper.MapperException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MapperException.class)
    public ResponseEntity<ErrorResponse> mappingException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getClass().getSimpleName(),
                "",
                HttpStatus.FAILED_DEPENDENCY.value()
        );

        return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
    }
}
