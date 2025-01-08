package com.example.demo.dto.code;

import com.example.demo.domain.Code;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeRequest {

    @NotBlank(message = "level은 필수값입니다.")
    private Integer level;

    @NotBlank(message = "code는 필수값입니다.")
    @Pattern(regexp = "^[0-9]{4}$", message = "code는 4자리 숫자 형태로 구성되어야 합니다.")
    private String code;

    @NotBlank(message = "name은 필수값입니다.")
    @Pattern(regexp = "^.{1,25}$", message = "name은 최대 25자까지만 허용됩니다.")
    private String name;

    @NotBlank(message = "필수 여부는 필수값입니다.")
    @Pattern(regexp = "^[YN]$", message = "chk_use는 Y, N만 허용됩니다.")
    private String chk_use;

    private String top_code;

    public Code toCode() {
        return new Code(this);
    }

}
