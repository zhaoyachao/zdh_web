(function (document, window, $) {

    // Example Bootstrap Table Events
    // ------------------------------
    (function () {
        $('#exampleTableEvents').attr("data-height",$(document.body).height()*0.8)
        $('#add').click(function () {
            parent.layer.open({
                type: 2,
                title: 'ETL任务配置',
                shadeClose: false,
                resize: true,
                fixed: false,
                maxmin: true,
                shade: 0.1,
                area: ['45%', '60%'],
                //area: ['450px', '500px'],
                content: "dispatch_task_add_index?id=-1", //iframe的url
                end: function () {
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'dispatch_task_list'
                    });
                }
            });
        })

        $('#remove').click(function () {

            var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
            if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
                alert("请先选择要删除的记录!");
                return;
            } else {
                var ids = new Array();// 声明一个数组
                $(rows).each(function () {// 通过获得别选中的来进行遍历
                    ids.push(this.job_id);// job_id为获得到的整条数据中的一列
                });
                console.log(ids)
                deleteMs(ids)
            }

        })

        function deleteMs(ids) {
            $.ajax({
                url: "dispatch_task_delete",
                data: "ids=" + ids,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success");
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'dispatch_task_list'
                    });
                },
                error: function (data) {
                    console.info("error: " + data.responseText);
                }

            });
        }

        function executeMs(job_id) {
            $('#execute').attr({disabled: "disabled"});
            layer.msg('手动开始执行');
            $.ajax({
                url: "dispatch_task_execute",
                data: "job_id=" + job_id,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.info("success")
                    layer.msg('执行成功');
                    $("#execute").removeAttr('disabled');
                },
                error: function (data) {
                    $("#execute").removeAttr('disabled');
                    layer.msg('执行失败');
                    console.info("error: " + data.responseText);
                }

            });
        }

        function executeQuartz(job_id) {
            $('#execute_quartz').attr({disabled: "disabled"});
            layer.msg('添加到调度器开始执行');
            $.ajax({
                url: "dispatch_task_execute_quartz",
                data: "job_id=" + job_id,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.info("success")
                    layer.msg('添加成功');
                    $("#execute_quartz").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'dispatch_task_list'
                    });
                },
                complete: function () {
                    $("#execute_quartz").removeAttr('disabled');
                    console.info("complete")
                },
                error: function (data) {
                    $("#execute_quartz").removeAttr('disabled');
                    layer.msg('添加失败');
                    console.info("error: " + data.responseText);
                }

            });
        }

        function pauseQuartz(job_id, row_status) {
            if (row_status != 'pause' && row_status != 'running') {
                layer.msg("任务状态未启动,或者不是处于暂停状态 无法完成暂停或恢复")
                return false;
            }
            var status = "pause"
            if (row_status == 'pause') {
                status = "running"
            }
            $('#pause').attr({disabled: "disabled"});
            layer.msg('暂停调度器任务');
            $.ajax({
                url: "dispatch_task_quartz_pause",
                data: "job_id=" + job_id + "&status=" + status,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.info("success")
                    if (status == 'running') {
                        layer.msg('恢复成功');
                    } else {
                        layer.msg('暂停成功');
                    }

                    $("#pause").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'dispatch_task_list'
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
                url: "dispatch_task_quartz_del",
                data: "job_id=" + job_id,
                type: "post",
                dataType: "json",
                success: function (data) {
                    console.info("success")
                    layer.msg('删除成功');
                    $("#minus_sign").removeAttr('disabled');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'dispatch_task_list'
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

                $("#id").val(row.job_id)
                top.layer.open({
                    type: 2,
                    title: '调度任务配置',
                    shadeClose: false,
                    resize: true,
                    fixed: false,
                    maxmin: true,
                    shade: 0.1,
                    area: ['45%', '60%'],
                    //area: ['450px', '500px'],
                    content: "dispatch_task_add_index?id=" + row.job_id, //iframe的url
                    end: function () {
                        $('#exampleTableEvents').bootstrapTable('refresh', {
                            url: 'dispatch_task_list'
                        });
                    }
                });

            },
            'click #del': function (e, value, row, index) {
                layer.confirm('是否删除任务', {
                    btn: ['确定','取消'] //按钮
                }, function(index){
                    var ids = new Array();// 声明一个数组
                    ids.push(row.job_id);
                    deleteMs(ids)
                    layer.close(layer.index)
                }, function(){

                });

            },
            'click #execute': function (e, value, row, index) {
                executeMs(row.job_id)
            },
            'click #log_txt': function (e, value, row, index) {
                openTabPage("log_txt.html?id=" + row.job_id, "日志")
            },
            'click #copy': function (e, value, row, index) {
                $("#id").val(row.job_id)
                top.layer.open({
                    type: 2,
                    title: '调度任务配置',
                    shadeClose: false,
                    resize: true,
                    fixed: false,
                    maxmin: true,
                    shade: 0.1,
                    area: ['45%', '60%'],
                    //area: ['450px', '500px'],
                    content: "dispatch_task_add_index?id=" + row.job_id+"&is_copy=true", //iframe的url
                    end: function () {
                        $('#exampleTableEvents').bootstrapTable('refresh', {
                            url: 'dispatch_task_list'
                        });
                    }
                });
            }
        };

        window.operateEvents2 = {
            'click #execute_quartz': function (e, value, row, index) {
                executeQuartz(row.job_id)
            },
            'click #pause': function (e, value, row, index) {
                $("#id").val(row.job_id)
                pauseQuartz(row.job_id, row.status)
            },
            'click #minus_sign': function (e, value, row, index) {
                delQuartz(row.job_id)
            }
        };


        function operateFormatter(value, row, index) {
            return [
                ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
                ' <button id="execute" name="execute" type="button" class="btn btn-outline btn-sm" title="执行">\n' +
                '                                        <i class="glyphicon glyphicon-play" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
                '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="log_txt" name="log_txt" type="button" class="btn btn-outline btn-sm" title="日志">\n' +
                '                                        <i class="glyphicon glyphicon-file" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="copy" name="copy" type="button" class="btn btn-outline btn-sm" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
                '                                    </button>'
                +
                '</div>'

            ].join('');

        }

        function operateFormatter2(value, row, index) {
            var status_context = "暂停"
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
            url: "dispatch_task_list",
            search: true,
            pagination: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            iconSize: 'outline',
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
            }, {
                field: 'more_task',
                title: '任务类型',
                sortable: true
            },  {
                field: 'job_context',
                title: '调度说明',
                sortable: false
            }, {
                field: 'etl_task_id',
                title: 'ETL任务ID',
                sortable: false
            }, {
                field: 'etl_context',
                title: 'ETL任务说明',
                sortable: false
            }, {
                field: 'status',
                title: '调度状态',
                sortable: false,
                visible:true
            }, {
                field: 'last_status',
                title: '执行状态',
                sortable: true,
                visible:true
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
            }, {
                field: 'count',
                title: '执行次数',
                sortable: true,
                visible:false
            }, {
                field: 'command',
                title: 'command',
                sortable: true,
                visible:false
            }, {
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
