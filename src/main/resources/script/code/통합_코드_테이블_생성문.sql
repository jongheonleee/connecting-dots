-- 핵심 도메인 코드 부여
-- 1. 1000 : 회원
-- 2. 2000 : 서비스
-- 3. 3000 : 게시판
-- 4. 4000 : 댓글, 대댓글
-- 5. 5000 : 리포트
-- 6. 6000 : 관리자
-- 7. 7000 : 집계
-- 8. 8000 : AI(혐오 분류기 모델)
-- 9. 0000 : 코드 -> 최상위 루트

-- 회원(1000) 세부 코드 부여
-- 1001 : 신규가입
-- 1002 : 정보변경
-- 1003 : 탈퇴대기
-- 1004 : 탈퇴처리
-- 1005 : 탈퇴완료
-- 1006 : 탈퇴보류
-- 1007 : 탈퇴취소
-- 1008 : 제재대기
-- 1009 : 제재처리
-- 1010 : 제재완료
-- 1011 : 제재취소
-- 1012 : 제재취소
-- 1013 : 휴먼상태


-- 서비스(2000) 분류 세부 코드 부여
-- 2001 : 제재 관련 정책/규칙
-- 2002 : 회원 등급 관련 정책/규칙

-- 게시판(3000) 분류 세부 코드 부여
-- 3001 : 생성
-- 3002 : 변경
-- 3003 : 비정상 신고
-- 3004 : 분석중
-- 3005 : 분석완료
-- 3006 : 제재대상
-- 3007 : 정상
-- 3008 : 비정상
-- 3009 : 비공개
-- 3010 : 기타

-- 댓글, 대댓글(4000) 분류 세부 코드 부여
-- 4001 : 생성
-- 4002 : 변경


-- 리포트(5000) 분류 세부 코드 부여
-- 5001 : 생성
-- 5002 : 변경
-- 5003 : 처리 대기중
-- 5004 : 처리중
-- 5005 : 처리완료
-- 5006 : 처리보류
-- 5007 : 처리불가
-- 5008 : 기타




-- 테이블 초기화
delete from common_code
where level = 3;

delete from common_code
where level = 2;

delete from common_code
where level = 1;

delete from common_code
where level = 0;



-- 최상위 루트 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq)
VALUES(0, '0000', '코드', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1);

-- 회원 도메인 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(1, '1000', '회원', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '0000');

-- 회원 상세 코드 추가
-- 회원 신규가입
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1001', '신규가입', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 정보변경
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1002', '정보변경', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 탈퇴대기
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1003', '탈퇴대기', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 탈퇴처리
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1004', '탈퇴처리', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 탈퇴완료
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1005', '탈퇴완료', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 탈퇴보류
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1006', '탈퇴보류', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 탈퇴취소
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1007', '탈퇴취소', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 제재대기
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1008', '제재대기', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 제재처리
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1009', '제재처리', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 제재완료
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1010', '제재완료', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 제재보류
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1011', '제재보류', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 제재취소
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1012', '제재취소', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');

-- 회원 휴먼
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '1013', '휴먼상태', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '1000');


-- 서비스 도메인 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(1, '2000', '서비스', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '0000');

-- 서비스 상세 코드 추가
-- 제재 관련 정책/규칙
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '2001', '제재 관련 정책/규칙', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '2000');

-- 회원 등급 관련 정책/규칙
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '2002', '회원 등급 관련 정책/규칙', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '2000');


-- 게시판 도메인 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(1, '3000', '게시판', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '0000');

-- 게시판 상세 코드 추가
-- 게시판 생성
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '3001', '생성', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '3000');

-- 게시판 변경
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '3002', '변경', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '3000');

-- 게시판 비정상 신고
-- 게시판 비정상
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '3008', '비정상', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '3000');

-- 게시판 비공개
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '3009', '비공개', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '3000');

-- 게시판 기타
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '3010', '기타', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '3000');


-- 댓글, 대댓글 도메인 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(1, '4000', '댓글, 대댓글', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '0000');

-- 댓글, 대댓글 상세 코드 추가
-- 댓글, 대댓글 생성
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '4001', '생성', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '4000');

-- 댓글, 대댓글 변경
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '4002', '변경', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '4000');


-- 리포트 도메인 코드 추가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(1, '5000', '리포트', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '0000');

-- 리포트 상세 코드 추가
-- 리포트 생성
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5001', '생성', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 변경
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5002', '변경', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 처리 대기중
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5003', '처리 대기중', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 처리중
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5004', '처리중', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 처리완료
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5005', '처리완료', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 처리보류
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5006', '처리보류', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 처리불가
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5007', '처리불가', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 리포트 기타
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5008', '기타', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');


-- 리포트 기타
INSERT INTO common_code(level, code, name, chk_use, reg_date, reg_user_seq, up_date, up_user_seq, top_code)
VALUES(2, '5008', '기타', 'Y', CURRENT_DATE, 1, CURRENT_DATE, 1, '5000');

-- 변경 쿼리
UPDATE common_code
SET
    level = 3, name = '기타', chk_use = 'Y', reg_date = CURRENT_DATE, reg_user_seq = 1,
    up_date = CURRENT_DATE, up_user_seq = 1, top_code = '5000'
WHERE code = '5008';

-- 삭제 쿼리
-- 레벨별 삭제 쿼리
delete from common_code
where level = 3;

delete from common_code
where level = 2;

delete from common_code
where level = 1;

delete from common_code
where level = 0;

-- 상세 코드 삭제 쿼리
delete from common_code
where code = '5008';


-- 데이터 전체 카운팅 쿼리
select COUNT(*)
from common_code;

-- 데이터 전체 조회 쿼리
select *
from common_code;

-- 데이터 상세 조회 쿼리
select *
from common_code
where code = '5008';

