package com.example.demo.application.comment;

import com.example.demo.dto.comment.CommentDetailResponse;
import com.example.demo.dto.comment.CommentRequest;
import com.example.demo.dto.comment.CommentResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    CommentResponse create(CommentRequest request);

    @Transactional(rollbackFor = Exception.class)
    void modify(CommentRequest request);

    void increaseLikeCnt(Integer cno);

    void increaseDislikeCnt(Integer cno);

    void remove(Integer cno);

    @Transactional(rollbackFor = Exception.class)
    void removeByBno(Integer bno);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();

    CommentResponse readByCno(Integer cno);

    List<CommentDetailResponse> readByBno(Integer bno);
}
