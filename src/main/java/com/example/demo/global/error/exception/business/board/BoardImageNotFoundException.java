package com.example.demo.global.error.exception.business.board;
import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class BoardImageNotFoundException extends EntityNotFoundException {

    public BoardImageNotFoundException() {
        super(BOARD_IMAGE_NOT_FOUND);
    }
}
