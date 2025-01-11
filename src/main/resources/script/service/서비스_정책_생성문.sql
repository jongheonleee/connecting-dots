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
(
    'SAT0001', '[제재활동 정책] 출신지 차별 & 외모 차별 & 정치 성향',
    'SA0001', 'AND',
    'SA0002', 'AND',
    'SA0003',
    '제재 활동 정책 1 : 출신지 차별 & 외모 차별 & 정치 성향', 'Y', CURRENT_DATE,
    1, CURRENT_DATE, 1,
    '2001'
);
