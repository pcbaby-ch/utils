/*
Navicat MySQL Data Transfer

Source Server         : 49.4.10.6_EMS
Source Server Version : 50728
Source Host           : 49.4.10.6:3306
Source Database       : ems

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2019-10-22 17:58:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(10) NOT NULL COMMENT '权限名称',
  `permission` varchar(10) NOT NULL COMMENT '权限内容（存储数据:btn_userPage_userAddBtn）',
  `create_user` varchar(25) NOT NULL,
  `create_time` datetime NOT NULL,
  `last_update_user` varchar(25) NOT NULL,
  `last_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='XX系统权限资源表-后期扩展需要，暂时忽略XX';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(25) NOT NULL COMMENT '角色名称（唯一不可重复）',
  `permissions` varchar(255) NOT NULL COMMENT '角色权限（示例存储数据:btn_userPage_userAddBtn,page_userPage{按钮权限btn开头，页面权限page开头}）',
  `create_user` varchar(25) NOT NULL,
  `create_time` datetime NOT NULL,
  `last_update_user` varchar(25) NOT NULL,
  `last_update_time` datetime NOT NULL,
  PRIMARY KEY (`id`,`role_name`),
  KEY `UK_roleName` (`role_name`) COMMENT '角色唯一'
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='系统角色权限表（记录某个角色的权限资源）';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(25) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(25) NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(25) NOT NULL DEFAULT '' COMMENT '姓名',
  `title` varchar(25) DEFAULT '' COMMENT '职位',
  `phone_number` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `email` varchar(25) DEFAULT '' COMMENT '邮件',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '启用状态，"0":禁用,"1":启用',
  `ref_role_ids` varchar(50) DEFAULT '' COMMENT '用户角色id(用户关联的角色集合,存储示例:3,4,5,7)',
  `create_user` varchar(25) NOT NULL,
  `create_time` datetime NOT NULL,
  `last_update_user` varchar(25) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标识(0:未删除；1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_phoneNumber` (`phone_number`) COMMENT '手机号唯一',
  UNIQUE KEY `UK_userName` (`user_name`) COMMENT '用户名唯一'
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='系统用户表';
