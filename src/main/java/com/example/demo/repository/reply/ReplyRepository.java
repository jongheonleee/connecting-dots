package com.example.demo.repository.reply;

import com.example.demo.dto.reply.ReplyDto;
import java.util.List;
import java.util.Map;

public interface ReplyRepository {

    int count();

    int countByBno(Integer bno);

    int countByCno(Integer cno);

    boolean existsByRcno(Integer rcno);

    boolean existsByRcnoForUpdate(Integer rcno);

    int insert(ReplyDto dto);

    List<ReplyDto> selectByBnoAndCno(Map<String, Object> map);

    List<ReplyDto> selectByBno(Integer bno);

    List<ReplyDto> selectByCno(Integer cno);

    ReplyDto select(Integer rcno);

    List<ReplyDto> selectAll();

    int update(ReplyDto dto);

    int increaseRecoCnt(Integer rcno);

    int increaseNotRecoCnt(Integer rcno);

    int delete(Integer rcno);

    int deleteByBno(Integer bno);

    int deleteByCno(Integer cno);

    int deleteByBnoAndCno(Map<String, Object> map);

    int deleteAll();
}
