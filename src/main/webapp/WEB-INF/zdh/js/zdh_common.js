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

function getTablePageCommon(url){

    return {
        "method": "POST",
        "url": url,
        "dataType": "json",
        "search": "true",
        "pagination": "true",
        "showRefresh": "true",
        "showToggle": "true",
        "showColumns": "true",
        "iconSize": "outline",
        "toolbar": "#exampleTableEventsToolbar",
        "icons": {
        "refresh": "glyphicon-repeat",
            "toggle": "glyphicon-list-alt",
            "columns": "glyphicon-list"
        },
        "sidePagination": "server",
        "pageNumber": 1,
        "pageSize": 10,
        "responseHandler": res => {
            // 关闭加载层
            layer.close(loadIndex);
            layer.msg(res.msg);
            return {
                "total":res.result.total,
                "rows": res.result.rows
            }
        },

        "queryParams": function (params) {
            // 此处使用了LayUi组件 是为加载层
            loadIndex = layer.load(1);
            let resRepor = {
                //服务端分页所需要的参数
                limit: params.limit,
                offset: params.offset
            };
            return resRepor;
        },
    }
}