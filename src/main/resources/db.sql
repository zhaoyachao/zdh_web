
create database if NOT EXISTS zdh;

create user 'zyc'@'%' identified by '123456';
create user 'zyc'@'localhost' identified by '123456';
create user 'zyc'@'127.0.0.1' identified by '123456';
GRANT USAGE ON *.* to zyc@'%';
GRANT ALL PRIVILEGES on *.* to zyc@'%';
GRANT USAGE ON *.* to zyc@'localhost';
GRANT ALL PRIVILEGES on *.* to zyc@'localhost';
GRANT USAGE ON *.* to zyc@'127.0.0.1';
GRANT ALL PRIVILEGES on *.* to zyc@'127.0.0.1';

ALTER USER 'zyc'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
ALTER USER 'zyc'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY '123456';
ALTER USER 'zyc'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';

FLUSH PRIVILEGES;

use zdh;


drop table if EXISTS zdh_ha_info;
create table zdh_ha_info(
id int not null AUTO_INCREMENT,
zdh_instance VARCHAR(128),
zdh_url VARCHAR(500),
zdh_host VARCHAR(128),
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
insert into data_sources_type_info(sources_type) values('Greenplum');
insert into data_sources_type_info(sources_type) values('TIDB');

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ;

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

alter table zdh_ha_info add column application_id varchar(500);
alter table zdh_ha_info add column history_server varchar(500);
alter table zdh_ha_info add column master varchar(500);

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

-- 2020-12-05
alter table quartz_job_info add column jsmind_data text;
alter table task_log_instance add column jsmind_data text;

alter table task_log_instance add column run_jsmind_data text;
alter table task_log_instance add column next_tasks text;
alter table task_log_instance add column pre_tasks text;
alter table task_log_instance add column group_id varchar(100) after  job_context;
alter table task_log_instance add column group_context varchar(500) after  group_id;


drop table task_group_log_instance;
CREATE TABLE `task_group_log_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` varchar(100) DEFAULT NULL,
  `job_context` varchar(100) DEFAULT NULL,
  `etl_date` varchar(30) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `run_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `is_notice` varchar(10) DEFAULT NULL,
  `process` varchar(10) DEFAULT NULL,
  `thread_id` varchar(100) DEFAULT NULL,
  `retry_time` timestamp NULL DEFAULT NULL,
  `executor` varchar(100) DEFAULT NULL,
  `etl_info` text,
  `url` varchar(100) DEFAULT NULL,
  `application_id` varchar(100) DEFAULT NULL,
  `history_server` varchar(100) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `server_ack` varchar(100) DEFAULT NULL,
  `concurrency` varchar(100) DEFAULT NULL,
  `last_task_log_id` varchar(100) DEFAULT NULL,
  `more_task` varchar(20) DEFAULT NULL,
  `job_type` varchar(100) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `step_size` varchar(100) DEFAULT NULL,
  `job_model` varchar(2) DEFAULT NULL,
  `plan_count` varchar(5) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `command` varchar(100) DEFAULT NULL,
  `params` text,
  `last_status` varchar(100) DEFAULT NULL,
  `last_time` timestamp NULL DEFAULT NULL,
  `next_time` timestamp NULL DEFAULT NULL,
  `expr` varchar(100) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `etl_task_id` varchar(100) DEFAULT NULL,
  `etl_context` varchar(100) DEFAULT NULL,
  `is_script` varchar(100) DEFAULT NULL,
  `job_ids` varchar(100) DEFAULT NULL,
  `jump_dep` varchar(100) DEFAULT NULL,
  `jump_script` varchar(100) DEFAULT NULL,
  `interval_time` varchar(100) DEFAULT NULL,
  `alarm_enabled` varchar(100) DEFAULT NULL,
  `email_and_sms` varchar(100) DEFAULT NULL,
  `alarm_account` varchar(200) DEFAULT NULL,
  `cur_time` timestamp NULL DEFAULT NULL,
  `is_retryed` varchar(4) DEFAULT NULL,
  `server_id` varchar(100) DEFAULT NULL,
  `time_out` varchar(100) DEFAULT NULL,
  `process_time` text,
  `priority` varchar(4) DEFAULT NULL,
  `quartz_time` timestamp NULL DEFAULT NULL,
  `use_quartz_time` varchar(5) DEFAULT NULL,
  `time_diff` varchar(50) DEFAULT NULL,
  `jsmind_data` text,
  `run_jsmind_data` text,
  PRIMARY KEY (`id`)
);

-- 2020-12-19 ;
alter table task_group_log_instance add column next_tasks text;
alter table task_group_log_instance add column pre_tasks text;

-- 2020-12-27
drop TABLE if EXISTS every_day_notice;
create table every_day_notice(
id bigint NOT NULL AUTO_INCREMENT,
msg text comment '通知消息',
is_delete varchar(10) comment '是否删除消息',
primary key (id)
);


-- 2021-01-24
drop TABLE if EXISTS resource_tree_info;
create table resource_tree_info(
id bigint NOT NULL AUTO_INCREMENT,
parent varchar(100) comment '父节点',
`text` varchar(200) comment '节点名称',
level varchar(10) comment '层级',
`owner` varchar(100) comment '拥有者',
icon varchar(200) comment '资源图标',
resource_desc varchar(200) comment '资源说明',
`order` varchar(10) comment '顺序',
is_enable varchar(100) comment '是否启用',
`create_time` timestamp NULL DEFAULT current_timestamp ,
`update_time` timestamp NULL DEFAULT current_timestamp,
url text comment 'url链接',
primary key (id)
);

INSERT INTO `resource_tree_info` VALUES (802848818109353984,'#','ZDH','1','1','fa fa-folder',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-30 08:43:35',''),(802850170428461056,'802848818109353984','总监控','2','1','fa fa-bar-chart',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-29 21:56:12','monitor.html'),(802852358580080640,'802848818109353984','ETL采集','2','1','fa fa-tasks',NULL,'2',NULL,'2021-01-23 10:34:34','2021-01-30 14:31:56',''),(802876953722884096,'802848818109353984','数据质量报告','2','1','fa fa-hourglass-half',NULL,'4',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:43','quality_index.html'),(802918652050411520,'802852358580080640','数据源管理','3','1','non',NULL,'1',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:11','data_sources_index.html'),(802918760057933824,'802852358580080640','ETL任务','3','1','non',NULL,'2',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:14','etl_task_index.html'),(802919044364636160,'802852358580080640','多源ETL任务','3','1','non',NULL,'3',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:17','etl_task_more_sources_index.html'),(802919157430489088,'802852358580080640','SQL任务','3','1','non',NULL,'4',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:20','sql_task_index.html'),(802930870510948352,'802852358580080640','Drools任务','3','1','non',NULL,'5',NULL,'2021-01-23 10:34:34','2021-01-29 22:31:30','etl_task_drools_index.html'),(802931116691427328,'802852358580080640','SSH任务','3','1','non',NULL,'6',NULL,'2021-01-23 10:34:34','2021-05-05 05:31:28','etl_task_ssh_index.html'),(802931308593418240,'802852358580080640','调度ETL','3','1','non',NULL,'8',NULL,'2021-01-23 10:34:34','2021-05-05 05:31:34','dispatch_task_index.html'),(802931697527033856,'802848818109353984','下载管理','2','1','fa fa-download',NULL,'3',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:39','download_index.html'),(802932157390524416,'802848818109353984','指标查询','2','1','fa fa-columns',NULL,'5',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:46','quota_index.html'),(802932355596554240,'802848818109353984','血缘分析','2','1','fa fa-asterisk',NULL,'6',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:49','consanguin_index.html'),(802932548165439488,'802848818109353984','权限管理','2','1','fa fa-cog',NULL,'7',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:52','permission_index.html'),(802932890089295872,'802848818109353984','使用技巧','2','1','fa fa-hand-o-right',NULL,'17',NULL,'2021-01-23 10:34:34','2021-02-12 19:24:01','zdh_help.index'),(802933021148712960,'802848818109353984','Cron','2','1','fa fa-glass',NULL,'9',NULL,'2021-01-23 10:34:34','2021-01-29 22:24:58','cron.html'),(802933165302747136,'802848818109353984','README','2','1','fa fa-info',NULL,'18',NULL,'2021-01-23 10:34:34','2021-02-12 19:24:11','readme.html'),(805193674668380160,'802848818109353984','设置','2','1','fa fa-user',NULL,'15',NULL,'2021-01-29 21:52:12','2021-01-30 08:39:40',''),(805357642351382528,'802848818109353984','测试2','2','1','',NULL,'',NULL,'2021-01-30 08:43:45','2021-01-30 09:26:36',''),(805369519538180096,'805193674668380160','文件服务器','3','1','fa fa-file',NULL,'1',NULL,'2021-01-30 09:30:57','2021-01-30 09:47:06','file_manager.html'),(805372907965386752,'805193674668380160','同步元数据','3','1','fa fa-circle-o',NULL,'3',NULL,'2021-01-30 09:44:24','2021-01-30 13:51:20','function:load_meta_databases()'),(805374183432261632,'805193674668380160','用户中心','3','1','','','0','1','2021-01-30 09:49:28','2021-01-30 09:49:28','user.html'),(805431924678987776,'805193674668380160','生成监控任务','3','1','fa fa-gavel',NULL,'7',NULL,'2021-01-30 13:38:55','2021-01-30 13:52:20','function:del_system_job()'),(805531084459610112,'802848818109353984','升级扩容','2','1','fa fa-cloud',NULL,'16',NULL,'2021-01-30 20:12:57','2021-02-12 19:22:22',''),(808616077255774208,'805531084459610112','部署管理','3','1','fa fa-linux',NULL,'3',NULL,'2021-02-08 08:31:36','2021-02-11 20:23:56','server_manager_index.html'),(809886572093640704,'805193674668380160','通知管理','3','1','fa fa-comments',NULL,'9',NULL,'2021-02-11 20:40:06','2021-02-11 20:40:21','notice_update_index.html'),(812083004473085952,'802848818109353984','数仓管理','2','1','fa fa-database','','16','1','2021-02-18 06:07:56','2021-02-18 06:07:56',''),(812083162921308160,'812083004473085952','数据集市','3','1','fa fa-database',NULL,'1',NULL,'2021-02-18 06:08:33','2021-05-05 05:33:45','data_ware_house_index.html'),(830752386808025088,'802848818109353984','联系作者','2','1','fa fa-pencil','','20','1','2021-04-10 18:33:23','2021-04-10 18:33:23','mail_compose.html'),(839494480422768640,'802852358580080640','发布源ETL','3','1','non','','7','1','2021-05-05 05:31:21','2021-05-05 05:31:21','etl_task_apply_index.html'),(839494660022865920,'812083004473085952','数据发布','3','1','fa fa-send','','3','1','2021-05-05 05:32:04','2021-05-05 05:32:04','data_issue_index.html'),(839494774946795520,'812083004473085952','申请明细','3','1','fa fa-truck','','3','1','2021-05-05 05:32:31','2021-05-05 05:32:31','data_apply_index.html'),(839494892357947392,'812083004473085952','数据审批','3','1','fa fa-gg','','5','1','2021-05-05 05:32:59','2021-05-05 05:32:59','data_approve_index');


drop TABLE if EXISTS user_resource_info;
create table user_resource_info(
id bigint NOT NULL AUTO_INCREMENT,
user_id varchar(100) comment '用户id',
resource_id varchar(100) comment '资源id',
`create_time` timestamp NULL DEFAULT NULL,
`update_time` timestamp NULL DEFAULT NULL,
primary key (id)
);
INSERT INTO `user_resource_info` VALUES (120,'2','802850170428461056',NULL,NULL),(121,'2','802852358580080640',NULL,NULL),(122,'2','802931697527033856',NULL,NULL),(123,'2','802876953722884096',NULL,NULL),(124,'2','802932157390524416',NULL,NULL),(125,'2','802932355596554240',NULL,NULL),(126,'2','802932548165439488',NULL,NULL),(127,'2','802932890089295872',NULL,NULL),(128,'2','802933021148712960',NULL,NULL),(129,'2','802933165302747136',NULL,NULL),(130,'2','805193674668380160',NULL,NULL),(131,'2','805374183432261632',NULL,NULL),(132,'2','805369519538180096',NULL,NULL),(133,'2','805372907965386752',NULL,NULL),(134,'2','802918652050411520',NULL,NULL),(135,'2','802918760057933824',NULL,NULL),(136,'2','802919044364636160',NULL,NULL),(137,'2','802919157430489088',NULL,NULL),(138,'2','802930870510948352',NULL,NULL),(139,'2','802931116691427328',NULL,NULL),(140,'2','802931308593418240',NULL,NULL),(356,'1','805374183432261632',NULL,NULL),(357,'1','802848818109353984',NULL,NULL),(358,'1','802850170428461056',NULL,NULL),(359,'1','802918652050411520',NULL,NULL),(360,'1','805369519538180096',NULL,NULL),(361,'1','812083162921308160',NULL,NULL),(362,'1','802852358580080640',NULL,NULL),(363,'1','802918760057933824',NULL,NULL),(364,'1','802919044364636160',NULL,NULL),(365,'1','802931697527033856',NULL,NULL),(366,'1','805372907965386752',NULL,NULL),(367,'1','808616077255774208',NULL,NULL),(368,'1','802876953722884096',NULL,NULL),(369,'1','802919157430489088',NULL,NULL),(370,'1','802930870510948352',NULL,NULL),(371,'1','802932157390524416',NULL,NULL),(372,'1','802931116691427328',NULL,NULL),(373,'1','802932355596554240',NULL,NULL),(374,'1','802931308593418240',NULL,NULL),(375,'1','802932548165439488',NULL,NULL),(376,'1','805431924678987776',NULL,NULL),(377,'1','802933021148712960',NULL,NULL),(378,'1','805193674668380160',NULL,NULL),(379,'1','805531084459610112',NULL,NULL),(380,'1','812083004473085952',NULL,NULL),(381,'1','802932890089295872',NULL,NULL),(382,'1','802933165302747136',NULL,NULL),(383,'1','830752386808025088',NULL,NULL),(384,'1','839494480422768640',NULL,NULL),(385,'1','839494660022865920',NULL,NULL),(386,'1','839494774946795520',NULL,NULL),(387,'1','839494892357947392',NULL,NULL);

-- 2021-01-32;
alter table task_log_instance add column is_disenable varchar(10) comment '是否禁用true:禁用,false:启用';

--201-02-08;
alter table zdh_ha_info add column online varchar(10) comment '是否上线1:上线,0:逻辑下线2:物理下线';

--2021-02-10;
drop TABLE if EXISTS server_task_info;
create table server_task_info(
id bigint NOT NULL AUTO_INCREMENT,
build_task varchar(200) comment '构建任务说明',
build_ip varchar(200) comment '构建服务器',
git_url varchar(500) comment 'git地址',
build_type varchar(10) comment '构建工具类型,GRADLE/MAVEN',
build_command text comment '构建命令',
remote_ip varchar(200) comment '部署服务器',
remote_path varchar(200) comment '部署路径',
create_time timestamp not NULL DEFAULT current_timestamp ,
update_time timestamp not NULL DEFAULT current_timestamp,
owner varchar(100) comment '拥有者',
build_branch varchar(200) comment '分支名',
build_username varchar(100) comment '构建用户',
build_privatekey text comment '构建服务器密钥地址',
build_path varchar(500) comment '构建地址',
primary key (id)
);

-- 2021-02-11;
drop TABLE if EXISTS server_task_instance;
create table server_task_instance(
id bigint NOT NULL AUTO_INCREMENT,
templete_id varchar(100) comment '',
build_task varchar(200) comment '构建任务说明',
build_ip varchar(200) comment '构建服务器',
git_url varchar(500) comment 'git地址',
build_type varchar(10) comment '构建工具类型,GRADLE/MAVEN',
build_command text comment '构建命令',
remote_ip varchar(200) comment '部署服务器',
remote_path varchar(200) comment '部署路径',
create_time timestamp NOT NULL DEFAULT current_timestamp ,
update_time timestamp NOT NULL DEFAULT current_timestamp,
owner varchar(100) comment '拥有者',
version_type varchar(200) comment '部署类型BRANCH/TAG',
version varchar(200) comment '版本',
build_branch varchar(200) comment '分支/标签',
status varchar(200) comment '部署状态',
build_username varchar(100) comment '构建用户',
build_privatekey text comment '构建服务器密钥地址',
build_path varchar(500) comment '构建地址',
primary key (id)
);

-- 2021-02-21;

alter table task_log_instance add column depend_level varchar(10) not null default '0' comment '判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行';


-- 2021-04-24;
alter table data_sources_info add column is_delete varchar(10) not null default '0' comment '是否删除,0:未删除,1:删除';


-- 2021-05-03;
drop TABLE if EXISTS apply_info;
create table apply_info(
id bigint NOT NULL AUTO_INCREMENT,
apply_context varchar(500) comment '',
issue_id varchar(200) comment '数据发布id',
approve_id varchar(200) comment '审批人id',
status varchar(10) comment '状态0:初始态,1:通过,2:未通过,3:撤销申请',
create_time timestamp NOT NULL DEFAULT current_timestamp ,
update_time timestamp NOT NULL DEFAULT current_timestamp,
owner varchar(100) comment '拥有者',
reason text comment '原因',
primary key (id)
);

-- 2021-05-05;
DROP TABLE IF EXISTS `etl_apply_task_info`;
CREATE TABLE `etl_apply_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_file_columns` text DEFAULT NULL COMMENT '输入数据源文件列名',
  `data_sources_table_columns` text DEFAULT NULL COMMENT '输入数据源表列名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `column_datas` text DEFAULT NULL COMMENT '输入输出自定映射关系',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `column_size` varchar(100) DEFAULT NULL COMMENT '字段个数',
  `rows_range` varchar(100) DEFAULT NULL COMMENT '行数范围',
  `error_rate` varchar(10) DEFAULT NULL COMMENT '错误率',
  `enable_quality` varchar(10) DEFAULT NULL COMMENT '是否开启质量检测',
  `duplicate_columns` varchar(200) DEFAULT NULL COMMENT '去重字段',
  `primary_columns` varchar(100) DEFAULT NULL COMMENT '不可重复字段',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
   is_delete varchar(10) not null default '0' comment '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 2021-06-11;
alter table task_group_log_instance add column schedule_source varchar(64) not null default '1' comment '调度来源,1:例行,2:手动';
alter table task_log_instance add column schedule_source varchar(64) not null default '1' comment '调度来源,1:例行,2:手动';

-- 2021-06-14
alter table etl_task_info add column repartition_num_input varchar(64) not null default '' comment '洗牌个数默认空';
alter table etl_task_info add column repartition_cols_input varchar(256) not null default '' comment '洗牌字段默认空';
alter table etl_task_info add column model_output varchar(64) not null default '' comment '写入模式默认空';
alter table etl_task_info add column partition_by_output varchar(256) not null default '' comment '分区字段默认空';
alter table etl_task_info add column merge_output varchar(256) not null default '-1' comment '合并小文件默认-1 不合并';


alter table etl_more_task_info add column model_output varchar(64) not null default '' comment '写入模式默认空';
alter table etl_more_task_info add column partition_by_output varchar(256) not null default '' comment '分区字段默认空';
alter table etl_more_task_info add column merge_output varchar(256) not null default '-1' comment '合并小文件默认-1 不合并';

alter table sql_task_info add column model_output varchar(64) not null default '' comment '写入模式默认空';
alter table sql_task_info add column partition_by_output varchar(256) not null default '' comment '分区字段默认空';
alter table sql_task_info add column merge_output varchar(256) not null default '-1' comment '合并小文件默认-1 不合并';

alter table etl_drools_task_info add column model_output varchar(64) not null default '' comment '写入模式默认空';
alter table etl_drools_task_info add column partition_by_output varchar(256) not null default '' comment '分区字段默认空';
alter table etl_drools_task_info add column merge_output varchar(256) not null default '-1' comment '合并小文件默认-1 不合并';


alter table etl_apply_task_info add column repartition_num_input varchar(64) not null default '' comment '洗牌个数默认空';
alter table etl_apply_task_info add column repartition_cols_input varchar(256) not null default '' comment '洗牌字段默认空';
alter table etl_apply_task_info add column model_output varchar(64) not null default '' comment '写入模式默认空';
alter table etl_apply_task_info add column partition_by_output varchar(256) not null default '' comment '分区字段默认空';
alter table etl_apply_task_info add column merge_output varchar(256) not null default '-1' comment '合并小文件默认-1 不合并';


-- 2021-06-26;
alter table resource_tree_info modify column text varchar(1024);

-- 2021-09-12;
CREATE TABLE `etl_task_flink_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sql_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入参数',
  `etl_sql` text COMMENT 'sql任务处理逻辑',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `host` varchar(100) DEFAULT NULL,
  `port` varchar(100) DEFAULT NULL,
  `user_name` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2021-09-18;
alter table quartz_job_info add column alarm_email varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table quartz_job_info add column alarm_sms varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table quartz_job_info add column alarm_zdh varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

alter table task_group_log_instance add column alarm_email varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_group_log_instance add column alarm_sms varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_group_log_instance add column alarm_zdh varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

alter table task_log_instance add column alarm_email varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_log_instance add column alarm_sms varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_log_instance add column alarm_zdh varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

-- 2021-09-19;
alter table quartz_job_info add column notice_error varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table quartz_job_info add column notice_finish varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table quartz_job_info add column notice_timeout varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

alter table task_group_log_instance add column notice_error varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_group_log_instance add column notice_finish varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_group_log_instance add column notice_timeout varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

alter table task_log_instance add column notice_error varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_log_instance add column notice_finish varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';
alter table task_log_instance add column notice_timeout varchar(8) not null default 'off' comment '启用邮箱告警 on:启用,off:禁用';

CREATE TABLE `notice_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `msg_type` varchar(200) DEFAULT NULL COMMENT '消息类型',
  `msg_title` varchar(500) DEFAULT NULL COMMENT '主题',
  `msg_url` varchar(500) DEFAULT NULL COMMENT '消息连接',
  `msg` text COMMENT '消息',
  `is_see` varchar(100) DEFAULT NULL COMMENT '是否查看,true/false',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


alter table zdh_download_info add column is_notice varchar(8) default 'false' comment '是否已经通知true/false';

alter table apply_info add column is_notice varchar(8) not null default 'false' comment '是否已经通知true/false';

alter table etl_task_flink_info add column checkpoint varchar(256) not null default '' comment 'checkpoint地址';

-- 2021-09-25;
-- 审批流配置表;
create table approval_config(
`id` bigint NOT NULL AUTO_INCREMENT,
code varchar(64) not null default '0' comment '审批流程code',
code_name varchar(128) not null default '' comment '审批流程名称',
`type` tinyint(1) not null default 0 comment '0 单人审批；1 多人审批。单人审批，意思是同一级审批只要有审批人审批后，其他人默认审批。多人审批，必须是同一级所以人审批，才进行下一步审批节点',
`create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
employee_id varchar(64) not null default '' comment '发布人id'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批人设置表;
create table approval_auditor(
`id` bigint NOT NULL AUTO_INCREMENT,
code varchar(64) not null default '0' comment '审批流程code',
level int not null default 0 comment '审批节点',
auditor_id varchar(64) not null default '' comment '审批人id',
auditor_group varchar(64) not null default '' comment '审批人所在组',
`create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批表;
create table approval(
`id` varchar(64) not null default '0' comment '审批事件id',
event varchar(64) not null default '0' comment '审批事件名称',
code varchar(64) not null default '0' comment '审批流程code',
level int not null default 0 comment '审批节点',
auditor_id varchar(64) not null default '' comment '审批人id',
auditor_group varchar(64) not null default '' comment '审批人所在组',
status tinyint(1) not null default '0' comment '审批状态:0 无操作 1 待审批 2 通过 3 驳回'
next_approval_id varchar(64) not null default '0' comment '下游审批事件id',
`type` tinyint(1) not null default 0 comment '0 单人审批；1 多人审批',
`create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 事件表, 用户因何事发起了审批流程;


-- 2021-10-03;
alter table account_info add column enable varchar(8) default 'false' comment '是否启用true/false';
alter table account_info add column user_group varchar(8) default '' comment '用户所在组';
alter table account_info add column roles text comment '角色列表';
alter table account_info add column signature varchar(64) comment '签名';

-- 2021-10-03 新增角色表;
CREATE TABLE `role_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL COMMENT '角色code',
  `name` varchar(500) DEFAULT NULL COMMENT '角色名',
  `enable` varchar(8) default 'false' comment '是否启用true/false';
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop TABLE if EXISTS role_resource_info;
create table role_resource_info(
id bigint NOT NULL AUTO_INCREMENT,
role_id varchar(100) comment 'role id',
resource_id varchar(100) comment '资源id',
`create_time` timestamp NULL DEFAULT NULL,
`update_time` timestamp NULL DEFAULT NULL,
primary key (id)
);


create table user_group_info(
id bigint NOT NULL AUTO_INCREMENT,
name varchar(100) comment '组名',
`enable` varchar(8) default 'false' comment '是否启用true/false',
`create_time` timestamp NULL DEFAULT NULL,
`update_time` timestamp NULL DEFAULT NULL,
primary key (id)
);

create table approval_event_info(
`id` bigint NOT NULL AUTO_INCREMENT,
code varchar(64) not null default '0' comment '审批流程code',
event_code varchar(64) not null default '' comment '事件code',
event_context varchar(128) not null default '' comment '事件说明',
`create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table resource_tree_info add column resource_type varchar(64) comment '1:目录,2:菜单,3:函数,4:接口';

alter table issue_data_info add column status varchar(64) comment '1:发布,2:未发布';

CREATE TABLE `process_flow_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flow_id` varchar(64) NOT NULL DEFAULT '0' COMMENT '流程标识id',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '事件code',
  `config_code` varchar(64) NOT NULL DEFAULT '0' COMMENT '审批流程code',
  `context` varchar(128) NOT NULL DEFAULT '' COMMENT '事件说明',
  `auditor_id` text COMMENT '审批人id,逗号分割',
  `is_show` varchar(128) NOT NULL DEFAULT '0' COMMENT '1:可见,0:不可见',
  `owner` varchar(100) not null DEFAULT '' COMMENT '拥有者',
  `pre_id` varchar(100) not null DEFAULT '' COMMENT '流程id',
  `create_time` timestamp NULL DEFAULT  null COMMENT '创建时间',
  `status` varchar(10) not null default '0' comment '流程状态,0:未审批,1:审批完成,2:不通过,3:撤销',
  `is_end` varchar(64) not null DEFAULT '0' comment '0:非最后一个节点,1:最后一个节点',
  `level` varchar(64) not null DEFAULT '0' comment '审批节点环节',
   event_id varchar(64) not null default '' comment '发起流程的具体事件唯一键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--2021-11-01;
alter table etl_task_flink_info add column server_type varchar(64) comment '服务类型:windows/linux';
alter table etl_task_flink_info add column command text comment '命令';

--2021-11-08;


--2021-11-27;
CREATE TABLE etl_task_jdbc_info (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `etl_sql` text DEFAULT NULL COMMENT 'sql任务处理逻辑',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--2021-12-05;
CREATE TABLE etl_task_datax_info (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `datax_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `datax_json` text,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--2021-12-08;
CREATE TABLE quality_rule_info (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_code` varchar(200) DEFAULT NULL COMMENT '规则code',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `rule_type` varchar(100) DEFAULT NULL COMMENT '规则类型,1:sql表达式,2:正则',
  `rule_expr` text COMMENT '规则内容',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--2021-12-12;
CREATE TABLE `quality_task_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quality_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `data_sources_table_columns` text COMMENT '输入表字段名',
  `data_sources_file_columns` text COMMENT '输入文件字段名',
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `quality_rule_config` text COMMENT '规则配置json',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

alter table quality add column etl_context varchar(256) default null comment '质量任务名称';
alter table quality add column job_context varchar(256) default null comment '调度任务名称';

-- 2021-12-19;
CREATE TABLE blood_source_info (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `input_type` varchar(200) DEFAULT null COMMENT '',
  `input_md5` varchar(100) DEFAULT NULL COMMENT '输入源唯一标识，数据源类型+数据源url 组合生成的md5',
  `input` varchar(100) DEFAULT NULL COMMENT '数据库名称+表名/远程文件路径',
  `output_type` text DEFAULT NULL COMMENT '输出数据源类型',
  `output_md5` varchar(100) DEFAULT NULL COMMENT '输入源唯一标识，数据源类型+数据源url 组合生成的md5',
  `output` varchar(100) DEFAULT NULL COMMENT '数据库名称+表名/远程文件路径',
  `version` varchar(100) DEFAULT NULL COMMENT 'version',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2021-12-24;
alter table data_sources_info add column update_time timestamp default current_timestamp() comment '更新时间';

alter table etl_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table etl_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table etl_more_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table etl_more_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table sql_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table sql_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table ssh_task_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table ssh_task_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';


alter table etl_task_flink_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table etl_task_jdbc_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table etl_task_jdbc_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table etl_task_datax_info add column update_time timestamp default current_timestamp() comment '更新时间';
alter table etl_task_datax_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

alter table etl_apply_task_info add column update_time timestamp default current_timestamp() comment '更新时间';

CREATE TABLE `alarm_sms_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT NULL COMMENT '任务说明',
  `msg` text COMMENT '信息',
  `msg_type` varchar(100) DEFAULT NULL COMMENT '信息类型，通知,营销',
  `msg_url` varchar(500) DEFAULT NULL COMMENT '短信附带连接',
  `phone` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` varchar(8) DEFAULT '0' COMMENT '状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

CREATE TABLE `user_operate_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `operate_url` varchar(500) COMMENT '操作url',
  `operate_context` varchar(500) COMMENT '操作说明',
  `operate_input` text  COMMENT '输入参数',
  `operate_output` text COMMENT '输出结果',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

-- 2022-01-03;
CREATE TABLE `etl_task_batch_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_pre_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `etl_suffix_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
  `repartition_num_input` varchar(64) NOT NULL DEFAULT '' COMMENT '洗牌个数默认空',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `status` varchar(8) not null default '0' comment '0:未执行,1:执行中,2:执行失败,3:执行成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2022-01-05;
alter table resource_tree_info add column notice_title varchar(8) not null default '' comment '提示语';

-- 2022-01-16;
CREATE TABLE `quartz_executor_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `instance_name` varchar(512) DEFAULT NULL COMMENT '调度器唯一实例名',
  `status` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `is_handle` varchar(100) DEFAULT NULL COMMENT '是否处理过,true/false',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2022-01-18;
alter table quartz_job_info add column misfire varchar(8) not null default '0' comment '恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行';

-- 2022-02-04;
alter table resource_tree_info add column event_code varchar(64) not null default '' comment '绑定事件';

CREATE TABLE `enum_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enum_code` varchar(512) DEFAULT NULL COMMENT '枚举标识',
  `enum_context` varchar(200) DEFAULT NULL COMMENT '枚举说明',
  `enum_type` varchar(100) DEFAULT NULL COMMENT '枚举类型',
  `enum_json` text comment '枚举明细',
  `owner` varchar(64) default null COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table param_info;
CREATE TABLE `param_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_name` varchar(512) DEFAULT NULL COMMENT '参数名称',
  `param_value` text COMMENT '参数名称',
  `param_context` varchar(200) DEFAULT NULL COMMENT '参数说明',
  `param_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `param_timeout` varchar(200) DEFAULT NULL COMMENT '缓存超时时间,单位秒',
  `owner` varchar(64) default null COMMENT '拥有者',
  `status` varchar(64) default null COMMENT '状态,启用:on, 关闭:off',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp  DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp  DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER USER 'zyc'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
ALTER USER 'zyc'@'127.0.0.1' IDENTIFIED WITH mysql_native_password BY '123456';

FLUSH PRIVILEGES;

alter table issue_data_info add column label_params text  comment '数据标签,多个逗号分割';

CREATE TABLE `data_tag_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_code` varchar(512) DEFAULT NULL COMMENT '标识code',
  `tag_name` varchar(200) DEFAULT NULL COMMENT '标识名称',
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
  `owner` varchar(64) default null COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `product_tag_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(512) DEFAULT NULL COMMENT '产品标识code',
  `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `owner` varchar(64) default null COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `data_tag_group_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_group_code` varchar(512) DEFAULT NULL COMMENT '标识组code',
  `tag_group_name` varchar(200) DEFAULT NULL COMMENT '标识组名称',
  `tag_codes` varchar(512) DEFAULT NULL COMMENT '标识code列表,逗号分割',
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
  `owner` varchar(64) default null COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table account_info add column tag_group_code varchar(512) not null default ''  comment '数据组标识,多个逗号分割';
alter table data_sources_info add column tag_group_code varchar(512) not null default ''  comment '数据组标识,多个逗号分割';

-- 2022-02-09;
insert into data_sources_type_info(sources_type) values('FTP');

-- 2022-02-26;
CREATE TABLE `etl_task_unstructure_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `unstructure_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `project_id` varchar(100) DEFAULT NULL,
  `data_sources_choose_file_input` varchar(100) DEFAULT null comment '输入数据源',
  `input_path` varchar(512) DEFAULT null comment '文件读取路径',
  `data_sources_choose_file_output` varchar(100) DEFAULT NULL,
  `data_sources_choose_jdbc_output` varchar(100) DEFAULT NULL,
  `output_path` varchar(512) DEFAULT null comment '文件写入路径',
  `etl_sql` text,
  `unstructure_params_output` varchar(500) DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `etl_task_unstructure_log_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `unstructure_id` varchar(100) DEFAULT null comment '非结构化任务ID',
  `unstructure_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `project_id` varchar(100) DEFAULT NULL,
  `data_sources_choose_file_input` varchar(100) DEFAULT null comment '输入数据源',
  `input_path` varchar(512) DEFAULT null comment '文件读取路径',
  `data_sources_choose_file_output` varchar(100) DEFAULT NULL,
  `data_sources_choose_jdbc_output` varchar(100) DEFAULT NULL,
  `output_path` varchar(512) DEFAULT null comment '文件写入路径',
  `etl_sql` text,
  `unstructure_params_output` varchar(500) DEFAULT NULL,
  `msg` text comment '执行日志',
  `status` varchar(64) default null COMMENT '状态,true/false',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 2022-03-05;
alter table resource_tree_info add column product_code varchar(100) not null default ''  comment '产品code';

update resource_tree_info set product_code='zdh';

alter table role_info add column product_code varchar(100) not null default ''  comment '产品code';

update role_info set product_code='zdh';

-- 2022-04-01;
alter table user_operate_log add column `time` varchar(100) not null default ''  comment '请求响应耗时';

alter table approval_event_info add column `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间'
alter table approval_event_info add column `skip_account` text  comment '可跳过审批的用户';

-- 2022-04-05;
alter table blood_source_info add column `input_json` text  comment '输入源配置';
alter table blood_source_info add column `output_json` text  comment '输出源配置';

INSERT INTO zdh.param_info
(id, param_name, param_value, param_context, param_type, param_timeout, owner, status, is_delete, create_time, update_time)
VALUES(4, 'zdh_is_pass', 'false', '系统访问控制', '1', '120', '1', 'on', '0', '2022-04-05 18:27:30', '2022-04-05 19:57:47');

-- 2022-04-09;
ALTER TABLE etl_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_more_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_more_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_task_batch_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_batch_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_task_datax_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_datax_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_task_flink_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_flink_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_task_jdbc_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_jdbc_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE etl_task_unstructure_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_unstructure_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE etl_task_unstructure_log_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_task_unstructure_log_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_apply_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE etl_apply_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_drools_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE sql_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE sql_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE ssh_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE ssh_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE apply_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE apply_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE approval_auditor_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE approval_config_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE approval_event_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE approval_event_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE blood_source_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE data_tag_group_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE data_tag_group_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE data_tag_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE data_tag_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE enum_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE enum_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE etl_task_update_logs MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE issue_data_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE jar_file_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE jar_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE notice_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE notice_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE param_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE param_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE process_flow_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE product_tag_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE product_tag_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE quality MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE quality_rule_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE quality_rule_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE quality_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE quality_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE quartz_executor_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE quartz_executor_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE quartz_job_info MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
ALTER TABLE quartz_job_info MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
ALTER TABLE quartz_job_info MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
ALTER TABLE quartz_job_info MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
ALTER TABLE quartz_job_info MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';

ALTER TABLE resource_tree_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE resource_tree_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE role_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE role_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE role_resource_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE role_resource_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE server_task_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE server_task_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE server_task_instance MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE server_task_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE task_group_log_instance MODIFY COLUMN cur_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例逻辑调度时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN run_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例开始执行时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例更新时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
ALTER TABLE task_group_log_instance MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';

ALTER TABLE task_log_instance MODIFY COLUMN cur_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例逻辑调度时间';
ALTER TABLE task_log_instance MODIFY COLUMN run_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例开始执行时间';
ALTER TABLE task_log_instance MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例更新时间';
ALTER TABLE task_log_instance MODIFY COLUMN start_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '开始时间';
ALTER TABLE task_log_instance MODIFY COLUMN end_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '结束时间';
ALTER TABLE task_log_instance MODIFY COLUMN last_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '上次调度时间';
ALTER TABLE task_log_instance MODIFY COLUMN next_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '下次调度时间';
ALTER TABLE task_log_instance MODIFY COLUMN quartz_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '实例触发时间';

ALTER TABLE user_group_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE user_group_info MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';


ALTER TABLE user_operate_log MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';
ALTER TABLE user_operate_log MODIFY COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '更新时间';

ALTER TABLE zdh_download_info MODIFY COLUMN create_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间';

ALTER TABLE zdh_logs MODIFY COLUMN log_time DATETIME DEFAULT CURRENT_TIMESTAMP NULL COMMENT '日志生成时间';

ALTER TABLE every_day_notice add COLUMN show_type varchar(16) not null default '1' COMMENT '展示类型,1:弹框,2:文字';

ALTER TABLE user_operate_log add COLUMN ip varchar(16) not null default '' COMMENT 'ip地址';

INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(962422175220895744, '937123552732123136', '申请人查询', '4', '1', 'fa fa-coffee', '', '5', '1', '2022-04-09 18:42:08', '2022-04-09 18:42:08', 'data_ware_house_apply', '5', '', '', 'zdh');


INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(962774453505232896, '904058054746574848', 'HTTP页面', '5', '1', 'fa fa-coffee', '', '7', '1', '2022-04-10 18:01:57', '2022-04-10 18:01:57', 'http_detail.html', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(962774552792797184, '904058054746574848', 'EMAIL页面', '5', '1', 'fa fa-coffee', '', '8', '1', '2022-04-10 18:02:21', '2022-04-10 18:02:21', 'email_detail.html', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963925904835219456, '802848818109353984', '智能营销', '2', '1', 'fa fa-coffee', '', '1', '1', '2022-04-13 22:17:25', '2022-04-13 22:56:59', '', '1', '高级', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963926116140060672, '963925904835219456', '标签管理', '3', '1', 'fa fa-coffee', '', '1', '1', '2022-04-13 22:18:15', '2022-04-13 22:41:37', '', '1', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963932648793706496, '963925904835219456', '智能策略', '3', '1', 'fa fa-coffee', '', '5', '1', '2022-04-13 22:44:12', '2022-04-13 22:49:01', '', '1', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963933243336298496, '963925904835219456', '智能调度', '3', '1', 'fa fa-coffee', '', '8', '1', '2022-04-13 22:46:34', '2022-04-13 22:49:09', '', '1', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963933821948923904, '963925904835219456', '智能圈人', '3', '1', 'fa fa-coffee', '', '2', '1', '2022-04-13 22:48:52', '2022-04-13 22:48:52', '', '1', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(963934828250533888, '963925904835219456', '效果分析', '3', '1', 'fa fa-coffee', '', '9', '1', '2022-04-13 22:52:52', '2022-04-13 22:53:12', '', '1', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964797665709658112, '802848818109353984', '自助服务', '2', '1', 'fa fa-coffee', '', '5', '1', '2022-04-16 08:01:29', '2022-04-16 08:14:44', 'self_service_index.html', '2', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964801146247974912, '964797665709658112', '新增页面', '3', '1', 'fa fa-coffee', '', '1', '1', '2022-04-16 08:15:18', '2022-04-16 08:15:18', 'self_service_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964801241953603584, '964797665709658112', '新增', '3', '1', 'fa fa-coffee', '', '2', '1', '2022-04-16 08:15:41', '2022-04-16 08:15:41', 'self_service_add', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964801341513797632, '964797665709658112', '更新', '3', '1', 'fa fa-coffee', '', '3', '1', '2022-04-16 08:16:05', '2022-04-16 08:16:05', 'self_service_update', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964801483776200704, '964797665709658112', '删除', '3', '1', 'fa fa-coffee', '', '4', '1', '2022-04-16 08:16:39', '2022-04-16 08:16:39', 'self_service_delete', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(964801562897551360, '964797665709658112', '查询', '3', '1', 'fa fa-coffee', '', '5', '1', '2022-04-16 08:16:58', '2022-04-16 08:16:58', 'self_service_list', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(965011294216261632, '964797665709658112', '明细', '3', '1', 'fa fa-coffee', '', '6', '1', '2022-04-16 22:10:22', '2022-04-16 22:10:22', 'self_service_detail', '4', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(965206803035983872, '964797665709658112', '执行SQL', '3', '1', 'fa fa-coffee', '', '7', '1', '2022-04-17 11:07:15', '2022-04-17 11:07:15', 'self_service_execute', '5', '', '', 'zdh');


 CREATE TABLE `self_history` (
      `id` bigint NOT NULL AUTO_INCREMENT,
      `history_context` varchar(200) DEFAULT NULL COMMENT '说明',
      `etl_sql` text COMMENT 'sql',
      `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
      `owner` varchar(500) DEFAULT NULL COMMENT '账号',
      `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(967380640318099456, '802852358580080640', '日志采集', '3', '1', 'non', '', '13', '1', '2022-04-23 11:05:18', '2022-04-23 11:05:52', 'etl_task_log_index.html', '2', '', '', 'zdh');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(967465655009808384, '926763179978002432', '数据库监控', '3', '1', 'fa fa-coffee', '', '5', '1', '2022-04-23 16:43:07', '2022-04-23 16:43:07', 'druid', '2', '', '', 'zdh');
    INSERT INTO zdh.resource_tree_info
    (id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
    VALUES(969710130591436800, '964797665709658112', '导出', '3', '1', 'fa fa-coffee', '', '8', '1', '2022-04-29 21:21:51', '2022-04-29 21:21:51', 'self_service_export', '5', '', '', 'zdh');

-- 2022-09-03;
CREATE TABLE `label_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `label_code` varchar(200) DEFAULT NULL COMMENT '标签名',
  `label_context` varchar(200) DEFAULT NULL COMMENT '说明',
  `label_type` varchar(256) DEFAULT NULL COMMENT '标签分类',
  `label_person` varchar(256) DEFAULT NULL COMMENT '联系人',
  `param_json` text DEFAULT NULL COMMENT '标签可用参数',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `crowd_rule_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_context` varchar(200) DEFAULT NULL COMMENT '人群规则说明',
  `rule_type` varchar(256) DEFAULT NULL COMMENT '人群规则类别',
  `create_person` varchar(256) DEFAULT NULL COMMENT '创建人',
  `rule_expression` text DEFAULT NULL COMMENT '规则表达式',
  `rule_expression_cn` text DEFAULT NULL COMMENT '中文规则表达式',
  `rule_json` text DEFAULT NULL COMMENT '规则信息',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE product_tag_info add COLUMN status varchar(128) not null default '0' COMMENT '0:启用,1:不可申请,2:禁用';
ALTER TABLE product_tag_info add COLUMN ak varchar(128) not null default '' COMMENT '产品ak';
ALTER TABLE product_tag_info add COLUMN sk varchar(128) not null default '' COMMENT '产品sk';



CREATE TABLE `permission_user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_account` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `is_use_email` varchar(10) DEFAULT NULL COMMENT '是否开启邮箱告警',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_use_phone` varchar(10) DEFAULT NULL COMMENT '是否开启手机告警',
  `enable` varchar(8) DEFAULT 'false' COMMENT '是否启用true/false',
  `user_group` varchar(8) DEFAULT '' COMMENT '用户所在组',
  `product_code` varchar(128) DEFAULT '' COMMENT '用户所属产品',
  `roles` text COMMENT '角色列表',
  `signature` varchar(64) DEFAULT NULL COMMENT '签名',
  `tag_group_code` varchar(512) NOT NULL DEFAULT '' COMMENT '数据组标识,多个逗号分割',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE user_group_info add COLUMN product_code varchar(128) not null default '' COMMENT '所属产品';
ALTER TABLE approval_auditor_info modify COLUMN auditor_id varchar(128) not null default '' COMMENT '用户账户';

update user_group_info set product_code = 'zdh';

update process_flow_info set auditor_id = 'zyc' where auditor_id =1;

update process_flow_info set owner = 'zyc' where owner  ='1';
update process_flow_info set owner = "admin" where owner='2';



CREATE TABLE `strategy_group_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_context` varchar(256) DEFAULT NULL COMMENT '策略组说明',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `jsmind_data` text  COMMENT '策略关系',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `crowd_file_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(256) DEFAULT NULL COMMENT '文件名',
  `file_url` varchar(2048) DEFAULT NULL COMMENT '文件地址',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

update account_info set `user_password`='DHkr/CYfCgfo8nSO2XoqPQ==';


CREATE TABLE `permission_apply_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品code',
  `apply_type` varchar(512) DEFAULT NULL COMMENT '申请类型',
  `apply_code` varchar(200) DEFAULT NULL COMMENT '申请对象标识',
  `reason` varchar(512) DEFAULT NULL COMMENT '申请原因',
  `status` varchar(8) DEFAULT '0' COMMENT '状态,0:未处理,1:处理中,2:不通过,3:通过,4:撤销',
  `flow_id` varchar(64) NOT NULL DEFAULT '0' COMMENT '流程标识id',
  `owner` varchar(64) default null COMMENT '拥有者',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE process_flow_info add COLUMN other_handle varchar(128) default '0' COMMENT '外部系统接入审批流-事件触发标识,0:未处理,1:已处理,2:处理失败';

update process_flow_info set `other_handle`='0' where `other_handle`='' or other_handle is null;

ALTER TABLE process_flow_info modify COLUMN context varchar(1024) not null default '' COMMENT '事件说明';


CREATE TABLE `filter_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `filter_code` varchar(256) DEFAULT NULL COMMENT '过滤code',
  `filter_name` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `enable` varchar(8) DEFAULT '2' COMMENT '启用状态,1:启用,2:未启用',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


alter table approval_event_info add column `call_back` text  comment '回调接口';

alter table strategy_group_info add column `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式';
alter table strategy_group_info add column misfire varchar(8) default '0' comment '恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行';
alter table strategy_group_info add column `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级';
alter table strategy_group_info add column `status` varchar(100) DEFAULT NULL COMMENT '调度任务状态,create,running,pause,finish,remove,error';

alter table strategy_group_info add column `quartz_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例触发时间';
alter table strategy_group_info add column  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间';
alter table strategy_group_info add column   `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差';

alter table strategy_group_info add column `schedule_source` varchar(64) DEFAULT '1' COMMENT '调度来源,1:例行,2:手动';
alter table strategy_group_info add column `cur_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例逻辑调度时间';
alter table strategy_group_info add column `run_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例开始执行时间';

-- zdh.strategy_group_info definition
drop table strategy_group_instance;
drop table strategy_instance;
CREATE TABLE `strategy_group_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_context` varchar(256) DEFAULT NULL COMMENT '策略组说明',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `jsmind_data` text COMMENT '策略关系',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式',
  `misfire` varchar(8) NOT NULL DEFAULT '0' COMMENT '恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `status` varchar(100) DEFAULT NULL COMMENT '调度任务状态,create,running,pause,finish,remove,error',
  `quartz_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例触发时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `cur_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例逻辑调度时间',
  `run_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例开始执行时间',
  `run_jsmind_data` text COMMENT '生成实例血源关系',
  `next_tasks` text COMMENT '下游任务组实例id',
  `pre_tasks` text COMMENT '上游任务组实例id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table strategy_instance;
CREATE TABLE `strategy_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `strategy_context` varchar(100) DEFAULT NULL COMMENT '策略说明',
  `group_id` varchar(100) DEFAULT NULL COMMENT '组id',
  `group_context` varchar(100) DEFAULT NULL COMMENT '组实例说明',
  `group_instance_id` varchar(100) DEFAULT NULL COMMENT '组实例id',
  `instance_type` varchar(100) DEFAULT NULL COMMENT '实例类型',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `jsmind_data` text COMMENT '策略关系',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `expr` varchar(100) DEFAULT NULL COMMENT 'cron表达式/自定义表达式',
  `misfire` varchar(8) NOT NULL DEFAULT '0' COMMENT '恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行',
  `priority` varchar(4) DEFAULT NULL COMMENT '任务优先级',
  `status` varchar(100) DEFAULT NULL COMMENT '调度任务状态,create,running,pause,finish,remove,error',
  `quartz_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例触发时间',
  `use_quartz_time` varchar(5) DEFAULT NULL COMMENT '是否使用quartz 调度时间',
  `time_diff` varchar(50) DEFAULT NULL COMMENT '后退时间差',
  `schedule_source` varchar(64) NOT NULL DEFAULT '1' COMMENT '调度来源,1:例行,2:手动',
  `cur_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例逻辑调度时间',
  `run_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '实例开始执行时间',
  `run_jsmind_data` text COMMENT '生成实例血源关系',
  `next_tasks` text COMMENT '下游任务实例id',
  `pre_tasks` text COMMENT '上游任务实例id',
  `is_disenable` varchar(10) DEFAULT NULL COMMENT '是否禁用true:禁用,false:启用',
  `depend_level` varchar(10) NOT NULL DEFAULT '0' COMMENT '判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



alter table label_info add column `label_expression` text  COMMENT '运算表达式,仅支持sql';
alter table label_info add column `label_engine` varchar(16)  default '' COMMENT '标签计算引擎,mysql,hive,spark,presto';

-- 标签依赖数据
CREATE TABLE `label_depend_data_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_code` varchar(256) DEFAULT NULL COMMENT '过滤code',
  `data_name` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `data_time` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 标签计算信息表
CREATE TABLE `label_compute_info` (
  `id` int NOT NULL AUTO_INCREMENT,u
  `label_code` varchar(256) DEFAULT NULL COMMENT '过滤code',
  `filter_name` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `enable` varchar(8) DEFAULT '2' COMMENT '启用状态,1:启用,2:未启用',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


alter table quartz_job_info add column update_time DATETIME default CURRENT_TIMESTAMP comment '更新时间';
alter table quartz_job_info add column is_delete varchar(16) default "0" comment '是否删除,0:未删除,1:删除';

update etl_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update zdh_nginx a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_more_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update sql_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_update_logs a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update meta_database_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update jar_file_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update ssh_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_flink_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_jdbc_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_datax_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_unstructure_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_unstructure_log_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_apply_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_drools_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update quartz_job_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update data_sources_info a ,account_info  b set a.owner = b.user_name where a.owner = cast(b.id as char) ;
update quality_rule_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update quality_task_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update etl_task_batch_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update enum_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update notice_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update zdh_download_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update data_tag_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update data_tag_group_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update product_tag_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update approval_config_info a ,account_info  b set a.employee_id = b.user_name where a.employee_id =cast(b.id as char) ;

update issue_data_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update apply_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update apply_info a ,account_info  b set a.approve_id = b.user_name where a.approve_id =cast(b.id as char) ;

update resource_tree_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;

update label_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update crowd_rule_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;
update strategy_group_info a ,account_info  b set a.owner = b.user_name where a.owner =cast(b.id as char) ;

alter table account_info add column `create_time` datetime  default CURRENT_TIMESTAMP COMMENT '创建时间';

alter table task_group_log_instance add index idx_date_job_id(etl_date, job_id);
alter table task_group_log_instance add index idx_status(status);

alter table task_log_instance add index idx_group_id(group_id);
alter table task_log_instance add index idx_status(status);
alter table task_log_instance add index idx_notice(is_notice);

ALTER TABLE approval_auditor_info add COLUMN product_code varchar(128) not null default '' COMMENT '所属产品';
ALTER TABLE approval_auditor_info add COLUMN auditor_context varchar(128) not null default '' COMMENT '环节说明';

drop table approval_auditor_flow_info;
CREATE TABLE `approval_auditor_flow_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flow_context` varchar(256) DEFAULT NULL COMMENT '审批流说明',
  `flow_code` varchar(64) DEFAULT NULL COMMENT '审批流code',
  `product_code` varchar(64) DEFAULT NULL COMMENT '产品代码',
  `jsmind_data` text COMMENT '审批关系',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `etl_task_log_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `log_code` varchar(256) DEFAULT NULL COMMENT '日志唯一标识code',
  `log_context` varchar(256) DEFAULT NULL COMMENT '日志说明',
  `flume_command` varchar(1024) DEFAULT NULL COMMENT 'flume启动命令',
  `flume_path` varchar(256) DEFAULT NULL COMMENT 'flume地址',
  `job_config` text COMMENT '任务配置',
  `host` varchar(100) DEFAULT null COMMENT '主机地址',
  `port` varchar(100) DEFAULT null COMMENT '主机端口',
  `user_name` varchar(500) DEFAULT null COMMENT '用户名',
  `password` varchar(100) DEFAULT null COMMENT '密码',
  `log_example` text COMMENT '日志样例',
  `log_resovle_type` varchar(256) DEFAULT NULL COMMENT '日志解析类型,1:正则解析',
  `log_resovle_expr` varchar(256) DEFAULT NULL COMMENT '日志解析表达式',
  `log_output_type` varchar(256) DEFAULT NULL COMMENT '日志输出类型,string,json',
  `log_output_config` text COMMENT '日志输出配置',
  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名,文件名,topic',
  `data_sources_output_columns` text COMMENT '输出数据字段',

  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(997677161387134976, '891283647431184384', '审批流配置', '3', 'zyc', 'fa fa-coffee', '', '2', '1', '2022-07-16 01:32:51', '2022-07-16 01:32:51', 'approval_auditor_flow_index', '2', '审批流', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1003258949274701824, '802848818109353984', '优先级队列', '2', 'zyc', 'fa fa-coffee', '', '', '1', '2022-07-31 11:12:53', '2022-07-31 11:12:53', 'zdh_queue_index', '2', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1003281904503361536, '1003258949274701824', '查询', '3', 'zyc', 'fa fa-coffee', '', '1', '1', '2022-07-31 12:44:06', '2022-07-31 12:44:06', 'zdh_queue_list', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1003299570198777856, '1003258949274701824', '新增页面', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-07-31 13:54:18', '2022-07-31 13:54:18', 'zdh_queue_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1003300888858595328, '1003258949274701824', '更新', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-07-31 13:59:32', '2022-07-31 13:59:32', 'zdh_queue_update', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007983687721029632, '997677161387134976', '审批流-查询', '4', 'zyc', 'fa fa-coffee', '', '1', '1', '2022-08-13 12:07:19', '2022-08-13 12:07:19', 'approval_auditor_flow_list', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007983949596594176, '997677161387134976', '审批流-新增页面', '4', 'zyc', 'fa fa-coffee', '', '2', '1', '2022-08-13 12:08:21', '2022-08-13 12:08:21', 'approval_auditor_flow_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007984086867775488, '997677161387134976', '审批流-新增', '4', 'zyc', 'fa fa-coffee', '', '3', '1', '2022-08-13 12:08:54', '2022-08-13 12:08:54', 'approval_auditor_flow_add', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007984170279899136, '997677161387134976', '审批流-删除', '4', 'zyc', 'fa fa-coffee', '', '4', '1', '2022-08-13 12:09:14', '2022-08-13 12:09:14', 'approval_auditor_flow_delete', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007984244334530560, '997677161387134976', '审批流-更新', '4', 'zyc', 'fa fa-coffee', '', '5', '1', '2022-08-13 12:09:31', '2022-08-13 12:09:31', 'approval_auditor_flow_update', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007984327604047872, '997677161387134976', '审批流-明细', '4', 'zyc', 'fa fa-coffee', '', '6', '1', '2022-08-13 12:09:51', '2022-08-13 12:09:51', 'approval_auditor_flow_detail', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007993626464096256, '937123552732123136', '通知下游页面', '4', 'zyc', 'fa fa-coffee', '', '6', '1', '2022-08-13 12:46:48', '2022-08-13 12:46:48', 'notice_downstream_email_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1007993710824132608, '937123552732123136', '通知下游', '4', 'zyc', 'fa fa-coffee', '', '7', '1', '2022-08-13 12:47:08', '2022-08-13 12:47:08', 'notice_downstream_email', '5', '', '', 'zdh');

INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1008151332059615232, '904053585896017920', '子任务-跳过', '6', 'zyc', 'fa fa-coffee', '', '6', '1', '2022-08-13 23:13:28', '2022-08-13 23:13:28', 'skip', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1013053374364389376, '963925904835219456', '人群文件', '3', 'zyc', 'fa fa-coffee', '', '6', '1', '2022-08-27 11:52:26', '2022-08-27 11:52:26', 'crowd_file_index', '2', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1013053483735060480, '963925904835219456', '过滤规则', '3', 'zyc', 'fa fa-coffee', '', '7', '1', '2022-08-27 11:52:52', '2022-08-27 11:52:52', 'filter_index', '2', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583013045538816, '967380640318099456', '查询', '4', 'zyc', 'fa fa-coffee', '', '1', '1', '2022-09-03 11:24:19', '2022-09-03 11:24:19', 'etl_task_log_list', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583094641528832, '967380640318099456', '明细', '4', 'zyc', 'fa fa-coffee', '', '2', '1', '2022-09-03 11:24:38', '2022-09-03 11:24:38', 'etl_task_log_detail', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583234529955840, '967380640318099456', '新增页面', '4', 'zyc', 'fa fa-coffee', '', '3', '1', '2022-09-03 11:25:12', '2022-09-03 11:25:12', 'etl_task_log_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583353778212864, '967380640318099456', '新增', '4', 'zyc', 'fa fa-coffee', '', '3', '1', '2022-09-03 11:25:40', '2022-09-03 11:25:40', 'etl_task_log_add', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583426222231552, '967380640318099456', '删除', '4', 'zyc', 'fa fa-coffee', '', '4', '1', '2022-09-03 11:25:57', '2022-09-03 11:25:57', 'etl_task_log_delete', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015583512050274304, '967380640318099456', '更新', '4', 'zyc', 'fa fa-coffee', '', '5', '1', '2022-09-03 11:26:18', '2022-09-03 11:26:18', 'etl_task_log_update', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1015585046146322432, '904058054746574848', 'FLUME页面', '5', 'zyc', 'fa fa-coffee', '', '9', '1', '2022-09-03 11:32:24', '2022-09-03 11:32:24', 'flume_detail', '3', '', '', 'zdh');

update task_log_instance set status= case when `status` in ('check_dep','wait_retry','check_dep_finish','create') or (`status`= 'dispatch' and job_type in ('JDBC','GROUP','SHELL','HDFS')) then 'killed' when `status` in ('error','finish','skip') then `status` else 'kill' end
where group_id='963565723408404480' and (`status` != 'error' and `status` != 'killed');


CREATE TABLE `user_operate_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `operate_url` varchar(500) DEFAULT NULL COMMENT '操作url',
  `operate_context` varchar(500) DEFAULT NULL COMMENT '操作说明',
  `operate_input` text COMMENT '输入参数',
  `operate_output` text COMMENT '输出结果',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `time` varchar(100) NOT NULL DEFAULT '' COMMENT '请求响应耗时',
  `ip` varchar(16) NOT NULL DEFAULT '' COMMENT 'ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `we_mock_tree_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent` varchar(100) DEFAULT NULL,
  `text` varchar(1024) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL COMMENT '层级',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `icon` varchar(200) DEFAULT NULL,
  `resource_desc` varchar(10) DEFAULT NULL COMMENT '资源说明',
  `order` varchar(200) DEFAULT NULL,
  `is_delete` varchar(10) not null DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `url` text COMMENT 'url链接',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '1:目录,2:菜单',
  `notice_title` varchar(8) NOT NULL DEFAULT '' COMMENT '提示语',
  `event_code` varchar(64) NOT NULL DEFAULT '' COMMENT '绑定事件',
  `product_code` varchar(100) NOT NULL DEFAULT '' COMMENT '产品code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO zdh.param_info
(id, param_name, param_value, param_context, param_type, param_timeout, owner, status, is_delete, create_time, update_time)
VALUES(10, 'zdh_check_impls', 'com.zyc.zdh.job.CheckDepJob', '系统调度检查实现类', '1', '-1', 'zyc', 'on', '0', '2022-10-08 20:55:56', '2022-10-08 20:55:56');


CREATE TABLE `we_mock_data_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `wemock_context` varchar(128) DEFAULT NULL,
  `req_type` varchar(1024) DEFAULT 'post' COMMENT '请求类型',
  `req_encode` varchar(128) DEFAULT 'utf-8' comment '请求编码',
  `req_timeout` varchar(128) DEFAULT '10' comment '超时时间',
  `resp_content_type` varchar(256) DEFAULT 'application/json' comment '响应content_type',
  `resp_encode` varchar(128) DEFAULT 'utf-8' comment '响应编码',
  `url` varchar(2048) DEFAULT NULL COMMENT 'url',
  `header` text  COMMENT '响应信息',
  `resolve_type` varchar(200) DEFAULT null COMMENT '解析类型,0:静态，1:动态',
  `resp_context` text COMMENT '内容',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(10) not null DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `product_code` varchar(100) NOT NULL DEFAULT '' COMMENT '产品code',
  `mock_tree_id` varchar(100) NOT NULL DEFAULT '' COMMENT 'mock tree id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1025372388994322432, '#', 'test_product', '1', 'zyc', 'fa fa-folder', '', '1', '1', '2022-09-30 11:43:48', '2022-09-30 11:43:48', '', NULL, '', '', 'test_product');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030640518721179648, '934940905796800512', '树形信息新增页面', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:37:28', '2022-10-15 00:37:28', 'wemock_jstree_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030640652699832320, '934940905796800512', '树形-查询所有节点', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:38:00', '2022-10-15 00:40:10', 'wemock_jstree_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030640747650486272, '934940905796800512', '树形-新增根节点', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:38:23', '2022-10-15 00:39:52', 'wemock_add_root_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030640849358163968, '934940905796800512', '树形-查询指定节点信息', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:38:47', '2022-10-15 00:40:21', 'wemock_jstree_get_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641377911771136, '934940905796800512', '树形-更新节点信息', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:40:53', '2022-10-15 00:40:53', 'wemock_update_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641431800188928, '934940905796800512', '树形-新增节点信息', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:41:06', '2022-10-15 00:41:06', 'wemock_add_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641488435875840, '934940905796800512', '树形-删除节点信息', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:41:19', '2022-10-15 00:41:19', 'wemock_del_node', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641579972366336, '934940905796800512', '树形-更新节点层级', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:41:41', '2022-10-15 00:41:41', 'wemock_update_parent', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641732816998400, '934940905796800512', 'mock数据-查询', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:42:18', '2022-10-15 00:42:18', 'wemock_data_list', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641800286572544, '934940905796800512', 'mock数据-查询明细', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:42:34', '2022-10-15 00:42:34', 'wemock_data_detail', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641851285114880, '934940905796800512', 'mock数据-新增', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:42:46', '2022-10-15 00:42:46', 'wemock_data_add', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641898072576000, '934940905796800512', 'mock数据-更新', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:42:57', '2022-10-15 00:42:57', 'wemock_data_update', '5', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030641985137938432, '934940905796800512', 'mock数据-新增页面', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:43:18', '2022-10-15 00:43:18', 'wemock_data_add_index', '3', '', '', 'zdh');
INSERT INTO zdh.resource_tree_info
(id, parent, `text`, `level`, owner, icon, resource_desc, `order`, is_enable, create_time, update_time, url, resource_type, notice_title, event_code, product_code)
VALUES(1030642818059603968, '934940905796800512', 'mock数据-删除', '3', 'zyc', 'fa fa-coffee', '', '', '1', '2022-10-15 00:46:36', '2022-10-15 00:46:36', 'wemock_data_delete', '3', '', '', 'zdh');


ALTER TABLE strategy_instance add COLUMN touch_type varchar(128) not null default 'database' COMMENT '推送类型,database,queue';
ALTER TABLE strategy_instance add COLUMN strategy_id varchar(128) not null default '0' COMMENT '策略id';
ALTER TABLE strategy_group_instance add COLUMN strategy_group_id varchar(128) not null default '0' COMMENT '策略组id';


CREATE TABLE `touch_config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `touch_context` varchar(128) DEFAULT NULL,
  `touch_config` text COMMENT '配置json格式',
  `touch_task` varchar(128) DEFAULT 'email' comment '触达任务类型',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(10) not null DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE label_info add COLUMN data_sources_choose_input varchar(128) not null default '0' COMMENT '数据源id';

ALTER TABLE crowd_file_info add COLUMN data_type varchar(128) not null default '' COMMENT '数据类型,phone,email,id_card,可扩展';


CREATE TABLE `etl_task_datax_auto_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `etl_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `data_sources_choose_input` varchar(100) DEFAULT NULL COMMENT '输入数据源id',
  `data_source_type_input` varchar(100) DEFAULT NULL COMMENT '输入数据源类型',
  `data_sources_table_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源表名',
  `data_sources_file_name_input` varchar(100) DEFAULT NULL COMMENT '输入数据源文件名',
  `data_sources_table_columns` text COMMENT '输入数据源表列名',
  `data_sources_params_input` varchar(500) DEFAULT NULL COMMENT '输入数据源参数',
  `data_sources_filter_input` varchar(500) DEFAULT NULL COMMENT '输入数据源过滤条件',

  `data_sources_choose_output` varchar(100) DEFAULT NULL COMMENT '输出数据源id',
  `data_source_type_output` varchar(100) DEFAULT NULL COMMENT '输出数据源类型',
  `data_sources_table_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源表名',
  `data_sources_file_name_output` varchar(100) DEFAULT NULL COMMENT '输出数据源文件名',
  `data_sources_params_output` varchar(500) DEFAULT NULL COMMENT '输出数据源参数',
  `column_datas` text COMMENT '输入输出自定映射关系',
  `data_sources_clear_output` varchar(500) DEFAULT NULL COMMENT '数据源数据源删除条件',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `company` varchar(100) DEFAULT NULL COMMENT '表所属公司',
  `section` varchar(100) DEFAULT NULL COMMENT '表所属部门',
  `service` varchar(100) DEFAULT NULL COMMENT '表所属服务',
  `update_context` varchar(100) DEFAULT NULL COMMENT '更新说明',
  `column_size` varchar(100) DEFAULT NULL COMMENT '字段个数',
  `rows_range` varchar(100) DEFAULT NULL COMMENT '行数范围',
  `error_rate` varchar(10) DEFAULT NULL COMMENT '错误率',
  `enable_quality` varchar(10) DEFAULT NULL COMMENT '是否开启质量检测',
  `duplicate_columns` varchar(200) DEFAULT NULL COMMENT '去重字段',
  `primary_columns` varchar(100) DEFAULT NULL COMMENT '不可重复字段',
  `file_type_input` varchar(10) DEFAULT NULL COMMENT '输入文件类型',
  `encoding_input` varchar(10) DEFAULT NULL COMMENT '输入文件编码',
  `sep_input` varchar(10) DEFAULT NULL COMMENT '输入分割符',
  `file_type_output` varchar(10) DEFAULT NULL COMMENT '输出文件类型',
  `encoding_output` varchar(10) DEFAULT NULL COMMENT '输出文件编码',
  `sep_output` varchar(10) DEFAULT NULL COMMENT '输出文件分割符',
  `header_input` varchar(10) DEFAULT NULL COMMENT '输入是否包含表头',
  `header_output` varchar(10) DEFAULT NULL COMMENT '输出是否包含表头',
  `repartition_cols_input` varchar(256) NOT NULL DEFAULT '' COMMENT '洗牌字段默认空',
  `model_output` varchar(64) NOT NULL DEFAULT '' COMMENT '写入模式默认空',
  `partition_by_output` varchar(256) NOT NULL DEFAULT '' COMMENT '分区字段默认空',
  `merge_output` varchar(256) NOT NULL DEFAULT '-1' COMMENT '合并小文件默认-1 不合并',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



ALTER TABLE role_resource_info add COLUMN role_code varchar(128) not null default '' COMMENT '角色code';
update role_resource_info a left join role_info b on a.role_id =b.id set a.role_code=b.code;

update permission_user_info set roles='admin' where user_account ='zyc';

alter table touch_config_info add column sign varchar(128) not null default '' comment '签名';
alter table touch_config_info add column template_code varchar(256) not null default '' comment '模板code';
alter table touch_config_info add column platform varchar(256) not null default '' comment '短信平台';

alter table product_tag_info add column product_type varchar(256) not null default '' comment '产品类型';

drop table permission_bigdata_info;
CREATE TABLE `permission_bigdata_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_account` varchar(200) DEFAULT NULL COMMENT '产品code',
  `product_code` varchar(200) DEFAULT NULL COMMENT '产品code',
  `product_type` varchar(100) DEFAULT NULL COMMENT '产品类型',
  `resource_type` varchar(100) DEFAULT NULL COMMENT '资源类型,文件,目录,表',
  `resource_manage_group` varchar(100) DEFAULT NULL COMMENT '用户组是否作为当前路径管理层',
  `permission_rule` varchar(100) DEFAULT NULL COMMENT '权限规则',
  `path` varchar(100) DEFAULT NULL COMMENT '路径',
  `user_group_code` varchar(100) DEFAULT NULL COMMENT '用户组',
  `permission_depth_level` varchar(100) DEFAULT NULL COMMENT '权限层级',
  `owner` varchar(100) DEFAULT NULL COMMENT '拥有者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table user_group_info add column group_code varchar(256) not null default '' comment '组code';
ALTER TABLE we_mock_data_info add COLUMN is_disenable varchar(128) not null default 'on' COMMENT '是否禁用on,off';

alter table task_log_instance add column schedule_id varchar(256) not null default '' comment '调度器标识';

alter table process_flow_info add column agent_user varchar(256) not null default '' comment '代理人id';

alter table label_info add column label_use_type varchar(16) not null default 'batch' comment '使用方式,batch:值查人,single:人查值';
alter table label_info add column label_data_time_effect varchar(16) not null default 'day' comment '数据时效性, day:天级,hour:小时级,second:准实时';
alter table label_info add column label_data_update_type varchar(16) not null default 'overwrite' comment '数据更新类型,overwrite:覆盖,append:追加,get_append:值追加';
alter table label_info add column label_event_time_column varchar(256) not null default '' comment '事件事件字段,默认为空';
alter table label_info add column label_data_code varchar(256) not null default '' comment '数据标识,使用场景：从kafka来的数据根据此标识找到可以加工的标签都有哪些';

CREATE TABLE `plugin_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plugin_type` varchar(200) not null DEFAULT '' COMMENT '插件类型',
  `plugin_code` varchar(200) not null DEFAULT '' COMMENT '插件code',
  `plugin_name` varchar(200) not null DEFAULT '' COMMENT '插件名称',
  `plugin_json` text COMMENT '插件配置',
  `owner` varchar(100) not null DEFAULT '' COMMENT '拥有者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE approval_auditor_info add COLUMN auditor_rule varchar(128) not null default '' COMMENT '审批规则,根据规则自动查询审批人信息,实现动态审批人效果';

update user_group_info set group_code ='bigdata1' where id='6';
update user_group_info set group_code ='bigdata2' where id='7';
update user_group_info set group_code ='bigdata3' where id='8';
update user_group_info set group_code ='bigdata5' where id='9';
update user_group_info set group_code ='g1' where id='10';

update permission_user_info a inner join user_group_info b on a.user_group = b.id set a.user_group =b.group_code ;

update approval_auditor_info a inner join user_group_info b on find_in_set(b.id, a.auditor_group)
set a.auditor_group  = replace(a.auditor_group, b.id, b.group_code);

ALTER TABLE user_group_info add COLUMN teams text  COMMENT '组下所属团队';

ALTER TABLE touch_config_info add COLUMN touch_param_codes text  COMMENT '触达参数,多个逗号分割';

alter table strategy_group_info add column group_type varchar(256) not null default 'offline' comment '实时online,离线:offline';
alter table strategy_group_info add column data_node varchar(1024) not null default '' comment '数据节点';
alter table strategy_group_instance add column group_type varchar(256) not null default 'offline' comment '实时online,离线:offline';
alter table strategy_group_instance add column data_node varchar(1024) not null default '' comment '数据节点';

alter table strategy_instance add column group_type varchar(256) not null default 'offline' comment '实时online,离线:offline';
alter table strategy_instance add column data_node varchar(1024) not null default '' comment '数据节点';

alter table label_info add column label_data_update_time varchar(256) not null default '' comment '数据更新时间,调度根据此时间决定是否加工标签数据';
alter table label_info add column status varchar(8) not null default '2' comment '1:发布,2:未发布';
alter table label_info add column label_default varchar(1024) not null default '' comment '标签默认值,json格式';

drop table risk_event_info;
CREATE TABLE `risk_event_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `event_type` varchar(200) not null DEFAULT '' COMMENT '事件类型',
  `event_code` varchar(200) not null DEFAULT '' COMMENT '事件code',
  `event_name` varchar(200) not null DEFAULT '' COMMENT '事件名称',
  `event_json` text COMMENT '事件配置',
  `owner` varchar(100) not null DEFAULT '' COMMENT '拥有者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) comment '风控事件信息' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 标签依赖数据
CREATE TABLE `label_depend_data_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_code` varchar(256) DEFAULT NULL COMMENT '过滤code',
  `data_name` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `data_time` varchar(2048) DEFAULT NULL COMMENT '过滤名称',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 标签计算信息表
CREATE TABLE `label_complete_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `label_code` varchar(256) DEFAULT NULL COMMENT '标签code',
  `task_time` varchar(2048) DEFAULT NULL COMMENT '任务时间',
  `owner` varchar(500) DEFAULT NULL COMMENT '账号',
  `enable` varchar(8) DEFAULT '2' COMMENT '启用状态,1:启用,2:未启用',
  `is_delete` varchar(16) DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) comment '标签完成信息' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


alter table filter_info add column engine_type varchar(16) not null default 'file' comment '计算引擎类型,file,redis,hive';

alter table resource_tree_info MODIFY COLUMN resource_desc text COMMENT '资源说明';

-- quartz;
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

-- 2022-01-16;
alter table QRTZ_SCHEDULER_STATE add column STATUS varchar(16) not null default 'online' comment '状态，下线offline,上线online';

-- 2022-04-01;
alter table QRTZ_SCHEDULER_STATE add column RUNNING varchar(16) not null default '' comment '正在执行中的任务数';

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





