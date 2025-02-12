package com.example.demo.service.board;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.SearchCondition;
import com.example.demo.dto.board.BoardDetailResponse;
import com.example.demo.dto.board.BoardRequest;
import com.example.demo.dto.board.BoardResponse;
import com.example.demo.dto.board.BoardUpdateRequest;
import com.example.demo.global.error.exception.business.BusinessException;
import com.example.demo.global.error.exception.business.InvalidValueException;
import com.example.demo.service.board.impl.BoardServiceImpl;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    int count();

    BoardResponse create(BoardRequest request, List<MultipartFile> files);

    BoardResponse recover(RuntimeException e);

    PageResponse readForMain(Integer page, Integer pageSize);

    PageResponse readByCategoryForMain(String cateCode, Integer page, Integer pageSize);

    PageResponse readBySearchConditionForMain(SearchCondition sc);

    BoardDetailResponse readDetailByBno(Integer bno);

    PageResponse readByUserSeqForMain();

    void modify(BoardUpdateRequest request, List<MultipartFile> files);

    void increaseReco(Integer bno);

    void increaseNotReco(Integer bno);

    void remove(Integer bno);
}
