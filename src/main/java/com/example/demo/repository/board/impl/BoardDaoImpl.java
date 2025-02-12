package com.example.demo.repository.board.impl;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardMainDto;
import com.example.demo.repository.board.BoardRepository;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardRepository {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardMapper.";


    @Override
    public int count() {
        return session.selectOne(namespace + "count");
    }

    // 게시글 제목(TT), 작성자(WR), 카테고리(CT) 기반으로 검색
    @Override
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    @Override
    public int countByCategory(String cate_code) {
        return session.selectOne(namespace + "countByCategory", cate_code);
    }

    @Override
    public boolean existsByBno(Integer bno) {
        return session.selectOne(namespace + "existsByBno", bno);
    }

    @Override
    public boolean existsByBnoForUpdate(Integer bno) {
        return session.selectOne(namespace + "existsByBnoForUpdate", bno);
    }

    @Override
    public int insert(BoardDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    @Override
    public BoardDto select(Integer bno) {
        return session.selectOne(namespace + "selectByBno", bno);
    }

    @Override
    public List<BoardDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }


    @Override
    public List<BoardDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }


    @Override
    public List<BoardMainDto> selectForMain(Map map) {
        return session.selectList(namespace + "selectForMain", map);
    }

    @Override
    public List<BoardMainDto> selectForMainByCategory(Map map) {
        return session.selectList(namespace + "selectForMainByCategory", map);
    }

    @Override
    public List<BoardMainDto> selectForMainBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectForMainBySearchCondition", sc);
    }

    @Override
    public int update(BoardDto dto) {
        return session.update(namespace + "update", dto);
    }

    @Override
    public int increaseViewCnt(BoardDto dto) {
        return session.update(namespace + "increaseViewCnt", dto);
    }

    @Override
    public int increaseRecoCnt(BoardDto dto) {
        return session.update(namespace + "increaseRecoCnt", dto);
    }

    @Override
    public int increaseNotRecoCnt(BoardDto dto) {
        return session.update(namespace + "increaseNotRecoCnt", dto);
    }

    @Override
    public int delete(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }

    @Override
    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }
//
//    // v2 조회 내용 추가
//    public List<BoardResponseDto> selectV2(Map map) {
//        return session.selectList(namespace + "selectV2", map);
//    }
//
//    public List<BoardResponseDto> selectV2ByCategory(Map map) {
//        return session.selectList(namespace + "selectV2ByCategory", map);
//    }
//
//    public List<BoardResponseDto> selectV2BySearchCondition(SearchCondition sc) {
//        return session.selectList(namespace + "selectV2BySearchCondition", sc);
//    }
//
//    public List<BoardFormDto> selectTopByComment(Integer n) {
//        return session.selectList(namespace + "selectTopByComment", n);
//    }
//
//    public List<BoardFormDto> selectTopByView(Integer n) {
//        return session.selectList(namespace + "selectTopByView", n);
//    }
//
//    public List<BoardFormDto> selectTopByReco(Integer n) {
//        return session.selectList(namespace + "selectTopByReco", n);
//    }



}
