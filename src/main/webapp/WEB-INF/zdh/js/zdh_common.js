//配置访问根目录
var server_context='';

function is_empty(data){
    if (data == "" || typeof(data)=="undefined") {
        return true;
    }
    return false;
}

function guid3() {
    return $.ajax({
        url: server_context+"/get_id",
        async: false
    }).responseText;
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

function getTableCommon(url){

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
        "pageNumber": 1,
        "pageSize": 10,
        "responseHandler": res => {
            // 关闭加载层
            layer.msg(res.msg);
            return res.result;
        }
    }
}

function getResourceDesc(){
    var url = window.location.pathname;
    url = url.replace("/","");
    $.ajax({
        type: 'POST',
        url: server_context+"/jstree_get_desc",
        dataType: 'json',
        data: {'url': url},
        //成功返回
        success: function (data) {
            if(data.code != '200'){
                console.info(data.msg);
                return ;
            }
            layer.open({
                type: 1
                ,title: false //不显示标题栏
                ,closeBtn: true
                ,area: [$(document.body).width()*0.48+"px", "auto"]
                ,shade: 0.8
                ,id: 'notice_layer' //设定一个id，防止重复弹出
                ,btnAlign: 'c'
                ,moveType: 1 //拖拽模式，0或者1
                ,content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">功能说明<br>'+data.result+'<br></div>'
            });


        },
        //处理完成
        complete: function () {
            console.info("complete")
        },
        //报错
        error: function (data) {
            console.info("error: " + data.responseText);
        }
    });
}