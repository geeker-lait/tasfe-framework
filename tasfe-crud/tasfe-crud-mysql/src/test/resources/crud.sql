--读写分离非分片
CREATE SCHEMA IF NOT EXISTS `tasfe_crud`;
CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `dept_id` INT NOT NULL,
  `order_id` INT NOT NULL,
  `user_name` VARCHAR(16) DEFAULT NULL,
  `password` VARCHAR(16) DEFAULT NULL,
  `mobile_phone` VARCHAR(16) DEFAULT NULL,
  `office_phone` VARCHAR(16) DEFAULT NULL,
  `email` VARCHAR(32) DEFAULT NULL,
  `job` VARCHAR(16) DEFAULT NULL,
  `status` LONGTEXT NULL,
  `create_user` VARCHAR(16) DEFAULT NULL,
  `update_user` VARCHAR(16) DEFAULT NULL,
  `create_time` DATE DEFAULT NULL,
  `update_time` DATE DEFAULT NULL,
   KEY `id` (`id`)
);


CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_member` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `dept_id` INT NOT NULL,
  `order_id` INT NOT NULL,
  `user_name` VARCHAR(16) DEFAULT NULL,
  `password` VARCHAR(16) DEFAULT NULL,
  `mobile_phone` VARCHAR(16) DEFAULT NULL,
  `office_phone` VARCHAR(16) DEFAULT NULL,
  `email` VARCHAR(32) DEFAULT NULL,
  `job` VARCHAR(16) DEFAULT NULL,
  `status` LONGTEXT NULL,
  `create_user` VARCHAR(16) DEFAULT NULL,
  `update_user` VARCHAR(16) DEFAULT NULL,
  `create_time` DATE DEFAULT NULL,
  `update_time` DATE DEFAULT NULL,
   KEY `id` (`id`)
);
