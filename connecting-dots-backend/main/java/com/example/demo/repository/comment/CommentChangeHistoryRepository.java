package com.example.demo.repository.comment;

import com.example.demo.dto.comment.CommentChangeHistoryDto;
import java.util.List;

public interface CommentChangeHistoryRepository {

    int count();

    int countByCno(Integer cno);

    boolean existsByCno(Integer cno);

    boolean existsByCnoForUpdate(Integer cno);

    CommentChangeHistoryDto select(Integer seq);

    List<CommentChangeHistoryDto> selectByCno(Integer cno);

    CommentChangeHistoryDto selectLatestByCno(Integer cno);

    List<CommentChangeHistoryDto> selectAll();

    int insert(CommentChangeHistoryDto dto);

    int update(CommentChangeHistoryDto dto);

    int delete(Integer seq);

    int deleteByCno(Integer cno);

    int deleteAll();
}
