//配置访问根目录
var server_context='';

function is_empty(data){
    if (data == "" || typeof(data)=="undefined") {
        return true;
    }
    return false;
}