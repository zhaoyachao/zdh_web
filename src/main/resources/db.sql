
create database if NOT EXISTS mydb;

create user 'zyc'@'%' identified by '123456';
create user 'zyc'@'localhost' identified by '123456';
create user 'zyc'@'127.0.0.1' identified by '123456';
GRANT USAGE ON *.* to zyc@'%';
GRANT ALL PRIVILEGES on *.* to zyc@'%';
GRANT USAGE ON *.* to zyc@'localhost';
GRANT ALL PRIVILEGES on *.* to zyc@'localhost';
GRANT USAGE ON *.* to zyc@'127.0.0.1';
GRANT ALL PRIVILEGES on *.* to zyc@'127.0.0.1';

FLUSH PRIVILEGES;


use mydb;

drop table if EXISTS zdh_ha_info;
create table zdh_ha_info(
id int not null AUTO_INCREMENT,
zdh_instance VARCHAR(100),
zdh_url VARCHAR(100),
zdh_host VARCHAR(15),
zdh_port varchar(5),
zdh_status varchar(10),
PRIMARY KEY (id)
);


drop table if EXISTS account_info;
create table account_info(
id int not null AUTO_INCREMENT,
user_name varchar(50),
user_password varchar(100),
email varchar(100),
PRIMARY KEY (id)
);
alter table account_info add column is_use_email varchar(10);
alter table account_info add column phone varchar(11);
alter table account_info add column is_use_phone varchar(10);

INSERT INTO `account_info` VALUES (2,'zyc','123456','zyc@qq.com','false');

drop table if EXISTS data_sources_info;
create table data_sources_info(
 id int not null AUTO_INCREMENT,
 data_source_context varchar(100),
 data_source_type varchar(100),
 driver varchar(100),
 url varchar(100),
 username varchar(100),
 password varchar(100),
 owner varchar(100),
  PRIMARY KEY (id)
);

INSERT INTO `data_sources_info` VALUES (53,'mydb','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2'),(54,'csv','HDFS','','','zyc@qq.com','123456','2'),(55,'mydb2','JDBC','com.mysql.cj.jdbc.Driver','jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false','zyc','123456','2'),(56,'HIVE1','HIVE','','','','','2');


drop table if EXISTS data_sources_type_info;
create table data_sources_type_info(
 id int not null AUTO_INCREMENT,
 sources_type varchar(100),
 PRIMARY KEY (id)
);
insert into data_sources_type_info(sources_type) values('JDBC');
insert into data_sources_type_info(sources_type) values('HDFS');
insert into data_sources_type_info(sources_type) values('HBASE');
insert into data_sources_type_info(sources_type) values('MONGODB');
insert into data_sources_type_info(sources_type) values('ES');
insert into data_sources_type_info(sources_type) values('HIVE');
insert into data_sources_type_info(sources_type) values('KAFKA');
insert into data_sources_type_info(sources_type) values('HTTP');
insert into data_sources_type_info(sources_type) values('REDIS');
insert into data_sources_type_info(sources_type) values('CASSANDRA');
insert into data_sources_type_info(sources_type) values('SFTP');
insert into data_sources_type_info(sources_type) values('KUDU');
insert into data_sources_type_info(sources_type) values('外部上传');
insert into data_sources_type_info(sources_type) values('FLUME');
insert into data_sources_type_info(sources_type) values('外部下载');

drop table if EXISTS etl_task_info;
create table etl_task_info(
 id int not null AUTO_INCREMENT,
 etl_context VARCHAR(200),
 data_sources_choose_input varchar(100),
 data_source_type_input varchar(100),
 data_sources_table_name_input varchar(100),
 data_sources_file_name_input varchar(100),
 data_sources_file_columns text,
 data_sources_table_columns text,
 data_sources_params_input varchar(500),
 data_sources_filter_input varchar(500),

 data_sources_choose_output varchar(100),
 data_source_type_output varchar(100),
 data_sources_table_name_output varchar(100),
 data_sources_file_name_output varchar(100),
 data_sources_params_output varchar(500),
 column_datas text,
 data_sources_clear_output varchar(500),
  owner varchar(100),
   PRIMARY KEY (id)
);
alter table etl_task_info add column create_time TIMESTAMP after owner;
alter table etl_task_info add column company varchar(100) ;
alter table etl_task_info add column section varchar(100);
alter table etl_task_info add column service varchar(100);
alter table etl_task_info add column update_context varchar(100);
alter table etl_task_info modify column id bigint(20) auto_increment ;


