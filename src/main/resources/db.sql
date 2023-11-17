
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

-- 具体表结构及数据见release/db/xxx.sql






