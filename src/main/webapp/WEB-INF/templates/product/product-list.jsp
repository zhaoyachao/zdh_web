<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <%@include file="../common.jsp"%>
<link rel="stylesheet" type="text/css"
	href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css"
	href="/static/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/css/style.css" />
	<link rel="stylesheet" href="/static/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<title>建材列表</title>
  </head>
  <body class="pos-r">
<div class="pos-a" style="width:200px;left:0;top:0; bottom:0; height:100%; border-right:1px solid #e5e5e5; background-color:#f5f5f5; overflow:auto;">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div style="margin-left:200px;">
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 产品管理 <span class="c-gray en">&gt;</span> 产品列表 <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="page-container">
		<div class="text-c"> 日期范围：
			<input type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'logmax\')||\'%y-%M-%d\'}' })" id="logmin" class="input-text Wdate" style="width:120px;">
			-
			<input type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'logmin\')}',maxDate:'%y-%M-%d' })" id="logmax" class="input-text Wdate" style="width:120px;">
			<input type="text" name="" id="" placeholder=" 产品名称" style="width:250px" class="input-text">
			<button name="" id="" class="btn btn-success" type="submit"><i class="Hui-iconfont">&#xe665;</i> 搜产品</button>
		</div>
		<div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"><a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a> <a class="btn btn-primary radius" onclick="product_add('添加产品','product-add.html')" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 添加产品</a></span> <span class="r">共有数据：<strong id="stro">0</strong> 条</span> </div>
		<div class="mt-20">
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<!-- <th width="40"><input name="" type="checkbox" value=""></th> -->
						<th width="40">ID</th>
						<th width="60">缩略图</th>
						<th width="100">产品名称</th>
						<th>描述</th>
						<th width="100">单价</th>
						<th width="60">发布状态</th>
						<th width="100">操作</th>
					</tr>
				</thead>
				<tbody>
					<!-- <tr class="text-c va-m">
						<td><input name="" type="checkbox" value=""></td>
						<td>001</td>
						<td><a onClick="product_show('哥本哈根橡木地板','product-show.html','10001')" href="javascript:;"><img width="60" class="product-thumb" src="temp/product/Thumb/6204.jpg"></a></td>
						<td class="text-l"><a style="text-decoration:none" onClick="product_show('哥本哈根橡木地板','product-show.html','10001')" href="javascript:;"><img title="国内品牌" src="static/h-ui.admin/images/cn.gif"> <b class="text-success">圣象</b> 哥本哈根橡木地板KS8373</a></td>
						<td class="text-l">原木的外在,实木条形结构,色泽花纹自然,写意;款式设计吸取实木地板的天然去雕饰之美,在视觉上给人带来深邃联想.多款产品适合搭配不同的风格的室内装饰;功能流露出尊贵典雅的大气韵味。</td>
						<td><span class="price">356.0</span> 元/平米</td>
						<td class="td-status"><span class="label label-success radius">已发布</span></td>
						<td class="td-manage"><a style="text-decoration:none" onClick="product_stop(this,'10001')" href="javascript:;" title="下架"><i class="Hui-iconfont">&#xe6de;</i></a> <a style="text-decoration:none" class="ml-5" onClick="product_edit('产品编辑','product-add.html','10001')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5" onClick="product_del(this,'10001')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
					</tr> -->
				</tbody>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">
var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false
	},
	data: {
		simpleData: {
			enable:true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: 0
		}
	},
	 async: {  
         enable: true,  
         url: "/product/treeData.do",  
         autoParam: ["id"]  
     },
	callback: {
		beforeClick: function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.isParent) {
				zTree.expandNode(treeNode);
				return false;
			} else {
				//treeNode.id
				refreshTable(treeNode.id);
				return true;
			}
		}
	}
};


