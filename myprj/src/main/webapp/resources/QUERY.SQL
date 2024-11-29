/* Drop Tables */
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS CALENDAR;
DROP TABLE IF EXISTS RESERVATION;
DROP TABLE IF EXISTS COMMENT;

/* Create Tables */
CREATE TABLE MEMBER
(
	midx int NOT NULL AUTO_INCREMENT,
	id varchar(20) NOT NULL,
	password varchar(200) NOT NULL,
	birthday varchar(10) NOT NULL,
	name varchar(10) NOT NULL,
	phone varchar(15) NOT NULL,
	email varchar(30) NOT NULL,
	/* date date DEFAULT NOW(), SYSDATE() NOT NULL, */
	date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	modify date,
	delyn char(1) DEFAULT 'N' NOT NULL,
	adminyn char(1) DEFAULT 'N' NOT NULL,
	ip varchar(15) NOT NULL,
	PRIMARY KEY (midx),
	UNIQUE (id)
);

CREATE TABLE BOARD
(
	bidx int NOT NULL AUTO_INCREMENT,
	title varchar(50) NOT NULL,
	summary varchar(100),
	contents text,
	thumbnail varchar(100),
	date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	modify date,
	view int DEFAULT 0 NOT NULL,
	ip varchar(15) NOT NULL,
	delyn char(1) DEFAULT 'N' NOT NULL,
	midx int NOT NULL,
	boardcode varchar(10) NOT NULL,
	period int DEFAULT 0 NOT NULL,
	PRIMARY KEY (bidx)
);

CREATE TABLE CALENDAR
(
	calidx int NOT NULL AUTO_INCREMENT,
	startday date NOT NULL,
	adultprice int NOT NULL,
	childprice int NOT NULL,
	date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	modify date,
	delyn char(1) DEFAULT 'N' NOT NULL,
	bidx int NOT NULL,
	PRIMARY KEY (calidx)
);

CREATE TABLE RESERVATION
(
	ridx int NOT NULL AUTO_INCREMENT,
	adultnumber int DEFAULT 0 NOT NULL,
	childnumber int DEFAULT 0 NOT NULL,
	date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	modify date,
	name varchar(10) NOT NULL,
	phone varchar(11) NOT NULL,
	ip varchar(15) NOT NULL,
	delyn char(1) DEFAULT 'N' NOT NULL,
	calidx int NOT NULL,
	midx int NOT NULL,
	bidx int NOT NULL,
	PRIMARY KEY (ridx)
);

CREATE TABLE COMMENT
(
	cidx int NOT NULL AUTO_INCREMENT,
	contents text NOT NULL,
	date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ip varchar(15) NOT NULL,
	delyn char(1) DEFAULT 'N' NOT NULL,
	bidx int NOT NULL,
	midx int NOT NULL,
	PRIMARY KEY (cidx)
);

