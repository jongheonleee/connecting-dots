package com.example.demo.global.error.exception.business.category;

public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException() {
        this("이미 존재하는 카테고리입니다.");
    }

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }

}
