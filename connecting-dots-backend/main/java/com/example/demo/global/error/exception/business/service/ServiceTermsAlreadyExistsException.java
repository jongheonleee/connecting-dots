package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ServiceTermsAlreadyExistsException extends BusinessException  {

    public ServiceTermsAlreadyExistsException() {
        super(SERVICE_TERMS_ALREADY_EXISTS);
    }
}
