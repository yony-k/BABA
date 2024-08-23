INSERT INTO MEMBER (MEMBER_ID, EMAIL, MEMBER_NAME, PASSWORD, ROLE) VALUES
(1, 'minji@gmail.com', '김민지', '{noop}1234', 'USER'),
(2, 'minjun@gmail.com', '김민준', '{noop}1234', 'USER'),
(3, 'alice@example.com', '엘리스', '{noop}1234', 'ADMIN');

INSERT INTO POST (ID, CONTENT, LIKE_COUNT, SHARE_COUNT, TITLE, TYPE, MEMBER_ID, VIEW_COUNT) VALUES
(1, '와, 여기가 미국이구나!!', 10, 2, '미국 여행기!', 'INSTAGRAM', 1, 150),
(2, '미국 햄버거 짠데 맛있네? ㅋㅋ', 25, 5, '미국 햄버거', 'INSTAGRAM', 1, 230),
(3, '여기 카페 에스프레소 굿굿', 15, 3, '에스프레소', 'TWITTER', 2, 180),
(4, '오운완 !!', 8, 1, 'Daily Post', 'INSTAGRAM', 3, 120);

INSERT INTO HASH_TAG (ID, TAG_NAME) VALUES
(1, '여행'),
(2, '맛집'),
(3, '카페'),
(4, '오운완'),
(5, '코딩');

INSERT INTO POST_HASH_TAG_MAP (ID, HASHTAG_ID, POST_ID) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 2, 2),
(4, 3, 3),
(5, 4, 4);