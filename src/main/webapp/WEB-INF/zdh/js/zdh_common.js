//配置访问根目录
var server_context='';

function is_empty(data){
    if (data == "" || typeof(data)=="undefined") {
        return true;
    }
    return false;
}

//生成uuid
function guuid() {
    return 'xxx_xxx_yxxx_xx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}