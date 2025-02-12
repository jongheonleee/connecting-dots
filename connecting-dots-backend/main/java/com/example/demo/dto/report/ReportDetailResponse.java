package com.example.demo.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReportDetailResponse {

    private Integer rno;
    private ReportCategoryResponse category;
    private ReportProcessDetailsResponse process;
    private String title;
    private String cont;
    private String reg_date;
    private Integer repo_seq; // 신고자 -> 추후에 UserResponse로 변경
    private Integer resp_seq; // 신고 대상자 -> 추후에 UserResponse로 변경

    public static ReportDetailResponse of(final ReportDto reportDto, final ReportCategoryResponse category, final ReportProcessDetailsResponse process) {
        return ReportDetailResponse.builder()
                                   .rno(reportDto.getRno())
                                   .category(category)
                                   .process(process)
                                   .title(reportDto.getTitle())
                                   .cont(reportDto.getCont())
                                   .reg_date(reportDto.getReg_date())
                                   .repo_seq(reportDto.getRepo_seq())
                                   .resp_seq(reportDto.getResp_seq())
                                   .build();
    }
}
