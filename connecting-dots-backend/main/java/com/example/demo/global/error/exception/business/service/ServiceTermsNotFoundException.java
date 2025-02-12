package com.example.demo.global.error.exception.business.service;


import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ServiceTermsNotFoundException extends EntityNotFoundException {

    public ServiceTermsNotFoundException() {
        super(SERVICE_TERMS_NOT_FOUND);
    }

}
