package com.example.demo.global.error.exception.business.board;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class BoardCategoryNotFoundException extends EntityNotFoundException{

    public BoardCategoryNotFoundException() {
        super(BOARD_CATEGORY_NOT_FOUND);
    }
}
