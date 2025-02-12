package com.example.demo.dto.reply;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReplyRequest {

    private Integer rcno;
    @NotNull(message = "cno는 필수값입니다.")
    private Integer cno;
    @NotNull(message = "bno는 필수값입니다.")
    private Integer bno;
    @NotBlank(message = "cont는 필수값입니다.")
    private String cont;
    private Integer like_cnt;
    private Integer dislike_cnt;
    @NotNull(message = "user_seq는 필수값입니다.")
    private Integer user_seq;
    private String writer;
}
