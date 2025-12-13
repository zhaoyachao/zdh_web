<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>ZDH ${tableDesc}新增</title>
    <meta name="keywords" content="ZDH ${tableDesc}新增">
    <meta name="description" content="ZDH ${tableDesc}新增">

    <link rel="shortcut icon" href="img/favicon.ico">

    <link href="css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="css/plugins/jsTree/style.min.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css?v=4.1.0" rel="stylesheet">
    <link href="css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="css/fileinput.css" media="all" rel="stylesheet" type="text/css" />
    <script src="js/jquery.min.js?v=2.1.4"></script>
    <script src="js/fileinput.js" type="text/javascript"></script>
    <link rel="stylesheet" data-name="vs/editor/editor.main" href="js/plugins/vs/editor/editor.main.css">
    <link rel="stylesheet" href="js/plugins/vs/editor/zdh.css">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">

    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>${tableDesc}配置</h5>
                    <div class="ibox-tools">
                        <a class="collapse-link">
                            <i class="fa fa-chevron-up"></i>
                        </a>
                        <a class="dropdown-toggle" data-toggle="dropdown" href="buttons.html#">
                            <i class="fa fa-eye"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-user">
                            <li><a href="javascript:void(0);" onclick="getResourceDesc()">功能说明</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="ibox-content">

                    <form id="${controller}_form" name="${controller}_form" method="post" class="form-horizontal"
                          action="">

                        <div class="form-group">
                            <label class="col-sm-2 control-label">归属产品</label>
                            <div class="col-sm-10">
                                <select id="product_code" name="product_code"
                                        data-placeholder="归属产品...."
                                        class="chosen-select form-control m-b" >
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">归属组</label>
                            <div class="col-sm-10">
                                <select id="dim_group" name="dim_group"
                                        data-placeholder="归属组...."
                                        class="chosen-select form-control m-b" >
                                </select>
                            </div>
                        </div>

                        <#list columns as col>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">${col.remarks}</label>
                            <div class="col-sm-10">
                                <input id="${col.actualColumnName}" name="${col.actualColumnName}" type="text"
                                       placeholder="${col.remarks}" class="form-control" aria-required="true"> </span>
                            </div>
                        </div>
                        </#list>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">创建时间</label>
                            <div class="col-sm-10">
                                <input id="create_time" name="create_time" type="text" readonly
                                       placeholder="创建时间" class="form-control" aria-required="true"> </span>
                            </div>
                        </div>

                        <div class="hr-line-dashed"></div>

                        <!-- 输出数据源结束-->

                        <div class="form-group">
                            <div class="text-center">
                                <button id="save_etl_task" name="save_zdh" class="btn btn-primary"
                                        οnsubmit='return false'
                                        type="button">保存
                                </button>
                                <button id="update_etl_task" name="save_zdh" class="btn btn-primary"
                                        οnsubmit='return false'
                                        type="button">更新
                                </button>
                                <button id="reset" class="btn btn-white" type=reset>清空</button>
                            </div>
                        </div>


                    </form>

                </div>
            </div>


        </div>
    </div>
</div>


<script src="js/zdh_common.js"></script>
<script>
    var require = {
        paths: {
            'vs': 'js/plugins/vs'
        }
    };

</script>
<!-- 全局js -->
<script src="js/jquery.min.js?v=2.1.4"></script>
<script src="js/bootstrap.min.js?v=3.3.6"></script>

<!-- Bootstrap table -->
<script src="js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<!-- 自定义js -->
<script src="js/content.js?v=1.0.0"></script>

<!-- Chosen -->
<script src="js/plugins/chosen/chosen.jquery.js"></script>

<!-- layer javascript -->
<script src="js/plugins/layer/layer.min.js"></script>

<!-- jsTree plugin javascript -->
<script src="js/plugins/jsTree/jstree.min.js"></script>
<!-- iCheck -->
<script src="js/plugins/iCheck/icheck.min.js"></script>

<script src="js/admin/dim_product_common.js"></script>
<script src="js/admin/dim_group_common.js"></script>

