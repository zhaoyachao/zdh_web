//基本连接线样式
var connectorPaintStyle = {
    lineWidth: 2,
    strokeStyle: "#4a90e2",
    joinstyle: "round",
    outlineColor: "transparent",
    outlineWidth: 4
};

// 鼠标悬浮在连接线上的样式
var connectorHoverStyle = {
    lineWidth: 2,
    strokeStyle: "green",
    joinstyle: "round"
};

//端点的颜色样式
var paintStyle = {
    fillStyle: "#ccc",
    radius: 10,
    lineWidth:6 ,
};

// 鼠标悬浮在端点上的样式
var hoverPaintStyle = {
    fillStyle: "#4a90e2",
};

//设置连接端点和连接线
var hollowCircle = {
    endpoint: ["Dot", { radius: 2 }],  //端点的形状
    connectorStyle: connectorPaintStyle,
    connectorHoverStyle: connectorHoverStyle,
    paintStyle: paintStyle,
    hoverPaintStyle: hoverPaintStyle ,
    isSource: true,    //是否可以拖动（作为连线起点）
    // connector: ["StateMachine", { stub: [40, 60], gap: 10, cornerRadius: 5, alwaysRespectStubs: true }],  //连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
    connector: ["Bezier", { curviness: 50 }],  //连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
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