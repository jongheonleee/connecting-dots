package com.example.demo.global.error.exception.business.code;
import static com.example.demo.global.error.exception.ErrorCode.CODE_NOT_FOUND;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class CodeNotFoundException extends EntityNotFoundException{

    public CodeNotFoundException() {
        super(CODE_NOT_FOUND);
    }

}
