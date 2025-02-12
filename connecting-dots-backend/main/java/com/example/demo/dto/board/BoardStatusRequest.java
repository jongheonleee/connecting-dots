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
public class BoardStatusRequest {

    @NotBlank(message = "code는 필수값입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.")
    private String stat_code;

    @NotNull(message = "bno는 필수값입니다.")
    private Integer bno;

    @NotBlank(message = "days는 필수값입니다.")
    private Integer days;

    private String comt;
}