<script>
    $(document).ready(function () {
        $('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });
    });


    (function (document, window, $) {


        (function () {

            var url = location.search; //这一条语句获取了包括问号开始到参数的最后，不包括前面的路径
            var params = url.substr(1);//去掉问号
            var pa = params.split("&");
            var s = new Object();//定义一个全局变量-存储任务ID
            for (var i = 0; i < pa.length; i++) {
                s[pa[i].split("=")[0]] = unescape(pa[i].split("=")[1]);
            }


            //s.id=-1 表示新增,否则表示更新
            if (s.id == -1) {
                console.info("新增按钮触发");
                //$('#data_sources_table_name_input').chosen();
                //$('#data_sources_table_name_output').chosen();
                $('#update_etl_task').hide();
                $('#save_etl_task').show();
            } else {

                if (s.is_copy == "true") {
                    console.info("拷贝按钮触发");
                    $('#update_etl_task').hide();
                    $('#save_etl_task').show()
                } else {
                    console.info("更新按钮触发");
                    $('#save_etl_task').hide();
                    $('#update_etl_task').show();
                }

                if (s.only_read == "true") {
                    $('#update_etl_task').hide();
                    $('#save_etl_task').hide();
                    $('#reset').hide();
                }
                buildParam()
            }

            function getMyDate(str) {
                var oDate = new Date(str),
                    oYear = oDate.getFullYear(),
                    oMonth = oDate.getMonth() + 1,
                    oDay = oDate.getDate(),
                    oHour = oDate.getHours(),
                    oMin = oDate.getMinutes(),
                    oSen = oDate.getSeconds(),
                    oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + " " + getzf(oHour) + ":" + getzf(oMin) + ":" + getzf(oSen);//最后拼接时间
                return oTime;
            };

            //补0操作
            function getzf(num) {
                if (parseInt(num) < 10) {
                    num = '0' + num;
                }
                return num;
            }


            function buildParam() {
                if (s.id != '-1') {
                    console.info("更新按钮触发--开始赋值对应参数");
                    //开始赋值

                    $.ajax({
                        url: server_context+"/${controller}_detail",
                        data: "id=" + s.id,
                        type: "post",
                        async: false,
                        dataType: "json",
                        success: function (data) {
                            console.info("success");
                            // layer.alert(JSON.stringify(data[0]))

                            //获取输入数据源id
                            //var data_sources_choose_input = data[0].data_sources_choose_input
                            //console.info("参数赋值:输入数据源id:" + data_sources_choose_input)
                            var product_code=data.result.product_code;
                            $("#product_code").val(product_code);
                            $("#product_code").trigger("chosen:updated");

                            var dim_group=data.result.dim_group;
                            $("#dim_group").val(dim_group);
                            $("#dim_group").trigger("chosen:updated");


                           <#list columns as col>
                            var ${col.actualColumnName} = data.result.${col.actualColumnName};
                            $("#${col.actualColumnName}").val(${col.actualColumnName});
                           </#list>

                            var create_time = data.result.create_time;
                            $("#create_time").val(getMyDate(create_time));

                        },
                        error: function (data) {
                            console.info("error: " + data.responseText);
                        }

                    });


                }
            }

        })();


    })(document, window, jQuery);


    $('#save_etl_task').click(function () {

        // if ($('#file_name').val() == '') {
        //     layer.msg("文件名不能为空！");
        //     return;
        // }

        var formData = new FormData($("#${controller}_form")[0]);//获取表单中的文件

        var index1 = layer.load(1, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
        });
        $.ajax({
            type: 'POST',
            url: server_context+"/${controller}_add",
            async: false,
            data:formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            //发送数据前
            beforeSend: function () {
                // 禁用按钮防止重复提交
                $("#save_etl_task").attr({disabled: "disabled"});
            },
            //成功返回
            success: function (data) {
                if(data.code != "200"){
                    layer.msg(data.msg);
                    return;
                }
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
            },
            //处理完成
            complete: function () {
                $("#save_etl_task").removeAttr('disabled');
                console.info("complete");
                layer.close(index1);
            },
            //报错
            error: function (data) {
                $("#save_etl_task").removeAttr('disabled');
                console.info("error: " + data.responseText);
            }
        });

    });

    $('#update_etl_task').click(function () {

        var url = location.search; //这一条语句获取了包括问号开始到参数的最后，不包括前面的路径
        var params = url.substr(1);//去掉问号
        var pa = params.split("&");
        var s = new Object();
        for(var i = 0; i < pa.length; i ++){
            s[pa[i].split("=")[0]] = unescape(pa[i].split("=")[1]);
        }


        $.ajax({
            type: 'POST',
            url: server_context+"/${controller}_update",
            dataType: 'json',
            data: $("#${controller}_form").serialize()+'&id='+s.id,
            //发送数据前
            beforeSend: function () {
                // 禁用按钮防止重复提交
                $("#update_dispatch_task").attr({disabled: "disabled"});
            },
            //成功返回
            success: function (data) {
                if(data.code != '200'){
                    parent.layer.msg("更新失败"+data.msg);
                    return ;
                }
                $("#update_dispatch_task").removeAttr('disabled');
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                // closeTab()
            },
            //处理完成
            complete: function () {
                $("#update_dispatch_task").removeAttr('disabled');
                console.info("complete")
            },
            //报错
            error: function (data) {
                $("#update_dispatch_task").removeAttr('disabled');
                layer.msg(data.responseText);
                console.info("error: " + data.responseText);
            }
        });

    })

</script>

</body>

</html>
