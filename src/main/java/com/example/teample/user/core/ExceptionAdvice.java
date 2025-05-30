package com.example.teample.user.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> npe(NullPointerException e) {
        return  ResponseEntity.notFound().build();
    }

}
