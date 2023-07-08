set ff=unix
bin_path=`dirname "$0"`
cd "$bin_path/.."
pt=`pwd`
APP_NAME=${pt}"/zdh_web.jar"
kill -15 `ps -ef |grep "APP_NAME" |grep java|awk -F " " '{print $2}'`