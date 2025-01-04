package com.example.demo.exception.board;

public class BoardImgNotFoundException extends RuntimeException {

    public BoardImgNotFoundException() {
        this("해당 게시글의 이미지를 찾을 수 없습니다.");
    }

    public BoardImgNotFoundException(String message) {
        super(message);
    }

}
