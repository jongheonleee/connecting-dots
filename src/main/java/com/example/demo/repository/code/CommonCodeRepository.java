package com.example.demo.repository.code;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeDto;
import java.util.List;

public interface CommonCodeRepository {

    int count();

    int countBySearchCondition(SearchCondition sc);

    List<CodeDto> selectByTopCode(String top_code);

    CodeDto selectBySeq(Integer seq);

    CodeDto selectByCode(String code);

    List<CodeDto> selectBySearchCondition(SearchCondition sc);

    List<CodeDto> selectAll();

    int insert(CodeDto dto);

    int update(CodeDto dto);

    int updateUse(CodeDto dto);

    int deleteByLevel(Integer level);

    int deleteByCode(String code);

    int delete(Integer seq);
}
