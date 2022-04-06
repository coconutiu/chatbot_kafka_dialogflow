DROP TABLE IF EXISTS `Customer`;
DROP TABLE IF EXISTS `Agent`;
-- DROP TABLE IF EXISTS `Customer_Policy`;

CREATE TABLE Customer(
                         id char(20) not null primary key,
                         name char(20),
                         agentid char(56),
                         gender char(56),
                         dob char(56),
                         nationality char(56),
                         active char(56),
                         version char(56)
);
-- CREATE TABLE Policy(
--                          id char(20) not null primary key,
--                          policyname char(56)
-- );

CREATE TABLE Agent(
                       id char(20) not null primary key,
                       name char(20),
                       phone char(20)

);
INSERT INTO Customer VALUES (1,'Syab',2,'Female','06/07/2002','Singapore','True','6.0');
INSERT INTO Customer VALUES (2,'Xin',3,'Female','06/06/2002','Singapore','True','7.0');
INSERT INTO Customer VALUES (3,'Xiaohan',1,'Female','06/08/2002','China','True','8.0');

INSERT INTO Agent VALUES (1,'Agent Phang','66666666');
INSERT INTO Agent VALUES (2,'Agent Zhang','88888888');
INSERT INTO Agent VALUES (3,'Agent Liu','77777777');

-- insert into Policy values(1,'Policy 1');
-- insert into Policy values(2,'Policy 2');
-- insert into Policy values(3,'Policy 3');
--
-- insert into Customer_Policy values (1,1,1);
-- insert into Customer_Policy values (2,2,1);
-- insert into Customer_Policy values (3,3,2);
-- insert into Customer_Policy values (4,1,2);

