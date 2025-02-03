package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ReportProcessNotFoundException extends EntityNotFoundException {

    public ReportProcessNotFoundException() {
        super(REPORT_PROCESS_NOT_FOUND);
    }
}
