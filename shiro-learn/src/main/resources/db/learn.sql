/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : learn

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 14/08/2019 15:35:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT 0,
  `sort` int(10) UNSIGNED NULL DEFAULT NULL,
  `external` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否外部链接',
  `available` tinyint(1) UNSIGNED NULL DEFAULT 0,
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sys_resource_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '用户管理', 'menu', NULL, NULL, 0, 1, 0, 1, 'fa fa-users', '2018-05-16 17:02:54', '2018-05-16 17:02:54');
INSERT INTO `permission` VALUES (2, '用户列表', 'menu', '/users', 'users', 1, 1, 0, 1, NULL, '2017-12-22 13:56:15', '2018-05-16 14:44:20');
INSERT INTO `permission` VALUES (3, '新增用户', 'button', NULL, 'user:add', 2, 2, 0, 1, NULL, '2018-05-16 14:07:43', '2018-05-16 14:16:23');
INSERT INTO `permission` VALUES (4, '批量删除用户', 'button', NULL, 'user:batchDelete', 2, 3, 0, 1, NULL, '2018-05-16 14:12:23', '2018-05-16 14:16:35');
INSERT INTO `permission` VALUES (5, '编辑用户', 'button', NULL, 'user:edit', 2, 4, 0, 1, NULL, '2018-05-16 14:12:50', '2018-05-16 14:16:43');
INSERT INTO `permission` VALUES (6, '删除用户', 'button', NULL, 'user:delete', 2, 5, 0, 1, NULL, '2018-05-16 14:13:09', '2018-05-16 14:51:50');
INSERT INTO `permission` VALUES (7, '分配用户角色', 'button', NULL, 'user:allotRole', 2, 6, 0, 1, NULL, '2018-05-16 14:15:28', '2018-05-16 14:16:54');
INSERT INTO `permission` VALUES (8, '系统配置', 'menu', NULL, NULL, 0, 2, 0, 1, 'fa fa-cogs', '2017-12-20 16:40:06', '2017-12-20 16:40:08');
INSERT INTO `permission` VALUES (9, '资源管理', 'menu', '/resources', 'resources', 8, 1, 0, 1, NULL, '2017-12-22 15:31:05', '2017-12-22 15:31:05');
INSERT INTO `permission` VALUES (10, '新增资源', 'button', NULL, 'resource:add', 9, 2, 0, 1, NULL, '2018-05-16 14:07:43', '2018-05-16 14:16:23');
INSERT INTO `permission` VALUES (11, '批量删除资源', 'button', NULL, 'resource:batchDelete', 9, 3, 0, 1, NULL, '2018-05-16 14:12:23', '2018-05-16 14:16:35');
INSERT INTO `permission` VALUES (12, '编辑资源', 'button', NULL, 'resource:edit', 9, 4, 0, 1, NULL, '2018-05-16 14:12:50', '2018-05-16 14:16:43');
INSERT INTO `permission` VALUES (13, '删除资源', 'button', NULL, 'resource:delete', 9, 5, 0, 1, NULL, '2018-05-16 14:13:09', '2018-05-16 14:51:50');
INSERT INTO `permission` VALUES (14, '角色管理', 'menu', '/roles', 'roles', 8, 2, 0, 1, '', '2017-12-22 15:31:27', '2018-05-17 12:51:06');
INSERT INTO `permission` VALUES (15, '新增角色', 'button', NULL, 'role:add', 14, 2, 0, 1, NULL, '2018-05-16 14:07:43', '2018-05-16 14:16:23');
INSERT INTO `permission` VALUES (16, '批量删除角色', 'button', NULL, 'role:batchDelete', 14, 3, 0, 1, NULL, '2018-05-16 14:12:23', '2018-05-16 14:16:35');
INSERT INTO `permission` VALUES (17, '编辑角色', 'button', NULL, 'role:edit', 14, 4, 0, 1, NULL, '2018-05-16 14:12:50', '2018-05-16 14:16:43');
INSERT INTO `permission` VALUES (18, '删除角色', 'button', NULL, 'role:delete', 14, 5, 0, 1, NULL, '2018-05-16 14:13:09', '2018-05-16 14:51:50');
INSERT INTO `permission` VALUES (19, '分配角色资源', 'button', NULL, 'role:allotResource', 14, 6, 0, 1, NULL, '2018-05-17 10:04:21', '2018-05-17 10:04:21');
INSERT INTO `permission` VALUES (20, '数据监控', 'menu', '', '', NULL, 3, 0, 1, 'fa fa-heartbeat', '2018-05-17 12:38:20', '2018-05-17 12:53:06');
INSERT INTO `permission` VALUES (21, 'Druid监控', 'menu', '/druid/index.html', 'druid', 20, 1, 1, 1, '', '2018-05-17 12:46:37', '2018-05-17 12:52:33');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'role:root', '超级管理员', 1, '2017-12-20 16:40:24', '2017-12-20 16:40:26');
INSERT INTO `role` VALUES (2, 'role:admin', '管理员', 1, '2017-12-22 13:56:39', '2017-12-22 13:56:39');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) UNSIGNED NOT NULL,
  `resources_id` bigint(20) UNSIGNED NOT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (27, 1, 20, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (28, 1, 21, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (29, 1, 1, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (30, 1, 2, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (31, 1, 3, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (32, 1, 4, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (33, 1, 5, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (34, 1, 6, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (35, 1, 7, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (36, 1, 8, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (37, 1, 9, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (38, 1, 10, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (39, 1, 11, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (40, 1, 12, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (41, 1, 13, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (42, 1, 14, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (43, 1, 15, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (44, 1, 16, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (45, 1, 17, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (46, 1, 18, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (47, 1, 19, '2018-05-17 12:52:41', '2018-05-17 12:52:41');
INSERT INTO `role_permission` VALUES (48, 2, 20, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (49, 2, 21, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (50, 2, 2, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (51, 2, 3, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (52, 2, 8, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (53, 2, 9, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (54, 2, 10, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (55, 2, 14, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (56, 2, 15, '2018-05-17 12:52:51', '2018-05-17 12:52:51');
INSERT INTO `role_permission` VALUES (57, 2, 1, '2018-05-17 12:52:51', '2018-05-17 12:52:51');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `department_id` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `mobile` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `gender` tinyint(2) UNSIGNED NULL DEFAULT NULL COMMENT '性别',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `reg_ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册IP',
  `last_login_ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最近登录IP',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近登录时间',
  `login_count` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '登录次数',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `status` int(1) UNSIGNED NULL DEFAULT NULL COMMENT '用户状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'root', '超级管理员', 'CGUx1FN++xS+4wNDFeN6DA==', NULL, NULL, '15151551516', '843977358@qq.com', NULL, NULL, 'https://static.zhyd.me/static/img/favicon.ico', NULL, '127.0.0.1', '2018-05-17 13:09:35', 228, NULL, 1, '2018-01-02 09:32:15', '2018-05-17 13:09:35');
INSERT INTO `user` VALUES (2, 'admin', '管理员', 'gXp2EbyZ+sB/A6QUMhiUJQ==', NULL, NULL, '15151551516', '843977358@qq.com', NULL, NULL, NULL, '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2018-05-17 13:08:30', 13, NULL, 1, '2018-01-02 15:56:34', '2018-05-17 13:08:30');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `role_id` bigint(20) UNSIGNED NOT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1, '2018-01-02 10:47:27', '2018-01-02 10:47:27');
INSERT INTO `user_role` VALUES (2, 2, 2, '2018-01-05 18:21:02', '2018-01-05 18:21:02');

SET FOREIGN_KEY_CHECKS = 1;
