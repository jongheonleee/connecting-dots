package com.example.demo.global.error.exception.business.report;

import com.example.demo.global.error.exception.business.BusinessException;
import static com.example.demo.global.error.exception.ErrorCode.*;


public class ReportProcessAlreadyExistsException extends BusinessException {

    public ReportProcessAlreadyExistsException() {
        super(REPORT_PROCESS_ALREADY_EXISTS);
    }

}
