package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.BOARD_INVALID_CONTENT;

import com.example.demo.global.error.exception.business.BusinessException;

public class BoardInvalidContentException extends BusinessException {

    public BoardInvalidContentException() {
        super(BOARD_INVALID_CONTENT);
    }
}
