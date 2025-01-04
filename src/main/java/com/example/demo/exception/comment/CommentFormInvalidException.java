package com.example.demo.exception.comment;

public class CommentFormInvalidException extends RuntimeException {

    public CommentFormInvalidException() {
        this("댓글 폼이 올바르지 않습니다.");
    }


    public CommentFormInvalidException(String message) {
        super(message);
    }



}
