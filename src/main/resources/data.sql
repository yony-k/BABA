-- 시퀀스 생성
CREATE SEQUENCE MEMBER_SEQ START WITH 1;

-- MEMBER 테이블 수정
ALTER TABLE MEMBER ALTER COLUMN MEMBER_ID SET DEFAULT NEXT VALUE FOR MEMBER_SEQ;

-- MEMBER 테이블 데이터 삽입 (5명)
INSERT INTO MEMBER (EMAIL, MEMBER_NAME, PASSWORD, ROLE) VALUES
                                                                       ('kimminji@example.com', '김민지', '{noop}password1', 'USER'),
                                                                       ('kimyuna@example.com', '김유나', '{noop}password2', 'USER'),
                                                                       ('leejinsuk@example.com', '이진석', '{noop}password3', 'USER'),
                                                                       ('parkjihoon@example.com', '박지훈', '{noop}password4', 'USER'),
                                                                       ('choisoo@example.com', '최수영', '{noop}password5', 'USER');
-- POST 테이블 데이터 삽입 (20개)
INSERT INTO POST (ID, CONTENT, LIKE_COUNT, SHARE_COUNT, TITLE, TYPE, MEMBER_ID, VIEW_COUNT, CREATED_AT, UPDATED_AT) VALUES
                                                                                                                        (1, '아침에 남산 타워에서 보는 서울 즥이네!', 2, 0, '남산', 'INSTAGRAM', 1, 44, '2024-07-21T10:00:00', '2024-08-21T10:00:00'),
                                                                                                                        (2, '여행 중 찍은 맛있는 음식 사진입니다.', 3, 1, '여행은 음식이지', 'INSTAGRAM', 2, 33, '2024-06-22T11:00:00', '2024-07-22T11:00:00'),
                                                                                                                        (3, '오늘의 바디 체크', 15, 2, '운동 끝', 'FACEBOOK', 3, 250, '2024-08-03T12:00:00', '2024-08-20T12:00:00'),
                                                                                                                        (4, '강릉 카페 뷰는 쩐다', 5, 8, '강릉 카페', 'INSTAGRAM', 4, 15, '2024-08-04T13:00:00', null),
                                                                                                                        (5, '올림픽 보고 자극받아서 헬스 조짐', 17, 5, '맛있다', 'TWITTER', 5, 57, '2024-08-20T14:00:00', null),
                                                                                                                        (6, '애들 데리고 하츄핑 보러왔다. 재우고 운동옴.', 77, 11, '하츄핑 너 뭔데?', 'INSTAGRAM', 5, 100, '2024-08-20T14:00:00', null),
                                                                                                                        (7, '리트리버를 키우는건지 애기를 키우는건지ㅋㅋ', 208, 39, '물개', 'INSTAGRAM', 2, 300, '2024-08-01T14:00:00', null),
                                                                                                                        (8, '와, 이게 바로 미국 햄버거!!', 10, 0, '미국 여행기!', 'TWITTER', 1, 22, '2024-08-10 10:20:00', '2024-08-12 10:20:00');


-- HASH_TAG 테이블 데이터 삽입 (20개)
INSERT INTO HASH_TAG (ID, TAG_NAME) VALUES
                                        (1, '오운완'),
                                        (2, '갓생'),
                                        (3, '육퇴'),
                                        (4, '소통해요'),
                                        (5, '먹스타그램'),
                                        (6, '여행');


-- POST_HASH_TAG_MAP 테이블 데이터 삽입 (포스트와 해시태그 매핑)
INSERT INTO POST_HASH_TAG_MAP (ID, POST_ID, HASHTAG_ID) VALUES
                                                            (1, 1, 1),
                                                            (2, 1, 2),
                                                            (3, 2, 5),
                                                            (4, 3, 1),
                                                            (5, 3, 4),
                                                            (6, 5, 1),
                                                            (7, 6, 3),
                                                            (8, 6, 1),
                                                            (9, 6, 2),
                                                            (10, 7, 3),
                                                            (11, 7, 4),
                                                            (12, 2, 4),
                                                            (13, 8, 5),
                                                            (14, 8, 6),
                                                            (15, 8, 4);