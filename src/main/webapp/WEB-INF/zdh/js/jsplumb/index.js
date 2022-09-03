$(document).ready(function(){
    
    //设置左侧为可复制的
    $(".deviceLeft .deviceLeft_box").children().draggable({
        helper: "clone",
        scope: "zlg",
    });

    function guid2() {
        return 'xxx_xxx_yxxx_xx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    //设置右侧为拖拽存放区
    var i=0;
    $("#m1").droppable({
        scope:"zlg",
        drop:function (event , ui) {
            var left = parseInt(ui.offset.left - $(this).offset().left);
            var top = parseInt(ui.offset.top - $(this).offset().top);
            var tp=ui.draggable[0].dataset.type;
                    i++;
                    cls_str="node node"+tp+"css tasks";
                    var id = guid2();
                    $(this).append('<div class="'+cls_str+'" style="position: absolute" id="' + id + '" data-type="'+tp+'" data-id=" " >' + $(ui.helper).html() + '</div>');
                    $("#" + id).css("left", left).css("top", top);
                    jsPlumb.addEndpoint(id, { anchors: "Top" }, hollowCircle);
                    jsPlumb.addEndpoint(id, { anchors: "Bottom" }, hollowCircle);
                    jsPlumb.draggable(id);
                    jsPlumb.makeTarget(id, {
                        anchor: "Continuous"
                    });
                    $("#" + id).draggable({ containment: "parent",grid: [10, 10] });
                    doubleclick("#" + id,tp);
        }
    });

    //基本连接线样式
    var connectorPaintStyle = {
        lineWidth: 2,
        strokeStyle: "#61b8d0",
    };

    // 鼠标悬浮在连接线上的样式
    var connectorHoverStyle = {
        lineWidth: 2,
        strokeStyle: "green",
    };

    //端点的颜色样式
    var paintStyle = {
        fillStyle: "#ccc",
        radius: 10,
        lineWidth:6 ,
    };

    // 鼠标悬浮在端点上的样式
    var hoverPaintStyle = {
        fillStyle: "#aaa",
    };

    //设置连接端点和连接线
    var hollowCircle = {
        endpoint: ["Dot", { radius: 5 }],  //端点的形状
        connectorStyle: connectorPaintStyle,
        connectorHoverStyle: connectorHoverStyle,
        paintStyle: paintStyle,
        hoverPaintStyle: hoverPaintStyle ,
        isSource: true,    //是否可以拖动（作为连线起点）
        connector: ["StateMachine", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true }],  //连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
        isTarget: true,    //是否可以放置（连线终点）
        maxConnections: -1,    // 设置连接点最多可以连接几条线
        connectorOverlays:[
            [ "Arrow", { width:10, length:20, location:1, id:"arrow" } ],
            ["Custom", {
                create:function(component) {
                    return $('<span style="background:#fff;position:relative;z-index:999;cursor:pointer;"></span>');
                },
                location:0.5,
                id:"customOverlay",
            }],
        ],
    };

    //鼠标进入增加一个删除的小图标
    $("#m1").on("mouseenter", ".node", function () {
        $(this).append('<img src="img/jsplumb/close2.png"  style="position:absolute; right: 0; bottom: 0;" />');
        var widthnum = $(this).css("width").substr(0,5);
        $("img").css("right", 0).css("top", -10);
    });
    //鼠标离开小图标消失
    $("#m1").on("mouseleave", ".node", function () {
        $("img").remove();
    });
    //节点小图标的单击事件
    $("#m1").on("click", "img",function () {
        var div=$(this).parent();
        layer.confirm('确定要删除此节点吗', {
            btn: ['确定','取消'] //按钮
        }, function(index){
            jsPlumb.removeAllEndpoints(div.attr("id"));
            div.remove();
            layer.close(layer.index);
        }, function(){

        });
    });

    //连接线中的文字双击事件
    $("#deviceRight").on("click", "._jsPlumb_overlay", function () {
        var that=$(this);
        that.removeClass('_jsPlumb_overlay');
        var text = that.text();
        that.html("");
        that.append('<input type="text" id="myDropDown" value="' + text + '" />');
        $('#myDropDown').blur(function () {
            that.html($("#myDropDown").val());
            that.addClass('_jsPlumb_overlay')
        });
        return false
    });

    //连接线的双击事件
    jsPlumb.bind("dblclick", function (conn, originalEvent) {

        layer.confirm('确定删除此连线吗', {
            btn: ['确定','取消'] //按钮
        }, function(index){
            jsPlumb.detach(conn);
            layer.close(layer.index);
        }, function(){

        });
    });

    // 当连线建立前
    jsPlumb.bind('beforeDrop', function (info) {
        if(info.sourceId==info.targetId){//判断当开始和终点为一个节点时，不连线。
            return false
        }
        console.info("链接自动建立");
        return true // 链接会自动建立
    })

});

