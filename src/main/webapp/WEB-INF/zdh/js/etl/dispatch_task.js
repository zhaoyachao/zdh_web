(function (document, window, $) {

    // Example Bootstrap Table Events
    // ------------------------------
    (function () {
        var height=400;
        if($(document.body).height()*0.8>height){
            height=$(document.body).height()*0.8
        }
        $('#exampleTableEvents').attr("data-height",height);
        $('#add').click(function () {
            layer.confirm('是否新增调度任务', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                openTabPage(server_context+"/dispatch_task_group_add_index.html?id=-1", "创建调度任务");
                layer.close(layer.index);
            }, function(){

            });
        });


        $('#remove').click(function () {

            var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
            if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
                layer.msg("请先选择要删除的记录!");
                return;
            } else {

                layer.confirm('是否删除调度任务', {
                    btn: ['确定','取消'] //按钮
                }, function(index){
                    var ids = new Array();// 声明一个数组
                    $(rows).each(function () {// 通过获得别选中的来进行遍历
                        ids.push(this.job_id);// job_id为获得到的整条数据中的一列
                    });
                    console.log(ids);
                    deleteMs(ids);
                    layer.close(layer.index);
                }, function(){

                });


            }

        });

        function deleteMs(ids) {
            $.ajax({
                url: server_context+"/dispatch_task_group_delete",
                data: "ids=" + ids,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context+'/dispatch_task_list2'
                    });
                },
                error: function (data) {
                    console.info("error: " + data.responseText);
                }

            });
        }

        function executeMs(job_id,reset_count,concurrency) {
            var index=top.layer.msg('手动开始执行',{time:"-1"});
            $('#execute').attr({disabled: "disabled"});
            $.ajax({
                url: server_context+"/dispatch_task_execute",
                data: "job_id=" + job_id+"&reset_count="+reset_count+"&concurrency="+concurrency,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    top.layer.close(index);
                    layer.msg(data.msg);
                    $("#execute").removeAttr('disabled');
                },
                error: function (data) {
                    $("#execute").removeAttr('disabled');
                    top.layer.close(index);
                    layer.msg('执行失败');
                    console.info("error: " + data.responseText);
                }

            });
        }

        function executeQuartz(job_id,reset) {
            $('#execute_quartz').attr({disabled: "disabled"});
            var index=top.layer.msg('添加到调度器开始执行',{time:"-1"});

            var msg="";
            $.ajax({
                url: server_context+"/dispatch_task_execute_quartz",
                data: "job_id=" + job_id+"&reset="+reset,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    console.info(data);
                    top.layer.close(index);
                    msg=data.msg
                },
                complete: function () {
                    $("#execute_quartz").removeAttr('disabled');
                    console.info("complete");
                },
                error: function (data) {
                    $("#execute_quartz").removeAttr('disabled');
                    layer.msg('添加失败');
                    console.info("error: " + data.responseText);
                }
            });
            top.layer.msg(msg);
            $("#execute_quartz").removeAttr('disabled');
            $('#exampleTableEvents').bootstrapTable('refresh', {
                url: server_context+'/dispatch_task_list2'
            });
        }

        function pauseQuartz(job_id, row_status) {
            if (row_status != 'pause' && row_status != 'running') {
                layer.msg("任务状态未启动,或者不是处于暂停状态 无法完成暂停或恢复");
                return false;
            }
            var status = "pause";
            if (row_status == 'pause') {
                status = "running"
            }
            $('#pause').attr({disabled: "disabled"});
            layer.msg('暂停调度器任务');
            $.ajax({
                url: server_context+"/dispatch_task_quartz_pause",
                data: "job_id=" + job_id + "&status=" + status,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    if(data.code != "200"){
                        layer.msg(data.msg);
                        return
                    }
                    if (status == 'running') {
                        layer.msg('恢复成功');
                    } else {
                        layer.msg('暂停成功');
                    }

                    $("#pause").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context+'/dispatch_task_list2'
                    });
                },
                complete: function () {
                    $("#pause").removeAttr('disabled');
                    console.info("complete")
                },
                error: function (data) {
                    $("#pause").removeAttr('disabled');
                    if (status == 'running') {
                        layer.msg('恢复失败');
                    } else {
                        layer.msg('暂停失败');
                    }
                    console.info("error: " + data.responseText);
                }

            });
        }

        function delQuartz(job_id) {

            $('#minus_sign').attr({disabled: "disabled"});
            layer.msg('删除调度器任务');
            $.ajax({
                url: server_context+"/dispatch_task_quartz_del",
                data: "job_id=" + job_id,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    if(data.code != "200"){
                        layer.msg(data.msg);
                        return
                    }
                    layer.msg(data.msg);
                    $("#minus_sign").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context+'/dispatch_task_list2'
                    });
                },
                complete: function () {
                    $("#minus_sign").removeAttr('disabled');
                    console.info("complete")
                },
                error: function (data) {
                    $("#minus_sign").removeAttr('disabled');

                    layer.msg('删除失败');

                    console.info("error: " + data.responseText);
                }

            });
        }

        window.operateEvents = {
            'click #edit': function (e, value, row, index) {

                $("#id").val(row.job_id);
                openTabPage(server_context+"/dispatch_task_group_add_index.html?id="+ row.job_id, "修改调度任务");


            },
            'click #del': function (e, value, row, index) {
                layer.confirm('是否删除任务', {
                    btn: ['确定','取消'] //按钮
                }, function(index){
                    var ids = new Array();// 声明一个数组
                    ids.push(row.job_id);
                    deleteMs(ids);
                    layer.close(layer.index)
                }, function(){

                });

            },
            'click #execute': function (e, value, row, index) {

                parent.layer.open({
                    type: 2,
                    title: '手动执行配置',
                    shadeClose: false,
                    resize: true,
                    fixed: false,
                    maxmin: true,
                    shade: 0.1,
                    area : ['45%', '60%'],
                    //area: ['450px', '500px'],
                    content: server_context+"/task_group_exe_detail_index?id="+row.job_id, //iframe的url
                    end : function () {
                        console.info("弹框结束")
                    }
                });

            },
            'click #copy': function (e, value, row, index) {
                $("#id").val(row.job_id);
                openTabPage(server_context+"/dispatch_task_group_add_index?id=" + row.job_id+"&is_copy=true", "复制调度任务");
                // top.layer.open({
                //     type: 2,
                //     title: '调度任务配置',
                //     shadeClose: false,
                //     resize: true,
                //     fixed: false,
                //     maxmin: true,
                //     shade: 0.1,
                //     area: ['45%', '60%'],
                //     //area: ['450px', '500px'],
                //     content: server_context+"/dispatch_task_group_add_index?id=" + row.job_id+"&is_copy=true", //iframe的url
                //     end: function () {
                //         $('#exampleTableEvents').bootstrapTable('refresh', {
                //             url: server_context+'/dispatch_task_list2'
                //         });
                //     }
                // });
            }
        };

        window.operateEvents2 = {
            'click #execute_quartz': function (e, value, row, index) {
                layer.confirm('是否启用调度', {
                    btn: ['启用','重置并启用','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"启用调度",
                    btn1:function(index){
                        executeQuartz(row.job_id,"0");
                        layer.close(layer.index)
                    },
                    btn2:function(index){
                        executeQuartz(row.job_id,"1");
                        layer.close(layer.index)
                    }
                });
            },
            'click #pause': function (e, value, row, index) {
                layer.confirm('是否暂停/恢复调度', {
                    btn: ['暂停/恢复','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"暂停/恢复调度"
                }, function(index){
                    $("#id").val(row.job_id);
                    pauseQuartz(row.job_id, row.status);
                    layer.close(layer.index)
                }, function(index){
                });
            },
            'click #minus_sign': function (e, value, row, index) {

                layer.confirm('是否关闭调度', {
                    btn: ['关闭','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"关闭调度"
                }, function(index){
                    delQuartz(row.job_id);
                    layer.close(layer.index)
                }, function(index){
                });
            }
        };

        window.operateEvents3 = {
            'click #task_group_log_instance': function (e, value, row, index) {
                window.location.protocol+"://"+window.location.host
                window.open(server_context+"/task_group_log_instance_index.html?job_id=" + row.job_id+"&task_log_id="+row.task_log_id);
                //openTabPage("task_group_log_instance.html?job_id=" + row.job_id+"&task_log_id="+row.task_log_id, "任务组实例:"+row.job_context)
            }
        };

        function operateFormatter(value, row, index) {
            return [
                ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
                ' <button id="execute" name="execute" type="button" class="btn btn-outline btn-sm" title="手动执行">\n' +
                '                                        <i class="glyphicon glyphicon-play" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
                '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="copy" name="copy" type="button" class="btn btn-outline btn-sm" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
                '                                    </button>'
                +
                '</div>'

            ].join('');

        }

        function operateFormatter2(value, row, index) {
            var status_context = "暂停";
            if (row.status == "pause") {
                status_context = "恢复"
            }

            return [
                ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +

                ' <button id="execute_quartz" name="execute_quartz" type="button" class="btn btn-outline btn-sm">调度\n' +

                '                                        <i class="glyphicon glyphicon-retweet" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="pause" name="pause" type="button" class="btn btn-outline btn-sm">' + status_context + '<i class="glyphicon glyphicon-pause" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="minus_sign" name="minus_sign" type="button" class="btn btn-outline btn-sm">停用\n' +
                '                                        <i class="glyphicon glyphicon-minus-sign" aria-hidden="true"></i>\n' +
                '                                    </button>'
                +
                '</div>'

            ].join('');

        }

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
            url: server_context+"/dispatch_task_list2",
            search: true,
            pagination: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            iconSize: 'outline',
            responseHandler:function (res) {
                if(!Array.isArray(res)){
                    if(res.code == "201"){
                        layer.msg(res.msg);
                    }else{
                        layer.msg("未返回有效数据");
                    }
                }
                return res;
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
                title: '调度器状态及执行记录',
                sortable: true,
                width:150,
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
                        '<button type="button" id="task_group_log_instance" class="btn btn-warning btn-xs">执行记录</button>'+
                        '</div>'+
                        '</div>'
                    ].join('');
                }

            }, {
                field: 'operate',
                title: '测试基础操作及日志分析操作',
                events: operateEvents,//给按钮注册事件
                align : "center",
                valign : "middle",
                formatter: operateFormatter //表格中增加按钮
            }, {
                field: 'operate2',
                title: ' 调 度 器 相 关 及 调 度 任 务 操 作 ',
                events: operateEvents2,//给按钮注册事件
                align : "center",
                valign : "middle",
                formatter: operateFormatter2 //表格中增加按钮
            }, {
                field: 'start_time',
                title: '开始时间',
                sortable: true,
                visible:false
            }, {
                field: 'end_time',
                title: '结束时间',
                sortable: true,
                visible:false
            }, {
                field: 'job_model',
                title: '执行模式',
                sortable: true,
                visible:false
            }, {
                field: 'plan_count',
                title: '计划执行次数',
                sortable: true,
                visible:false
            },  {
                field: 'last_time',
                title: '上次任务执行时间',
                sortable: true,
                visible:false,
                formatter: function (value, row, index) {
                    return getMyDate(value);
                }
            }, {
                field: 'next_time',
                title: '下次任务执行时间',
                sortable: true,
                visible:false,
                formatter: function (value, row, index) {
                    return getMyDate(value);
                }
            }, {
                field: 'params',
                title: '参数',
                sortable: true,
                visible:false
            }, {
                field: 'task_log_id',
                title: '最后的执行日志id',
                sortable: true,
                visible:false
            }
            ]
        });


        function openTabPage(url, title) {
            var wpd = $(window.parent.document);
            var mainContent = wpd.find('.J_mainContent');
            var thisIframe = mainContent.find("iframe[data-id='" + url + "']");
            var pageTabs = wpd.find('.J_menuTabs .page-tabs-content ')
            pageTabs.find(".J_menuTab.active").removeClass("active");
            mainContent.find("iframe").css("display", "none");
            if (thisIframe.length > 0) {	// 选项卡已打开
                thisIframe.css("display", "inline");
                pageTabs.find(".J_menuTab[data-id='" + url + "']").addClass("active");
            } else {
                var menuItem = wpd.find("a.J_menuItem[href='" + url + "']");
                var dataIndex = title == undefined ? menuItem.attr("data-index") : '9999';
                var _title = title == undefined ? menuItem.find('.nav-label').text() : title;
                var iframe = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + url + '" frameborder="0" data-id="' + url
                    + '" seamless="" style="display: inline;"></iframe>';
                pageTabs.append(
                    ' <a href="javascript:;" class="J_menuTab active" data-id="' + url + '">' + _title + ' <i class="fa fa-times-circle"></i></a>');
                mainContent.append(iframe);
                //显示loading提示
                var loading = top.layer.load();
                mainContent.find('iframe:visible').load(function () {
                    //iframe加载完成后隐藏loading提示
                    top.layer.close(loading);
                });
            }

        }

    })();
})(document, window, jQuery);
