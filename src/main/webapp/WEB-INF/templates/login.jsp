<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	session="false"%>
<!doctype html>
<html lang="zh-CN">
<head>
<title>登录界面</title>
<link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/static/css/login/login.css">
<!--  <link rel="stylesheet" type="text/css" href="/static/js/toastr/toastr.css" > -->
<!--  jQuery文件。务必在bootstrap.min.js 之前引入 -->
<!-- <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script> -->
<script src="/static/js/jquery-1.10.2.min.js"></script>
<!--  最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/js/jquery.cookie.js"></script>
<script src="/static/js/layer/layer.js"></script>
<!--  <script src="/static/js/toastr/toastr.min.js"></script> -->
<script type="text/javascript">
	//toastr.options.positionClass = 'toast-center-center';
	$(function() {
		if($.cookie("checkRemember")=="true"){
			$("#username").val($.cookie("username"));
			$("#password").val($.cookie("password"));
			$("#rememberMe").attr("checked",true);
		}
		
		$("#btn-submit").click(function() {
			var username = $("#username").val();
			if (username == '') {
				//toastr.warning();
				layer.msg("用户名为空！");
				return;
			}
			var password = $("#password").val();
			if (password == '') {
				layer.msg("密码为空！");
				return;
			}
			var checkRemember=$("#rememberMe").prop("checked");
            if(checkRemember==true){
            	$.cookie("checkRemember","true",{ expires: 7 });
            	$.cookie("username",username,{ expires: 7 });
            	$.cookie("password",password,{ expires: 7 });
            }else{
            	$.cookie("checkRemember","false",{expire:-1});
            	$.cookie("username","",{expire:-1});
            	$.cookie("password","",{expire:-1});
            	$("#rememberMe").attr("checked",false);
            }
			$("#login").submit();
		});
	});
</script>
</head>

<body
	style="background-image:url(/static/h-ui.admin/images/admin-login-bg.jpg);background-repeat:no-repeat;background-size:100% 100%;background-attachment:fixed;">
	<!-- <link rel="stylesheet" type="text/css"  href="/static/css/bootstrap.min.css">
<script type="text/javascript" src="/static/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.min.js"></script> -->


	<div style="width: 100%; height:100%; text-align: center;">

		<div style="width: 30%;margin-left: 35%;margin-top: 10%;">
			<form id="login" method="post" action="">

				<input id="username" name="username" type="text"
					class="form-control text-width text-height" placeholder="请输入用户名" />
				<input id="password" name="password" type="password"
					class="form-control text-width text-height" placeholder="请输入密码" />
				<div class="checkbox">
					<label style="text-align: left;"> <input type="checkbox"
						id="rememberMe" name="rememberMe"> 记住密码
					</label>
				</div>
				<div class="text-width2">
					<button id="btn-submit" type="button" class="btn text-width">
						<a>登录</a>
					</button>
				</div>
			</form>
		</div>

	</div>
	<script type="text/javascript">
	  if (window != top)
          top.location.href = location.href;
	</script>
</body>
</html>
