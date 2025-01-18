package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class BoardChangeHistoryNotFoundException extends EntityNotFoundException  {

    public BoardChangeHistoryNotFoundException() {
        super(BOARD_CHANGE_HISTORY_NOT_FOUND);
    }
}
