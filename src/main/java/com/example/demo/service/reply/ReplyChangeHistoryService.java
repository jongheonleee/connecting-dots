package com.example.demo.service.reply;

import com.example.demo.dto.reply.ReplyChangeHistoryRequest;
import com.example.demo.dto.reply.ReplyChangeHistoryResponse;
import java.util.List;

public interface ReplyChangeHistoryService {

    ReplyChangeHistoryResponse create(ReplyChangeHistoryRequest request);

    ReplyChangeHistoryResponse readBySeq(Integer seq);

    List<ReplyChangeHistoryResponse> readByRcno(Integer rcno);

    List<ReplyChangeHistoryResponse> readAll();

    void modify(ReplyChangeHistoryRequest request);

    void remove(Integer seq);

    void removeByRcno(Integer rcno);

    void removeAll();
}
