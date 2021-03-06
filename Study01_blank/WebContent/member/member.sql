DROP table member;

create table member(
	name VARCHAR2(100),
	id VARCHAR2(50) PRIMARY KEY,
	pass VARCHAR2(50),
	birth number(4),
	gender VARCHAR2(10),
	job VARCHAR2(100),
	address VARCHAR2(100),
	regDate DATE
);
select * from MEMBER;
UPDATE member SET pass='1234',birth=2001,gender='남자',job='ㄹㄹ',address='djelsrk' where id='cksdud7362';
