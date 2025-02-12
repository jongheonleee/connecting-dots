package com.example.demo.dto.comment;

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
public class CommentRequest {

    private Integer cno;
    @NotNull(message = "bno 필수값입니다.")
    private Integer bno;
    @NotBlank(message = "cont 필수값입니다.")
    private String cont;
    @NotNull(message = "user_seq 필수값입니다.")
    private Integer user_seq;
    @NotBlank(message = "writer 필수값입니다.")
    private String writer;
}
