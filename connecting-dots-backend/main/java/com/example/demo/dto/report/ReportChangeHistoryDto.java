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
public class ReportChangeHistoryDto {

    private Integer seq;
    private Integer rno;
    private String title;
    private String cont;
    private String appl_begin;
    private String appl_end;
    private String comt;
    private String reg_date;
    private Integer reg_user_seq;
    private String up_date;
    private Integer up_user_seq;

    public ReportChangeHistoryResponse toResponse() {
        return ReportChangeHistoryResponse.builder()
                                        .seq(seq)
                                        .rno(rno)
                                        .title(title)
                                        .cont(cont)
                                        .appl_begin(appl_begin)
                                        .appl_end(appl_end)
                                        .comt(comt)
                                        .build();
    }

    public void updateApplEnd(final String appl_end, final String up_date, final Integer up_user_seq) {
        this.appl_end = appl_end;
        this.up_date = up_date;
        this.up_user_seq = up_user_seq;
    }

}
