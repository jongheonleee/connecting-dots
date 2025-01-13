package com.example.demo.global.error;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;

    private ErrorResponse(String message, int status, List<FieldError> errors, String code) {
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.code = code;
    }

}
