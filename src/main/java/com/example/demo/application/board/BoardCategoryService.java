package com.example.demo.application.board;

import com.example.demo.dto.board.BoardCategoryRequest;
import com.example.demo.dto.board.BoardCategoryResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface BoardCategoryService {

    int count();

    BoardCategoryResponse create(BoardCategoryRequest request);

    BoardCategoryResponse readByCateCode(String cate_code);

    List<BoardCategoryResponse> readByTopCate(String top_cate);

    List<BoardCategoryResponse> readAll();

    void modify(BoardCategoryRequest request);

    void modifyChkUseY(String cate_code);

    void modifyChkUseN(String cate_code);

    void remove(String cate_code);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
