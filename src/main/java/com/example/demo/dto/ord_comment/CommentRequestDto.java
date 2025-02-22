package com.example.demo.dto.ord_comment;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequestDto {

    private Integer bno;
    private Integer cno;
    private String comment;
    private String writer;

    public CommentResponseDto createCommentDto() {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setBno(bno);
        commentResponseDto.setContent(comment);
        commentResponseDto.setWriter(writer);
        return commentResponseDto;
    }
}
