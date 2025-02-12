package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardImgDto;
import java.util.List;

public interface BoardImgRepository {

    int count();

    int insert(BoardImgDto dto);

    int insertAll(List<BoardImgDto> dtos);

    boolean existsByIno(int ino);

    boolean existsByInoForUpdate(Integer ino);

    List<BoardImgDto> selectByBno(Integer bno);

    BoardImgDto selectByIno(int ino);

    List<BoardImgDto> selectAll();

    int update(BoardImgDto dto);

    int deleteByBno(int bno);

    int deleteByIno(int ino);

    int deleteAll();
}
