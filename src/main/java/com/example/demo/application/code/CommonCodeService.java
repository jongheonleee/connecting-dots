package com.example.demo.application.code;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.code.CodeRequest;
import com.example.demo.dto.code.CodeResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CommonCodeService {

    int count();

    List<CodeResponse> readByTopCode(String top_code);

    CodeResponse readBySeq(Integer seq);

    CodeResponse readByCode(String code);

    PageResponse readBySearchCondition(SearchCondition sc);

    List<CodeResponse> readAll();

    CodeResponse create(CodeRequest request);

    void modify(CodeRequest request);

    void modifyUse(CodeRequest request);

    void removeByLevel(Integer level);

    void removeByCode(String code);

    void removeBySeq(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
