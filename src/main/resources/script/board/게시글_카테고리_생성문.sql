-- 미완성본
INSERT INTO board_category(
    cate_code, top_cate, name,
    comt, reg_date, reg_user_seq,
    up_date, up_user_seq, ord,
    chk_use, level
) VALUES
(
    'BC000000', 'BC000000', '최상위 카테고리',
    '사용하는 카테고리 아님', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 1,
    'N', 0
),
-- 대분류 파트 -> 커리어, 재태크, 자기계발, 학업,
(
    'BC010000', 'BC000000', '커리어',
    '커리어 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 1,
    'Y', 1
),
(
    'BC020000', 'BC000000', '재태크',
    '재태크 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 2,
    'Y', 1
),
(
    'BC030000', 'BC000000', '자기계발',
    '자기계발 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 3,
    'Y', 1
),
(
    'BC040000', 'BC000000', '학업',
    '학업 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 4,
    'Y', 1
),
-- 중분류 파트 -> (커리어 부분만 작성) 전체, IT, 비즈니스/창업, 기획/UX, 마케팅, 데이터분석, 크리에이티브
(
    'BC010100', 'BC010000', '전체',
    '커리어 전체 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 1,
    'Y', 2
)
,
(
    'BC010200', 'BC010000', 'IT',
    'IT 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 2,
    'Y', 2
),
(
    'BC010300', 'BC010000', '비즈니스/창업',
    '비즈니스/창업 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 3,
    'Y', 2
),
(
    'BC010400', 'BC010000', '기획/UX',
    '기획/UX 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 4,
    'Y', 2
),
(
    'BC010500', 'BC010000', '마케팅',
    '마케팅 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 5,
    'Y', 2
),
(
    'BC010600', 'BC010000', '데이터분석',
    '데이터분석 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 6,
    'Y', 2
),
(
    'BC010700', 'BC010000', '크리에이티브',
    '크리에이티브 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 7,
    'Y', 2
),
-- 소분류 파트 -> (커리어 - IT 파트만 적용) 인공지능, 프론트엔드 개발, 백엔드 개발, 데이터 사이언스, DevOps/Infra, 게임 개발, 컴퓨터 공학/SW 엔지니어링, 프로젝트
(
    'BC010201', 'BC010200', '인공지능',
    '인공지능 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 1,
    'Y', 3
),
(
    'BC010202', 'BC010200', '프론트엔드 개발',
    '프론트엔드 개발 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 2,
    'Y', 3
),
(
    'BC010203', 'BC010200', '백엔드 개발',
    '백엔드 개발 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 3,
    'Y', 3
),
(
    'BC010204', 'BC010200', '데이터 사이언스',
    '데이터 사이언스 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 4,
    'Y', 3
),
(
    'BC010205', 'BC010200', 'DevOps/Infra',
    'DevOps/Infra 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 5,
    'Y', 3
),
(
    'BC010206', 'BC010200', '게임 개발',
    '게임 개발 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 6,
    'Y', 3
),
(
    'BC010207', 'BC010200', '컴퓨터 공학/SW 엔지니어링',
    '컴퓨터 공학/SW 엔지니어링 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 7,
    'Y', 3
),
(
    'BC010208', 'BC010200', '프로젝트',
    '프로젝트 관련 게시판', CURRENT_DATE, 1,
    CURRENT_DATE, 1, 8,
    'Y', 3
);
