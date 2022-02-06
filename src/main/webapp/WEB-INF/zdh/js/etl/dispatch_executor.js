(function (document, window, $) {

    // Example Bootstrap Table Events
    // ------------------------------
    (function () {
        var height=400;
        if($(document.body).height()*0.8>height){
            height=$(document.body).height()*0.8
        }
        $('#exampleTableEvents').attr("data-height",height);

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
                ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +

                ' <button id="offline" name="offline" type="button" class="btn btn-outline btn-sm">' + '下线' + '<i class="glyphicon glyphicon-circle-arrow-down" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="online" name="online" type="button" class="btn btn-outline btn-sm">上线\n' +
                '                                        <i class="glyphicon glyphicon-circle-arrow-up" aria-hidden="true"></i>\n' +
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
            url: server_context+"/dispatch_executor_list",
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
                field: 'sched_name',
                title: '调度器',
                sortable: false
            },{
                field: 'instance_name',
                title: '调度器名称',
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