drop table if EXISTS etl_more_task_info;
create table etl_more_task_info(
 id bigint(20) not null AUTO_INCREMENT,
 etl_context VARCHAR(200),
 etl_ids varchar(200),
 etl_sql text,
 data_sources_choose_output varchar(100),
 data_source_type_output varchar(100),
 data_sources_table_name_output varchar(100),
 data_sources_file_name_output varchar(100),
 data_sources_params_output varchar(500),
 data_sources_clear_output varchar(500),
 owner varchar(100),
 PRIMARY KEY (id)
);
alter table etl_more_task_info add column create_time TIMESTAMP after owner;

drop table if EXISTS dispatch_task_info;
create table dispatch_task_info(
id int not null AUTO_INCREMENT,
dispatch_context varchar(100),
etl_task_id varchar(100),
etl_context varchar(100),
 owner varchar(100),
PRIMARY KEY (id)
);

drop table if EXISTS quartz_job_info;
create table quartz_job_info(
job_id VARCHAR(100) ,
job_context VARCHAR(100) ,
more_task VARCHAR(20),
job_type VARCHAR(100),
start_time TIMESTAMP ,
end_time TIMESTAMP,
step_size VARCHAR(100),
job_model VARCHAR(2),
plan_count VARCHAR(5),
count int,
command VARCHAR(100),
params text,
last_status VARCHAR(100),
last_time TIMESTAMP,
next_time TIMESTAMP,
expr VARCHAR(100),
status VARCHAR(100),
ip VARCHAR(100),
user VARCHAR(100),
password VARCHAR(100),
etl_task_id VARCHAR(100),
etl_context varchar(100),
owner varchar(100),
PRIMARY key(job_id)
);
alter table quartz_job_info modify column command text;
alter table quartz_job_info add column is_script varchar(10);

drop table if exists task_logs;
create table task_logs(
id bigint(20) not null AUTO_INCREMENT,
job_id VARCHAR(100) ,
job_context VARCHAR(100) ,
etl_date VARCHAR(20),
status varchar(10),
update_time TIMESTAMP ,
owner VARCHAR(100),
is_notice varchar(10),
process varchar(10),
PRIMARY key(id)
);
alter table task_logs modify column id bigint(20) auto_increment ;
alter table task_logs add column start_time TIMESTAMP after status;

drop TABLE if EXISTS zdh_logs;
create table zdh_logs(
job_id VARCHAR(100),
log_time TIMESTAMP ,
msg text,
level varchar(10)
);

drop table if EXISTS  zdh_nginx;
create table zdh_nginx(
id int not null AUTO_INCREMENT,
username VARCHAR(50),
password varchar(20),
host varchar(15),
port varchar(5),
owner varchar(10),
tmp_dir varchar(100),
nginx_dir varchar(100),
PRIMARY key(id)
);

drop table if EXISTS  zdh_download_info;
create table zdh_download_info(
id int not null AUTO_INCREMENT,
user_name VARCHAR(100),
file_name VARCHAR(200),
create_time TIMESTAMP,
down_count int,
etl_date TIMESTAMP,
owner VARCHAR(100),
PRIMARY key(id)
);
alter table zdh_download_info add column job_context varchar(100) after owner;
alter table zdh_download_info modify column job_context varchar(100);


drop table if EXISTS etl_task_meta;
create table etl_task_meta(
id  int not null AUTO_INCREMENT,
context varchar(100),
parent_id varchar(20),
PRIMARY key(id)
);

insert into etl_task_meta(context,parent_id)values('ZDH总公司','0');
insert into etl_task_meta(context,parent_id)values('ZDH分公司','0');

insert into etl_task_meta(context,parent_id)values('采购部','1');
insert into etl_task_meta(context,parent_id)values('财务部','1');

drop table if EXISTS etl_task_update_logs;
create table etl_task_update_logs(
id VARCHAR(100),
update_context varchar(100),
update_time TIMESTAMP,
owner VARCHAR(100)
);

--2020-05-25 更新;
alter table etl_task_info add column primary_columns varchar(100);
alter table etl_task_info add column column_size varchar(100);
alter table etl_task_info add column rows_range varchar(100);
alter table etl_task_info add column error_rate varchar(10);
alter table etl_task_info add column enable_quality varchar(10);
alter table etl_task_info add column duplicate_columns varchar(200);

drop table if EXISTS quality;
create table quality(
id VARCHAR(100),
dispatch_task_id varchar(100),
etl_task_id varchar(100),
etl_date varchar(20),
status varchar(20),
report VARCHAR(500),
create_time TIMESTAMP,
owner varchar(100)
);

--2020-05-26 更新;
alter table etl_more_task_info add column drop_tmp_tables varchar(500);


