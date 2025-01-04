package com.example.demo.exception.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends RuntimeException {

    public InternalServerError() {
        this("서부 내부 환경 장애로 인한 에러가 발생했습니다.");
    }

    public InternalServerError(String message) {
        super(message);
    }

}
