--读写分离非分片
CREATE SCHEMA IF NOT EXISTS `tasfe_crud`;
CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `deptId` INT NOT NULL,
  `orderId` INT NOT NULL,
  `userName` VARCHAR(16) DEFAULT NULL,
  `password` VARCHAR(16) DEFAULT NULL,
  `mobilePhone` VARCHAR(16) DEFAULT NULL,
  `officePhone` VARCHAR(16) DEFAULT NULL,
  `email` VARCHAR(32) DEFAULT NULL,
  `job` VARCHAR(16) DEFAULT NULL,
  `status` LONGTEXT NULL,
  `createUser` VARCHAR(16) DEFAULT NULL,
  `updateUser` VARCHAR(16) DEFAULT NULL,
  `createTime` DATE DEFAULT NULL,
  `updateTime` DATE DEFAULT NULL,
   KEY `id` (`id`)
);


CREATE TABLE IF NOT EXISTS `tasfe_crud`.`t_member` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `deptId` INT NOT NULL,
  `orderId` INT NOT NULL,
  `userName` VARCHAR(16) DEFAULT NULL,
  `password` VARCHAR(16) DEFAULT NULL,
  `mobilePhone` VARCHAR(16) DEFAULT NULL,
  `officePhone` VARCHAR(16) DEFAULT NULL,
  `email` VARCHAR(32) DEFAULT NULL,
  `job` VARCHAR(16) DEFAULT NULL,
  `status` LONGTEXT NULL,
  `createUser` VARCHAR(16) DEFAULT NULL,
  `updateUser` VARCHAR(16) DEFAULT NULL,
  `createTime` DATE DEFAULT NULL,
  `updateTime` DATE DEFAULT NULL,
   KEY `id` (`id`)
);
