<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	session="false"%>
<!doctype html>
<html lang="zh-CN">
<head>
<title>404页面</title>
<link href="/static/lib/Hui-iconfont/1.0.8/iconfont.css" rel="stylesheet" type="text/css" />
<link href="/static/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="/static/h-ui.admin/css/H-ui.admin.css" rel="stylesheet" type="text/css" />

<script src="/static/js/jquery-1.10.2.min.js"></script>


<script type="text/javascript">
if (window != top)
    top.location.href = location.href;
</script>
</head>

<body>
<section class="container-fluid page-404 minWP text-c">
	<p class="error-title"><i class="Hui-iconfont va-m" style="font-size:80px">&#xe688;</i>
		<span class="va-m"> 404</span>
	</p>
	<p class="error-description">不好意思，您访问的页面不存在~</p>
	<p class="error-info">您可以：
		<a href="javascript:;" onclick="history.go(-1)" class="c-primary">&lt; 返回上一页</a>
		<span class="ml-20">|</span>
		<a href="/getIndex" class="c-primary ml-20">去首页 &gt;</a>
	</p>
</section>
</body>
</html>
