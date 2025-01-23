package com.example.demo.application.comment;

import com.example.demo.repository.mybatis.comment.CommentChangeHistoryDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentChangeHistoryServiceImpl {

    private final CommentChangeHistoryDaoImpl commentChangeHistoryDao;
    private final CustomFormatter formatter;

}
