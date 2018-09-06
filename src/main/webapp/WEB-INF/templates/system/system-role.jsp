<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<title>角色管理</title>

</head>

<body>
	<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
	管理员管理 <span class="c-gray en">&gt;</span> 角色管理 <a
		class="btn btn-success radius r"
		style="line-height:1.6em;margin-top:3px"
		href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="page-container">
		<div class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l"> <a href="javascript:;" onclick="datadel()"
				class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i>
					批量删除</a> <a class="btn btn-primary radius" href="javascript:;"
				onclick="admin_role_add('添加角色','admin-role-add.html','800')"><i
					class="Hui-iconfont">&#xe600;</i> 添加角色</a>
			</span> <span class="r" id="totalnum">共有数据：<strong id="stro">0</strong> 条
			</span>
		</div>
		<div class="mt-20">
			<table
				class="table table-border table-bordered table-hover table-bg table-sort table-responsive">
				<thead>
					<tr>
						<th scope="col" colspan="6">角色管理</th>
					</tr>
					<tr class="text-c">
						<th width="40">ID</th>
						<th width="200">角色名</th>
						<th>角色类型</th>
						<th width="300">描述</th>
						<th width="70">操作</th>
					</tr>
				</thead>
				<tbody>
					<%-- <c:forEach items="${roleList}" var="roles">
	<tr class="text-c">
				<td><input type="checkbox" value="" name=""></td>
				<td>${roles.id}</td>
				<td>${roles.roleType}超级管理员</td>
				<td><a href="#">${roles.roleName}</a></td>
				<td>${roles.content}</td>
				<td class="f-14"><a title="编辑" href="javascript:;" onclick="admin_role_edit('角色编辑','admin-role-add.html','${roles.id}')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a> <a title="删除" href="javascript:;" onclick="admin_role_del(this,'${roles.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
			</tr>
	</c:forEach>--%>
				</tbody>
			</table>
		</div>
	</div>


	<!--请在下方写此页面业务相关的脚本-->
	<script type="text/javascript"
		src="/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="/static/lib/laypage/1.2/laypage.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
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
					"mData":"roleName",
					"aTargets" : [ 1 ],
					"mRender" : function(data, type, full) {
						return data;
					},
					"orderable": false
				},{
					"mData":"roleType",
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
				},{
					"mData":"id",
					"aTargets" : [ 4 ],
					"className":"f-14",
					"mRender" : function(data, type, full) {
						return "<a title=\"编辑\" href=\"javascript:;\" onclick=\"admin_role_edit('角色编辑','/system/editRole','"+data+"')\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i></a> <a title=\"删除\" href=\"javascript:;\" onclick=\"admin_role_del(this,'"+data+"')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>";
					},
					"orderable": false
				},{
					sDefaultContent : '',
					aTargets : [ '_all' ]
				},
				 {"orderable":false,"aTargets":[1,2,3,4]}// 制定列不参与排序
				],
				"bServerSide" : true,//这个用来指明是通过服务端来取数据
				"sAjaxSource" : "/system/list.do", //这个是请求的地址 
				
				"fnServerData" : retrieveData,
			});

		});

		// 3个参数的名字可以随便命名,但必须是3个参数,少一个都不行    
		function retrieveData(sSource, aoData, fnCallback, oSettings) {
			 oSettings.jqXHR = $.ajax({
				url : sSource, //这个就是请求地址对应sAjaxSource    
				data : {
					"aoData" : JSON.stringify(aoData)
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
		/*管理员-角色-添加*/
		function admin_role_add(title, url, w, h) {
			layer_show(title, url, w, h);
		}
		/*管理员-角色-编辑*/
		function admin_role_edit(title, url, id, w, h) {
			layer_show(title, url+"?id="+id, w, h);
		}
		/*管理员-角色-删除*/
		function admin_role_del(obj, id) {
			layer.confirm('角色删除须谨慎，确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/system/delRole.do',
					data:{"id":id},
					dataType : 'json',
					success : function(data) {
						$(obj).parents("tr").remove();
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
					},
					error : function(data) {
						console.log(data.msg);
					},
				});
			});
		}
	</script>
</body>
</html>
