package com.example.demo.application.comment;

import com.example.demo.dto.comment.CommentChangeHistoryRequest;
import com.example.demo.dto.comment.CommentChangeHistoryResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CommentChangeHistoryService {

    CommentChangeHistoryResponse create(CommentChangeHistoryRequest request);

    CommentChangeHistoryResponse readBySeq(Integer seq);

    List<CommentChangeHistoryResponse> readByCno(Integer cno);

    void modify(CommentChangeHistoryRequest request);

    void remove(Integer seq);

    @Transactional(rollbackFor = Exception.class)
    void removeByCno(Integer cno);

    @Transactional(rollbackFor = Exception.class)
    void removeAll();
}
