(function(document, window, $) {
    (function() {
        var height = 400;
        if ($(document.body).height() * 0.8 > height) {
            height = $(document.body).height() * 0.8;
        }
        $('#exampleTableEvents').attr("data-height", height);

        function getMyDate(str) {
            if (!str) {
                return "";
            }
            var num = Number(str);
            if (String(str).length === 10) {
                num = num * 1000;
            }
            var oDate = new Date(num);
            var oYear = oDate.getFullYear();
            var oMonth = oDate.getMonth() + 1;
            var oDay = oDate.getDate();
            var oHour = oDate.getHours();
            var oMin = oDate.getMinutes();
            var oSen = oDate.getSeconds();
            return oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + " " + getzf(oHour) + ":" + getzf(oMin) + ":" + getzf(oSen);
        }

        function getzf(num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        }

        function refreshTable() {
            $('#exampleTableEvents').bootstrapTable('refresh', {
                url: server_context + "/wechat_comment_list_by_page?" + $("#wechat_comment_form").serialize(),
                contentType: "application/json;charset=utf-8",
                dataType: "json"
            });
        }

        $('#sync').click(function() {
            var wechat_channel = $("#wechat_channel").val();
            var article_id = $("#article_id").val();
            var product_code = $("#product_code").val();
            if (!wechat_channel || !article_id || !product_code) {
                layer.msg("同步前请先填写产品、服务号、文章ID");
                return;
            }
            $.ajax({
                url: server_context + "/wechat_comment_sync",
                data: {
                    wechat_channel: wechat_channel,
                    article_id: article_id,
                    product_code: product_code
                },
                type: "post",
                dataType: "json",
                success: function(data) {
                    layer.msg(data.msg);
                    if (data.code === '200') {
                        refreshTable();
                    }
                },
                error: function(data) {
                    console.info("error: " + data.responseText);
                }
            });
        });

        $('#remove').click(function() {
            var rows = $("#exampleTableEvents").bootstrapTable('getSelections');
            if (rows.length === 0) {
                layer.msg("请先选择要删除的记录!");
                return;
            }
            layer.confirm('是否删除选中的评论/回复', {btn: ['确定', '取消']}, function() {
                var ids = [];
                $(rows).each(function() {
                    ids.push(this.id);
                });
                deleteMs(ids);
                layer.close(layer.index);
            });
        });

        function deleteMs(ids) {
            $.ajax({
                url: server_context + "/wechat_comment_delete",
                data: "ids=" + ids,
                type: "post",
                dataType: "json",
                success: function(data) {
                    if (data.code !== '200') {
                        parent.layer.msg("执行失败: " + data.msg);
                        return;
                    }
                    parent.layer.msg("执行成功");
                    refreshTable();
                },
                error: function(data) {
                    console.info("error: " + data.responseText);
                }
            });
        }

        window.operateEvents = {
            'click #reply': function(e, value, row) {
                layer.use('extend/layer.ext.js', function () {
                    layer.prompt({
                        formType: 2,
                        title: '请输入回复内容'
                        //area: ['520px', '180px']
                    }, function(value, index) {
                        if (!value || value.trim().length === 0) {
                            layer.msg("回复内容不能为空");
                            return;
                        }
                        $.ajax({
                            url: server_context + "/wechat_comment_reply_add",
                            data: {id: row.id, content: value},
                            type: "post",
                            dataType: "json",
                            success: function(data) {
                                layer.msg(data.msg);
                                if (data.code === '200') {
                                    refreshTable();
                                }
                            },
                            error: function(data) {
                                console.info("error: " + data.responseText);
                            }
                        });
                        layer.close(index);
                    });
                });
            },
            'click #del': function(e, value, row) {
                layer.confirm('是否删除该记录', {btn: ['确定', '取消']}, function() {
                    deleteMs([row.id]);
                    layer.close(layer.index);
                });
            },
            'click #del_reply': function(e, value, row) {
                layer.confirm('是否删除该回复记录', {btn: ['确定', '取消']}, function() {
                    $.ajax({
                        url: server_context + "/wechat_comment_reply_delete",
                        data: {"id": row.id},
                        type: "post",
                        dataType: "json",
                        success: function(data) {
                            if (data.code !== '200') {
                                parent.layer.msg("执行失败: " + data.msg);
                                return;
                            }
                            parent.layer.msg("执行成功");
                            refreshTable();
                        },
                        error: function(data) {
                            console.info("error: " + data.responseText);
                        }
                    });
                    layer.close(layer.index);
                });
            },
        };

        function operateFormatter(value, row, index) {
            var replyClass = "btn btn-outline btn-sm " + get_edit_class(row);
            var delClass = "btn btn-outline btn-sm " + get_del_class(row);
            return [
                '<div class="btn-group" role="group">',
                '<button id="reply" type="button" class="' + replyClass + '" title="回复"><i class="glyphicon glyphicon-comment" aria-hidden="true"></i></button>',
                '<button id="del" type="button" class="' + delClass + '" title="删除评论"><i class="glyphicon glyphicon-trash" aria-hidden="true"></i></button>',
                '<button id="del_reply" type="button" class="' + delClass + '" title="删除回复"><i class="glyphicon glyphicon-trash" aria-hidden="true"></i></button>',
                '</div>'
            ].join('');
        }

        $('#exampleTableEvents').bootstrapTable('destroy').bootstrapTable({
            method: "POST",
            dataType: 'json',
            url: server_context + "/wechat_comment_list_by_page?" + $("#wechat_comment_form").serialize(),
            search: true,
            pagination: true,
            showRefresh: true,
            showToggle: true,
            showColumns: true,
            sidePagination: "server",
            pageNumber: 1,
            pageSize: 10,
            iconSize: 'outline',
            toolbar: '#exampleTableEventsToolbar',
            icons: {
                refresh: 'glyphicon-repeat',
                toggle: 'glyphicon-list-alt',
                columns: 'glyphicon-list'
            },
            queryParams: function(params) {
                loadIndex = layer.load(1);
                return {limit: params.limit, offset: params.offset};
            },
            responseHandler: res => {
                layer.close(loadIndex);
                layer.msg(res.msg);
                return {"total": res.result.total, "rows": res.result.rows};
            },
            columns: [
                {checkbox: true, field: 'state', sortable: false},
                {field: 'id', title: '主键ID', sortable: false},
                {field: 'wechat_channel', title: '服务号', sortable: false},
                {field: 'article_id', title: '文章ID', sortable: false},
                {field: 'comment_id', title: '评论ID', sortable: false},
                {field: 'parent_comment_id', title: '父评论ID', sortable: false},
                {field: 'comment_type', title: '类型', sortable: false},
                {field: 'openid', title: 'openid', sortable: false},
                {field: 'content', title: '内容', sortable: false},
                {field: 'status', title: '状态', sortable: false},
                {
                    field: 'comment_time',
                    title: '评论时间',
                    sortable: false,
                    formatter: function(value) {
                        return getMyDate(value);
                    }
                },
                {
                    field: 'create_time',
                    title: '创建时间',
                    sortable: true,
                    formatter: function(value) {
                        return getMyDate(value);
                    }
                },
                {
                    field: 'operate',
                    title: '操作',
                    events: operateEvents,
                    width: 150,
                    formatter: operateFormatter
                }
            ]
        });
    })();
})(document, window, jQuery);
