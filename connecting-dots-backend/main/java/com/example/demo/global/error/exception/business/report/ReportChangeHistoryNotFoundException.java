package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;

public class ReportChangeHistoryNotFoundException extends EntityNotFoundException {

    public ReportChangeHistoryNotFoundException() {
        super(REPORT_CHANGE_HISTORY_NOT_FOUND);
    }

}
