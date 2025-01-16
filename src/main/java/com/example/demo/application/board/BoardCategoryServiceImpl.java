package com.example.demo.application.board;

import com.example.demo.domain.BoardCategory;
import com.example.demo.dto.board.BoardCategoryDto;
import com.example.demo.dto.board.BoardCategoryRequest;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.example.demo.global.error.exception.business.board.BoardCategoryAlreadyExistsException;
import com.example.demo.global.error.exception.business.board.BoardCategoryNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCategoryServiceImpl {

    private final BoardCategoryDaoImpl boardCategoryDao;
    private final CustomFormatter formatter;


    public int count() {
        return boardCategoryDao.count();
    }

    public BoardCategoryResponse create(BoardCategoryRequest request) {
        checkDuplicated(request);
        var dto = createDto(request);
        checkApplied(1, boardCategoryDao.insert(dto));
        return createResponse(dto);
    }

    public BoardCategoryResponse readByCateCode(String cate_code) {
        checkExisted(cate_code);
        var dto = boardCategoryDao.selectByCateCode(cate_code);
        return createResponse(dto);
    }

    public List<BoardCategoryResponse> readByTopCate(String top_cate) {
        checkExisted(top_cate);
        return boardCategoryDao.selectByTopCate(top_cate)
                               .stream()
                               .map(this::createResponse)
                               .toList();

    }

    public List<BoardCategoryResponse> readAll() {
        return boardCategoryDao.selectAll()
                               .stream()
                               .map(this::createResponse)
                               .toList();
    }

    public void modify(BoardCategoryRequest request) {
        checkExistedForUpdate(request.getCate_code());
        var dto = createDto(request);
        dto.setCate_code(request.getCate_code());
        checkApplied(1, boardCategoryDao.update(dto));
    }

    public void modifyChkUseY(String cate_code) {
        checkExistedForUpdate(cate_code);
        checkApplied(1, boardCategoryDao.updateChkUseY(cate_code));
    }

    public void modifyChkUseN(String cate_code) {
        checkExistedForUpdate(cate_code);
        checkApplied(1, boardCategoryDao.updateChkUseN(cate_code));
    }

    public void remove(String cate_code) {
        checkExistedForUpdate(cate_code);
        checkApplied(1, boardCategoryDao.deleteByCateCode(cate_code));
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = boardCategoryDao.count();
        int rowCnt = removeLevelByLevel();
        checkApplied(totalCnt, rowCnt);
    }


    private void checkDuplicated(BoardCategoryRequest request) {
        boolean exists = boardCategoryDao.existsByCateCode(request.getCate_code());
        if (exists) {
            log.info("[BOARD_CATEGORY_SERVICE] 이미 존재하는 키 값입니다. : {}", request.getCate_code());
            throw new BoardCategoryAlreadyExistsException();
        }
    }

    private void checkApplied(Integer expected, Integer actual) {
        if (!expected.equals(actual)) {
            log.error("[BOARD_CATEGORY_SERVICE] DBMS에 정상적으로 반영되지 않았습니다.");
            throw new NotApplyOnDbmsException();
        }
    }

    private void checkExisted(String cate_code) {
        boolean exists = boardCategoryDao.existsByCateCode(cate_code);
        if (!exists) {
            log.info("[BOARD_CATEGORY_SERVICE] 존재하지 않는 키 값입니다. : {}", cate_code);
            throw new BoardCategoryNotFoundException();
        }
    }

    private void checkExistedForUpdate(String cate_code) {
        boolean exists = boardCategoryDao.existsByCateCodeForUpdate(cate_code);
        if (!exists) {
            log.info("[BOARD_CATEGORY_SERVICE] 존재하지 않는 키 값입니다. : {}", cate_code);
            throw new BoardCategoryNotFoundException();
        }
    }

    private int removeLevelByLevel() {
        int rowCnt = 0;
        for (int i= BoardCategory.MAX_LEVEL; i>0; i--)
            rowCnt += boardCategoryDao.deleteByLevel(i);
        return rowCnt;
    }


    private BoardCategoryDto createDto(BoardCategoryRequest request) {
        return BoardCategoryDto.builder()
                               .cate_code(request.getCate_code())
                               .top_cate(request.getTop_cate())
                               .name(request.getName())
                               .ord(request.getOrd())
                               .chk_use(request.getChk_use())
                               .level(request.getLevel())
                               .comt(request.getComt())
                               .reg_user_seq(formatter.getManagerSeq())
                               .reg_date(formatter.getCurrentDateFormat())
                               .up_date(formatter.getCurrentDateFormat())
                               .up_user_seq(formatter.getManagerSeq())
                               .build();
    }

    private BoardCategoryResponse createResponse(BoardCategoryDto dto) {
        return BoardCategoryResponse.builder()
                                    .cate_code(dto.getCate_code())
                                    .top_cate(dto.getTop_cate())
                                    .name(dto.getName())
                                    .ord(dto.getOrd())
                                    .chk_use(dto.getChk_use())
                                    .level(dto.getLevel())
                                    .comt(dto.getComt())
                                    .build();
    }

}
