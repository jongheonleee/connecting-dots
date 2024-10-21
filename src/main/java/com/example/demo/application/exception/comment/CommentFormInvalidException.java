package com.example.demo.application.exception.comment;

public class CommentFormInvalidException extends RuntimeException {
    public CommentFormInvalidException(String message) {
        super(message);
    }

}
