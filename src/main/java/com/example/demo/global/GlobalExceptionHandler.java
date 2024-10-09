package com.example.demo.global;

import com.example.demo.exception.InternalServerError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InternalServerError.class})
    public String handleException() {
        return "error";
    }
}
