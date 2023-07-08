#!/bin/bash

bin_path=`dirname "$0"`
cd "$bin_path/.."
pt=`pwd`
APP_NAME=${pt}"/zdh_web.jar"

#APP_DIR=./
APP_DIR=`pwd`
echo "当前路径"
echo $APP_DIR

#使用说明，用来提示输入参数
usage() {
 echo "Usage: sh zdh_web.sh [start|stop|restart|status]"
 exit 1
}

#检查程序是否在运行
is_exist(){
 pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `

 echo "ps number is: ${pid}"
 #如果不存在返回1，存在返回0
 if [ -z "${pid}" ]; then
 return 1
 else
 return 0
 fi
}

#启动方法
start(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is already running. pid=${pid} ."
 else
 nohup java -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xss512M -jar "$APP_NAME"  >> web.log  &
 nohup java -jar $APP_DIR/$APP_NAME > $APP_DIR/log.out 2>&1 &
 #nohup java -jar $APP_DIR/$APP_NAME
 echo "${APP_NAME} start success"
 fi
}

#停止方法
stop(){
 is_exist
 if [ $? -eq "0" ]; then
 kill -9 $pid
 else
 echo "${APP_NAME} is not running"
 fi
}

#输出运行状态
status(){
 is_exist
 if [ $? -eq "0" ]; then
 echo "${APP_NAME} is running. Pid is ${pid}"
 else
 echo "${APP_NAME} is NOT running."
 fi
}

#重启
restart(){
 stop
 start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
 "start")
 start
 ;;
 "stop")
 stop
 ;;
 "status")
 status
 ;;
 "restart")
 restart
 ;;
 *)
 usage
 ;;
esac