package com.example.demo.exception.board;

public class BoardFormInvalidException extends RuntimeException {

    public BoardFormInvalidException() {
        this("작성하신 게시글은 게시글 규칙과 정책에 부합하지 않습니다. 게시글 내용을 재검토해주세요!.");
    }

    public BoardFormInvalidException(String message) {
        super(message);
    }

}
