package com.example.demo.application.reply;

import com.example.demo.repository.mybatis.reply.ReplyChangeHistoryDaoImpl;
import com.example.demo.repository.mybatis.reply.ReplyDaoImpl;
import com.example.demo.utils.CustomFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl {

    private final ReplyDaoImpl replyDao;
    private final ReplyChangeHistoryDaoImpl replyChangeHistoryDao;
    private final CustomFormatter formatter;
}
