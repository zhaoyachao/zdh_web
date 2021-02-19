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
                    cls_str="node node"+tp+"css tasks"
                    var id = guid2();
                    $(this).append('<div class="'+cls_str+'" style="position: absolute" id="' + id + '" data-type="'+tp+'" data-id=" " >' + $(ui.helper).html() + '</div>');
                    $("#" + id).css("left", left).css("top", top);
                    jsPlumb.addEndpoint(id, { anchors: "Top" }, hollowCircle);
                    jsPlumb.addEndpoint(id, { anchors: "Bottom" }, hollowCircle);
                    jsPlumb.draggable(id);
                    jsPlumb.makeTarget(id, {
                        anchor: "Continuous"
                    })
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
    }

    // 鼠标悬浮在端点上的样式
    var hoverPaintStyle = {
        fillStyle: "#aaa",
    }

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
        var div=$(this).parent()
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
        var that=$(this)
        that.removeClass('_jsPlumb_overlay')
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

    //双击节点内容区域时的事件
    function doubleclick(id,tp) {
        switch (tp) {
            case "tasks"://服务器
                doubleclick_tasks(id)
                break;
            case "shell":
                doubleclick_shell(id)
                break;
            case "group":
                doubleclick_group(id)
                break;
            case "jdbc":
                doubleclick_jdbc(id)
                break;
        }
    }

    function doubleclick_tasks(id) {
                $(id).dblclick(function () {
                    var text = $(this).text();
                    var div = $(this)
                    var etl_task_id=div.attr("etl_task_id")
                    var url='job_detail.html'
                    if( div.attr("etl_task_id") == "" || div.attr("etl_task_id") == undefined ){
                        url=url+"?etl_task_id=-1"
                    }else{
                        var more_task=div.attr("more_task")
                        url=url+"?etl_task_id="+etl_task_id+"&more_task="+more_task
                    }
                    layer.open({
                        type: 2,
                        area: ['700px', '450px'],
                        fixed: false, //不固定
                        maxmin: true,
                        content: encodeURI(url),
                        end: function () {
                            console.info("index:doubleclick:"+$("#etl_task_text").val())
                            if($("#etl_task_text").val()==""){
                                console.info("无修改-不更新")
                                return ;
                            }

                            var etl_task_info=JSON.parse($("#etl_task_text").val())
                            div.attr("etl_task_id",etl_task_info.etl_task_id);
                            div.attr("etl_context",etl_task_info.etl_context);
                            div.attr("more_task",etl_task_info.more_task);
                            //div.width(etl_task_info.etl_context.length*16)
                            div.css("width","auto")
                            div.css("display","inline-block")
                            div.css("*display","inline")
                            div.css("*zoom","1")
                            div.html(etl_task_info.etl_context);
                        }
                    });
                });
        }

    function doubleclick_shell(id) {
        $(id).dblclick(function () {
            var etl_context = $(this).text();
            var div = $(this)
            var command=div.attr("command")
            var url='shell_detail.html'
            if( command == "" || command == undefined ){
                url=url+"?command=-1"
            }else{
                var is_script=div.attr("is_script")
                url=url+"?command="+command+"&is_script="+is_script+"&etl_context="+etl_context
            }
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: encodeURI(url),
                end: function () {
                    console.info("index:doubleclick:"+$("#etl_task_text").val())
                    if($("#etl_task_text").val()==""){
                        console.info("无修改-不更新")
                        return ;
                    }

                    var etl_task_info=JSON.parse($("#etl_task_text").val())
                    div.attr("command",etl_task_info.command);
                    div.attr("is_script",etl_task_info.is_script);
                    div.attr("etl_context",etl_task_info.etl_context)
                    div.css("width","auto")
                    div.css("display","inline-block")
                    div.css("*display","inline")
                    div.css("*zoom","1")
                    div.html(etl_task_info.etl_context);
                }
            });
        });
    }

    function doubleclick_group(id) {
        $(id).dblclick(function () {
            var text = $(this).text();
            var div = $(this)
            var etl_task_id=div.attr("etl_task_id")
            var url='group_detail.html'
            if( div.attr("etl_task_id") == "" || div.attr("etl_task_id") == undefined ){
                url=url+"?etl_task_id=-1"
            }else{
                var more_task=div.attr("more_task")
                url=url+"?etl_task_id="+etl_task_id
            }
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: encodeURI(url),
                end: function () {
                    console.info("index:doubleclick:"+$("#etl_task_text").val())
                    if($("#etl_task_text").val()==""){
                        console.info("无修改-不更新")
                        return ;
                    }

                    var etl_task_info=JSON.parse($("#etl_task_text").val())
                    div.attr("etl_task_id",etl_task_info.etl_task_id);
                    div.attr("etl_context",etl_task_info.etl_context);
                    //div.width(etl_task_info.etl_context.length*16)
                    div.css("width","auto")
                    div.css("display","inline-block")
                    div.css("*display","inline")
                    div.css("*zoom","1")
                    div.html(etl_task_info.etl_context);
                }
            });
        });
    }

    function doubleclick_jdbc(id) {
        $(id).dblclick(function () {
            var text = $(this).text();
            var div = $(this)
            var etl_context=div.attr("etl_context");
            //alert(etl_context)
            var url='jdbc_detail.html';
            if( div.attr("etl_context") == "" || div.attr("etl_context") == undefined ){
                url=url+"?etl_context=-1"
            }else{
                var jdbc_url=div.attr("url")
                var driver=div.attr("driver")
                var username=div.attr("username")
                var password=div.attr("password")
                var jdbc_sql=div.attr("jdbc_sql")
                $("#jdbc_url_text").val(jdbc_url)
                $("#jdbc_sql_text").val(jdbc_sql)
                url=url+"?etl_context="+etl_context+"&driver="+driver+"&username="+username+"&password="+password
            }
            layer.open({
                type: 2,
                area: ['700px', '450px'],
                fixed: false, //不固定
                maxmin: true,
                content: encodeURI(url),
                end: function () {
                    console.info("index:doubleclick:"+$("#etl_task_text").val())
                    if($("#etl_task_text").val()==""){
                        console.info("无修改-不更新")
                        return ;
                    }

                    var etl_task_info=JSON.parse($("#etl_task_text").val())
                    //alert(etl_task_info.jdbc_sql)
                    div.attr("url",etl_task_info.url);
                    div.attr("driver",etl_task_info.driver);
                    div.attr("username",etl_task_info.username);
                    div.attr("password",etl_task_info.password);
                    div.attr("jdbc_sql",etl_task_info.jdbc_sql);
                    div.attr("etl_context",etl_task_info.etl_context);
                    //div.width(etl_task_info.etl_context.length*16)
                    div.css("width","auto")
                    div.css("display","inline-block")
                    div.css("*display","inline")
                    div.css("*zoom","1")
                    div.html(etl_task_info.etl_context);
                }
            });
        });
    }
    // 当连线建立前
    jsPlumb.bind('beforeDrop', function (info) {
        if(info.sourceId==info.targetId){//判断当开始和终点为一个节点时，不连线。
            return false
        }
        console.info("链接自动建立")
        return true // 链接会自动建立
    })

})
 