--2020-05-28 更新;
drop table if EXISTS sql_task_info;
create table sql_task_info(
 id bigint not null AUTO_INCREMENT,
 sql_context VARCHAR(200),
 data_sources_choose_input varchar(100),
 data_source_type_input varchar(100),
 data_sources_params_input varchar(500),
 etl_sql text ,
 data_sources_choose_output varchar(100),
 data_source_type_output varchar(100),
 data_sources_table_name_output varchar(100),
 data_sources_file_name_output varchar(100),
 data_sources_params_output varchar(500),
 data_sources_clear_output varchar(500),
 owner varchar(100),
 create_time TIMESTAMP ,
 company varchar(100),
 section varchar(100),
 service varchar(100),
 update_context varchar(100),
 PRIMARY KEY (id)
);

--2020-06-06 更新;
drop table if EXISTS meta_database_info;
create table meta_database_info(
 id bigint not null AUTO_INCREMENT,
 db_name VARCHAR(200),
 tb_name varchar(100),
 owner varchar(100),
 create_time TIMESTAMP ,
 PRIMARY KEY (id)
);

drop table if EXISTS meta_table_info;
create table meta_table_info(
 tb_id varchar(20),
 col_name varchar(100),
 data_type varchar(100),
 comment varchar(100),
 owner varchar(100),
 `order` varchar(5),
 create_time TIMESTAMP
);

--2020-06-08 更新;
alter table quartz_job_info add column job_ids varchar(500);

alter table etl_task_info add column file_type_input varchar(10);
alter table etl_task_info add column encoding_input varchar(10);
alter table etl_task_info add column sep_input varchar(10);

alter table etl_task_info add column file_type_output varchar(10);
alter table etl_task_info add column encoding_output varchar(10);
alter table etl_task_info add column sep_output varchar(10);

alter table etl_more_task_info add column file_type_output varchar(10);
alter table etl_more_task_info add column encoding_output varchar(10);
alter table etl_more_task_info add column sep_output varchar(10);

--2020-06-30 更新;
alter table etl_task_info add column header_input varchar(10);
alter table etl_task_info add column header_output varchar(10);

alter table etl_more_task_info add column header_output varchar(10);

alter table sql_task_info add column file_type_output varchar(10);
alter table sql_task_info add column encoding_output varchar(10);
alter table sql_task_info add column sep_output varchar(10);
alter table sql_task_info add column header_output varchar(10);

--2020-06-31 更新;
alter table zdh_ha_info add column web_port varchar(100) after zdh_port;


--2020-07-13 更新;
drop table if EXISTS jar_file_info;
create table jar_file_info(
 id varchar(20),
 file_name varchar(100),
 path varchar(100),
 create_time TIMESTAMP,
 jar_etl_id varchar(20),
 owner varchar(100),
 status varchar(10)
);

drop table if EXISTS jar_task_info;
create table jar_task_info(
 id varchar(20),
 etl_context varchar(100),
 files varchar(100),
 master varchar(100),
 deploy_mode varchar(20),
 cpu varchar(100),
 memory varchar(100),
 main_class varchar(100),
 spark_submit_params text,
 owner varchar(10),
 create_time TIMESTAMP
);

--2020-07-25 更新;
DROP TABLE IF EXISTS `etl_drools_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etl_drools_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL,
  `etl_id` varchar(200) DEFAULT NULL,
  `etl_drools` text,
  `data_sources_filter_input` text,
  `data_sources_choose_output` varchar(100) DEFAULT NULL,
  `data_source_type_output` varchar(100) DEFAULT NULL,
  `data_sources_table_name_output` varchar(100) DEFAULT NULL,
  `data_sources_file_name_output` varchar(100) DEFAULT NULL,
  `data_sources_params_output` varchar(500) DEFAULT NULL,
  `data_sources_clear_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `drop_tmp_tables` varchar(500) DEFAULT NULL,
  `file_type_output` varchar(10) DEFAULT NULL,
  `encoding_output` varchar(10) DEFAULT NULL,
  `sep_output` varchar(10) DEFAULT NULL,
  `header_output` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--2020-08-01 更新;
alter table quartz_job_info add column jump_dep varchar(10);
alter table quartz_job_info add column jump_script varchar(10);

alter table task_logs add column thread_id varchar(10);
alter table task_logs modify column thread_id varchar(100);
--2020-08-13 更新;
alter table task_logs add column retry_time TIMESTAMP;

--2020-08-14 更新;
alter table quartz_job_info add column interval_time varchar(20);

alter table quartz_job_info add column alarm_enabled varchar(10);
alter table quartz_job_info add column email_and_sms varchar(10);
alter table quartz_job_info add column alarm_account varchar(500);

alter table task_logs add column executor varchar(100);
alter table task_logs add column url varchar(100);
alter table task_logs add column etl_info text;

