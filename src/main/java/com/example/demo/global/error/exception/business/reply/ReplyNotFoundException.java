package com.example.demo.global.error.exception.business.reply;

import static com.example.demo.global.error.exception.ErrorCode.REPLY_NOT_FOUND;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class ReplyNotFoundException extends EntityNotFoundException {

    public ReplyNotFoundException() {
        super(REPLY_NOT_FOUND);
    }

}
