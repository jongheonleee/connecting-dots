package com.example.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PageHandlerTest {

    private PageHandler target;

    @DisplayName("현재 페이지가 1이며 총 개수가 100개 이하인 경우")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 100})
    public void test1(int totalCnt) {
        int page = 1;
        target = new PageHandler(page, totalCnt);
        String expectedPaging = createExpectedPaging(false, false, 1, totalCnt/10);
        assertEquals(expectedPaging, target.getResult());
    }

    @DisplayName("현재 페이지가 5이며 총 개수가 50개 이상인 경우")
    @ParameterizedTest
    @ValueSource(ints = {50, 100, 255, 500, 1000})
    public void test2(int totalCnt) {
        int page = 5;
        target = new PageHandler(page, totalCnt);
        String expectedPaging = createExpectedPaging(true, totalCnt > 100, 1, totalCnt/10);
        assertEquals(expectedPaging, target.getResult());
    }

    @DisplayName("현재 페이지가 15이며 총 개수가 200개 이상인 경우")
    @ParameterizedTest
    @ValueSource(ints = {200, 300, 400, 500})
    public void test3(int totalCnt) {
        int page = 15;
        target = new PageHandler(page, totalCnt);
        String expectedPaging = createExpectedPaging(true, totalCnt > 200, 11, 20);
        assertEquals(expectedPaging, target.getResult());
    }

    private String createExpectedPaging(boolean prev, boolean next, int beginPage, int endPage) {
        StringBuilder sb = new StringBuilder();

        if (beginPage > endPage) {
            endPage = beginPage;
        } else if (endPage > beginPage * 10) {
            endPage = beginPage * 10;
        }

        if (prev) {
            sb.append("<");
        }

        for (int i=beginPage; i<=endPage; i++) {
            sb.append(" ").append(i).append(" ");
        }

        if (next) {
            sb.append(">");
        }

        return sb.toString();
    }

}