drop table if EXISTS ssh_task_info;
create table ssh_task_info(
 id bigint not null AUTO_INCREMENT,
 ssh_context VARCHAR(200),
 host varchar(100),
 port varchar(100),
 user_name varchar(500),
 password varchar(100),
 ssh_cmd text ,
 ssh_script_path varchar(100),
 ssh_script_context text,
 ssh_params_input varchar(500),
 owner varchar(100),
 create_time TIMESTAMP ,
 company varchar(100),
 section varchar(100),
 service varchar(100),
 update_context varchar(100),
 PRIMARY KEY (id)
);

--2020-08-30更新;
alter table etl_drools_task_info add column more_task varchar(100);

alter table zdh_ha_info add column application_id varchar(100);
alter table zdh_ha_info add column history_server varchar(100);
alter table zdh_ha_info add column master varchar(100);

alter table task_logs add column application_id varchar(100);
alter table task_logs add column history_server varchar(100);
alter table task_logs add column master varchar(100);

--2020-09-13更新;
insert into data_sources_type_info(sources_type) values('MEMSQL');

--2020-09-19更新;
alter table zdh_logs add column task_logs_id varchar(100);
alter table quartz_job_info add column task_log_id varchar(100);

alter table task_logs add column server_ack varchar(2);


--2020-09-22;
create table task_log_instance(
id bigint not null AUTO_INCREMENT,
job_id VARCHAR(100) ,
job_context VARCHAR(100) ,
etl_date VARCHAR(30),
status varchar(10),
run_time TIMESTAMP ,
update_time TIMESTAMP ,
owner VARCHAR(100),
is_notice varchar(10),
process varchar(10),
thread_id VARCHAR(100),
retry_time TIMESTAMP ,
executor VARCHAR(100),
etl_info text,
url VARCHAR(100),
application_id VARCHAR(100),
history_server VARCHAR(100),
master VARCHAR(100),
server_ack VARCHAR(100),
concurrency VARCHAR(100),
last_task_log_id VARCHAR(100),
more_task VARCHAR(20),
job_type VARCHAR(100),
start_time TIMESTAMP ,
end_time TIMESTAMP,
step_size VARCHAR(100),
job_model VARCHAR(2),
plan_count VARCHAR(5),
`count` int,
command VARCHAR(100),
params text,
last_status VARCHAR(100),
last_time TIMESTAMP,
next_time TIMESTAMP,
expr VARCHAR(100),
ip VARCHAR(100),
`user` VARCHAR(100),
password VARCHAR(100),
etl_task_id VARCHAR(100),
etl_context varchar(100),
is_script varchar(100),
job_ids varchar(100),
jump_dep varchar(100),
jump_script varchar(100),
interval_time varchar(100),
alarm_enabled varchar(100),
email_and_sms varchar(100),
alarm_account varchar(200),
PRIMARY KEY (id)
);

alter table task_log_instance add column cur_time TIMESTAMP;
alter table task_log_instance add column is_retryed varchar(4);

alter table task_log_instance add column server_id varchar(100);

alter table task_log_instance add column time_out varchar(100);
alter table quartz_job_info add column time_out varchar(100);

alter table task_log_instance add column process_time text;

-- 2020-11-14 更新;
alter table zdh_ha_info add column create_time timestamp null default current_timestamp;
alter table zdh_ha_info add column update_time timestamp null default current_timestamp;

drop table if EXISTS issue_data_info;
CREATE TABLE `issue_data_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `issue_context` varchar(200) DEFAULT NULL,
  `data_sources_choose_input` varchar(100) DEFAULT NULL,
  `data_source_type_input` varchar(100) DEFAULT NULL,
  `data_sources_table_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_name_input` varchar(100) DEFAULT NULL,
  `data_sources_file_columns` text,
  `data_sources_table_columns` text,
  `column_datas` text,
  `owner` varchar(100) DEFAULT NULL,
  `create_time` timestamp default current_timestamp,
  `company` varchar(100) DEFAULT NULL,
  `section` varchar(100) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `update_context` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- 2020-11-21 更新;
alter table task_log_instance add column priority varchar(4);
alter table task_log_instance add column quartz_time TIMESTAMP;
alter table task_log_instance add column use_quartz_time varchar(5);
alter table task_log_instance add column time_diff varchar(50);

alter table quartz_job_info add column priority varchar(4);
alter table quartz_job_info add column quartz_time TIMESTAMP;
alter table quartz_job_info add column use_quartz_time varchar(5);
alter table quartz_job_info add column time_diff varchar(50);


create database if NOT EXISTS quartz;

use quartz;
-- quartz
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID))
ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME))
ENGINE=InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);





