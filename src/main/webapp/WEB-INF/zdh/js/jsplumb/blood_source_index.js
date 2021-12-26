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
            cls_str="node nodetaskscss tasks";
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
            //doubleclick("#" + id,tp);
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



    // 当连线建立前
    jsPlumb.bind('beforeDrop', function (info) {
        if(info.sourceId==info.targetId){//判断当开始和终点为一个节点时，不连线。
            return false
        }
        console.info("链接自动建立")
        return true // 链接会自动建立
    })

    //导出json
    $('.btn1').click(function(){
        var ojson={
            tasks:[],
            shell:[],
            line:[]
        }

        //服务器
        $("#m1 .tasks").each(function (idx, elem) {
            var $elem = $(elem);
            var param={
                id: $elem.attr('etl_task_id'),
                etl_task_id: $elem.attr('etl_task_id'),
                etl_context:$elem.attr('etl_context'),
                more_task:$elem.attr('more_task'),
                divId:$elem.attr('id'),
                name: $elem[0].innerText,
                positionX: parseInt($elem.css("left"), 10),
                positionY: parseInt($elem.css("top"), 10),
                type:$elem.data('type')
            }
            ojson.tasks.push(param)
        });

        //连线
        $.each(jsPlumb.getConnections(), function (idx, connection) {
            var param={
                connectionId: connection.id,
                pageSourceId: connection.sourceId,
                pageTargetId: connection.targetId
            }
            ojson.line.push(param)
        });

        //打印json
        ojson=JSON.stringify(ojson);
        console.log(ojson)

    })

})
 

