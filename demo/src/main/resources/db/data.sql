-- 사용자 임시 데이터
INSERT INTO users (
    student_id, department, school_year, major, gender, name, phone,
    profile_image, user_id, password, join_date, is_deleted, is_verified
) VALUES
('20240001', '소프트웨어융합대', 2, '컴퓨터공학과', 'M', '강대기', '010-1111-2222', NULL, 'test1', '1234', NOW(), FALSE, TRUE),
('20240002', '소프트웨어융합대', 2, '소프트웨어학과', 'F', '김현철', '010-3333-4444', NULL, 'test2', '1234', NOW(), FALSE, FALSE),
('20240003', '디자인대학', 3, '패션디자인학과', 'F', '박지수', '010-5555-6666', NULL, 'test3', '1234', NOW(), FALSE, TRUE);

-- 추가 샘플 데이터
INSERT INTO users (
    student_id, department, school_year, major, gender, name, phone,
    profile_image, user_id, password, join_date, is_deleted, is_verified
) VALUES
-- 2학년 컴퓨터공학과 사용자 2명 추가 (매칭 테스트용)
('20240004', '소프트웨어융합대', 2, '컴퓨터공학과', 'F', '이수민',   '010-2222-3333', NULL, 'test4', '1234', NOW(), FALSE, TRUE),
('20240005', '소프트웨어융합대', 2, '컴퓨터공학과', 'M', '최민호',   '010-4444-5555', NULL, 'test5', '1234', NOW(), FALSE, TRUE),
('20240006', '소프트웨어융합대', 2, '소프트웨어학과', 'F', '정현아', '010-6666-7777', NULL, 'test6', '1234', NOW(), FALSE, TRUE),
('20240007', '소프트웨어융합대', 3, '컴퓨터공학과', 'M', '박준영', '010-8888-9999', NULL, 'test7', '1234', NOW(), FALSE, TRUE),
('20240008', '디자인대학', 1, '시각디자인학과', 'F', '홍예린', '010-1010-2020', NULL, 'test8', '1234', NOW(), FALSE, TRUE);
('20250006', '디자인대학', 1, '시각디자인학과', 'F', '유슈현',   '010-2222-3333', NULL, 'test9', '1234', NOW(), FALSE, TRUE),
('20250007', '디자인대학', 1, '시각디자인학과', 'M', '하현오',   '010-4444-5555', NULL, 'test10', '1234', NOW(), FALSE, TRUE),
('20240009', '디자인대학', 2, '시각디자인학과', 'M', '김유연',   '010-4444-5555', NULL, 'test11', '1234', NOW(), FALSE, TRUE);
