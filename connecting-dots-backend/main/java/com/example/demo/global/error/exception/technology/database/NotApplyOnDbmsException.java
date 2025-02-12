package com.example.demo.global.error.exception.technology.database;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.technology.InternalServerException;



public class NotApplyOnDbmsException extends InternalServerException {

    public NotApplyOnDbmsException() {
        super(NOT_APPLY_ON_DBMS);
    }

}
