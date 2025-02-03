package com.example.demo.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReportProcessDetailsDto {

    private Integer seq;
    private Integer rno;
    private String pros_code;
    private String appl_begin;
    private String appl_end;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;
    private String chk_use;

    public ReportProcessDetailsResponse toResponse() {
        return ReportProcessDetailsResponse.builder()
                .seq(seq)
                .rno(rno)
                .pros_code(pros_code)
                .appl_begin(appl_begin)
                .appl_end(appl_end)
                .chk_use(chk_use)
                .build();
    }

    public void updateApplEnd(String appl_end, String up_date, Integer up_user_seq) {
        this.appl_end = appl_end;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }
}
