package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardCategoryDto;
import java.util.List;

public interface BoardCategoryRepository {

    int count();

    int countByLevel(Integer level);

    boolean existsByCateCode(String cate_code);

    boolean existsByCateCodeForUpdate(String cate_code);

    int insert(BoardCategoryDto dto);

    BoardCategoryDto selectByCateCode(String cate_code);

    List<BoardCategoryDto> selectAll();

    List<BoardCategoryDto> selectByTopCate(String top_cate);

    int update(BoardCategoryDto dto);

    int updateChkUseY(String cate_code);

    int updateChkUseN(String cate_code);

    int deleteByCateCode(String cate_code);

    int deleteByLevel(int level);
}
