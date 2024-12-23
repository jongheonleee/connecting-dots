package com.example.demo.application.exception.board;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() {
        this("해당 게시글을 찾을 수 없습니다.");
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

}
