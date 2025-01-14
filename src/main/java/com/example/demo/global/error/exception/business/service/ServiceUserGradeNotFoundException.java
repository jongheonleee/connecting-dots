package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceUserGradeNotFoundException extends EntityNotFoundException {

    public ServiceUserGradeNotFoundException() {
        super(SERVICE_USER_GRADE_NOT_FOUND);
    }
}
