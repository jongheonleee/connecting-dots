package com.example.demo.global.error.exception.business.board;
import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;
public class BoardStatusNotFoundException extends EntityNotFoundException {

    public BoardStatusNotFoundException() {
        super(BOARD_STATUS_NOT_FOUND);
    }

}