$(document).ready(function(){
	var t = $("#treeDemo");
	t = $.fn.zTree.init(t, setting);
	var zTree = $.fn.zTree.getZTreeObj("tree");

});
var idz;
function refreshTable(id){
	$('.table-sort').dataTable().fnDestroy();
	this.idz=id;
	alert(idz);
	$('.table-sort').dataTable({
		"bSort": true,
		"aLengthMenu":[5,10,15,20],  //用户可自选每页展示数量 5条或10条
		"aaSorting" : [ 0, "desc" ],//默认第几个排序
		"bStateSave" : true,//状态保存
		"searching" : false,//禁用搜索（搜索框）
		"paging" : true,//开启表格分页
		"iDisplayLength" : 5,
		"aoColumnDefs" : [ /* {
			"mData" : null,//这是关键  
			"mRender" : function(data, type, row) {
				return "<input type=\"checkbox\">";//一个操作  
			},
			"aTargets" : [ 0 ]//第1列  
		}, */ {
			"mData":"id",
			"aTargets" : [0],
			"mRender" : function(data, type, full) {
				return data;
			},
			"orderable": true
		}, {
			"mData":"pictureUrl",
			"aTargets" : [ 1 ],
			"mRender" : function(data, type, full) {
				return data;
			},
			"orderable": false
		},{
			"mData":"productName",
			"aTargets" : [ 2 ],
			"mRender" : function(data, type, full) {
				return data;
			},
			"orderable": false
		},{
			"mData":"content",
			"aTargets" : [ 3 ],
			"mRender" : function(data, type, full) {
				return data;
			},
			"orderable": false
		},
		{
			"mData":"price",
			"aTargets" : [ 4 ],
			"mRender" : function(data, type, full) {
				return data;
			},
			"orderable": false
		},{
			"mData":"status",
			"aTargets" : [ 5 ],
			"className":"td-status",
			"mRender" : function(data, type, full) {
				return "<span class=\"label label-success radius\">已发布</span>";
			},
			"orderable": false
		},{
			"mData":"id",
			"aTargets" : [ 6 ],
			"className":"td-manage",
			"mRender" : function(data, type, full) {
				return "<a style=\"text-decoration:none\" onClick=\"product_stop(this,'10001')\" href=\"javascript:;\" title=\"下架\"><i class=\"Hui-iconfont\">&#xe6de;</i></a> <a style=\"text-decoration:none\" class=\"ml-5\" onClick=\"product_edit('产品编辑','product-add.html','10001')\" href=\"javascript:;\" title=\"编辑\"><i class=\"Hui-iconfont\">&#xe6df;</i></a> <a style=\"text-decoration:none\" class=\"ml-5\" onClick=\"product_del(this,'10001')\" href=\"javascript:;\" title=\"删除\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>";
			},
			"orderable": false
		},{
			sDefaultContent : '',
			aTargets : [ '_all' ]
		},
		 {"orderable":false,"aTargets":[1,2,3,4]}// 制定列不参与排序
		],
		"bServerSide" : true,//这个用来指明是通过服务端来取数据
		"sAjaxSource" : "/product/tableData.do", //这个是请求的地址 
		
		"fnServerData" : retrieveData,
	});
}


// 3个参数的名字可以随便命名,但必须是3个参数,少一个都不行    
function retrieveData(sSource, aoData, fnCallback, oSettings) {
 oSettings.jqXHR = $.ajax({
	url : sSource, //这个就是请求地址对应sAjaxSource    
	data : {
		"aoData" : JSON.stringify(aoData),
		"id":idz
	}, //这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数 ,分页,排序,查询等的值   
	type : 'post',
	dataType : 'json',
	scriptCharset: 'utf-8',
	/* contentType: "application/x-www-form-urlencoded; charset=utf-8", */
	async : false,
	success : function(result) {
		//alert((result.aaData)[0].content);
		$("#stro").html(result.totalNum);
		fnCallback(result); //把返回的数据传给这个方法就可以了,datatable会自动绑定数据的    
	},
	error : function(msg) {
	}
});
}
/*产品-添加*/
function product_add(title,url){
	var index = layer.open({
		type: 2,
		title: title,
		content: url
	});
	layer.full(index);
}
/*产品-查看*/
function product_show(title,url,id){
	var index = layer.open({
		type: 2,
		title: title,
		content: url
	});
	layer.full(index);
}
/*产品-审核*/
function product_shenhe(obj,id){
	layer.confirm('审核文章？', {
		btn: ['通过','不通过'], 
		shade: false
	},
	function(){
		$(obj).parents("tr").find(".td-manage").prepend('<a class="c-primary" onClick="product_start(this,id)" href="javascript:;" title="申请上线">申请上线</a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已发布</span>');
		$(obj).remove();
		layer.msg('已发布', {icon:6,time:1000});
	},
	function(){
		$(obj).parents("tr").find(".td-manage").prepend('<a class="c-primary" onClick="product_shenqing(this,id)" href="javascript:;" title="申请上线">申请上线</a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-danger radius">未通过</span>');
		$(obj).remove();
    	layer.msg('未通过', {icon:5,time:1000});
	});	
}
/*产品-下架*/
function product_stop(obj,id){
	layer.confirm('确认要下架吗？',function(index){
		$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="product_start(this,id)" href="javascript:;" title="发布"><i class="Hui-iconfont">&#xe603;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已下架</span>');
		$(obj).remove();
		layer.msg('已下架!',{icon: 5,time:1000});
	});
}

/*产品-发布*/
function product_start(obj,id){
	layer.confirm('确认要发布吗？',function(index){
		$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="product_stop(this,id)" href="javascript:;" title="下架"><i class="Hui-iconfont">&#xe6de;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已发布</span>');
		$(obj).remove();
		layer.msg('已发布!',{icon: 6,time:1000});
	});
}

/*产品-申请上线*/
function product_shenqing(obj,id){
	$(obj).parents("tr").find(".td-status").html('<span class="label label-default radius">待审核</span>');
	$(obj).parents("tr").find(".td-manage").html("");
	layer.msg('已提交申请，耐心等待审核!', {icon: 1,time:2000});
}

/*产品-编辑*/
function product_edit(title,url,id){
	var index = layer.open({
		type: 2,
		title: title,
		content: url
	});
	layer.full(index);
}

/*产品-删除*/
function product_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		$.ajax({
			type: 'POST',
			url: '',
			dataType: 'json',
			success: function(data){
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
			},
			error:function(data) {
				console.log(data.msg);
			},
		});		
	});
}
</script>
</body>
</html>
