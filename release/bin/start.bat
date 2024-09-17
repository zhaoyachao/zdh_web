@echo off
chcp 65001
set bin_path = %~dp0
cd %bin_path%../

set RUN_MODE=prod
set ZDH_RUN_MODE=%RUN_MODE%
echo 当前环境:%RUN_MODE%
java -Dspring.profiles.active=%RUN_MODE% -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xss512M -jar zdh_web.jar