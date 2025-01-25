package com.example.demo.application.board.impl;

import static com.example.demo.domain.Code.BOARD_CREATE;
import static com.example.demo.domain.Code.BOARD_MODIFY;

import com.example.demo.domain.Code;
import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardCategoryResponse;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardDetailResponse;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.dto.board.BoardImgResponse;
import com.example.demo.dto.board.BoardMainDto;
import com.example.demo.dto.board.BoardMainResponse;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.dto.board.BoardUpdateRequest;
import com.example.demo.dto.comment.CommentDetailResponse;
import com.example.demo.global.error.exception.business.BusinessException;
import com.example.demo.global.error.exception.business.InvalidValueException;
import com.example.demo.global.error.exception.business.board.BoardCategoryNotFoundException;
import com.example.demo.global.error.exception.business.board.BoardInvalidContentException;
import com.example.demo.global.error.exception.business.board.BoardNotFoundException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.global.error.exception.technology.network.RetryFailedException;
import com.example.demo.repository.mybatis.board.BoardCategoryDaoImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl {

    private static final int MAX_RETRY = 10;
    private static final int RETRY_DELAY = 5_000;

    private final BoardDaoImpl boardDao;// [✅]
    private final BoardCategoryDaoImpl boardCategoryDao;// [✅]
    private final BoardImgServiceImpl boardImgService; // -> 게시판 생성, 조회시 이미지 처리[✅]
    private final BoardStatusServiceImpl boardStatusService; // -> 게시판 상태 변경시 적용[✅]
    private final BoardChangeHistoryServiceImpl boardChangeHistoryService; // -> 게시판 변경 이력 기록[✅]
    private final BoardCategoryServiceImpl boardCategoryService; // -> 게시판 카테고리 정보[✅]
    //    private final UserServiceImpl userService; -> 게시판과 관련된 사용자 정보[]
//    private final CommentServiceImpl commentService; // -> 게시판과 관련된 댓글 정보[]
//    private final ReplyServiceImpl replyService; // -> 게시판과 관련된 답글 정보[]
    private final CustomFormatter formatter;


    public int count() {
        return boardDao.count();
    }

    // 핵심 데이터는 재시도 복구 처리 대상
    @Retryable(
            value = { // exclude 외의 런타임 예외는 재시도 복구 처리
                    RuntimeException.class
            },
            exclude = { // 작성된 예외는 재시도 처리를 해도 의미가 없는 예외이므로 예외 처리에서 제외
                    BusinessException.class, InvalidValueException.class, DataIntegrityViolationException.class,
                    SQLSyntaxErrorException.class, InvalidDataAccessApiUsageException.class, InvalidDataAccessResourceUsageException.class,
                    EmptyResultDataAccessException.class, TypeMismatchDataAccessException.class
            },
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY),
            recover = "recover"
    )
    @Transactional(rollbackFor = Exception.class)
    public BoardResponse create(final BoardRequest request, final List<MultipartFile> files) {
        var boardDto = createBoardDto(request);
        checkApplied(1, boardDao.insert(boardDto));
        createBoardImages(boardDto, files);
        boardStatusService.create(createBoardStatusRequest(boardDto));
        boardChangeHistoryService.createInit(boardDto.getBno(), createBoardInitHistoryRequest(boardDto));
        return createResponse(boardDto);
    }

    @Recover
    public BoardResponse recover(RuntimeException e) {
        log.error("[BOARD] 게시글 예외 복구를 위해 재시도를 했지만 실패했습니다. 최대 재시도 횟수 : {}, 재시도 간격 : {}ms", MAX_RETRY, RETRY_DELAY);
        log.error("[BOARD] 예외 내용 : {}", e.getMessage());
        throw new RetryFailedException();
    }

    @Transactional(readOnly = true)
    public PageResponse readForMain(Integer page, Integer pageSize) {
        int totalCnt = boardDao.count();

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageSize", pageSize);

        List<BoardMainResponse> responses = boardDao.selectForMain(map)
                                                    .stream()
                                                    .map(this::createMainResponse)
                                                    .toList();

        SearchCondition sc = new SearchCondition(page, pageSize);
        return new PageResponse<BoardMainResponse>(totalCnt, sc, responses);
    }

    @Transactional(readOnly = true)
    public PageResponse readByCategoryForMain(String cateCode, Integer page, Integer pageSize) {
        boolean exists = boardCategoryDao.existsByCateCode(cateCode);
        if (!exists) {
            log.error("[BOARD] 해당 카테고리가 존재하지 않습니다. cateCode: {}", cateCode);
            throw new BoardCategoryNotFoundException();
        }

        int totalCnt = boardDao.countByCategory(cateCode);

        Map<String, Object> map = new HashMap<>();
        map.put("cate_code", cateCode);
        map.put("page", page);
        map.put("pageSize", pageSize);

        List<BoardMainResponse> responses = boardDao.selectForMainByCategory(map)
                                                    .stream()
                                                    .map(this::createMainResponse)
                                                    .toList();

        SearchCondition sc = new SearchCondition(page, pageSize);
        return new PageResponse<BoardMainResponse>(totalCnt, sc, responses);
    }

    @Transactional(readOnly = true)
    public PageResponse readBySearchConditionForMain(SearchCondition sc) {
        int totalCnt = boardDao.countBySearchCondition(sc);
        List<BoardMainDto> found = boardDao.selectForMainBySearchCondition(sc);
        List<BoardMainResponse> responses = found.stream()
                                                .map(this::createMainResponse)
                                                .toList();

        return new PageResponse<BoardMainResponse>(totalCnt, sc, responses);
    }

    @Transactional(readOnly = true)
    public BoardDetailResponse readDetailByBno(Integer bno) {
        boolean exists = boardDao.existsByBno(bno);
        if (!exists) {
            log.error("[BOARD] 해당 게시글이 존재하지 않습니다. bno: {}", bno);
            throw new BoardNotFoundException();
        }

        var boardStatusResponse = boardStatusService.readByBnoAtPresent(bno);
        Code code = Code.of(boardStatusResponse.getStat_code());
        if (code.equals(Code.BOARD_REMOVE) || code.equals(Code.BOARD_SANCTION) || code.equals(Code.BOARD_NOT_SHOW)) {
            log.error("[BOARD] 해당 게시글은 삭제 되었거나 제재된 게시글이거나 비공개 게시글입니다. bno: {}", bno);
            throw new BoardInvalidContentException();
        }


        BoardDto boardDto = boardDao.select(bno);
        BoardCategoryResponse boardCategoryResponse = boardCategoryService.readByCateCode(boardDto.getCate_code());
        List<BoardImgResponse> boardImgResponses = boardImgService.readByBno(bno);
        List<CommentDetailResponse> commentDetailResponses = null; // =  commentService.readByBnoForDetail(bno);

        return BoardDetailResponse.of(boardDto, boardCategoryResponse, boardImgResponses, commentDetailResponses);
    }


    @Retryable(
            value = { // exclude 외의 런타임 예외는 재시도 복구 처리
                    RuntimeException.class
            },
            exclude = { // 작성된 예외는 재시도 처리를 해도 의미가 없는 예외이므로 예외 처리에서 제외
                    BusinessException.class, InvalidValueException.class, DataIntegrityViolationException.class,
                    SQLSyntaxErrorException.class, InvalidDataAccessApiUsageException.class, InvalidDataAccessResourceUsageException.class,
                    EmptyResultDataAccessException.class, TypeMismatchDataAccessException.class
            },
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY),
            recover = "recover"
    )
    @Transactional(rollbackFor = Exception.class)
    public void modify(final BoardUpdateRequest request, final List<MultipartFile> files) {
        // 게시글이 존재하는지 확인
        boolean exists = boardDao.existsByBnoForUpdate(request.getBno());
        if (!exists) {
            log.error("[BOARD] 해당 게시글이 존재하지 않습니다. bno: {}", request.getBno());
            throw new BoardNotFoundException();
        }

        // 게시글을 수정한다
        BoardDto boardDto = BoardDto.builder()
                                    .bno(request.getBno())
                                    .cate_code(request.getCate_code())
                                    .user_seq(request.getUser_seq())
                                    .writer(request.getWriter())
                                    .title(request.getTitle())
                                    .cont(request.getCont())
                                    .comt(request.getComt())
                                    .up_user_seq(formatter.getManagerSeq())
                                    .up_date(formatter.getCurrentDateFormat())
                                    .build();
        checkApplied(1, boardDao.update(boardDto));

        // 이미지를 수정한다
        if (files != null && !files.isEmpty()) { // 이미지가 존재하면
            List<BoardImgResponse> foundOldBoardImgs = boardImgService.readByBno(request.getBno());
            // 기존 이미지 삭제한다
            for (BoardImgResponse foundOldBoardImg : foundOldBoardImgs) {
                boardImgService.removeByIno(foundOldBoardImg.getIno());
            }

            // 새로운 이미지를 등록한다
            createBoardImages(boardDto, files);
        }

        // 게시글 수정 후 현재값을 변경 이력에 기록하고 종료 시간을 설정함
        BoardChangeHistoryRequest newHistoryRequest = BoardChangeHistoryRequest.builder()
                                                                            .bno(request.getBno())
                                                                            .title(request.getTitle())
                                                                            .cont(request.getCont())
                                                                            .build();
        boardChangeHistoryService.renewBoardChangeHistory(request.getBno(), newHistoryRequest);


        // 상태를 수정한다
        BoardStatusRequest newStatusRequest = BoardStatusRequest.builder()
                                                                .bno(request.getBno())
                                                                .stat_code(BOARD_MODIFY.getCode())
                                                                .days(0)
                                                                .build();
        boardStatusService.renewState(newStatusRequest);
        // 변경 이력에 수정값을 등록한다
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(final Integer bno) {
        // 댓글과 대댓글을 삭제한다

        // 이미지를 삭제한다
        List<BoardImgResponse> foundOldBoardImgs = boardImgService.readByBno(bno);
        // 기존 이미지 삭제한다
        for (BoardImgResponse foundOldBoardImg : foundOldBoardImgs) {
            boardImgService.removeByIno(foundOldBoardImg.getIno());
        }

        // 상태를 삭제한다
        boardStatusService.removeByBno(bno);
        // 변경 이력을 삭제한다
        boardChangeHistoryService.removeByBno(bno);

        // 게시글을 삭제한다
        checkApplied(1, boardDao.delete(bno));
    }

    private void checkApplied(final Integer expected, final Integer actual) {
        if (expected != actual) {
            log.error("[BOARD] 적용된 행의 수가 일치하지 않습니다. expected: {}, actual: {}", expected, actual);
            throw new NotApplyOnDbmsException();
        }
    }

    private void createBoardImages(final BoardDto dto, final List<MultipartFile> files) {
        for (int i=0; i< files.size(); i++) {
            MultipartFile file = files.get(i);
            BoardImgRequest imgRequest = BoardImgRequest.builder()
                    .bno(dto.getBno())
                    .chk_thumb(i == 0 ? "Y" : "N") // 첫 번째 사진은 항상 썸네일로 설정
                    .build();
            boardImgService.saveBoardImage(imgRequest, file);
        }
    }


    private BoardStatusRequest createBoardStatusRequest(final BoardDto dto) {
        return BoardStatusRequest.builder()
                .bno(dto.getBno())
                .stat_code(BOARD_CREATE.getCode())
                .days(0)
                .build();
    }

    private BoardChangeHistoryRequest createBoardInitHistoryRequest(final BoardDto dto) {
        return BoardChangeHistoryRequest.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .cont(dto.getCont())
                .build();
    }

    private BoardDto createBoardDto(final BoardRequest request) {
        return BoardDto.builder()
                .cate_code(request.getCate_code())
                .user_seq(request.getUser_seq())
                .writer(request.getWriter())
                .title(request.getTitle())
                .cont(request.getCont())
                .comt(request.getComt())
                .reg_user_seq(formatter.getManagerSeq())
                .up_user_seq(formatter.getManagerSeq())
                .reg_date(formatter.getCurrentDateFormat())
                .up_date(formatter.getCurrentDateFormat())
                .build();
    }

    private BoardResponse createResponse(final BoardDto dto) {
        return BoardResponse.builder()
                .user_seq(dto.getUser_seq())
                .bno(dto.getBno())
                .cate_code(dto.getCate_code())
                .writer(dto.getWriter())
                .title(dto.getTitle())
                .view_cnt(dto.getView_cnt())
                .reco_cnt(dto.getReco_cnt())
                .not_reco_cnt(dto.getNot_reco_cnt())
                .cont(dto.getCont())
                .comt(dto.getComt())
                .build();
    }

    private BoardMainResponse createMainResponse(BoardMainDto dto) {
        return BoardMainResponse.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .cate_name(dto.getCate_name())
                .reg_date(dto.getReg_date())
                .view_cnt(dto.getView_cnt())
                .reco_cnt(dto.getReco_cnt())
                .thumb(dto.getThumb())
                .comment_cnt(dto.getComment_cnt())
                .build();
    }


}