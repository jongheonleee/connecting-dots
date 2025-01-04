package com.example.demo.repository.mybatis.board;

import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDetailDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.dto.board.BoardUpdatedFormDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl {

    @Autowired
    private SqlSession session;

    private static final String namespace = "com.example.demo.mapper.BoardMapper.";

    public int count() {
        return session.selectOne(namespace + "count");
    }

    public int count(SearchCondition sc) {
        return session.selectOne(namespace + "countBySearchCondition", sc);
    }

    public int count(String cate_code) {
        return session.selectOne(namespace + "countByCategory", cate_code);
    }

    public int insert(BoardFormDto dto) {
        return session.insert(namespace + "insert", dto);
    }

    public BoardFormDto select(Integer bno) {
        return session.selectOne(namespace + "select", bno);
    }

    public List<BoardFormDto> selectAll() {
        return session.selectList(namespace + "selectAll");
    }

    public List<BoardFormDto> selectAllByCategory(String cate_code) {
        return session.selectList(namespace + "selectByCategory", cate_code);
    }

    public List<BoardFormDto> selectBySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectBySearchCondition", sc);
    }

    public BoardDetailDto selectDetailByBno(Integer bno) {
        return session.selectOne(namespace + "selectByBno", bno);
    }


    public int update(BoardFormDto dto) {
        return session.update(namespace + "update", dto);
    }

    public int increaseViewCnt(Integer bno) {
        return session.update(namespace + "increaseViewCnt", bno);
    }

    public int increaseRecoCnt(Integer bno) {
        return session.update(namespace + "increaseRecoCnt", bno);
    }

    public int increaseNotRecoCnt(Integer bno) {
        return session.update(namespace + "increaseNotRecoCnt", bno);
    }

    public int delete(Integer bno) {
        return session.delete(namespace + "delete", bno);
    }

    public int deleteAll() {
        return session.delete(namespace + "deleteAll");
    }

    // v2 조회 내용 추가
    public List<BoardResponseDto> selectV2(Map map) {
        return session.selectList(namespace + "selectV2", map);
    }

    public List<BoardResponseDto> selectV2ByCategory(Map map) {
        return session.selectList(namespace + "selectV2ByCategory", map);
    }

    public List<BoardResponseDto> selectV2BySearchCondition(SearchCondition sc) {
        return session.selectList(namespace + "selectV2BySearchCondition", sc);
    }

    public List<BoardFormDto> selectTopByComment() {
        return session.selectList(namespace + "selectTopByComment");
    }

    public List<BoardFormDto> selectTopByView() {
        return session.selectList(namespace + "selectTopByView");
    }

    public List<BoardFormDto> selectTopByLike() {
        return session.selectList(namespace + "selectTopByLike");
    }



}
