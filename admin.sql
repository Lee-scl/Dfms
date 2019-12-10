/*
Navicat MySQL Data Transfer

Source Server         : LocalHost
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-12-11 00:36:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for login_info
-- ----------------------------
DROP TABLE IF EXISTS `login_info`;
CREATE TABLE `login_info` (
  `username` varchar(31) NOT NULL COMMENT '用户名',
  `password` varchar(31) NOT NULL COMMENT '密码',
  `rank` int(1) NOT NULL DEFAULT '1' COMMENT '用户权力',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_info
-- ----------------------------
INSERT INTO `login_info` VALUES ('root', '1234', '1');
INSERT INTO `login_info` VALUES ('ybjb', '1230', '1');
