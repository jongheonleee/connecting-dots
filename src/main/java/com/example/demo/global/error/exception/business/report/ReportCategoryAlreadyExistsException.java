package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.ErrorCode;
import com.example.demo.global.error.exception.business.BusinessException;

public class ReportCategoryAlreadyExistsException extends BusinessException  {

    public ReportCategoryAlreadyExistsException() {
        super(REPORT_CATEGORY_ALREADY_EXISTS);
    }
}
