package com.example.demo.dto.reply;

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
public class ReplyChangeHistoryRequest {

    private Integer seq;
    @NotNull(message = "rcno는 필수값 입니다.")
    private Integer rcno;

    @NotBlank(message = "변경 전 내용은 필수값 입니다.")
    private String bef_cont;

    @NotBlank(message = "변경 후 내용은 필수값 입니다.")
    private String aft_cont;

    @NotBlank(message = "적용 시간은 필수값 입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "적용 시간은 yyyy-MM-dd HH:mm:ss 형식이어야 합니다.")
    private String appl_time;

    private String comt;

    @NotNull(message = "cno는 필수값 입니다.")
    private Integer cno;

    @NotNull(message = "bno는 필수값 입니다.")
    private Integer bno;
}
