package com.example.demo.application.comment;

import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

    private final CommentDaoImpl commentDao;
    private final CustomFormatter formatter;
}
