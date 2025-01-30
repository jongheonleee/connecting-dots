package com.example.demo.global.error.exception.business.report;

import static com.example.demo.global.error.exception.ErrorCode.*;

import com.example.demo.global.error.exception.business.EntityNotFoundException;


public class ReportCategoryNotFoundException extends EntityNotFoundException {

    public ReportCategoryNotFoundException() {
        super(REPORT_CATEGORY_NOT_FOUND);
    }

}
