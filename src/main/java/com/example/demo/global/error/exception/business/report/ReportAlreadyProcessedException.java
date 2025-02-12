package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ReportAlreadyProcessedException extends BusinessException {

    public ReportAlreadyProcessedException() {
        super(REPORT_ALREADY_PROCESSED);
    }

}
