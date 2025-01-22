package com.example.demo.dto.board;

import com.example.demo.dto.comment.CommentDetailResponse;
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
public class BoardDetailResponse {

    // 게시글 정보
    private Integer user_seq;
    private Integer bno;
    private String title;
    private String writer;
    private Integer view_cnt;
    private Integer reco_cnt;
    private Integer not_reco_cnt;
    private String cont;

    // 게시글 카테고리
    private BoardCategoryResponse category;

    // 게시글 이미지
    private List<BoardImgResponse> boardImgs;

    // 게시글 댓글
    private List<CommentDetailResponse> comments;

    public static BoardDetailResponse of(BoardDto board, BoardCategoryResponse category, List<BoardImgResponse> boardImgs, List<CommentDetailResponse> comments) {
        return BoardDetailResponse.builder()
            .user_seq(board.getUser_seq())
            .bno(board.getBno())
            .title(board.getTitle())
            .writer(board.getWriter())
            .view_cnt(board.getView_cnt())
            .reco_cnt(board.getReco_cnt())
            .not_reco_cnt(board.getNot_reco_cnt())
            .cont(board.getCont())
            .category(category)
            .boardImgs(boardImgs)
            .comments(comments)
            .build();
    }
}
