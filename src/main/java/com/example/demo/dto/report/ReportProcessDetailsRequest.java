package com.example.demo.dto.report;

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
public class ReportProcessDetailsRequest {
    private Integer seq;

    @NotNull(message = "rno는 필수값입니다.")
    private Integer rno;

    @NotBlank(message = "pros_code는 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "pros_code 대문자 2글자와 숫자 6자리로 구성되어야 합니다.")
    private String pros_code;

    private String chk_use;

    public ReportProcessDetailsDto toDto(final String currDateTime, final Integer managerSeq, final String applBeginTime, final String applEndTime) {
        return ReportProcessDetailsDto.builder()
                                    .seq(seq)
                                    .rno(rno)
                                    .pros_code(pros_code)
                                    .appl_begin(applBeginTime)
                                    .appl_end(applEndTime)
                                    .chk_use(chk_use)
                                    .reg_date(currDateTime)
                                    .reg_user_seq(managerSeq)
                                    .up_date(currDateTime)
                                    .up_user_seq(managerSeq)
                                    .build();
    }
}
