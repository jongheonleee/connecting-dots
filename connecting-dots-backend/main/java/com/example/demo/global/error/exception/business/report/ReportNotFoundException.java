package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class ReportNotFoundException extends EntityNotFoundException {

    public ReportNotFoundException() {
        super(REPORT_NOT_FOUND);
    }

}
