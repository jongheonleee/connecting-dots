package com.example.demo.dto.report;

import jakarta.validation.constraints.Max;
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
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReportRequest {

    private Integer rno;

    @NotBlank(message = "cate_code는 필수값입니다.")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "cate_code 대문자 2글자와 숫자 6자리로 구성되어야 합니다.")
    private String cate_code;

    @NotBlank(message = "title은 필수값입니다.")
    private String title;

    @NotBlank(message = "cont는 필수값입니다.")
    private String cont;

    private String chk_change;

    private String comt;

    @NotNull(message = "repo_seq는 필수값입니다.")
    private Integer repo_seq; // 신고자

    @NotNull(message = "resp_seq는 필수값입니다.")
    private Integer resp_seq; // 신고 대상자
    private Integer boar; // bno
    private Integer cmnt; // cno
    private Integer repl; // rcno


    @NotNull(message = "type는 필수값입니다.")
    @Range(min = 0, max = 2, message = "type은 0, 1, 2 중 하나여야 합니다.")
    private Integer type; // 0 : bno, 1 : cno, 2 : rcno


    public ReportDto toDto(final String currDateTime, final  String lastDateTime, final Integer managerSeq) {
        return ReportDto.builder()
                        .rno(rno)
                        .cate_code(cate_code)
                        .title(title)
                        .cont(cont)
                        .chk_change(chk_change)
                        .comt(comt)
                        .repo_seq(repo_seq)
                        .resp_seq(resp_seq)
                        .boar(boar)
                        .cmnt(cmnt)
                        .repl(repl)
                        .type(type)
                        .reg_date(currDateTime)
                        .reg_user_seq(managerSeq)
                        .up_date(currDateTime)
                        .up_user_seq(managerSeq)
                        .build();
    }
}
