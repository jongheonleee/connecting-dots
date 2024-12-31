delete from category;

-- 카테고리 데이터
insert into category
values ('01','0', '일상', '-', now(), 'admin1', now(), 'admin1');

-- 일상 하위 데이터
-- 웃긴글, 고민글, 잡담
insert into category
values ('0101','01', '웃긴글', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0102','01', '고민글', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0103','01', '잡담', '-', now(), 'admin1', now(), 'admin1');



insert into category
values ('02','0', '꿀 팁', '-', now(), 'admin1', now(), 'admin1');

-- 꿀 팁 하위 데이터
-- 일상 속 소소한 꿀 팁, 알고리즘 꿀 팁, 취업 꿀 팁, 공부 꿀 팁

insert into category
values ('0201','02', '일상 속 소소한 꿀 팁', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0202','02', '알고리즘 꿀 팁', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0203','02', '취업 꿀 팁', '-', now(), 'admin1', now(), 'admin1');



insert into category
values ('03','0', 'IT 커리어', '-', now(), 'admin1', now(), 'admin1');

-- IT 커리어 하위 데이터
-- 취업, 커리어 전환, 프로젝트
insert into category
values ('0301','03', '취업', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0302','03', '커리어 전환', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0303','03', '프로젝트', '-', now(), 'admin1', now(), 'admin1');





insert into category
values ('04','0', '공부', '-', now(), 'admin1', now(), 'admin1');

-- 공부 하위 데이터
-- 자바, 스프링, 자료구조, 알고리즘, 네트워크, 데이터베이스

insert into category
values ('0401','04', '자바', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0402','04', '스프링', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0403','04', '자료구조', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0404','04', '알고리즘', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0405','04', '네트워크', '-', now(), 'admin1', now(), 'admin1');

insert into category
values ('0406','04', '데이터베이스', '-', now(), 'admin1', now(), 'admin1');


select *
from category;

# delete from category;