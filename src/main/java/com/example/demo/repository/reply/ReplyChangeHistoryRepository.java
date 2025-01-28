package com.example.demo.repository.reply;

import com.example.demo.dto.reply.ReplyChangeHistoryDto;
import java.util.List;

public interface ReplyChangeHistoryRepository {

    int count();

    int countByRcno(Integer rcno);

    int deleteAll();

    int insert(ReplyChangeHistoryDto dto);

    boolean existsBySeq(Integer seq);

    boolean existsBySeqForUpdate(Integer seq);

    List<ReplyChangeHistoryDto> selectAll();

    ReplyChangeHistoryDto selectBySeq(Integer seq);

    List<ReplyChangeHistoryDto> selectByRcno(Integer rcno);

    int update(ReplyChangeHistoryDto dto);

    int deleteBySeq(Integer seq);

    int deleteByRcno(Integer rcno);
}
