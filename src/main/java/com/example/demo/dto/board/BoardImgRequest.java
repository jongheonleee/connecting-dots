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
public class BoardImgRequest {
    @NotNull(message = "bno 필수값입니다.")
    private Integer bno;
    private String name;
    @NotBlank(message = "chk_thumb 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_thumb은 Y, N만 허용됩니다.")
    private String chk_thumb;
    @NotBlank(message = "img 필수값입니다.")
    private String img;
    private String comt;
}
