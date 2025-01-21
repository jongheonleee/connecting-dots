package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardMainDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.board.BoardMapper.";


    public int count() {
        return session.selectOne(namespace + "count");
    }

    // 게시글 제목(TT), 작성자(WR), 카테고리(CT) 기반으로 검색
    public int countBySearchCondition(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public int countByCategory(String cate_code) {
        return session.selectOne(namespace + "countByCategory", cate_code);
    }

    public boolean existsByBno(Integer bno) {
        return session.selectOne(namespace + "existsByBno", bno);
    }

    public boolean existsByBnoForUpdate(Integer bno) {
        return session.selectOne(namespace + "existsByBnoForUpdate", bno);
    }

    public int insert(BoardDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public BoardDto select(Integer bno) {
        return session.selectOne(namespace + "selectByBno", bno);
    }

    public List<BoardDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }


    public List<BoardDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }


    public List<BoardMainDto> selectForMain(Map map) {
        return session.selectList(namespace + "selectForMain", map);
    }

    public List<BoardMainDto> selectForMainByCategory(Map map) {
        return session.selectList(namespace + "selectForMainByCategory", map);
    }

    public List<BoardMainDto> selectForMainBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectForMainBySearchCondition", sc);
    }

    public int update(BoardDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int increaseViewCnt(BoardDto dto) {
        return session.update(namespace + "increaseViewCnt", dto);
    }

    public int increaseRecoCnt(BoardDto dto) {
        return session.update(namespace + "increaseRecoCnt", dto);
    }

    public int increaseNotRecoCnt(BoardDto dto) {
        return session.update(namespace + "increaseNotRecoCnt", dto);
    }

    public int delete(Integer bno) {
        return session.delete(namespace + "deleteByBno", bno);
    }

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
