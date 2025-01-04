package com.example.demo.exception.category;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        this("해당 카테고리를 찾을 수 없습니다.");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

}
