package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.BusinessException;

public class BoardCategoryAlreadyExistsException extends BusinessException {

    public BoardCategoryAlreadyExistsException() {
        super(BOARD_CATEGORY_ALREADY_EXISTS);
    }
}
