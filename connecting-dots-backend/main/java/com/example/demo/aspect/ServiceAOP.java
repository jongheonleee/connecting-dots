package com.example.demo.aspect;

public class ServiceAOP {

    /**
     * 현재 서비스 부분에 산발적으로 나타나는 코드들이 있음
     * - rowCnt != 1 일 때 throw new InternalServerError("게시글 등록 실패");
     * - ....
     *
     * 이런 부분 추후에 AOP로 처리할 예정
     */
}
