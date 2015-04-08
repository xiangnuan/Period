/*
 Navicat Premium Data Transfer

 Source Server         : sqlite3
 Source Server Type    : SQLite
 Source Server Version : 3008001
 Source Database       : main

 Target Server Type    : SQLite
 Target Server Version : 3008001
 File Encoding         : utf-8

 Date: 04/08/2015 14:12:10 PM
*/

PRAGMA foreign_keys = false;

-- ----------------------------
--  Table structure for qc_user
-- ----------------------------
DROP TABLE IF EXISTS "qc_user";
CREATE TABLE "qc_user" (
	 "uid" INTEGER,
	 "password" text,
	 "name" TEXT,
	 "age" integer,
	 "height" integer,
	 "weight" integer,
	 "period" integer,
	 "count" integer
);

PRAGMA foreign_keys = true;
