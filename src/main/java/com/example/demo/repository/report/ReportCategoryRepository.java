package com.example.demo.repository.report;

import com.example.demo.dto.report.ReportCategoryDto;
import java.util.List;

public interface ReportCategoryRepository {

    int count();

    int countByLevel(Integer level);

    boolean existsByCateCode(String cate_code);

    boolean existsByCateCodeForUpdate(String cate_code);

    List<ReportCategoryDto> selectAll();

    ReportCategoryDto selectByCateCode(String cate_code);

    List<ReportCategoryDto> selectByTopCate(String top_cate);

    int insert(ReportCategoryDto dto);

    int update(ReportCategoryDto dto);

    int updateChkUseY(String cate_code);

    int updateChkUseN(String cate_code);

    int deleteByCateCode(String cate_code);

    int deleteByLevel(Integer level);
}
