set bin_path = %~dp0
echo %bin_path%
cd %bin_path%../

java -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xss512M -jar zdh_web.jar