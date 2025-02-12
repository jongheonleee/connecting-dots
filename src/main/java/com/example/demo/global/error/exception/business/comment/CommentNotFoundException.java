package com.example.demo.global.error.exception.business.comment;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException() {
        super(COMMENT_NOT_FOUND);
    }

}
