package com.example.demo.application.board;

import static com.example.demo.domain.Code.BOARD_CREATE;

import com.example.demo.application.code.CommonCodeServiceImpl;
import com.example.demo.domain.BoardCategory;
import com.example.demo.domain.Code;
import com.example.demo.dto.board.BoardChangeHistoryRequest;
import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardImgRequest;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.BoardStatusDto;
import com.example.demo.dto.board.BoardStatusRequest;
import com.example.demo.global.error.exception.business.BusinessException;
import com.example.demo.global.error.exception.business.InvalidValueException;
import com.example.demo.global.error.exception.technology.database.NotApplyOnDbmsException;
import com.example.demo.global.error.exception.technology.network.RetryFailedException;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.utils.CustomFormatter;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
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
    private final BoardCategoryServiceImpl boardCategoryService;// [✅]
    private final BoardImgServiceImpl boardImgService; // -> 게시판 생성, 조회시 이미지 처리[✅]
    private final BoardStatusServiceImpl boardStatusService; // -> 게시판 상태 변경시 적용[✅]
    private final BoardChangeHistoryServiceImpl boardChangeHistoryService; // -> 게시판 변경 이력 기록[✅]
//    private final UserServiceImpl userService; -> 게시판과 관련된 사용자 정보[]
//    private final CommentServiceImpl commentService; // -> 게시판과 관련된 댓글 정보[]
//    private final ReplyServiceImpl replyService; // -> 게시판과 관련된 답글 정보[]
    private final CustomFormatter formatter;


    public int count() {
        return boardDao.count();
    }

    // 핵심 데이터는 재시도 복구 처리 대상
    @Retryable(
            value = {RuntimeException.class},
            exclude = {BusinessException.class, DuplicateKeyException.class, InvalidValueException.class, SQLSyntaxErrorException.class}, // 작성된 예외는 재시도 대상에서 제외
            maxAttempts = MAX_RETRY,
            backoff = @Backoff(delay = RETRY_DELAY)
    )
    @Transactional(rollbackFor = Exception.class)
    public BoardResponse create(final BoardRequest request, final List<MultipartFile> files) {
        // 게시글을 등록한다
        var dto = createDto(request);
        checkApplied(1, boardDao.insert(dto));
        // 이미지를 등록한다
        createBoardImages(dto, files);
        // 상태를 등록한다
        var boardStatusRequest = createBoardStatus(dto);
        boardStatusService.create(boardStatusRequest);
        // 변경 이력에 초기값을 등록한다
        var boardChangeHistoryRequest = createBoardInitHistory(dto);
        boardChangeHistoryService.createInit(dto.getBno(), boardChangeHistoryRequest);
        return createResponse(dto);
    }


    @Recover
    public BoardResponse recoverCreate(RuntimeException e) {
        log.error("[BOARD] 게시글 예외 복구를 위해 재시도를 했지만 실패했습니다. 최대 재시도 횟수 : {}, 재시도 간격 : {}ms", MAX_RETRY, RETRY_DELAY);
        log.error("[BOARD] 예외 내용 : {}", e.getMessage());
        throw new RetryFailedException();
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


    private BoardStatusRequest createBoardStatus(final BoardDto dto) {
        return BoardStatusRequest.builder()
                                 .bno(dto.getBno())
                                 .stat_code(BOARD_CREATE.getCode())
                                 .days(0)
                                 .build();
    }

    private BoardChangeHistoryRequest createBoardInitHistory(final BoardDto dto) {
        return BoardChangeHistoryRequest.builder()
                                        .bno(dto.getBno())
                                        .title(dto.getTitle())
                                        .cont(dto.getCont())
                                        .build();
    }

    private BoardDto createDto(final BoardRequest request) {
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

}
