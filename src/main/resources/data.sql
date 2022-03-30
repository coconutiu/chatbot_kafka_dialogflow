DROP TABLE IF EXISTS `Customer`;
-- DROP TABLE IF EXISTS `Policy`;
-- DROP TABLE IF EXISTS `Customer_Policy`;
CREATE TABLE Customer(
                         id char(20) not null primary key,
                         name char(20),
                         description char(56)
);
-- CREATE TABLE Policy(
--                          id char(20) not null primary key,
--                          policyname char(56)
-- );
-- CREATE TABLE Customer_Policy(
--                        id char(20) not null primary key,
--                        customerid char(20),
--                        policyid char(20)
--
-- );
INSERT INTO Customer VALUES (1,'Agent Phang','89666699');
INSERT INTO Customer VALUES (2,'Agent Zhang','89996666');
INSERT INTO Customer VALUES (3,'Agent Liu','89966669');

-- insert into Policy values(1,'Policy 1');
-- insert into Policy values(2,'Policy 2');
-- insert into Policy values(3,'Policy 3');
--
-- insert into Customer_Policy values (1,1,1);
-- insert into Customer_Policy values (2,2,1);
-- insert into Customer_Policy values (3,3,2);
-- insert into Customer_Policy values (4,1,2);

