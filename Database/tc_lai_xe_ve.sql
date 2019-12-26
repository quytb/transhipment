/*
 Navicat Premium Data Transfer

 Source Server         : VungTau_dev
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 103.141.140.12:3306
 Source Schema         : db_phatlocan

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 14/11/2019 15:19:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tc_lai_xe_ve
-- ----------------------------
DROP TABLE IF EXISTS `tc_lai_xe_ve`;
CREATE TABLE `tc_lai_xe_ve`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lai_xe_id` int(11) NULL DEFAULT NULL COMMENT 'id lai xe',
  `lenh_id` int(11) NULL DEFAULT NULL COMMENT 'id lenh',
  `tc_ve_id` int(11) NULL DEFAULT NULL COMMENT 'id bang tc_ve',
  `ve_status` int(1) NOT NULL,
  `ly_do_huy` varchar(255) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT '' COMMENT 'lý do nếu hủy vé',
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `created_by` int(11) NULL DEFAULT NULL,
  `last_updated_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `last_updated_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNQ_lai_xe_ve_lai_xe_id_lenh_id,_tc_ve_id`(`lai_xe_id`, `lenh_id`, `tc_ve_id`) USING BTREE COMMENT 'Rang buoc doc nhat'
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf32 COLLATE = utf32_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
