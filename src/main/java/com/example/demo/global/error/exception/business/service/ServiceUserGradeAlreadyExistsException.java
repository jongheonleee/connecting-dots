package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ServiceUserGradeAlreadyExistsException extends BusinessException {

    public ServiceUserGradeAlreadyExistsException() {
        super(SERVICE_USER_GRADE_ALREADY_EXISTS);
    }
}
