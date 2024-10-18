package com.example.demo.presentation;

import com.example.demo.application.exception.global.InternalServerError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InternalServerError.class})
    public String handleException() {
        return "error";
    }
}
