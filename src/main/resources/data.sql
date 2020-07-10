--
CREATE TABLE base_admin_user  (
  id int NOT NULL AUTO_INCREMENT ,
  sys_user_name varchar(50) NOT NULL,
  sys_user_pwd varchar(250) DEFAULT NULL ,
  role_id int DEFAULT NULL ,
  user_phone varchar(11) NOT NULL ,
  reg_time varchar(32) NOT NULL ,
  user_status int NOT NULL DEFAULT 0
)
--
-- -- ----------------------------
-- -- Records of base_admin_user
-- -- ----------------------------
INSERT INTO base_admin_user VALUES (1, 'admin', '3ef7164d1f6167cb9f2658c07d3c2f0a', 1, '13411182215', '2018-11-22 10:57:33', 1);
INSERT INTO base_admin_user VALUES (2, 'jackson', '6565673a6caee66a6acbd51415bddbda', 2, '19563648695', '2018-11-22 10:57:33', 1);
INSERT INTO base_admin_user VALUES (4, 'alice', '5e1030d25f5ca46aac4c0369b908d762', 2, '11111111111', '2018-11-22 11:01:58', 1);
