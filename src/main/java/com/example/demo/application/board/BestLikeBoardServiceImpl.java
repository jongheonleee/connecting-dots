package com.example.demo.application.board;

import com.example.demo.dto.board.BestLikeBoardDto;
import com.example.demo.dto.board.BestLikeBoardUpdateDto;
import com.example.demo.dto.board.BoardFormDto;
import com.example.demo.dto.board.BoardResponseDto;
import com.example.demo.global.error.exception.technology.InternalServerError;
import com.example.demo.repository.mybatis.board.BestLikeBoardDaoImpl;
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
public class BestLikeBoardServiceImpl {

    private final BestLikeBoardDaoImpl bestLikeBoardDao;
    private final BoardDaoImpl boardDao;

    public int count() {
        return bestLikeBoardDao.count();
    }

    public int countUsed() {
        return bestLikeBoardDao.countUsed();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveForBestLikeBoards(Integer cnt) {
        List<BoardFormDto> bestCommentBoards = boardDao.selectTopByReco(cnt);

        bestCommentBoards.stream()
                .forEach(board -> {
                    var bestLikeBoardDto = new BestLikeBoardDto();
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
                    bestLikeBoardDto.setBno(board.getBno());
                    bestLikeBoardDto.setAppl_begin(formattedApplBegin);
                    bestLikeBoardDto.setAppl_end(formattedApplEnd);
                    bestLikeBoardDto.setReg_id(userId);
                    bestLikeBoardDto.setReg_date(formattedCurrentTime);
                    bestLikeBoardDto.setUp_id(userId);
                    bestLikeBoardDto.setUp_date(formattedCurrentTime);
                    bestLikeBoardDto.setUsed("Y");

                    int rowCnt = bestLikeBoardDao.insert(bestLikeBoardDto);
                    if (rowCnt != 1) {
                        throw new InternalServerError(null);
                    }
                });
    }

    public void save(BestLikeBoardDto dto) {
        int rowCnt = bestLikeBoardDao.insert(dto);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }

    public List<BoardResponseDto> readForView(Map<String, Object> map) {
        return bestLikeBoardDao.selectForView(map);
    }

    public BestLikeBoardDto read(Integer seq) {
        return bestLikeBoardDao.select(seq);
    }

    public List<BestLikeBoardDto> readAll() {
        return bestLikeBoardDao.selectAll();
    }

    public void modify(BestLikeBoardUpdateDto dto) {
        int rowCnt = bestLikeBoardDao.update(dto);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }

    public void modifyUsed(String up_id) {
        int totalCnt = bestLikeBoardDao.countForChangeUsed();
        int rowCnt = bestLikeBoardDao.updateUsed(up_id);
        if (rowCnt != totalCnt) {
            throw new InternalServerError(null);
        }
    }

    public void remove(Integer seq) {
        int rowCnt = bestLikeBoardDao.delete(seq);
        if (rowCnt != 1) {
            throw new InternalServerError(null);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = bestLikeBoardDao.count();
        int rowCnt = bestLikeBoardDao.deleteAll();
        if (rowCnt != totalCnt) {
            throw new InternalServerError(null);
        }
    }

}
