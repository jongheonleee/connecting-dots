package com.example.demo.application.reply;

import com.example.demo.dto.reply.ReplyRequest;
import com.example.demo.dto.reply.ReplyResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ReplyService {

    ReplyResponse create(ReplyRequest request);

    void modify(ReplyRequest request);

    void remove(Integer rcno);

    void removeByCno(Integer cno);

    void removeByBno(Integer bno);

    void removeAll();

    ReplyResponse readByRcno(Integer rcno);

    List<ReplyResponse> readByCno(Integer cno);

    List<ReplyResponse> readAll();
}
