/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50546
Source Host           : localhost:3306
Source Database       : zzoa

Target Server Type    : MYSQL
Target Server Version : 50546
File Encoding         : 65001

Date: 2017-04-05 19:07:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_column
-- ----------------------------
DROP TABLE IF EXISTS `tb_column`;
CREATE TABLE `tb_column` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `column_sign` varchar(255) DEFAULT NULL,
  `column_name` varchar(255) NOT NULL COMMENT '列名',
  `is_show` int(11) NOT NULL DEFAULT '1' COMMENT '是否展示 1显示 0不显示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_column
-- ----------------------------
INSERT INTO `tb_column` VALUES ('1', 'name', '姓名', '1');
INSERT INTO `tb_column` VALUES ('2', 'department', '部门', '1');
INSERT INTO `tb_column` VALUES ('3', 'position', '职位', '1');
INSERT INTO `tb_column` VALUES ('4', 'entryDate', '入职日期', '1');
INSERT INTO `tb_column` VALUES ('5', 'isFormal', '是否转正', '1');
INSERT INTO `tb_column` VALUES ('6', 'formalDate', '转正日期', '1');
INSERT INTO `tb_column` VALUES ('7', 'companyAge', '司龄', '1');
INSERT INTO `tb_column` VALUES ('8', 'idNumber', '身份证号', '1');
INSERT INTO `tb_column` VALUES ('9', 'birth', '出生日期', '1');
INSERT INTO `tb_column` VALUES ('10', 'age', '年龄', '0');
INSERT INTO `tb_column` VALUES ('11', 'gender', '性别', '0');
INSERT INTO `tb_column` VALUES ('12', 'preSeniority', '入职前工龄', '0');
INSERT INTO `tb_column` VALUES ('13', 'seniority', '工龄', '0');
INSERT INTO `tb_column` VALUES ('14', 'payCard', '工资卡号', '0');
INSERT INTO `tb_column` VALUES ('15', 'politicalStatus', '政治面貌', '0');
INSERT INTO `tb_column` VALUES ('16', 'jobNumber', '工号', '0');
INSERT INTO `tb_column` VALUES ('17', 'contractStart', '合同起始日期', '0');
INSERT INTO `tb_column` VALUES ('18', 'contractEnd', '合同结束日期', '0');
INSERT INTO `tb_column` VALUES ('19', 'tel', '电话', '0');
INSERT INTO `tb_column` VALUES ('20', 'graduateSchool', '毕业院校', '0');
INSERT INTO `tb_column` VALUES ('21', 'major', '专业', '0');
INSERT INTO `tb_column` VALUES ('22', 'education', '学历', '0');
INSERT INTO `tb_column` VALUES ('23', 'graduateDate', '毕业日期', '0');
INSERT INTO `tb_column` VALUES ('24', 'professionalLevel', '职称/资格级别', '0');
INSERT INTO `tb_column` VALUES ('25', 'professionalName', '职称/资格名称', '0');
INSERT INTO `tb_column` VALUES ('26', 'certificateValidity', '证书有效期', '0');
INSERT INTO `tb_column` VALUES ('27', 'nativePlace', '籍贯', '0');
INSERT INTO `tb_column` VALUES ('28', 'homeAddress', '家庭住址', '0');
INSERT INTO `tb_column` VALUES ('29', 'censusRegister', '户籍', '0');
INSERT INTO `tb_column` VALUES ('30', 'fileKeep', '档案保管', '0');
INSERT INTO `tb_column` VALUES ('31', 'remark', '备注', '0');
INSERT INTO `tb_column` VALUES ('32', 'emergencyContactor', '紧急联系人', '0');
INSERT INTO `tb_column` VALUES ('33', 'emergencyTel', '联系电话', '0');
INSERT INTO `tb_column` VALUES ('34', 'company', '所属公司', '0');
INSERT INTO `tb_column` VALUES ('35', 'onJob', '是否在职', '0');
INSERT INTO `tb_column` VALUES ('36', 'leaveTime', '离职日期', '0');
INSERT INTO `tb_column` VALUES ('37', 'itAttributes', '高新技术人员属性', '0');
INSERT INTO `tb_column` VALUES ('38', 'researchStaff', '高新技术研发人员', '0');
INSERT INTO `tb_column` VALUES ('39', 'inAssurance', '参加社保', '0');

