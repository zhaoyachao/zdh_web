<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<title>[简历]赵亚超-java后台开发工程师/北京</title>
<link rel="shortcut icon" href="/static/assets/images/favicon.ico"
	type="image/x-icon">
<link rel="stylesheet" href="/static/assets/css/typo.css">
<link rel="stylesheet" href="/static/assets/css/font-awesome.min.css">
<link rel="stylesheet" href="/static/assets/css/index.css">
<script>
	function loading() {
		document.getElementsByClassName('avatar')[0].style.display = 'block';
		document.getElementsByClassName('loading')[0].style.display = 'none';
	}
</script>
</head>

<body>

	<header class="header"></header>

	<article class="container"> <section class="side">
	<!-- 个人肖像 --> <section class="me"> <section class="portrait">
	<div class="loading">
		<span></span> <span></span> <span></span> <span></span> <span></span>
	</div>
	<!-- 头像照片 --> <img class="avatar" src="/static/assets/images/avatar.jpg"
		onload="loading()"> </section>

	<h1 class="name">赵亚超</h1>
	<h4 class="info-job">java后台开发工程师 / 北京</h4>

	</section> <!-- 基本信息 --> <section class="profile info-unit">
	<h2>
		<i class="fa fa-user" aria-hidden="true"></i>基本信息
	</h2>
	<hr />
	<ul>
		<li><label>个人信息</label> <span>赵亚超 / 男 / 24岁</span></li>
		<li><label>毕业学校</label> <span>郑州大学</span></li>
		<li><label>博客</label> <span><a href="http://blog.csdn.net/zhaoyachao123">http://blog.csdn.net/zhaoyachao123</a></span></li>
	    <li><label>目前所在单位</label> <span>北京亿阳信通</span></li>
	</ul>
	</section> <!-- 联系方式 --> <section class="contact info-unit">
	<h2>
		<i class="fa fa-phone" aria-hidden="true"></i>联系方式
	</h2>
	<hr />
	<ul>
		<li><label>手机</label><a href="tel:15236479806" target="_blank">152-3647-9806</a></li>
		<li><label>邮箱</label><a href="mailto:1299898281@qq.com"
			target="_blank">1299898281@qq.com</a></li>
		
	</ul>
	</section> <!-- 技能点 --> <section class="skill info-unit">
	<h2>
		<i class="fa fa-code" aria-hidden="true"></i>技能点
	</h2>
	<hr />
	<ul>
		<li><label>HTML</label>
		<progress value="90" max="100"></progress></li>
		<li><label>CSS</label>
		<progress value="85" max="100"></progress></li>
		<li><label>JavaScript</label>
		<progress value="85" max="100"></progress></li>
		<li><label>SPRINGMVC</label>
		<progress value="90" max="100"></progress></li>
		<li><label>SPRING,SHIRO</label>
		<progress value="90" max="100"></progress></li>
		<li><label>SPRINGBOOT</label>
		<progress value="90" max="100"></progress></li>
		<li><label>MYBATIS,JPA</label>
		<progress value="90" max="100"></progress></li>
		<li><label>MYSQL,REDIS</label>
		<progress value="80" max="100"></progress></li>
		<li><label>ACTIVEMQ</label>
		<progress value="80" max="100"></progress></li>
		<li><label>SHELL相关</label>
		<progress value="70" max="100"></progress></li>
	</ul>
	</section> <!-- 技术栈 --> <!-- <div class="stack info-unit">
                    <h2><i class="fa fa-code" aria-hidden="true"></i>其他</h2>
                    <hr/>
                    <ul>
                        <li>
                            <label>前端</label>
                            <span>React、jQuery、微信小程序、Bootstrap、SASS、LESS</span>
                        </li>
                        <li>
                            <label>后端</label>
                            <span>Node.js、MySQL、MongoDB、WordPress、ThinkPHP</span>
                        </li>
                        <li>
                            <label>其他</label>
                            <span>Git、SVN、Markdown</span>
                        </li>
                    </ul>
                </div> --> </section> <section class="main"> <!-- 教育经历 -->
	<section class="edu info-unit">
	<h2>
		<i class="fa fa-graduation-cap" aria-hidden="true"></i>教育经历
	</h2>
	<hr />
	<ul>
		<li>
			<h3>
				<span>郑州大学 - 软件开发专业（本科）</span>
				<time>2012.9-2016.7</time>
			</h3>
			<p>
				专业排名
				<mark>20/240</mark>
				，期间曾获取优秀学生，三等奖学金多次
			</p>
		</li>
	</ul>
	</section> <!-- 工作经历 --> <section class="work info-unit">
	<h2>
		<i class="fa fa-shopping-bag" aria-hidden="true"></i>工作经历
	</h2>
	<hr />
	<ul>
		<li>
			<h3>
				<span>[经历1]北京亿阳信通科技有限公司－java后台开发工程师（实习）</span>
				<time>2015.9 至 2016.7</time>
			</h3>
			<ul class="info-content">
				<li>深度参与XX项目迭代XX的前端开发工作，独立承担并完成XX、XX、XXXX等功能点的开发，主要维护并修复XX、XX、XX等功能点bug若干。项目采用技术栈<mark>React+React
					Router+Node.js+SASS</mark>，实现<mark>前后端完全分离</mark>。
				</li>
				<li>配合UI和后端，根据产品需求提供H5页面嵌入到后台模板，要求<mark>移动端显示正常</mark>。
				</li>
				<li>主要参与XXXXXXX的静态页面开发工作，要求<mark>在支付宝环境下完全兼容</mark>。
				</li>
			</ul>
		</li>
		<li>
			<h3>
				<span>[经历2]XX－前端开发工程师（实习）</span>
				<time>20XX.10-20XX.7</time>
			</h3>
			<ul class="info-content">
				<li>深度参与公司主线产品「XXXX」的前端开发工作，完成帖子快捷回复、<mark>全站图片懒加载</mark>、活动banner、帖子管理（使用Yii框架）等功能。项目采用技术栈phpWind+NAMP。<a
					href="http://itsay.tech/162/" target="_blank"><i
						class="fa fa-link" aria-hidden="true"></i>博客文章</a>。
				</li>
				<li>主要参与公司产品「XXXX」的前端开发工作，实现接入微博、微信、QQ等<mark>第三方账号登录</mark>等功能。项目采用技术栈WordPress+NAMP。
				</li>
				<li>活动页面的开发（七夕活动、抽奖活动以及承接外包页面）。</li>
				<li>论坛<mark>图片增量备份</mark>（CentOS+vsftpd+crontab）<a href="#"
					target="_blank"><i class="fa fa-link" aria-hidden="true"></i>博客文章</a>。
				</li>
			</ul>
		</li>
	</ul>
	</section> <!-- 项目经验 --> <section class="project info-unit">
	<h2>
		<i class="fa fa-terminal" aria-hidden="true"></i>个人项目
	</h2>
	<hr />
	<ul>
		<li>
			<h3>
				<span>[项目1]医学科学数据管理与共享平台</span> <span class="link"><a
					href="#" target="_blank">Demo</a></span>
				<time>201X.X-201X.X</time>
			</h3>
			<ul class="info-content">
				<li>技术栈：ThinkPHP+MongoDB+Axure</li>
				<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>
					[目标]实现多类型多来源医学科学数据的提交、管理和共享<br /> <i class="fa fa-users"
					aria-hidden="true"></i> [团队]同 2 位同专业同学一起<br /> <i
					class="fa fa-bars" aria-hidden="true"></i> [贡献]完成从<mark>“调研-设计-实现-文档”</mark>等工作，主要负责系统原型、功能框架及数据提交流程、元数据及源数据的管理与共享方案的设计以及系统开发等工作<br />
					<i class="fa fa-thumbs-o-up" aria-hidden="true"></i>
					[效果]作品最终取得第三届共享杯国家级竞赛“一等奖” （2/2000）</li>
			</ul>
		</li>
		<li>
			<h3>
				<span>[项目2]肿瘤流行病数据可视化</span> <span class="link"><a href="#"
					target="_blank">Demo</a></span>
				<time>201X.X-201X.X</time>
			</h3>
			<ul class="info-content">
				<li>技术栈：HTML 5+D3.js+ECharts+MySQL</li>
				<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>
					[目标]实现常见肿瘤流行病数据多维度可视化展示、数据透视及分析<br /> <i class="fa fa-users"
					aria-hidden="true"></i> [团队]与 1 位同学<br /> <i class="fa fa-bars"
					aria-hidden="true"></i> [贡献]分析项目需求，清洗并整理相关数据(扩展第三方知识组织系统和 Google
					trends 数据)，并用 <mark>D3.js</mark> 和 <mark>ECharts</mark>
					进行图形化展示以及实现简易自动分析 功能<br /> <i class="fa fa-thumbs-o-up"
					aria-hidden="true"></i> [效果]作品取得第二届共享杯国家级竞赛“特等奖”(1/1500)</li>
			</ul>
		</li>
		<li>
			<h3>
				<span>[项目3]肿瘤流行病数据可视化</span> <span class="link"><a href="#"
					target="_blank">Demo</a></span>
				<time>201X.X-201X.X</time>
			</h3>
			<ul class="info-content">
				<li>技术栈：HTML 5+D3.js+ECharts+MySQL</li>
				<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>
					[目标]实现常见肿瘤流行病数据多维度可视化展示、数据透视及分析<br /> <i class="fa fa-users"
					aria-hidden="true"></i> [团队]与 1 位同学<br /> <i class="fa fa-bars"
					aria-hidden="true"></i> [贡献]分析项目需求，清洗并整理相关数据(扩展第三方知识组织系统和 Google
					trends 数据)，并用 <mark>D3.js</mark> 和 <mark>ECharts</mark>
					进行图形化展示以及实现简易自动分析功能<br /> <i class="fa fa-thumbs-o-up"
					aria-hidden="true"></i> [效果]作品取得第二届共享杯国家级竞赛“特等奖”(1/1500)</li>
			</ul>
		</li>
		<li>
			<h3>
				<span>[项目4]肿瘤流行病数据可视化</span> <span class="link"><a href="#"
					target="_blank">Demo</a></span>
				<time>201X.X-201X.X</time>
			</h3>
			<ul class="info-content">
				<li>技术栈：HTML 5+D3.js+ECharts+MySQL</li>
				<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>
					[目标]实现常见肿瘤流行病数据多维度可视化展示、数据透视及分析<br /> <i class="fa fa-users"
					aria-hidden="true"></i> [团队]与 1 位<br /> <i class="fa fa-bars"
					aria-hidden="true"></i> [贡献]分析项目需求，清洗并整理相关数据(扩展第三方知识组织系统和 Google
					trends 数据)，并用 <mark>D3.js</mark> 和 <mark>ECharts</mark>
					进行图形化展示以及实现简易自动分析 功能<br /> <i class="fa fa-thumbs-o-up"
					aria-hidden="true"></i> [效果]作品取得第二届共享杯国家级竞赛“特等奖”(1/1500)</li>
			</ul>
		</li>

	</ul>
	</section> <!-- 自我评价 --> <section class="work info-unit">
	<h2>
		<i class="fa fa-pencil" aria-hidden="true"></i>自我评价/期望
	</h2>
	<hr />
	<p>
		[此处如果有一些能够量化的、且比较个性的指标会有加分，比如喜爱跑步坚持夜跑200小时或者200km等]<span
			class="mark-txt">“多静多思考，反省不张扬”</span>是我给自己总结的“十字箴言”，鞭策自己做人既不能以己度人，也不以人观己，要脚踏实地做事，坚持自己的梦想和本心。
	</p>
	</section> </section> </article>



	<footer class="footer">
	<p>
		© 2017 张大侠.文档最后更新时间为
		<time>2017年12月21日</time>
		.
	</p>
	</footer>

	<!-- 侧栏 -->
	<aside>
	<ul>
		<li><a href="https://gitee.com/itsay/resume" target="_blank">源代码</a></li>
		<li><a href="http://itsay.me/" target="_blank">Blog</a></li>
	</ul>
	</aside>

	<script src="/static/assets/js/index.js"></script>
</body>
</html>
