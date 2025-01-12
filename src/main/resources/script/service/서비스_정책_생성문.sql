-- 미완성

-- 이용 규칙 데이터 확인
select *
from service_rule_use;

select *
from common_code;


-- 서비스 정책 데이터 확인
select *
from service_terms;

-- 제재 활동 정책 데이터 구성
-- 01. 제재 정책 1 : 출신지 차별 & 외모 차별 & 정치 성향
-- 출신지 차별 : SA0001
-- 외모 차별 : SA0002
-- 정치 성향 : SA0003
-- 구분 코드 : 2001 -> 제재 정책 관련 코드
INSERT INTO service_terms
(
    poli_stat, name,
    rule_stat1, op1,
    rule_stat2, op2,
    rule_stat3,
    comt, chk_use, reg_date,
    reg_user_seq, up_date, up_user_seq,
    code)
VALUES
    -- 제재 활동 정책 생성문(더 추가할 예정)
(
    'SAT0001', '[제재활동 정책] 출신지 차별 & 외모 차별 & 정치 성향',
    'SA0001', 'AND',
    'SA0002', 'AND',
    'SA0003',
    '제재 활동 정책 1 : 출신지 차별 & 외모 차별 & 정치 성향', 'Y', CURRENT_DATE,
    1, CURRENT_DATE, 1,
    '2001'
),
    -- 회원 등급 정책 생성문
(
    'GAT0001', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 브론즈',
    'GA0001', 'AND',
    'GA0002', 'AND',
    'GA0003',
    '회원 등급 정책 - 브론즈 : 게시글 작성(100) & 좋아요(50) & 베스트 등록(10) 보다 적음',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
),
(
    'GAT0002', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 실버',
    'GA0004', 'AND',
    'GA0005', 'AND',
    'GA0006',
    '회원 등급 정책 - 실버 : 게시글 작성(100) & 좋아요(50) & 베스트 등록(10) 이상',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
),
(
    'GAT0003', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 골드',
    'GA0007', 'AND',
    'GA0008', 'AND',
    'GA0009',
    '회원 등급 정책 - 골드 : 게시글 작성(200) & 좋아요(200) & 베스트 등록(20) 이상',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
),
(
    'GAT0004', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 플래티넘',
    'GA0010', 'AND',
    'GA0011', 'AND',
    'GA0012',
    '회원 등급 정책 - 실버 : 게시글 작성(400) & 좋아요(400) & 베스트 등록(40) 이상',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
),
(
    'GAT0005', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 다이아',
    'GA0013', 'AND',
    'GA0014', 'AND',
    'GA0015',
    '회원 등급 정책 - 다이다 : 게시글 작성(800) & 좋아요(800) & 베스트 등록(80) 이상',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
),
(
    'GAT0006', '[회원 등급 정책] 커뮤니티 활동에 따른 회원 등급 정책 - 마스터',
    'GA0016', 'AND',
    'GA0017', 'AND',
    'GA0018',
    '회원 등급 정책 - 실버 : 게시글 작성(1000) & 좋아요(1000) & 베스트 등록(100) 이상',
    'Y', CURRENT_DATE, 1, CURRENT_DATE, 1,
    '2002'
);