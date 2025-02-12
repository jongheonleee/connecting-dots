package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.SERVICE_SANCTION_HISTORY_NOT_FOUND;

import com.example.demo.global.error.exception.business.BusinessException;

public class ServiceSanctionHistoryAlreadyExistsException extends BusinessException {

    public ServiceSanctionHistoryAlreadyExistsException() {
        super(SERVICE_SANCTION_HISTORY_NOT_FOUND);
    }
}