function get_color_by_status(is_disenable){
    //create,dispatch,check_dep,wait_retry,finish,error,etl,kill,killed
    if(is_disenable=='true'){
        return "LightGrey"
    }
    return ""
}

//双击节点内容区域时的事件
function doubleclick(id,tp) {
    switch (tp) {
        case "tasks"://服务器
            doubleclick_tasks(id);
            break;
        case "shell":
            doubleclick_shell(id);
            break;
        case "group":
            doubleclick_group(id);
            break;
        case "jdbc":
            doubleclick_jdbc(id);
            break;
        case "hdfs":
            doubleclick_hdfs(id);
            break;
        case "http":
            doubleclick_http(id);
            break;
        case "email":
            doubleclick_email(id);
            break;
        case "flume":
            doubleclick_flume(id);
            break;
    }
}

function doubleclick_tasks(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_task_id=div.attr("etl_task_id");
        var url=server_context+'/job_detail.html';
        if( div.attr("etl_task_id") == "" || div.attr("etl_task_id") == undefined ){
            url=url+"?etl_task_id=-1"
        }else{
            var more_task=div.attr("more_task");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            url=url+"?etl_task_id="+etl_task_id+"&more_task="+more_task+"&depend_level="+depend_level +"&time_out="+time_out
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("etl_task_id",etl_task_info.etl_task_id);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("more_task",etl_task_info.more_task);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_shell(id) {
    $(id).dblclick(function () {
        var etl_context = $(this).text();
        var div = $(this);
        var command=div.attr("command");
        var url=server_context+'/shell_detail.html';
        if( command == "" || command == undefined ){
            url=url+"?command=-1"
        }else{
            var is_script=div.attr("is_script");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            $("#shell_text").val(command);
            url=url+"?command="+command+"&is_script="+is_script+"&etl_context="+etl_context+"&depend_level="+depend_level +"&time_out="+time_out
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("command",etl_task_info.command);
                div.attr("is_script",etl_task_info.is_script);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_group(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_task_id=div.attr("etl_task_id");
        var url=server_context+'/group_detail.html';
        if( div.attr("etl_task_id") == "" || div.attr("etl_task_id") == undefined ){
            url=url+"?etl_task_id=-1"
        }else{
            var more_task=div.attr("more_task");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            url=url+"?etl_task_id="+etl_task_id+"&depend_level="+depend_level +"&time_out="+time_out
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("etl_task_id",etl_task_info.etl_task_id);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_jdbc(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_context=div.attr("etl_context");
        //alert(etl_context)
        var url=server_context+'/jdbc_detail.html';
        if( div.attr("etl_context") == "" || div.attr("etl_context") == undefined ){
            url=url+"?etl_context=-1"
        }else{
            var jdbc_url=div.attr("url");
            var driver=div.attr("driver");
            var username=div.attr("username");
            var password=div.attr("password");
            var jdbc_sql=div.attr("jdbc_sql");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            var data_sources_choose_input = div.attr("data_sources_choose_input");
            $("#jdbc_url_text").val(jdbc_url);
            $("#jdbc_sql_text").val(jdbc_sql);
            url=url+"?etl_context="+etl_context+"&driver="+driver+"&username="+username+"&password="+password+"&depend_level="+depend_level +"&time_out="+time_out
                +"&data_sources_choose_input="+data_sources_choose_input
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                //alert(etl_task_info.jdbc_sql)
                div.attr("url",etl_task_info.url);
                div.attr("driver",etl_task_info.driver);
                div.attr("username",etl_task_info.username);
                div.attr("password",etl_task_info.password);
                div.attr("jdbc_sql",etl_task_info.jdbc_sql);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                div.attr("data_sources_choose_input",etl_task_info.data_sources_choose_input);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_hdfs(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_context=div.attr("etl_context");
        //alert(etl_context)
        var url=server_context+'/hdfs_detail.html';
        if( div.attr("etl_context") == "" || div.attr("etl_context") == undefined ){
            url=url+"?etl_context=-1"
        }else{
            var hdfs_url=div.attr("url");
            var url_type=div.attr("url_type");
            var username=div.attr("username");
            var password=div.attr("password");
            var hdfs_path=div.attr("hdfs_path");
            var depend_level = div.attr("depend_level");
            var hdfs_mode=div.attr("hdfs_mode");
            var time_out = div.attr("time_out");
            $("#hdfs_url_text").val(hdfs_url);
            $("#hdfs_path_text").val(hdfs_path);
            url=url+"?etl_context="+etl_context+"&url_type="+url_type+"&username="+username+"&password="+password+"&depend_level="+depend_level +"&time_out="+time_out
                +"$hdfs_mode="+hdfs_mode
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                //alert(etl_task_info.jdbc_sql)
                div.attr("url",etl_task_info.url);
                div.attr("url_type",etl_task_info.url_type);
                div.attr("username",etl_task_info.username);
                div.attr("password",etl_task_info.password);
                div.attr("hdfs_path",etl_task_info.hdfs_path);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("hdfs_mode",etl_task_info.hdfs_mode);
                div.attr("time_out",etl_task_info.time_out);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_http(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_context=div.attr("etl_context");
        var url=server_context+'/http_detail.html';
        if( div.attr("etl_context") == "" || div.attr("etl_context") == undefined ){
            url=url+"?etl_context=-1"
        }else{
            var is_disenable = div.attr("is_disenable");
            var http_url=div.attr("url");
            var url_type=div.attr("url_type");
            var depend_level = div.attr("depend_level");
            var params=div.attr("params");
            var header=div.attr("header");
            var cookie=div.attr("cookie");
            var time_out = div.attr("time_out");
            $("#http_params").val(params);
            $("#http_header").val(header);
            $("#http_cookie").val(cookie);
            $("#http_url_text").val(http_url);
            url=url+"?etl_context="+etl_context+"&url_type="+url_type+"&depend_level="+depend_level
                +"&is_disenable="+is_disenable+"&time_out="+time_out
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("url",etl_task_info.url);
                div.attr("url_type",etl_task_info.url_type);
                div.attr("params",etl_task_info.params);
                div.attr("header",etl_task_info.header);
                div.attr("cookie",etl_task_info.cookie);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("is_disenable",etl_task_info.is_disenable);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_email(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_context=div.attr("etl_context");
        var url=server_context+'/email_detail.html';
        if( div.attr("etl_context") == "" || div.attr("etl_context") == undefined ){
            url=url+"?etl_context=-1"
        }else{
            var is_disenable = div.attr("is_disenable");
            var to_emails=div.attr("to_emails");
            var email_context=div.attr("email_context");
            var subject=div.attr("subject");
            var email_type=div.attr("email_type");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            $("#to_emails").val(to_emails);
            $("#email_context").val(email_context);
            url=url+"?etl_context="+etl_context+"&email_type="+email_type+"&depend_level="+depend_level
                +"&is_disenable="+is_disenable+"&time_out="+time_out+"&subject="+subject
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("to_emails",etl_task_info.to_emails);
                div.attr("email_type",etl_task_info.email_type);
                div.attr("email_context",etl_task_info.email_context);
                div.attr("subject",etl_task_info.subject);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("is_disenable",etl_task_info.is_disenable);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}

function doubleclick_flume(id) {
    $(id).dblclick(function () {
        var text = $(this).text();
        var div = $(this);
        var etl_task_id=div.attr("etl_task_id");
        var url=server_context+'/flume_detail.html';
        if( div.attr("etl_task_id") == "" || div.attr("etl_task_id") == undefined ){
            url=url+"?etl_task_id=-1"
        }else{
            var more_task=div.attr("more_task");
            var depend_level = div.attr("depend_level");
            var time_out = div.attr("time_out");
            url=url+"?etl_task_id="+etl_task_id+"&depend_level="+depend_level +"&time_out="+time_out
        }
        layer.open({
            type: 2,
            area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: true,
            content: encodeURI(url),
            end: function () {
                console.info("index:doubleclick:"+$("#etl_task_text").val());
                if($("#etl_task_text").val()==""){
                    console.info("无修改-不更新");
                    return ;
                }

                var etl_task_info=JSON.parse($("#etl_task_text").val());
                div.attr("etl_task_id",etl_task_info.etl_task_id);
                div.attr("etl_context",etl_task_info.etl_context);
                div.attr("depend_level",etl_task_info.depend_level);
                div.attr("time_out",etl_task_info.time_out);
                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.html(etl_task_info.etl_context);
                div.css('background', get_color_by_status(etl_task_info.is_disenable));
            }
        });
    });
}
 

