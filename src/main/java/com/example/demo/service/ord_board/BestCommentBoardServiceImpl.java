//package com.example.demo.application.board;
//
//
//import com.example.demo.dto.ord_board.BestCommentBoardDto;
//import com.example.demo.dto.ord_board.BestCommentBoardUpdateDto;
//import com.example.demo.dto.ord_board.BoardFormDto;
//import com.example.demo.dto.ord_board.BoardResponseDto;
//import com.example.demo.global.error.exception.technology.InternalServerException;
//import com.example.demo.repository.mybatis.board.BestCommentBoardDaoImpl;
//import com.example.demo.repository.mybatis.board.BoardDaoImpl;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Map;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@AllArgsConstructor
//public class BestCommentBoardServiceImpl {
//
//    private final BestCommentBoardDaoImpl bestCommentBoardDao;
//    private final BoardDaoImpl boardDao;
//
//    public int count() {
//        return bestCommentBoardDao.count();
//    }
//
//    public int countUsed() {
//        return bestCommentBoardDao.countUsed();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void saveForBestCommentBoards(Integer cnt) {
//        List<BoardFormDto> bestCommentBoards = boardDao.selectTopByComment(cnt);
//
//        bestCommentBoards.stream()
//                .forEach(board -> {
//                    var bestCommentBoardDto = new BestCommentBoardDto();
//                    // 시간 세팅
//                    LocalDateTime applBegin = LocalDateTime.now();
//                    LocalDateTime applEnd = applBegin.plusMonths(1);
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                    String formattedApplBegin = applBegin.format(formatter);
//                    String formattedApplEnd = applEnd.format(formatter);
//
//                    // 등록자 세팅
//                    String userId = "admin1234";
//
//                    // 등록 시간 세팅
//                    LocalDateTime currentTime = LocalDateTime.now();
//                    String formattedCurrentTime = currentTime.format(formatter);
//
//                    // dto에 값 세팅
//                    bestCommentBoardDto.setBno(board.getBno());
//                    bestCommentBoardDto.setAppl_begin(formattedApplBegin);
//                    bestCommentBoardDto.setAppl_end(formattedApplEnd);
//                    bestCommentBoardDto.setReg_id(userId);
//                    bestCommentBoardDto.setReg_date(formattedCurrentTime);
//                    bestCommentBoardDto.setUp_id(userId);
//                    bestCommentBoardDto.setUp_date(formattedCurrentTime);
//                    bestCommentBoardDto.setUsed("Y");
//
//                    int rowCnt = bestCommentBoardDao.insert(bestCommentBoardDto);
//                    if (rowCnt != 1) {
//                        throw new InternalServerException(null);
//                    }
//                });
//    }
//
//    public void save(BestCommentBoardDto dto) {
//        int rowCnt = bestCommentBoardDao.insert(dto);
//        if (rowCnt != 1) {
//            throw new InternalServerException(null);
//        }
//    }
//
//    public List<BoardResponseDto> readForView(Map<String, Object> map) {
//        return bestCommentBoardDao.selectForView(map);
//    }
//
//    public BestCommentBoardDto read(Integer seq) {
//        return bestCommentBoardDao.select(seq);
//    }
//
//    public List<BestCommentBoardDto> readAll() {
//        return bestCommentBoardDao.selectAll();
//    }
//
//    public void modify(BestCommentBoardUpdateDto dto) {
//        int rowCnt = bestCommentBoardDao.update(dto);
//        if (rowCnt != 1) {
//            throw new InternalServerException(null);
//        }
//    }
//
//    public void modifyUsed(String up_id) {
//        int totalCnt = bestCommentBoardDao.countForChangeUsed();
//        int rowCnt = bestCommentBoardDao.updateUsed(up_id);
//        if (totalCnt != rowCnt) {
//            throw new InternalServerException(null);
//        }
//    }
//
//    public void remove(Integer seq) {
//        int rowCnt = bestCommentBoardDao.delete(seq);
//        if (rowCnt != 1) {
//            throw new InternalServerException(null);
//        }
//    }
//
//
//    @Transactional(rollbackFor = Exception.class)
//    public void removeAll() {
//        int totalCnt = bestCommentBoardDao.count();
//        int rowCnt = bestCommentBoardDao.deleteAll();
//        if (totalCnt != rowCnt) {
//            throw new InternalServerException(null);
//        }
//    }
//
//
//}
