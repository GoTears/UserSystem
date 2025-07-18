/*
 Navicat Premium Data Transfer

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : user_center

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 18/07/2025 16:37:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `username` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名称',
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账号',
  `avatarUrl` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `userStatus` int NOT NULL DEFAULT 0 COMMENT '状态0-正常',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `userRole` int NOT NULL DEFAULT 0 COMMENT '用户角色 0 - 普通用户 1 - 管理员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('管理员', 1, 'admin', 'https://example.com/avatar1.png', 1, 'admin123456', '13800000001', 'admin@example.com', 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 1);
INSERT INTO `user` VALUES ('张三', 2, 'zhangsan', 'https://example.com/avatar2.png', 1, 'password123', '13800000002', 'zhangsan@example.com', 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES ('李四', 3, 'lisi', 'https://example.com/avatar3.png', 2, 'mypassword', '13800000003', 'lisi@example.com', 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 4, 'testuser', NULL, NULL, 'testpass123', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 5, 'admin', NULL, NULL, 'anotherpass', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 6, 'shortpwd', NULL, NULL, '123', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 7, '', NULL, NULL, 'validpassword', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 8, 'emptyPwd', NULL, NULL, '', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 9, 'deleteduser', NULL, NULL, 'validpassword', NULL, NULL, 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 1, 0);
INSERT INTO `user` VALUES (NULL, 10, 'banneduser', NULL, NULL, 'validpassword', NULL, NULL, 1, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);
INSERT INTO `user` VALUES (NULL, 11, 'bademail', NULL, NULL, 'validpassword', 'notaphone', 'not-an-email', 0, '2025-07-18 16:36:45', '2025-07-18 16:36:45', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
