package com.example.demo.repository.comment;

import com.example.demo.dto.comment.CommentDto;
import java.util.List;

public interface CommentRepository {

    int count();

    int countByBno(Integer bno);

    boolean existsByCno(Integer cno);

    boolean existsByCnoForUpdate(Integer cno);

    int insert(CommentDto dto);

    List<CommentDto> selectByBno(Integer bno);

    CommentDto selectByCno(Integer cno);

    List<CommentDto> selectAll();

    int update(CommentDto dto);

    int increaseLikeCnt(Integer cno);

    int increaseDislikeCnt(Integer cno);

    int deleteByBno(Integer bno);

    int deleteByCno(Integer cno);

    int deleteAll();
}
