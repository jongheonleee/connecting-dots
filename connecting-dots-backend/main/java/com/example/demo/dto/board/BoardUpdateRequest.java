package com.example.demo.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class BoardUpdateRequest {

    private Integer bno;

    @NotBlank(message = "cate_code 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "cate_code 대문자 2글자와 숫자 6자리로 구성되어야 합니다.")
    private String cate_code;


    @NotNull(message = "user_seq 필수값입니다.")
    private Integer user_seq;

    @NotBlank(message = "writer 필수값입니다.")
    private String writer;

    @NotBlank(message = "title 필수값입니다.")
    private String title;

    @NotBlank(message = "cont 필수값입니다.")
    private String cont;
    private String comt;
}
