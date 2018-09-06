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
<title>图片列表</title>
</head>
<body>
	<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
	图片管理 <span class="c-gray en">&gt;</span> 生成二维码 <a
		class="btn btn-success radius r"
		style="line-height:1.6em;margin-top:3px" href="javascript:void(0);"
		onclick="loadimg()" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="page-container">
		<div class="text-c">
			<input type="text" name="" id="imgtext" placeholder="二维码信息"
				style="width:250px" class="input-text">
			<button name="" id="" class="btn btn-success" type="submit"
				onclick="loadimg()">
				<i class="Hui-iconfont">&#xe665;</i>生成
			</button>
		</div>
		<div class="mt-20">
			<table
				class="table table-border table-bordered table-bg table-hover table-sort">
				<tbody>
					<tr class="text-c">
						<td><img alt="" src="img.do" id="imgshow"></td>
					</tr>
					<shiro:hasPermission name="1111">
						<tr class="text-c">
							<td><button name="" id="" class="btn btn-success"
									type="submit" onclick="download()">
									<i class="Hui-iconfont">&#xe665;</i>下载
								</button></td>
						</tr>
					</shiro:hasPermission>

				</tbody>
			</table>
		</div>
	</div>
	<!--请在下方写此页面业务相关的脚本-->
	<script type="text/javascript"
		src="/static/lib/My97DatePicker/4.8/WdatePicker.js"></script>
	<script type="text/javascript"
		src="/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="/static/lib/laypage/1.2/laypage.js"></script>
	<script type="text/javascript">
		var content;
		function loadimg() {
			var c = $("#imgtext").val();
			content = c;
			$("#imgshow").attr("src", "img.do?content=" + c);
		}

		function download() {
			window.location.href = "/picture/download?content=" + content;
		}
	</script>
</body>
</html>
