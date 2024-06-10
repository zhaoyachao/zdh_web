set ff=unix
bin_path=$(cd `dirname $0`; pwd)
cd "$bin_path/.."
pt=`pwd`
APP_NAME=${pt}"/zdh_web.jar"
RUN_MODE=prod
export ZDH_RUN_MODE=$RUN_MODE
nohup java -Dspring.profiles.active=$RUN_MODE -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xms512M -jar "$APP_NAME"  >> web.log  &