CREATE TABLE sys_user  (
  id int NOT NULL AUTO_INCREMENT ,
  username varchar(50) NOT NULL,
  password varchar(250) DEFAULT NULL ,
  user_phone varchar(11) ,
  telegram varchar(11) ,
  reg_time DATE ,
  data_limit BIGINT,
  expire_time DATE,
  data_usage BIGINT,
  user_type INTEGER NOT NULL DEFAULT 1,
  disabled BOOLEAN ,
  deleted BOOLEAN
);

INSERT INTO sys_user VALUES (1, 'admin', '028974a1894e75000bf41636adc8ed81', '11111111111','leeroy','2020-01-03', 107374182400,null,null, 0, false, false);

CREATE TABLE user_port  (
  id int NOT NULL AUTO_INCREMENT ,
  local_port int NOT NULL,
  user_id int NOT NULL ,
  data_limit BIGINT,
  data_usage BIGINT,
  disabled BOOLEAN ,
  deleted BOOLEAN ,
  expire_time DATE,
  create_time DATE ,
  update_time DATE
);


CREATE TABLE user_token  (
  id int NOT NULL AUTO_INCREMENT ,
  token varchar(250) NOT NULL,
  username varchar(50) NOT NULL,
  user_id INTEGER ,
  create_time BIGINT ,
  update_time BIGINT,
  expire_time BIGINT
);

CREATE TABLE user_port_forward  (
  id int NOT NULL AUTO_INCREMENT ,
    user_id int NOT NULL ,
  local_port int NOT NULL,
  remote_ip varchar(64),
  remote_host varchar(64),
  remote_port int,
  data_limit BIGINT,
  data_usage BIGINT,
  disabled BOOLEAN ,
  deleted BOOLEAN ,
  create_time DATE ,
  update_time DATE
);

CREATE TABLE clash  (
  id varchar(50) NOT NULL,
  config_name varchar(50) NOT NULL,
  text CLOB NOT NULL,
  user_id int NOT NULL ,
  disabled BOOLEAN ,
  deleted BOOLEAN ,
  expire_time DATE,
  create_time DATE ,
  update_time DATE
);

CREATE TABLE port  (
  id int NOT NULL AUTO_INCREMENT,
  local_port int NOT NULL ,
  internet_port int NOT NULL ,
  deleted BOOLEAN,
  create_time DATE,
  update_time DATE
);

CREATE TABLE server(
  id int NOT NULL AUTO_INCREMENT,
  server_name varchar(128) NOT NULL ,
  host varchar(256) NOT NULL ,
  port int NOT NULL ,
  state int NOT NULL DEFAULT 1 ,
  deleted BOOLEAN,
  create_time DATE,
  update_time DATE
);


alter table server add column key varchar(256) NOT NULL ;
