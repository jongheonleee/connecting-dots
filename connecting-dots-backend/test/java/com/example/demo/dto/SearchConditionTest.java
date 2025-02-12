package com.example.demo.dto;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchConditionTest {

    private SearchCondition target;

    @DisplayName("기본 생성자로 생성한 경우 -  ?page=10&pageSize=10&option=&keyword=")
    @Test
    public void test1() {
        target = new SearchCondition();
        String expectedQuery = "?page=1&pageSize=10&searchOption=&searchKeyword=&sortOption=";
        assertEquals(expectedQuery, target.getQueryString(1));
    }

    @DisplayName("생성자에 page 값 넣어준 경우 -  ?page=[page]&pageSize=10&searchOption=&searchKeyword=")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void test2(int page) {
        target = new SearchCondition(page, 10, "", "", "");
        String expectedQuery = String.format("?page=%d&pageSize=10&searchOption=&searchKeyword=&sortOption=", page);
        assertEquals(expectedQuery, target.getQueryString(page));

        Integer expectedOffset = (page - 1) * 10;
        assertEquals(expectedOffset, target.getOffset());
    }
}