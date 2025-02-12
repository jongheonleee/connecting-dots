-- 미완성 생성문
INSERT INTO service_user_conditions
(
    conds_code,
    name,
    cond_code1,
    chk_cond_code1,
    cond_code2,
    chk_cond_code2,
    cond_code3,
    chk_cond_code3,
    cond_code4,
    chk_cond_code4,
    chk_use,
    comt,
    reg_date,
    reg_user_seq,
    up_date,
    up_user_seq
)
VALUES
(
    'SUCS0001',
    '2025연도 서비스 이용약관[A]',
    'SUC0001',
    'Y',
    'SUC0002',
    'Y',
    'SUC0003',
    'Y',
    'SUC0004',
    'Y',
    'Y',
    '2025연도 서비스 이용약관[A]입니다.',
    CURRENT_DATE,
    1,
    CURRENT_DATE,
    1
);
