package com.example.demo.controller.reply;

import com.example.demo.dto.reply.ReplyRequest;
import com.example.demo.dto.reply.ReplyResponse;
import com.example.demo.service.reply.ReplyService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/{rcno}")
    public ResponseEntity<ReplyResponse> readByRcno(@PathVariable("rcno") Integer rcno) {
        return ResponseEntity.ok(replyService.readByRcno(rcno));
    }

    @GetMapping("/like/{rcno}")
    public ResponseEntity<Void> increaseLikeCnt(@PathVariable("rcno") Integer rcno) {
        replyService.increaseLikeCnt(rcno);
        return ResponseEntity.noContent()
                             .build();
    }

    @GetMapping("/dislike/{rcno}")
    public ResponseEntity<Void> increaseDislikeCnt(@PathVariable("rcno") Integer rcno) {
        replyService.increaseDislikeCnt(rcno);
        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ReplyResponse> create(@RequestBody @Valid ReplyRequest request) {
        var response = replyService.create(request);
        return ResponseEntity.created(URI.create("/api/reply/" + response.getRcno()))
                             .body(response);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modify(@RequestBody @Valid ReplyRequest request) {
        replyService.modify(request);
        return ResponseEntity.noContent()
                             .build();
    }

    @DeleteMapping("/{rcno}")
    public ResponseEntity<Void> remove(@PathVariable("rcno") Integer rcno) {
        replyService.remove(rcno);
        return ResponseEntity.noContent()
                             .build();
    }

}