/* Create Foreign Keys */
ALTER TABLE BOARD
	ADD FOREIGN KEY (midx)
	REFERENCES MEMBER (midx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE CALENDAR
	ADD FOREIGN KEY (bidx)
	REFERENCES BOARD (bidx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE RESERVATION
	ADD FOREIGN KEY (bidx)
	REFERENCES BOARD (bidx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE RESERVATION
	ADD FOREIGN KEY (calidx)
	REFERENCES CALENDAR (calidx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE RESERVATION
	ADD FOREIGN KEY (midx)
	REFERENCES MEMBER (midx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

ALTER TABLE COMMENT
	ADD FOREIGN KEY (bidx)
	REFERENCES BOARD (bidx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;
ALTER TABLE COMMENT
	ADD FOREIGN KEY (midx)
	REFERENCES MEMBER (midx)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

/* ============================================================== */

/* 샘플데이터 */
INSERT INTO MEMBER(id, password, birthday, name, phone, email, ip)
VALUES('admin', '12341234', '19000101', '관리자', '01012341234', 'admin@travle.com', '127,0,0,1');
INSERT INTO MEMBER(id, password, birthday, name, phone, email, ip)
VALUES('guest1', '1234', '19901101', '홍길동', '01011111111', 'guest1@travle.com', '127,0,0,1');
INSERT INTO MEMBER(id, password, birthday, name, phone, email, ip)
VALUES('guest2', '1234', '19950430', '강감찬', '01022222222', 'guest2@travle.com', '127,0,0,1');
INSERT INTO MEMBER(id, password, birthday, name, phone, email, ip)
VALUES('guest3', '1234', '19950430', '이순신', '01033333333', 'guest3@travle.com', '127,0,0,1');

INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을날 대전 하루여행, 맛집부터 디저트까지 완벽하게 즐기기', '대전 신세계백화점 부터 유성온천공원까지 볼거리 먹거리 가득한 대전 당일 치기 가을 여행 코스 입니다.', '돈까스 먹고 신세계백화점 가고', '', '127.0.0.1', 1, 'travel', 1);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('대전 당일 이색체험공방 데이트', '가을 감성 가득 담은 대전 원데이클래스 공방 투어부터 맛집탐방까지 특별한 하루 만들기.', '짬뽕 먹고 카페 가고', '', '127.0.0.1', 1, 'travel', 1);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을 낭만 핫플 투어, 대전 식도락 여행', '달달한 디저트부터 이색 동물들과의 교감까지, 오감만족 대전에서의 하루 어떠신가요?', '곱창 먹고 롯데백화점 가고', '', '127.0.0.1', 1, 'travel', 2);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을날 대전, 풍성한 식도락 즐기기 2박3일', '대전에서만 즐길 수 있는 특별한 음식들로만 채운 맛있는 기행. 가을 바람따라 걸어다니며 먹는 즐거움을 느껴보세요.', '현대프리미엄아울렛 가고 엑스포공원 가고', '', '127.0.0.1', 1, 'travel', 3);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('유성부터 반석까지 대전 즐기기 3박4일', '대전의 다양한 맛집과 명소를 찾아다니며 즐거운 시간을 보낼 수 있는 3박 4일간의 여정', '수통골 가고 칼국수 먹고', '', '127.0.0.1', 1, 'travel', 4);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을캠핑 in 대전, 도심 속 자연과 함께하는 특별한 피크닉', '금강로하스대청공원부터 시작하는 대전여행 삼박사일, 아이들과 함께 뛰놀고 연인과 손잡고 걸어보세요.', '칼국수 먹고 계족산 가고', '', '127.0.0.1', 1, 'travel', 4);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을날의 대전, 서구 힐링 먹방 투어', '노잼도시라는 편견을 깨줄 대전으로의 색다른 먹방 여행. 눈과 입 모두 즐거워지는 4일간의 기록', '스케이트장 가고 카페 가고', '', '127.0.0.1', 1, 'travel', 4);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('대전 늦가을 감성여행 2박3일', '화려하게 물든 단풍 구경부터 옛추억 떠올리는 레트로 식당까지. 늦가을 정취 물씬 느끼는 감성 여행 코스', '장태산자연휴양림 가고 한정식 먹고', '', '127.0.0.1', 1, 'travel', 3);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('대전 서구 힐링여행, 도심 속 자연산책과 별미 탐방', '가족과 함께 떠나기 좋은 11월의 대전 여행. 푸릇푸릇한 자연 경관과 맛있는 음식들로 몸과 마음을 채워보세요.', '장태산자연휴양림 가고 스테이크 먹고', '', '127.0.0.1', 1, 'travel', 3);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('가을 감성 낭만 가득! 대전 1박2일 문화체험 투어', '낭만이 가득한 11월말 어느 멋진 날, 특별한 경험과 잊지 못할 추억을 만들어 줄 코스로 초대합니다.', '갤러리아백화점타임월드점 가고 교보문고 가고', '', '127.0.0.1', 1, 'travel', 2);

INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('안녕하세요', null, '반갑습니다.', null, '127.0.0.1', 2, 'free', null);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('안녕하세요222', null, '반갑습니다. 반갑습니다.', null, '127.0.0.1', 2, 'free', null);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('안녕하세요333', null, '반갑습니다. 반갑습니다. 반갑습니다.', null, '127.0.0.1', 3, 'free', null);

INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('공지입니다.', null, '공지.', null, '127.0.0.1', 1, 'notice', null);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('공지입니다22', null, '공지글입니다', null, '127.0.0.1', 1, 'notice', null);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('공지입니다33', null, '공지공지공지', null, '127.0.0.1', 1, 'notice', null);
INSERT INTO BOARD(title, summary, contents, thumbnail, ip, midx, boardcode, period)
VALUES('공지입니다44', null, '공지글입니다!!!!!', null, '127.0.0.1', 1, 'notice', null);

INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-26', 10000, 8000, 1);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-27', 10000, 8000, 1);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-26', 20000, 15000, 2);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-27', 20000, 15000, 2);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-26', 30000, 20000, 3);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-27', 30000, 20000, 3);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-26', 40000, 30000, 4);
INSERT INTO CALENDAR(startday, adultprice, childprice, bidx)
VALUES('2024-11-27', 40000, 30000, 4);

INSERT INTO RESERVATION(adultnumber,
VALUES(1, 1, '홍길동', '01012341234', '127.0.0.1', 1, 2, 1);
INSERT INTO RESERVATION(adultnumber,
VALUES(1, 1, '홍길동', '01012341234', '127.0.0.1', 3, 2, 2);
INSERT INTO RESERVATION(adultnumber,
VALUES(1, 1, '강감찬', '01012344456', '127.0.0.1', 1, 3, 1);
INSERT INTO RESERVATION(adultnumber,
VALUES(1, 1, '강감찬', '0101234456', '127.0.0.1', 3, 3, 2);

INSERT INTO COMMENT(contents, ip, bidx, midx)
VALUES('댓글을 달아봅니다11','127.0.0.1', 6, 2);
INSERT INTO COMMENT(contents, ip, bidx, midx)
VALUES('댓글을 달아봅니다22','127.0.0.1', 6, 3);
INSERT INTO COMMENT(contents, ip, bidx, midx)
VALUES('댓글을 달아봅니다1111','127.0.0.1', 7, 3);
INSERT INTO COMMENT(contents, ip, bidx, midx)
VALUES('댓글을 달아봅니다1','127.0.0.1', 8, 2);


SELECT * FROM BOARD WHERE delyn = 'N' and boardcode = "notice" order by bidx desc limit 4

SELECT COUNT(*) AS cnt FROM BOARD WHERE delyn = 'N' AND boardcode = "travel" AND period = "1"


ALTER TABLE board
MODIFY COLUMN title VARCHAR(50) NOT NULL;

SELECT * FROM CALENDAR WHERE delyn = 'N' and bidx = 1;




select * from board_aws.board_aws order by bidx desc;

ALTER TABLE member ADD ip varchar(15) NOT NULL

ALTER TABLE MEMBER CHANGE admin adminyn char(1) DEFAULT 'N' NOT NULL


ALTER TABLE member
MODIFY COLUMN password varchar(200) NOT NULL


ALTER TABLE member
MODIFY COLUMN birthday varchar(10) NOT NULL

ALTER TABLE member
MODIFY COLUMN phone varchar(15) NOT NULL


ALTER TABLE board
MODIFY COLUMN modify date;

ALTER TABLE calendar
MODIFY COLUMN modify date;

ALTER TABLE reservation
MODIFY COLUMN modify date;

ALTER TABLE member
add COLUMN modify date;

ALTER TABLE member
MODIFY COLUMN modify date;

delete from board where bidx > 17;