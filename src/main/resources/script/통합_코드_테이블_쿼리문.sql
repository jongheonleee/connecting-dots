-- 핵심 도메인 코드 부여
-- 1. 1000 : 회원
-- 2. 2000 : 서비스
-- 3. 3000 : 게시판
-- 4. 4000 : 댓글
-- 5. 5000 : 대댓글
-- 6. 6000 : 리포트
-- 7. 7000 : 관리자
-- 8. 8000 : 집계
-- 9. 0000 : 코드 -> 최상위 루트

-- 회원 세부 코드 부여
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
-- 1011 : 제채보류
-- 1012 : 제재취소
-- 1013 : 휴먼


-- 테이블 초기화


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


-- 데이터 확인용 쿼리 
select *
from common_code;