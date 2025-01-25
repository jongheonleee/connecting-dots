package com.example.demo.global.error.exception.business.reply;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ReplyChangeHistoryNotFoundException extends EntityNotFoundException {

    public ReplyChangeHistoryNotFoundException() {
        super(REPLY_CHANGE_HISTORY_NOT_FOUND);
    }
}
