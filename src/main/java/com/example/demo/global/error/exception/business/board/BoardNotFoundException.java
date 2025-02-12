package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class BoardNotFoundException extends EntityNotFoundException {

    public BoardNotFoundException() {
        super(BOARD_NOT_FOUND);
    }
}
