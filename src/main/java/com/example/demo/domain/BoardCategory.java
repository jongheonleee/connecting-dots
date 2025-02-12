package com.example.demo.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BoardCategory {

    // 최상위 카테고리
    BOARD_ROOT_CATEGORY(0, "BC000000", "최상위 카테고리", null),

    // 대분류 카테고리 -> 커리어, 재태크, 자기계발, 학업
    BOARD_MAIN_CATEGORY_CAREER(1, "BC010000", "경력", "BC000000"),
    BOARD_MAIN_CATEGORY_FINANCE(1, "BC020000", "재태크", "BC000000"),
    BOARD_MAIN_CATEGORY_SELF_DEVELOPMENT(1, "BC030000", "자기계발", "BC000000"),
    BOARD_MAIN_CATEGORY_STUDY(1, "BC040000", "학업", "BC000000"),

    // 중분류 카테고리 -> (커리어 부분만 해당) 전체, IT, 비즈니스/창업, 기획/UX, 마케팅, 데이터분석, 크리에이티브
    BOARD_SUB_CATEGORY_CAREER_ALL(2, "BC010100", "전체", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_IT(2, "BC010200", "IT", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_BUSINESS(2, "BC010300", "비즈니스/창업", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_PLANNING_UX(2, "BC010400", "기획/UX", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_MARKETING(2, "BC010500", "마케팅", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_DATA_ANALYSIS(2, "BC010600", "데이터분석", "BC010000"),
    BOARD_SUB_CATEGORY_CAREER_CREATIVE(2, "BC010700", "크리에이티브", "BC010000"),

    // 소분류 카테고리 -> (커리어 - IT 부분만 해당) 인공지능, 프론트엔드 개발, 백엔드 개발, 데이터 사이언스, DevOps/Infra, 게임 개발, 컴퓨터 공학/SW 엔지니어, 프로젝트
    BOARD_DETAIL_CATEGORY_CAREER_IT_AI(3, "BC010201", "인공지능", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_FRONTEND(3, "BC010202", "프론트엔드 개발", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_BACKEND(3, "BC010203", "백엔드 개발", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_DATA_SCIENCE(3, "BC010204", "데이터 사이언스", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_DEVOPS_INFRA(3, "BC010205", "DevOps/Infra", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_GAME_DEVELOPMENT(3, "BC010206", "게임 개발", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_COMPUTER_ENGINEERING_SW_ENGINEER(3, "BC010207", "컴퓨터 공학/SW 엔지니어", "BC010200"),
    BOARD_DETAIL_CATEGORY_CAREER_IT_PROJECT(3, "BC010208", "프로젝트", "BC010200");


    public static final Integer MAX_LEVEL = 3;

    private final Integer level;
    private final String cateCode;
    private final String name;
    private final String topCode;

    private BoardCategory(Integer level, String cateCode, String name, String topCode) {
        this.level = level;
        this.cateCode = cateCode;
        this.name = name;
        this.topCode = topCode;
    }

    public static BoardCategory of(String cateCode) {
        return Arrays.stream(BoardCategory.values())
                .filter(c -> c.getCateCode().equals(cateCode))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
