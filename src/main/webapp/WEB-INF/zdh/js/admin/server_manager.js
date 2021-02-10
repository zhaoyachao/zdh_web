(function (document, window, $) {

    // Example Bootstrap Table Events
    // ------------------------------
    (function () {

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

        var height=400
        if($(document.body).height()*0.8>height){
            height=$(document.body).height()*0.8
        }
        $('#exampleTableEvents').attr("data-height",height)

        $('#add').click(function () {


            layer.confirm('一键部署', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                openTabPage("server_add_index.html?id=-1", "一键部署")
                layer.close(layer.index);
            }, function(){

            });
        })

        function update(id,online) {
            var context="上线"
            if(online=="0"){
                context="逻辑下线"
            }else if(online == "2") {
                context="物理下线"
            }

            layer.msg(context);
            $.ajax({
                url: "/server_manager_update",
                data:"id="+id+"&online="+online,
                type: "post",
                async:false,
                dataType: "json",
                success: function (data) {
                    console.info("success")
                    layer.msg(context+'成功');
                },
                complete: function () {
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: 'server_manager_list'
                    });
                    console.info("complete")
                },
                error: function (data) {
                    layer.msg(context+'失败');
                    console.info("error: " + data.responseText);
                }

            });
        }

        window.operateEvents2 = {
            'click #online': function (e, value, row, index) {
                layer.confirm('上线节点', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"上线节点"
                }, function(index){
                    update(row.id,"1")
                    layer.close(layer.index)
                }, function(index){
                });
            },
            'click #offline': function (e, value, row, index) {

                layer.confirm('逻辑下线节点', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"逻辑下线节点"
                }, function(index){
                    update(row.id,"0")
                    layer.close(layer.index)
                }, function(index){
                });
            },
            'click #offline2': function (e, value, row, index) {

                layer.confirm('物理下线节点', {
                    btn: ['确定','取消'], //按钮
                    cancel:function(index, layero){
                        console.log('关闭x号');
                    },
                    title:"物理下线节点"
                }, function(index){
                    update(row.id,"2")
                    layer.close(layer.index)
                }, function(index){
                });
            }
        };

        function operateFormatter2(value, row, index) {

            return [
                ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +

                ' <button id="online" name="online" type="button" class="btn btn-outline btn-sm">逻辑上线\n' +

                '                                        <i class="glyphicon glyphicon-arrow-up" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="offline" name="offline" type="button" class="btn btn-outline btn-sm">逻辑下线\n' +
                '                                        <i class="glyphicon glyphicon-arrow-down" aria-hidden="true"></i>\n' +
                '                                    </button>',
                ' <button id="offline2" name="offline2" type="button" class="btn btn-outline btn-sm">物理下线\n' +
                '                                        <i class="glyphicon glyphicon-arrow-down" aria-hidden="true"></i>\n' +
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
            url: "server_manager_list",
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
                field: 'id',
                title: 'ID',
                align : "center",
                valign : "middle",
                sortable: false
            },{
                field: 'zdh_instance',
                title: '节点实例名称',
                sortable: false,
                align : "center",
                valign : "middle",
                visible:true
            }, {
                field: 'zdh_url',
                title: '节点api',
                align : "center",
                valign : "middle",
                sortable: false,
                visible:false
            },  {
                field: 'zdh_host',
                title: '节点IP',
                align : "center",
                valign : "middle",
                sortable: false
            }, {
                field: 'zdh_port',
                title: '节点端口',
                align : "center",
                valign : "middle",
                sortable: false
            },  {
                field: 'online',
                title: '在线标识',
                align : "center",
                valign : "middle",
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var context = "离线"
                    var class_str = "btn-danger btn-xs"
                    if (value == "1") {
                        context = "在线"
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

            },{
                field: 'operate2',
                title: ' 上线下线操作 ',
                events: operateEvents2,//给按钮注册事件
                align : "center",
                valign : "middle",
                formatter: operateFormatter2 //表格中增加按钮
            }
            ]
        });


    })();
})(document, window, jQuery);
