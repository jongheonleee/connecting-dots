package com.example.demo.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceRuleUse {

    private static final StringBuilder sb = new StringBuilder();
    private static final String SPACE = " ";

    private String rule_stat;
    private String name;
    private String tar_name;
    private String op;
    private String val;
    private String code;
    private String chk_use;

    // 조건절 생성
    // tar_name op val
    // 추후에 서비스 정책에서 다량의 조건절을 만들어서 jdbc로 쿼리를 날릴 때 사용
    public String getCondition() {
        sb.setLength(0);
        sb.append(tar_name).append(SPACE).append(op).append(SPACE).append(val);
        return sb.toString();
    }
}
