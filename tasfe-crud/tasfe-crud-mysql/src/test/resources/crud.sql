--读写分离非分片
CREATE SCHEMA IF NOT EXISTS `tasfe_crud`;
CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_user` (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  user_id bigint(11),
  dept_id int(5),
  order_id int(5),
  user_name varchar(16),
  password varchar(16),
  mobile_phone varchar(16),
  office_phone varchar(16),
  email varchar(32),
  job varchar(16),
  status longtext,
  create_user varchar(16),
  update_user varchar(16),
  create_time date,
  update_time date,
  KEY `id` (`id`)
);


CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_member` (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  user_id bigint(11),
  dept_id int(5),
  order_id int(5),
  user_name varchar(16),
  password varchar(16),
  mobile_phone varchar(16),
  office_phone varchar(16),
  email varchar(32),
  job varchar(16),
  status longtext,
  create_user varchar(16),
  update_user varchar(16),
  create_time date,
  update_time date,
  KEY `id` (`id`)
);
