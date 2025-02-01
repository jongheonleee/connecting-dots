package com.example.demo.dto.report;

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
public class ReportChangeHistoryRequest {


    private Integer seq;

    @NotNull(message = "bno 필수값입니다.")
    private Integer rno;

    @NotBlank(message = "title 필수값입니다.")
    private String title;

    @NotBlank(message = "cont 필수값입니다.")
    private String cont;

    private String comt;

    public ReportChangeHistoryDto toDto(final String currDateTime, final Integer managerSeq, final String applBeginTime, final String applEndTime) {
        return ReportChangeHistoryDto.builder()
                .seq(seq)
                .rno(rno)
                .title(title)
                .cont(cont)
                .comt(comt)
                .appl_begin(applBeginTime)
                .appl_end(applEndTime)
                .reg_date(currDateTime)
                .reg_user_seq(managerSeq)
                .up_date(currDateTime)
                .up_user_seq(managerSeq)
                .build();
    }
}
