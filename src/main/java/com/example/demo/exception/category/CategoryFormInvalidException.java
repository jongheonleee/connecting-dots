package com.example.demo.exception.category;

public class CategoryFormInvalidException extends RuntimeException {

    public CategoryFormInvalidException() {
        this("입력하신 카테고리 데이터가 애플리케이션의 규칙과 정책에 부합하지 않습니다. 카테고리 데이터를 재검토해주세요.");
    }

    public CategoryFormInvalidException(String message) {
        super(message);
    }

}
