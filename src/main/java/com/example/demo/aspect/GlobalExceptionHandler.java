package com.example.demo.aspect;

import com.example.demo.global.error.exception.technology.InternalServerError;
import com.example.demo.global.error.exception.technology.network.RetryFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerError.class, RetryFailedException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }


}
