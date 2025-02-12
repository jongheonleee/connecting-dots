package com.example.demo.dto.comment;

import com.example.demo.dto.reply.ReplyResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommentDetailResponse {
    private CommentResponse comment;
    private List<ReplyResponse> replies;
}
