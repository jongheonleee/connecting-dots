package com.example.demo.exception.comment;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        this("해당 댓글을 찾지 못했습니다.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }



}
