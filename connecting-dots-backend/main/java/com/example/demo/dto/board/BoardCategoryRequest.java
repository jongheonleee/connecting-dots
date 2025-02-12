package com.example.demo.dto.board;

import jakarta.validation.constraints.Min;
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
public class BoardCategoryRequest {
    @NotBlank(message = "cate_code 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "cate_code 대문자 2글자와 숫자 6자리로 구성되어야 합니다.")
    private String cate_code;

    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "top_cate 대문자 2글자와 숫자 6자리로 구성되어야 합니다.")
    private String top_cate;

    @NotBlank(message = "name 필수값입니다.")
    private String name;

    @NotNull(message = "ord 필수값입니다.")
    @Min(value = 1, message = "ord는 1 이상의 숫자만 허용됩니다.")
    private Integer ord;

    @NotBlank(message = "chk_use 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use은 Y, N만 허용됩니다.")
    private String chk_use;

    @NotNull(message = "level 필수값입니다.")
    @Min(value = 1, message = "level는 1 이상의 숫자만 허용됩니다.")
    private Integer level;

    private String comt;

}
