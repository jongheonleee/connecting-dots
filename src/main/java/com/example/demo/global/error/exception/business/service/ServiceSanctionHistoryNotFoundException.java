package com.example.demo.global.error.exception.business.service;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceSanctionHistoryNotFoundException extends EntityNotFoundException {

    public ServiceSanctionHistoryNotFoundException() {
        super(SERVICE_SANCTION_HISTORY_NOT_FOUND);
    }
}
