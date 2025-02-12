package com.example.demo.global.error.exception.technology.network;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.technology.InternalServerException;

public class RetryFailedException extends InternalServerException {

    public RetryFailedException() {
        super(RETRY_FAILED);
    }
}
