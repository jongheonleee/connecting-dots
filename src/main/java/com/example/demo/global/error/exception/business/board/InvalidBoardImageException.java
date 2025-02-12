package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.BOARD_INVALID_IMAGE_FILE_NAME;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.InvalidValueException;

public class InvalidBoardImageException extends InvalidValueException {

    public InvalidBoardImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
