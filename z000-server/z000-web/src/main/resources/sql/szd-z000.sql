/*
 Navicat Premium Dump SQL

 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : 192.168.101.174:3306
 Source Schema         : szd-z000

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for core_client_buss_base
-- ----------------------------
DROP TABLE IF EXISTS `core_client_buss_base`;
CREATE TABLE `core_client_buss_base`  (
  `router_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由代码',
  `pk_val` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务主键值',
  `program` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '程序代码',
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号',
  `version` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `del_flag` bit(1) NULL DEFAULT NULL COMMENT '删除标记 true.已删除 false.未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`router_code`, `pk_val`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务主体' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of core_client_buss_base
-- ----------------------------
INSERT INTO `core_client_buss_base` VALUES ('CORE_SCP_03C', '112233', 'Z000_DLZ_02', '1862420489591459840', '', NULL, 'ADMIN', '2024-11-29 16:57:00', NULL, NULL);
INSERT INTO `core_client_buss_base` VALUES ('Z000_NOWF', '123', 'Z000_DAVE_03', '1839624807273074688', '', NULL, 'GAOYF', '2024-09-27 19:15:03', 'GAOYF', '2024-09-27 20:10:53');

-- ----------------------------
-- Table structure for z000_header
-- ----------------------------
DROP TABLE IF EXISTS `z000_header`;
CREATE TABLE `z000_header`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务ID',
  `cmpy_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生公司',
  `cstc_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生部门',
  `cmpy_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担公司',
  `cstc_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担部门',
  `cmpy_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生公司',
  `cstc_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生部门',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务人员',
  `curr` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种',
  `exchange_rate` decimal(9, 5) NULL DEFAULT NULL COMMENT '汇率',
  `buss_date` date NULL DEFAULT NULL COMMENT '业务日期',
  `buss_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态(A创建、B拒绝、C审批中、D审批完成、F付款完成或G已完成、E作废)',
  `sap_status_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外围系统状态描述',
  `payment_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务标题',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `buss_doc_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号',
  `bstp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `router_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务路由',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更改时间',
  PRIMARY KEY (`buss_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抬头表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_header
-- ----------------------------
INSERT INTO `z000_header` VALUES ('1859537053105131520', '2800', '2800000100', '2800', '2800000100', '2100', '2100000101', 'ADMIN', 'CNY', NULL, '2024-11-21', 'C', NULL, NULL, NULL, '222', 'VCH202411260001', NULL, NULL, 'ADMIN', '2024-12-09 15:05:31', 'ADMIN', NULL);
INSERT INTO `z000_header` VALUES ('1861217975202353152', '2800', '2800000100', '2800', '2800000100111', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-11-26', 'A', NULL, NULL, NULL, '222', NULL, NULL, NULL, 'ADMIN', '2024-12-03 11:56:20', 'ADMIN', NULL);
INSERT INTO `z000_header` VALUES ('1866012930470973440', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-09', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1866013873262432256', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-09', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1866014011926122496', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-09', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1866014176749686784', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-09', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1866014229832798208', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-09', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1867460397041324032', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2024-12-13', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1889682271024320512', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2025-02-12', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1892029060478013440', '2800', '2800000100', '1100', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2025-02-19', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);
INSERT INTO `z000_header` VALUES ('1892142367566336000', '2800', '2800000100', '2800', '2800000100', NULL, NULL, 'ADMIN', 'CNY', NULL, '2025-02-19', 'A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for z000_item
-- ----------------------------
DROP TABLE IF EXISTS `z000_item`;
CREATE TABLE `z000_item`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '费用ID',
  `item_no` int NOT NULL COMMENT '行项目',
  `bsub` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务小类',
  `accs` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会计科目',
  `debit_credit_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '借贷标识，S借 H贷',
  `taxr` decimal(5, 2) NULL DEFAULT NULL COMMENT '税率',
  `amt_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '金额',
  `amt_apply_no_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '金额(不含税)',
  `amt_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '税额',
  `supp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `cust` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户',
  `acc_subject_flag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '特别总账标识',
  `summary` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `cstc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成本中心',
  `prfc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '利润中心',
  `busc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务范围',
  `segment_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '段',
  `reas` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原因代码',
  `inv_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  `inv_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  PRIMARY KEY (`buss_id`, `item_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '行项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_item
-- ----------------------------
INSERT INTO `z000_item` VALUES ('1859537053105131520', 1, 'C001', '0051410100', NULL, 3.00, 1000.00, 970.87, 29.13, '1000', '1000', 'A', '111', '2100000101', '1002', '1500', '201', NULL, NULL, NULL);
INSERT INTO `z000_item` VALUES ('1861217975202353152', 1, 'C001', '0051410100', NULL, 3.00, 1000.00, 970.87, 29.13, '100', '10000', 'A', '摘要测试', '2100000102', '1004', '1500', '', NULL, NULL, NULL);
INSERT INTO `z000_item` VALUES ('1861217975202353152', 2, 'S002', NULL, NULL, 6.00, 507.00, 478.30, 28.70, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '48211556', '044002207111');
INSERT INTO `z000_item` VALUES ('1861217975202353152', 3, 'H001', NULL, NULL, 1.00, 152.00, 150.49, 1.51, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '11023903', '013002300104');
INSERT INTO `z000_item` VALUES ('1861217975202353152', 4, 'C001', NULL, NULL, 1.00, 175.00, 173.27, 1.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '11076021', '013002300104');
INSERT INTO `z000_item` VALUES ('1861217975202353152', 5, 'G001', NULL, NULL, 1.00, 175.00, 173.27, 1.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '11072171', '013002300104');

-- ----------------------------
-- Table structure for z000_mdm_cmpy
-- ----------------------------
DROP TABLE IF EXISTS `z000_mdm_cmpy`;
CREATE TABLE `z000_mdm_cmpy`  (
  `cmpy` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司代码',
  `cmpy_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `cmpy_name_short` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司简称',
  `acc_set` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账套',
  PRIMARY KEY (`cmpy`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公司代码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_mdm_cmpy
-- ----------------------------
INSERT INTO `z000_mdm_cmpy` VALUES ('1000', 'SAP公司1', 'SAP', 'Z001');
INSERT INTO `z000_mdm_cmpy` VALUES ('2800', '中国某控股（集团）有限公司', NULL, 'Z001');

-- ----------------------------
-- Table structure for z000_test_001
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_001`;
CREATE TABLE `z000_test_001`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `buss_doc_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号',
  `cstc_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制单人成本中心',
  `cmpy_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制单人公司代码',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报账人',
  `supp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `cust` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户',
  `cstc_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担成本中心',
  `cmpy_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担公司',
  `cstc_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生成本中心',
  `cmpy_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生公司',
  `bstp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `router_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
  `att_num` int NULL DEFAULT NULL COMMENT '附件张数',
  `curr` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种',
  `exchange_rate` decimal(9, 5) NULL DEFAULT NULL COMMENT '汇率',
  `buss_date` date NULL DEFAULT NULL COMMENT '业务日期',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用说明',
  `bank_summary` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行摘要',
  `user_num` int NULL DEFAULT NULL COMMENT '出差人数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态(A创建、B拒绝、C审批中、D审批完成、F付款完成或G已完成、E作废)',
  `sap_status_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外围系统状态描述',
  `payment_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `amt_bill_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '计划票据金额',
  `amt_bill_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '实际票据金额',
  `wf_title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `wf_create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `wf_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` bit(1) NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`buss_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_001
-- ----------------------------
INSERT INTO `z000_test_001` VALUES ('112233', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1811954200196812800', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1839554454643412992', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1839558650096652288', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1839559842197540864', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1839625151705124864', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001` VALUES ('1839918714678546432', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for z000_test_001_item
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_001_item`;
CREATE TABLE `z000_test_001_item`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `item_no` int NOT NULL COMMENT '行项目',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `start_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `end_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `days` int NULL DEFAULT NULL COMMENT '出差天数',
  `amt_hotel_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿申请金额',
  `amt_hotel_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿实际金额',
  `amt_hotel_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿税额',
  `amt_hotel_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿税额',
  `amt_tra_out_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通申请金额',
  `amt_tra_out_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通实际金额',
  `amt_tra_out_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通税额',
  `amt_tra_out_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通税额',
  `amt_tra_in_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '市内交通申请金额',
  `amt_tra_in_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '市内交通实际金额',
  `amt_other_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用申请金额',
  `amt_other_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用实际金额',
  `amt_other_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用税额',
  `amt_other_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用税额',
  `amt_total_sub_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用申请金额小计',
  `amt_total_sub_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用实际金额小计',
  `amt_total_sub_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用税额小计',
  `amt_total_sub_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用税额小计',
  `amt_subsidy_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '补贴申请金额',
  `amt_subsidy_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '补贴实际金额',
  `amt_total_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '申请合计',
  `amt_total_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '实际合计',
  `cstc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成本中心',
  `order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部订单',
  `prfc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '利润中心',
  `trade_partner_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '贸易伙伴',
  `busc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务范围',
  `segment_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '段',
  `apply_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单',
  `apply_item_no` int NULL DEFAULT NULL COMMENT '申请行项目',
  `contract_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合同号',
  `contract_item_no` int NULL DEFAULT NULL COMMENT '合同行项目',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`buss_id`, `item_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-行项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_001_item
-- ----------------------------
INSERT INTO `z000_test_001_item` VALUES ('1811954200196812800', 1, '2024-07-01', '2024-07-01', NULL, NULL, 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1833702452591988736', 1, '2024-09-11', '2024-09-14', 'sjz', 'bj', 3, NULL, 1111.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2');
INSERT INTO `z000_test_001_item` VALUES ('1833705812858638336', 1, '2024-09-11', '2024-09-14', 'sjz', 'bj', 2, NULL, 11111.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836228993876103168', 1, '2024-09-18', '2024-09-21', '石家庄', '北京', 33, NULL, 21111.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2');
INSERT INTO `z000_test_001_item` VALUES ('1836235252863995904', 1, '2024-09-18', '2024-09-20', 'sjz', 'bj', 3, NULL, 10000.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836238854739333120', 1, '2024-09-18', '2024-09-20', 'TJ', 'BJ', 2, NULL, 2000.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836239307430563840', 1, '2024-09-18', '2024-09-20', '天津', '北京', 12, NULL, 2000.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '第二次');
INSERT INTO `z000_test_001_item` VALUES ('1836244260383494144', 1, '2024-09-26', '2024-09-28', '1', '1', 1, NULL, 1.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO `z000_test_001_item` VALUES ('1836247057724215296', 1, '2024-09-18', '2024-09-19', '1', '1', 1, NULL, 1.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO `z000_test_001_item` VALUES ('1836250176357011456', 1, '2024-09-18', '2024-09-19', '1', '1', 1, NULL, 1.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO `z000_test_001_item` VALUES ('1836252682902441984', 1, '2024-09-25', '2024-09-26', '12', '12', 21, NULL, 21.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '12');
INSERT INTO `z000_test_001_item` VALUES ('1836314816936022016', 1, '2024-09-18', '2024-09-19', '123456', '123456', 0, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836320249373396992', 1, '2024-09-18', '2024-09-20', '12', '12', 0, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836354971721928704', 1, '2024-09-18', '2024-09-19', '1234', '1234', 0, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836576750168051712', 1, '2024-09-19', '2024-09-20', '22', '22', 1, NULL, 10.09, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1836736629289652224', 1, '2024-09-19', '2024-09-20', '2', '2', 0, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1837019748609691648', 1, '2024-09-20', '2024-09-21', '1', '1', 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1837034149475651584', 1, '2024-09-20', '2024-09-20', '2', '2', 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1837034977364152320', 1, '2024-09-20', '2024-09-20', '1', '1', 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL);
INSERT INTO `z000_test_001_item` VALUES ('1838126742481539072', 1, '2024-09-23', '2024-09-28', '2', '2', 2, NULL, 2.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2');
INSERT INTO `z000_test_001_item` VALUES ('1838768213010288640', 1, '2024-10-16', '2024-10-25', '2让', '让 ', 2, NULL, 222.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2');
INSERT INTO `z000_test_001_item` VALUES ('1838945869907496960', 1, '2024-09-25', '2024-09-25', '北京', '上海', 9, 10000.00, 10000.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL);

-- ----------------------------
-- Table structure for z000_test_001_item_sub1
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_001_item_sub1`;
CREATE TABLE `z000_test_001_item_sub1`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `item_no` int NOT NULL COMMENT '行项目',
  `item_sub_no` int NOT NULL COMMENT '子项目',
  `trade_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `bsub` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交通类型',
  `amt_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '金额',
  `amt_dedu` decimal(15, 2) NULL DEFAULT NULL COMMENT '扣减金额',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `inv_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  `inv_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  PRIMARY KEY (`buss_id`, `item_no`, `item_sub_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-子项目1' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_001_item_sub1
-- ----------------------------
INSERT INTO `z000_test_001_item_sub1` VALUES ('1837034977364152320', 1, 1, '2024-09-20', '222', 0.00, 0.00, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item_sub1` VALUES ('1837034977364152320', 1, 2, '2024-09-20', '333', 0.00, 0.00, NULL, NULL, NULL);
INSERT INTO `z000_test_001_item_sub1` VALUES ('1838945869907496960', 1, 1, '2024-09-25', '火车', 300.00, 0.00, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for z000_test_001_item_sub2
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_001_item_sub2`;
CREATE TABLE `z000_test_001_item_sub2`  (
  `buss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `item_no` int NOT NULL COMMENT '行项目',
  `item_sub_no` int NOT NULL COMMENT '子项目',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `start_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `end_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `inv_spec_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否专票',
  `inv_dedu_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否抵扣',
  `bsub` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交通类型',
  `flight_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '航班号/火车号',
  `seat_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '座位等级',
  `amt_a1` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A1总金额()',
  `amt_a2_dedu` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A2扣减金额',
  `amt_a` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A总金额(A1-A2)',
  `amt_b1_airport` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B1机场建设费(不可抵扣)',
  `amt_b2_insurance` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B2保险(不可抵扣)',
  `amt_b3_other` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B3其他(不可抵扣)',
  `amt_b_total_sub` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B小计(不可抵扣)',
  `taxr` decimal(15, 2) NULL DEFAULT NULL COMMENT '税率',
  `amt_c` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C金额(含税 A-B)',
  `amt_c_notax` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C金额(不含税)',
  `amt_c_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C税额',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `inv_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  `inv_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  PRIMARY KEY (`buss_id`, `item_no`, `item_sub_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-子项目2' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_001_item_sub2
-- ----------------------------

-- ----------------------------
-- Table structure for z000_test_002
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_002`;
CREATE TABLE `z000_test_002`  (
  `pk_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `buss_doc_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号',
  `cstc_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制单人成本中心',
  `cmpy_create` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制单人公司代码',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报账人',
  `supp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `cust` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户',
  `cstc_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担成本中心',
  `cmpy_exp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用承担公司',
  `cstc_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生成本中心',
  `cmpy_buss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务发生公司',
  `bstp` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `router_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由',
  `att_num` int NULL DEFAULT NULL COMMENT '附件张数',
  `curr` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种',
  `exchange_rate` decimal(9, 5) NULL DEFAULT NULL COMMENT '汇率',
  `buss_date` date NULL DEFAULT NULL COMMENT '业务日期',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用说明',
  `bank_summary` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行摘要',
  `user_num` int NULL DEFAULT NULL COMMENT '出差人数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态(A创建、B拒绝、C审批中、D审批完成、F付款完成或G已完成、E作废)',
  `sap_status_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外围系统状态描述',
  `payment_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `amt_bill_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '计划票据金额',
  `amt_bill_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '实际票据金额',
  `wf_title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `wf_create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `wf_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` bit(1) NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`pk_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_002
-- ----------------------------
INSERT INTO `z000_test_002` VALUES ('123', '123', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_002` VALUES ('222', '222', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_002` VALUES ('345', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `z000_test_002` VALUES ('456', '456', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for z000_test_002_item
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_002_item`;
CREATE TABLE `z000_test_002_item`  (
  `pk_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `pk_item_no` int NOT NULL COMMENT '行项目',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `start_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `end_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `days` int NULL DEFAULT NULL COMMENT '出差天数',
  `amt_hotel_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿申请金额',
  `amt_hotel_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿实际金额',
  `amt_hotel_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿税额',
  `amt_hotel_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '住宿税额',
  `amt_tra_out_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通申请金额',
  `amt_tra_out_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通实际金额',
  `amt_tra_out_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通税额',
  `amt_tra_out_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '长途交通税额',
  `amt_tra_in_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '市内交通申请金额',
  `amt_tra_in_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '市内交通实际金额',
  `amt_other_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用申请金额',
  `amt_other_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用实际金额',
  `amt_other_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用税额',
  `amt_other_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '其他费用税额',
  `amt_total_sub_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用申请金额小计',
  `amt_total_sub_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用实际金额小计',
  `amt_total_sub_apply_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用税额小计',
  `amt_total_sub_actual_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT '费用税额小计',
  `amt_subsidy_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '补贴申请金额',
  `amt_subsidy_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '补贴实际金额',
  `amt_total_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '申请合计',
  `amt_total_actual` decimal(15, 2) NULL DEFAULT NULL COMMENT '实际合计',
  `cstc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成本中心',
  `order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部订单',
  `prfc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '利润中心',
  `trade_partner_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '贸易伙伴',
  `busc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务范围',
  `segment_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '段',
  `apply_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单',
  `apply_item_no` int NULL DEFAULT NULL COMMENT '申请行项目',
  `contract_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合同号',
  `contract_item_no` int NULL DEFAULT NULL COMMENT '合同行项目',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pk_id`, `pk_item_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-行项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_002_item
-- ----------------------------
INSERT INTO `z000_test_002_item` VALUES ('345', 1, '2024-09-28', '2024-09-28', '石家庄', '北京', 0, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '', '', '', '', '', '', '', 0, '', 0, '');

-- ----------------------------
-- Table structure for z000_test_002_item_sub1
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_002_item_sub1`;
CREATE TABLE `z000_test_002_item_sub1`  (
  `pk_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `pk_item_no` int NOT NULL COMMENT '行项目',
  `pk_item_sub_no` int NOT NULL COMMENT '子项目',
  `trade_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `bsub` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交通类型',
  `amt_apply` decimal(15, 2) NULL DEFAULT NULL COMMENT '金额',
  `amt_dedu` decimal(15, 2) NULL DEFAULT NULL COMMENT '扣减金额',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `inv_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  `inv_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  PRIMARY KEY (`pk_id`, `pk_item_no`, `pk_item_sub_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-子项目1' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_002_item_sub1
-- ----------------------------
INSERT INTO `z000_test_002_item_sub1` VALUES ('345', 1, 1, '2024-09-28', '', 0.00, 0.00, '', '', '');

-- ----------------------------
-- Table structure for z000_test_002_item_sub2
-- ----------------------------
DROP TABLE IF EXISTS `z000_test_002_item_sub2`;
CREATE TABLE `z000_test_002_item_sub2`  (
  `pk_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务单号',
  `pk_item_no` int NOT NULL COMMENT '行项目',
  `pk_item_sub_no` int NOT NULL COMMENT '子项目',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `start_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出发城市',
  `end_city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到达城市',
  `inv_spec_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否专票',
  `inv_dedu_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否抵扣',
  `bsub` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交通类型',
  `flight_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '航班号/火车号',
  `seat_class` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '座位等级',
  `amt_a1` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A1总金额()',
  `amt_a2_dedu` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A2扣减金额',
  `amt_a` decimal(15, 2) NULL DEFAULT NULL COMMENT 'A总金额(A1-A2)',
  `amt_b1_airport` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B1机场建设费(不可抵扣)',
  `amt_b2_insurance` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B2保险(不可抵扣)',
  `amt_b3_other` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B3其他(不可抵扣)',
  `amt_b_total_sub` decimal(15, 2) NULL DEFAULT NULL COMMENT 'B小计(不可抵扣)',
  `taxr` decimal(15, 2) NULL DEFAULT NULL COMMENT '税率',
  `amt_c` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C金额(含税 A-B)',
  `amt_c_notax` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C金额(不含税)',
  `amt_c_tax` decimal(15, 2) NULL DEFAULT NULL COMMENT 'C税额',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `inv_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票代码',
  `inv_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  PRIMARY KEY (`pk_id`, `pk_item_no`, `pk_item_sub_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '零代码测试表单-子项目2' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of z000_test_002_item_sub2
-- ----------------------------
INSERT INTO `z000_test_002_item_sub2` VALUES ('345', 1, 1, '2024-09-28', '2024-09-28', '', '', '', '', '', '', '', 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '', '', '');

SET FOREIGN_KEY_CHECKS = 1;
