bin_path=`dirname "$0"`
echo $bin_path
cd "$bin_path"
pt=`pwd`
echo $pt
nohup java -Dfile.encoding=utf-8  -Xss512M -jar "$pt/../libs/zdh_web.jar" --spring.config.location="$pt/../conf/" >> "$pt/web.log"  &