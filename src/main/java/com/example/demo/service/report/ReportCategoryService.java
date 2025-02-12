package com.example.demo.service.report;

import com.example.demo.dto.report.ReportCategoryRequest;
import com.example.demo.dto.report.ReportCategoryResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ReportCategoryService {

    ReportCategoryResponse create(ReportCategoryRequest request);

    ReportCategoryResponse readByCateCode(String cate_code);

    List<ReportCategoryResponse> readByTopCate(String top_cate);

    List<ReportCategoryResponse> readAll();

    void modify(ReportCategoryRequest request);

    void modifyChkUseY(String cate_code);

    void modifyChkUseN(String cate_code);

    void remove(String cate_code);

    void removeAll();
}
