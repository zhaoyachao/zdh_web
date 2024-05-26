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
        cache: false,
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

function getBrowserType(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var browser='unknown';
    if (userAgent.indexOf("IE")!=-1) {//字符串含有IE字段，表明是IE浏览器
        browser="IE";
    }else if(userAgent.indexOf('Firefox')!=-1){//字符串含有Firefox字段，表明是火狐浏览器
        browser="Firefox";
    }else if(userAgent.indexOf('OPR')!=-1){//Opera
        browser="Opera";
    }else if(userAgent.indexOf('Chrome')!=-1){//Chrome
        browser="Chrome";
    }else if(userAgent.indexOf('Safari')!=-1){//Safari
        browser="Safari";
    }else if(userAgent.indexOf('Trident')!=-1){//IE内核
        browser='IE 11';
    }
    return browser;
}

function system_params() {

    parent.layer.open({
        type: 2,
        shade: false,
        title: false, //不显示标题
        area: ["80%", "90%"],
        content: server_context+"/system_params_index", //捕获的元素
        cancel: function (index) {
            layer.close(index);
        }
    });
}

function open_fontawesome(){
    parent.layer.open({
        type: 2,
        title: '图标库',
        shadeClose: false,
        resize: true,
        fixed: false,
        maxmin: true,
        shade: 0.1,
        area : ['45%', '60%'],
        //area: ['450px', '500px'],
        content: server_context+"/fontawesome.html", //iframe的url
        end : function () {
            console.info("弹框结束")
        }
    });
}

function open_sparksql_api(){
    window.open("https://spark.apache.org/docs/2.4.4/api/sql/index.html");
}