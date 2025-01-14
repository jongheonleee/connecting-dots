package com.example.demo.application.board;

import com.example.demo.dto.board.BestViewBoardDto;
import com.example.demo.dto.board.BestViewBoardUpdateDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.global.error.exception.technology.InternalServerError;
import com.example.demo.repository.mybatis.board.BestViewBoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BestViewBoardServiceImpl {

    private final BestViewBoardDaoImpl bestViewBoardDao;
    private final BoardDaoImpl boardDao;

    public int count() {
        return bestViewBoardDao.count();
    }

    public int countUsed() {
        return bestViewBoardDao.countUsed();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveForBestViewBoards(Integer cnt) {
        List<BoardFormDto> bestViewBoards = boardDao.selectTopByView(cnt);

        bestViewBoards.stream()
                .forEach(board -> {
                    var bestViewBoardDto = new BestViewBoardDto();
                    // 시간 세팅
                    LocalDateTime applBegin = LocalDateTime.now();
                    LocalDateTime applEnd = applBegin.plusMonths(1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedApplBegin = applBegin.format(formatter);
                    String formattedApplEnd = applEnd.format(formatter);

                    // 등록자 세팅
                    String userId = "admin1234";

                    // 등록 시간 세팅
                    LocalDateTime currentTime = LocalDateTime.now();
                    String formattedCurrentTime = currentTime.format(formatter);

                    // dto에 값 세팅
                    bestViewBoardDto.setBno(board.getBno());
                    bestViewBoardDto.setAppl_begin(formattedApplBegin);
                    bestViewBoardDto.setAppl_end(formattedApplEnd);
                    bestViewBoardDto.setReg_id(userId);
                    bestViewBoardDto.setReg_date(formattedCurrentTime);
                    bestViewBoardDto.setUp_id(userId);
                    bestViewBoardDto.setUp_date(formattedCurrentTime);
                    bestViewBoardDto.setUsed("Y");

                    int rowCnt = bestViewBoardDao.insert(bestViewBoardDto);
                    if (rowCnt != 1) {
                        throw new InternalServerError(null);
                    }
                });
    }

    public void save(BestViewBoardDto dto) {
        int rowCnt = bestViewBoardDao.insert(dto);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }

    public List<BoardResponseDto> readForView(Map<String, Object> map) {
        return bestViewBoardDao.selectForView(map);
    }

    public BestViewBoardDto read(Integer seq) {
        return bestViewBoardDao.select(seq);
    }

    public List<BestViewBoardDto> readAll() {
        return bestViewBoardDao.selectAll();
    }

    public void modify(BestViewBoardUpdateDto dto) {
        int rowCnt = bestViewBoardDao.update(dto);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }

    public void modifyUsed(String up_id) {
        int totalCnt = bestViewBoardDao.countForChangeUsed();
        int rowCnt = bestViewBoardDao.updateUsed(up_id);
        if (rowCnt != totalCnt) {
            throw new InternalServerError(null);
        }
    }

    public void remove(Integer seq) {
        int rowCnt = bestViewBoardDao.delete(seq);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = bestViewBoardDao.count();
        int rowCnt = bestViewBoardDao.deleteAll();
        if (rowCnt != totalCnt) {
            throw new InternalServerError(null);
        }
    }

}
