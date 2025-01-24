package com.example.demo.global.error.exception.business.comment;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class CommentChangeHistoryNotFoundException extends EntityNotFoundException {

    public CommentChangeHistoryNotFoundException() {
        super(COMMENT_CHANGE_HISTORY_NOT_FOUND);
    }
}
