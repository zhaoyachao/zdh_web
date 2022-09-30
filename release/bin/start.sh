set ff=unix
bin_path=`dirname "$0"`
echo $bin_path
cd "$bin_path/.."
pt=`pwd`
echo $pt
echo "java -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xss512M -jar zdh_web.jar"
nohup java -Dfile.encoding=utf-8 -Dloader.path=libs/,conf/ -Xss512M -jar zdh_web.jar >> web.log  &