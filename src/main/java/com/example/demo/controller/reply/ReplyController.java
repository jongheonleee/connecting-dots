package com.example.demo.controller.reply;

import com.example.demo.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;
}
