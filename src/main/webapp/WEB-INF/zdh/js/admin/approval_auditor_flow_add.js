$(document).ready(function(){
    
    //设置左侧为可复制的
    $(".deviceLeft2 .deviceLeft_box").children().draggable({
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
        endpoint: ["Dot", { radius: 2 }],  //端点的形状
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
            //删除数据
            deleteNode(div.attr("auditor_id"));
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
    });

});

//双击节点内容区域时的事件
function doubleclick(id,tp) {
    switch (tp) {
        case "flow"://服务器
            doubleclick_flow(id);
            break;
    }
}

function doubleclick_flow(id) {
    $(id).dblclick(function () {
        var flow_code = $('#flow_code').val();
        var product_code = $('#product_code').val();
        if(is_empty(flow_code) || is_empty(product_code)){
            layer.msg("请选择审批节点和产品");
            return ;
        }
        var param = "&flow_code="+flow_code+"&product_code="+product_code;
        var text = $(this).text();
        var div = $(this);
        var auditor_id=div.attr("auditor_id");
        var url=server_context+'/approval_auditor_add_index.html';
        if( div.attr("auditor_id") == "" || div.attr("auditor_id") == undefined ){
            url=url+"?auditor_id=-1"+param
        }else{
            $("#rule_param").val(div.attr("rule_param"));
            url=url+"?auditor_id="+auditor_id+param;
        }
        layer.open({
            type: 2,
            area: ['100%', '100%'],
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
                div.attr("auditor_id",etl_task_info.id);
                div.attr("level",etl_task_info.level);
                div.attr("level_name",etl_task_info.level_name);
                div.attr("auditor_rule",etl_task_info.auditor_rule);
                div.attr("auditor_context",etl_task_info.auditor_context);
                div.attr("type","flow");

                //div.width(etl_task_info.etl_context.length*16)
                div.css("width","auto");
                div.css("height","auto");
                div.css("display","inline-block");
                div.css("*display","inline");
                div.css("*zoom","1");
                div.attr("title", etl_task_info.level_name);
                div.html(etl_task_info.auditor_context);
            }
        });
    });
}

function deleteNode(auditor_id){

    $.ajax({
        url : server_context+"/approval_auditor_delete",
        data : "ids=" + auditor_id,
        type : "post",
        async:false,
        dataType : "json",
        success : function(data) {
            if(data.code != '200'){
                console.error(data.msg);
                parent.layer.msg("执行失败");
                return ;
            }
            parent.layer.msg("执行成功");
        },
        error: function (data) {
            console.info("error: " + data.responseText);
        }

    });
}
 

