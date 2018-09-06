<%@ page language="java" import="java.util.*" pageEncoding="utf-8" session="false"%>
<!doctype html>
<html>
<head>
<title></title>
<link rel="stylesheet" href="/static/js/table/docs/assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/js/table/dist/bootstrap-table.min.css">
   	<script src="/static/js/table/docs/assets/js/jquery.min.js"></script>
    <script src="/static/js/table/docs/assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/js/table/dist/bootstrap-table.min.js"></script>
    <script src="/static/js/table/dist/locale/bootstrap-table-zh-CN.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery.form.js"></script>
	<script type="text/javascript" src="/static/js/select/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="/static/js/select/bootstrap-multiselect.css" type="text/css"/>
    <script type="text/javascript" src="/static/js/jquery.serialize-object.min.js"></script>
    <script type="text/javascript">
  //设置查询参数
	function postQueryParams(params) {
		var queryParams = $("#searchForm").serializeObject();
		queryParams.limit=params.limit;
		queryParams.offset=params.offset;
		
		return queryParams;
	}
  function queryList(){
	  window.location.href="hello.do";
  }
  function logout(){
	  window.location.href="logout.do";
  }
    </script>
  </head>
  
  <body>
  <div style="position: absolute;top: 10px;">
	    		<form id="searchForm" name="searchForm"  method="post">
	    			<label>资源名称：</label><input type="text" name="resourceName" class="txtSearch">&nbsp;
	    			<input type="button" class="btn btn-info btn-round" value="查询" onclick="queryList()">&nbsp;&nbsp;
	    			<input type="button" class="btn btn-warning btn-round" value="重置" onclick="$('#searchForm')[0].reset();">
	    			<input type="button" class="btn btn-info btn-round" value="退出" onclick="logout()">&nbsp;&nbsp; 
	    		</form>
	    	</div>
   <table id="SysResourceList" data-toggle="table" style="margin-top: 10px;"
				data-url="resource/list.do" data-pagination="true"
				data-side-pagination="server" data-cache="false" data-query-params="postQueryParams"
				data-page-list="[10, 15, 20, 30, 50,100]" data-method="post"
				data-show-refresh="true" data-show-toggle="true"
				data-show-columns="true" data-toolbar="#toolbar"
				data-click-to-select="true" data-single-select="false"
				data-striped="true" data-content-type="application/x-www-form-urlencoded"
				>
				<thead>
					<tr>
						<th data-field="" data-checkbox="true"></th>
						<th data-field="resourceName">资源名称</th>
						<th data-field="resourcePath" >资源路径</th>
						<th data-field=resourceDesc>层级</th>
						<!-- <th data-field="isEnable" data-formatter="statusFormatter" >状态</th>
						<th data-field="createTime" data-formatter="dateFormatter" >创建时间</th>
						<th data-field="operator" data-formatter="operatorFormatter">操作</th> -->
					</tr>
				</thead>
			</table>
  </body>
</html>