-- ----------------------------
-- Table structure for tb_staff
-- ----------------------------
DROP TABLE IF EXISTS `tb_staff`;
CREATE TABLE `tb_staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `staff_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '姓名',
  `department` varchar(255) DEFAULT NULL COMMENT '部门',
  `position` varchar(255) DEFAULT NULL COMMENT '职位',
  `entry_date` datetime DEFAULT NULL COMMENT '入职日期',
  `is_formal` int(11) NOT NULL COMMENT '是否转正 1是 0否',
  `formal_date` datetime DEFAULT NULL COMMENT '转正日期',
  `company_age` float DEFAULT NULL COMMENT '司龄',
  `id_number` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `birth` datetime DEFAULT NULL COMMENT '出生日期',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` int(11) DEFAULT NULL COMMENT '性别 0女 1男',
  `pre_seniority` float DEFAULT NULL COMMENT '入职前工龄',
  `seniority` varchar(255) DEFAULT NULL COMMENT '工龄',
  `pay_card` varchar(255) DEFAULT NULL COMMENT '工资卡号',
  `political_status` varchar(255) DEFAULT NULL COMMENT '政治面貌',
  `job_number` varchar(255) DEFAULT NULL COMMENT '工号',
  `contract_start` datetime DEFAULT NULL COMMENT '合同开始日期',
  `contract_end` datetime DEFAULT NULL COMMENT '合同结束日期',
  `tel` varchar(255) DEFAULT NULL COMMENT '电话',
  `graduate_school` varchar(255) DEFAULT NULL COMMENT '毕业院校',
  `major` varchar(255) DEFAULT NULL COMMENT '专业',
  `education` varchar(255) DEFAULT NULL COMMENT '学历',
  `graduate_date` datetime DEFAULT NULL COMMENT '毕业日期',
  `professional_level` varchar(255) DEFAULT NULL COMMENT '职称/资格级别',
  `professional_name` varchar(255) DEFAULT NULL COMMENT '职称/资格名称',
  `certificate_validity` datetime DEFAULT NULL COMMENT '证书有效期',
  `native_place` varchar(255) DEFAULT NULL COMMENT '籍贯',
  `home_address` varchar(255) DEFAULT NULL COMMENT '家庭住址',
  `census_register` varchar(255) DEFAULT NULL COMMENT '户籍',
  `file_keep` varchar(255) DEFAULT NULL COMMENT '档案保管',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `emergency_contactor` varchar(255) DEFAULT NULL COMMENT '紧急联系人',
  `emergency_tel` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `company` varchar(255) DEFAULT NULL COMMENT '所属公司',
  `on_job` int(11) DEFAULT NULL COMMENT '是否在职 1在职 0离职',
  `leave_time` datetime DEFAULT NULL COMMENT '离职日期',
  `it_attributes` varchar(255) DEFAULT NULL COMMENT '高新技术 人员属性',
  `research_staff` varchar(255) DEFAULT NULL COMMENT '高新技术 研发人员',
  `in_assurance` int(255) DEFAULT NULL COMMENT '是否参加社保 0否 1是',
  `delete_flag` int(11) DEFAULT '0' COMMENT '删除标识 1删除 0未删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_staff
-- ----------------------------
INSERT INTO `tb_staff` VALUES ('1', '74', '房桂堂', '无锡研发中心', 'JAVA开发工程师', '2017-05-30 15:56:31', '1', '2017-07-30 15:56:41', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('2', '75', '小明', '无锡研发中心', 'JAVA开发工程师', '2017-05-12 15:56:31', '0', '2017-07-12 15:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('3', '76', '小冬瓜', '无锡研发中心', 'JAVA开发工程师', '2017-05-05 19:56:31', '0', '2017-07-18 15:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('4', '77', '小康', '无锡研发中心', 'JAVA开发工程师', '2017-05-12 15:56:34', '1', '2017-06-29 15:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('5', '78', '小菜', '无锡研发中心', 'c#开发工程师', '2017-04-12 15:56:31', '0', '2017-07-13 15:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('6', '79', '小彭', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('7', '80', '大一', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('8', '81', '大二', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('9', '82', '大三', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('10', '83', '大四', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('11', '84', '打五', '上海研发中心', 'JAVA开发工程师', '2017-03-16 09:34:36', '1', '2017-07-05 17:56:41', null, '', '0000-00-00 00:00:00', null, null, null, null, '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '', '', '', '', '0000-00-00 00:00:00', '', '', '0000-00-00 00:00:00', '', '', '', '', null, '', '', '', null, '0000-00-00 00:00:00', '', '', null, '0', null);
INSERT INTO `tb_staff` VALUES ('12', '1488527655781', 'a', 'a', 'a', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('14', '1488528316238', 'a', 'a', 'a', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('15', '1488528885769', 'c', 'c', 'c', null, '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('16', '1488530350684', 'r', 'r', 'r', '2017-03-23 10:10:00', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('17', '1488766929405', 'gg', 'gg', 'gg', '2017-03-08 10:50:00', '0', null, '2', '323232', '2017-03-08 07:10:00', '12', '1', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('18', '1488781588724', '房桂堂', 'v', 'z', '2017-03-06 00:00:00', '1', null, '12', '121212', null, '12', '1', '12', '', '1212', '12', '12', null, null, '110', 'nan', 'ruan', 'benke', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
INSERT INTO `tb_staff` VALUES ('19', '1488962685349', '方芳芳', 'aa', 'aa', '2017-03-05 00:00:00', '1', null, '2', '21', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(255) NOT NULL,
  `USER_NAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `DELETE_FLAG` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', '1111', 'admin', '123456', '2017-02-23 11:10:32', '0');
INSERT INTO `tb_user` VALUES ('2', '1487819552690', 'fang', '123', '2017-02-23 11:12:32', '0');
INSERT INTO `tb_user` VALUES ('3', '1487819562994', 'gu', '123', '2017-02-23 11:12:42', '1');
INSERT INTO `tb_user` VALUES ('4', '1487819570608', 'gen', '123', '2017-02-23 11:12:50', '0');
INSERT INTO `tb_user` VALUES ('5', '1487819577382', 'yan', '123', '2017-02-23 11:12:57', '1');
INSERT INTO `tb_user` VALUES ('6', '1487819584779', 'ceshi', '123', '2017-02-23 11:13:04', '0');
INSERT INTO `tb_user` VALUES ('7', '1487829297745', 'yy', '123', '2017-02-23 13:54:57', '1');
INSERT INTO `tb_user` VALUES ('8', '1487832276738', 'ceshi1', '123', '2017-02-23 14:44:36', '0');
INSERT INTO `tb_user` VALUES ('9', '1487836943677', 'fff', '123', '2017-02-23 16:02:23', '0');
