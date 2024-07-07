(function (document, window, $) {

    // Example Bootstrap Table Events
    // ------------------------------
    (function () {
        var height=400;
        var height2=400;
        if($(document.body).height()*0.8>height){
            height=$(document.body).height()*0.6;
            height2=$(document.body).height()*0.8
        }
        $('#exampleTableEvents').attr("data-height",height);
        $('#exampleTableEvents2').attr("data-height",height2);

        $('#add').click(function () {
            parent.layer.open({
                type: 2,
                title: '创建调度任务',
                shadeClose: false,
                resize: true,
                fixed: false,
                maxmin: true,
                shade: 0.1,
                area : ['45%', '60%'],
                //area: ['450px', '500px'],
                content: server_context+"/dispatch_system_task_add_index.html?id=-1", //iframe的url
                end : function () {
                    console.info("弹框结束");
                    $('#exampleTableEvents2').bootstrapTable('refresh', {
                        url: server_context+"/dispatch_system_task_list",
                        contentType: "application/json;charset=utf-8",
                        dataType: "json"
                    });
                }
            });
        });

        function update_executor(instance_name, status) {

            layer.msg('操作调度器');
            $.ajax({
                url: server_context+"/dispatch_executor_status",
                data: "instance_name=" + instance_name+"&status="+status,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    if(data.code != "200"){
                        parent.layer.msg(data.msg);
                        return
                    }
                    parent.layer.msg(data.msg);
                    $("#minus_sign").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context+'/dispatch_executor_list'
                    });
                },
                complete: function () {
                    console.info("complete")
                },
                error: function (data) {
                    parent.layer.msg('操作失败');

                    console.info("error: " + data.responseText);
                }

            });
        }

        window.operateEvents2 = {
            'click #offline': function (e, value, row, index) {
                layer.confirm('下线调度器', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"下线调度器"
                }, function(index){
                    update_executor(row.instance_name, "offline");
                    layer.close(layer.index)
                }, function(index){
                });
            },
            'click #online': function (e, value, row, index) {

                layer.confirm('上线调度器', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"上线调度器"
                }, function(index){
                    update_executor(row.instance_name, "online");
                    layer.close(layer.index)
                }, function(index){
                });
            }
        };


        function operateFormatter2(value, row, index) {

            return [
                ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +

                ' <button id="offline" name="offline" type="button" class="btn btn-outline btn-sm">' + '下线' + '<i class="glyphicon glyphicon-circle-arrow-down" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="online" name="online" type="button" class="btn btn-outline btn-sm">上线\n' +
                '                                        <i class="glyphicon glyphicon-circle-arrow-up" aria-hidden="true"></i>\n' +
                '                                    </button>'
                +
                '</div>'

            ].join('');

        }

        function update_system_task(id, status) {

            var url = server_context+"/dispatch_system_task_create";
            if(status == "offline"){
                url = server_context+"/dispatch_system_task_delete";
            }

            layer.msg('系统任务');
            $.ajax({
                url: url,
                data: "id=" + id,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    if(data.code != "200"){
                        parent.layer.msg(data.msg);
                        return
                    }
                    parent.layer.msg(data.msg);
                    $('#exampleTableEvents2').bootstrapTable('refresh', {
                        url: server_context+'/dispatch_system_task_list'
                    });
                },
                complete: function () {
                    console.info("complete")
                },
                error: function (data) {
                    parent.layer.msg('操作失败');

                    console.info("error: " + data.responseText);
                }

            });
        }

        window.operateEvents3 = {
            'click #create': function (e, value, row, index) {
                layer.confirm('启用调度任务', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"启用调度"
                }, function(index){
                    update_system_task(row.job_id, "online");
                    layer.close(layer.index)
                }, function(index){
                });
            },
            'click #delete': function (e, value, row, index) {
                layer.confirm('禁用调度任务', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"禁用调度"
                }, function(index){
                    update_system_task(row.job_id, "offline");
                    layer.close(layer.index)
                }, function(index){
                });
            },
        };

        function getMyDate(str){
            var oDate = new Date(str),
                oYear = oDate.getFullYear(),
                oMonth = oDate.getMonth()+1,
                oDay = oDate.getDate(),
                oHour = oDate.getHours(),
                oMin = oDate.getMinutes(),
                oSen = oDate.getSeconds(),
                oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +" "+getzf(oHour)+":"+getzf(oMin)+":"+getzf(oSen);//最后拼接时间
            return oTime;
        };
        //补0操作
        function getzf(num){
            if(parseInt(num) < 10){
                num = '0'+num;
            }
            return num;
        }

        $('#exampleTableEvents').bootstrapTable({
            method: "POST",
            url: server_context+"/dispatch_executor_list",
            search: true,
            pagination: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            iconSize: 'outline',
            responseHandler:function (res) {
                if(res.code != "200"){
                    layer.msg(res.msg);
                    return ;
                }
                return res.result;
            },
            toolbar: '#exampleTableEventsToolbar',
            icons: {
                refresh: 'glyphicon-repeat',
                toggle: 'glyphicon-list-alt',
                columns: 'glyphicon-list'
            },
            columns: [{
                checkbox: true,
                field: 'state',
                sortable: true
            }, {
                field: 'sched_name',
                title: '调度器',
                sortable: false
            },{
                field: 'instance_name',
                title: '调度器名称',
                sortable: true,
                visible:true
            },{
                field: 'running',
                title: '执行中任务数',
                sortable: true,
                visible:true
            },{
                field: 'executor_status',
                title: '状态',
                sortable: true,
                visible:true,
                formatter: function (value, row, index) {
                    var context = "已下线";
                    var class_str = "btn-danger btn-xs";

                    if (value == "on") {
                        context = "已上线";
                        class_str = "btn-primary  btn-xs"
                    }

                    return [
                        '<div style="text-align:center" >'+
                        '<div class="btn-group">'+
                        '<button type="button" class="btn '+class_str+'">'+context+'</button>'+
                        '</div>'+
                        '</div>'
                    ].join('');
                }
            },  {
                field: 'operate2',
                title: ' 调度器操作 ',
                events: operateEvents2,//给按钮注册事件
                align : "center",
                valign : "middle",
                formatter: operateFormatter2 //表格中增加按钮
            }
            ]
        });

        $('#exampleTableEvents2').bootstrapTable({
            method: 'POST',
            url: server_context+"/dispatch_system_task_list",
            search: true,
            pagination: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            iconSize: 'outline',
            responseHandler:function (res) {
                if(res.code == "200"){
                    return res.result;
                } else if(res.code == "201"){
                    layer.msg(res.msg);
                }else{
                    layer.msg("未返回有效数据");
                }
            },
            toolbar: '#exampleTableEventsToolbar',
            icons: {
                refresh: 'glyphicon-repeat',
                toggle: 'glyphicon-list-alt',
                columns: 'glyphicon-list'
            },
            columns: [{
                checkbox: true,
                field: 'state',
                sortable: true
            }, {
                field: 'job_id',
                title: 'JOB_ID',
                sortable: false
            },   {
                field: 'job_context',
                title: '调度说明',
                sortable: false
            }, {
                field: 'expr',
                title: '表达式',
                sortable: false,
                visible:true
            }, {
                field: 'use_quartz_time',
                title: '是否使用quart时间',
                sortable: false,
                visible:false
            }, {
                field: 'status',
                title: '调度器状态及操作',
                sortable: true,
                width:250,
                events: operateEvents3,//给按钮注册事件
                formatter: function (value, row, index) {
                    var context = "未启用";
                    var class_str = "btn-danger btn-xs";
                    if (value == "create") {
                        context = "未启用";
                        class_str = "btn-danger  btn-xs"
                    }
                    if (value == "finish") {
                        context = "已完成";
                        class_str = "btn-primary  btn-xs"
                    }
                    if (value == "running") {
                        context = "运行中";
                        class_str = "btn-primary  btn-xs"
                    }
                    if (value == "remove") {
                        context = "未启用";
                        class_str = "btn-danger  btn-xs"
                    }
                    if (value == "pause") {
                        context = "暂停中";
                        class_str = "btn-warning btn-xs"

                    }
                    return [
                        '<div style="text-align:center" >'+
                        '<div class="btn-group">'+
                        '<button type="button" class="btn '+class_str+'">'+context+'</button>'+
                        '<button type="button" id="create" class="btn btn-warning btn-xs">启用</button>'+
                        '<button type="button" id="delete" class="btn btn-danger btn-xs">禁用</button>'+
                        '</div>'+
                        '</div>'
                    ].join('');
                }

            }
            ]
        });

    })();
})(document, window, jQuery);
