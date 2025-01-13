-- 회원 등급 생성문
INSERT INTO service_user_grade
(
    stat_code, name, ord, short_exp, long_exp, img, chk_use, comt, reg_date, reg_user_seq, up_date, up_user_seq, poli_stat
)
VALUES
(
    'SUG0001', 'BRONZE', 6, '2025연도 connecting-dots 브론즈', '2025연도 connecting-dots 브론즈', '브론즈 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0001'
),
(
    'SUG0002', 'SILVER', 5, '2025연도 connecting-dots 실버', '2025연도 connecting-dots 실버', '실버 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0002'
),
(
    'SUG0003', 'GOLD', 4, '2025연도 connecting-dots 골드', '2025연도 connecting-dots 골드', '골드 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0003'
),
(
    'SUG0004', 'PLATINUM', 3, '2025연도 connecting-dots 플래티넘', '2025연도 connecting-dots 플래티넘', '플래티넘 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0004'
),
(
    'SUG0005', 'DIAMOND', 2, '2025연도 connecting-dots 다이아몬드', '2025연도 connecting-dots 다이아몬드', '다이아몬드 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0005'
),
(
    'SUG0006', 'MASTER', 1, '2025연도 connecting-dots 마스터', '2025연도 connecting-dots 마스터', '마스터 사진', 'Y', '...', CURRENT_DATE, 1, CURRENT_DATE, 1, 'GAT0006'
);