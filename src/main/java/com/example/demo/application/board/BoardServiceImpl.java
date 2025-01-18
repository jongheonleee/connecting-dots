package com.example.demo.application.board;

import com.example.demo.application.code.CommonCodeServiceImpl;
import com.example.demo.repository.mybatis.board.BoardDaoImpl;
import com.example.demo.repository.mybatis.board.BoardImgDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl {

    private final BoardDaoImpl boardDao;// [✅]
    private final BoardCategoryServiceImpl boardCategoryService;// [✅]
    private final BoardImgServiceImpl boardImgService; // -> 게시판 생성, 조회시 이미지 처리[✅]
    private final BoardStatusServiceImpl boardStatusService; // -> 게시판 상태 변경시 적용[✅]
    private final BoardChangeHistoryServiceImpl boardChangeHistoryService; // -> 게시판 변경 이력 기록[]
//    private final UserServiceImpl userService; -> 게시판과 관련된 사용자 정보[]
    private final CustomFormatter formatter;


}
