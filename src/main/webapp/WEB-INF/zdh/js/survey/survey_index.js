(function(document, window, $) {

    (function() {
        var height = 400;
        if ($(document.body).height() * 0.8 > height) {
            height = $(document.body).height() * 0.8
        }
        $('#exampleTableEvents').attr("data-height", height);

        $('#add').click(function () {
            parent.layer.open({
                type: 2,
                title: '问卷设计',
                shadeClose: false,
                resize: true,
                fixed: false,
                maxmin: true,
                shade: 0.1,
                area: ['90%', '85%'],
                content: server_context + "/survey_design_index",
                end: function () {
                    $('#exampleTableEvents-table').bootstrapTable('destroy');
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context + "/survey_list_by_page?" + $("#survey_form").serialize() + "&tm=" + new Date(),
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        dataType: "json"
                    });
                }
            });
        });

        $('#remove').click(function () {
            var rows = $("#exampleTableEvents").bootstrapTable('getSelections');
            if (rows.length == 0) {
                layer.msg("请先选择要删除的记录!");
                return;
            } else {
                layer.confirm('是否删除问卷', {
                    btn: ['确定', '取消']
                }, function(index) {
                    var ids = new Array();
                    $(rows).each(function() {
                        ids.push(this.id);
                    });
                    deleteMs(ids);
                    layer.close(layer.index);
                }, function() {

                });
            }
        });

        function deleteMs(ids) {
            $.ajax({
                url: server_context + "/survey_delete",
                data: "ids=" + ids.join(','),
                type: 'post',
                async: false,
                dataType: 'json',
                success: function(data) {
                    if (data.code != '200') {
                        parent.layer.msg("执行失败");
                        return;
                    }
                    parent.layer.msg("执行成功");
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url: server_context + "/survey_list_by_page?" + $("#survey_form").serialize(),
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        dataType: "json"
                    });
                },
                error: function(data) {
                    console.info("error: " + data.responseText);
                }
            });
        }

        window.operateEvents = {
            'click #edit': function(e, value, row, index) {
                top.layer.open({
                    type: 2,
                    title: '问卷设计',
                    shadeClose: false,
                    resize: true,
                    fixed: false,
                    maxmin: true,
                    shade: 0.1,
                    area: ['90%', '85%'],
                    content: server_context + "/survey_design_index?id=" + row.id,
                    end: function() {
                        $('#exampleTableEvents').bootstrapTable('refresh', {
                            url: server_context + "/survey_list_by_page?" + $("#survey_form").serialize(),
                            contentType: "application/x-www-form-urlencoded;charset=utf-8",
                            dataType: "json"
                        });
                    }
                });

            },
            'click #preview': function(e, value, row, index) {
                previewSurveyById(row.id);
            },
            'click #statistics': function(e, value, row, index) {
                var statisticsUrl = (server_context || '') + '/survey_statistics_index.html?id=' + row.id;
                var newWindow = window.open(statisticsUrl, '_blank');
                
                if (newWindow) {
                    newWindow.focus();
                } else {
                    layer.msg('浏览器阻止了新标签页，请允许弹出后重试');
                }
            },
            'click #del': function(e, value, row, index) {
                layer.confirm('是否删除问卷', {
                    btn: ['确定', '取消']
                }, function(index) {
                    var ids = new Array();
                    ids.push(row.id);
                    deleteMs(ids);
                    layer.close(layer.index);
                }, function() {

                });
            }
        };

        function operateFormatter(value, row, index) {
            var edit_class = "btn btn-outline btn-sm " + get_edit_class(row);
            var preview_class = "btn btn-outline btn-sm " + get_edit_class(row);
            var statistics_class = "btn btn-outline btn-sm btn-info";
            var del_class = "btn btn-outline btn-sm " + get_del_class(row);

            return [
                '<div class="btn-group" id="exampleTableEventsToolbar" role="group">',
                '<button id="edit" name="edit" type="button" class="' + edit_class + '" title="设计"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i></button>',
                '<button id="preview" name="preview" type="button" class="' + preview_class + '" title="预览"><i class="glyphicon glyphicon-eye-open" aria-hidden="true"></i></button>',
                '<button id="statistics" name="statistics" type="button" class="' + statistics_class + '" title="统计"><i class="glyphicon glyphicon-stats" aria-hidden="true"></i></button>',
                '<button id="del" name="del" type="button" class="' + del_class + '" title="删除"><i class="glyphicon glyphicon-trash" aria-hidden="true"></i></button>',
                '</div>'
            ].join('');
        }

        function paramsMatter(value, row, index) {
            var span = document.createElement("span");
            span.setAttribute("title", value);
            span.innerHTML = value;
            return span.outerHTML;
        }

        function formatTableUnit(value, row, index) {
            return {
                css: {
                    "white-space": "nowrap",
                    "text-overflow": "ellipsis",
                    "overflow": "hidden",
                    "max-width": "40px"
                }
            }
        }

        function statusFormatter(value, row, index) {
            var statusMap = {'0': '<span class="label label-warning">草稿</span>',
                             '1': '<span class="label label-success">已发布</span>',
                             '2': '<span class="label label-default">已关闭</span>'};
            return statusMap[value] || value;
        }

        function getMyDate(str) {
            if (!str) return '';
            var oDate = new Date(str),
                oYear = oDate.getFullYear(),
                oMonth = oDate.getMonth() + 1,
                oDay = oDate.getDate(),
                oHour = oDate.getHours(),
                oMin = oDate.getMinutes(),
                oSen = oDate.getSeconds(),
                oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + " " + getzf(oHour) + ":" + getzf(oMin) + ":" + getzf(oSen);
            return oTime;
        };

        function getzf(num) {
            if (parseInt(num) < 10) {
                num = '0' + num;
            }
            return num;
        }

        function previewSurveyById(id) {
            var previewUrl = (server_context || '') + '/api/survey_preview?id=' + id;

            var newWindow = window.open(previewUrl, '_blank');
            
            if (newWindow) {
                newWindow.focus();
            } else {
                alert('浏览器阻止了新标签页，请允许弹出后重试');
            }
        }

        $('#exampleTableEvents').bootstrapTable({
            method: 'POST',
            url: server_context + "/survey_list_by_page",
            cache: false,
            contentType: 'application/x-www-form-urlencoded;charset=utf-8',
            dataType: "json",
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
            sortable: true,
            sortOrder: 'desc',
            sortName: 'create_time',
            sidePagination: "server",
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 25, 50, 100],
            queryParams: function (params) {
                // 此处使用了LayUi组件 是为加载层
                //loadIndex = layer.load(1);
                let resRepor = {
                    //服务端分页所需要的参数
                    limit: params.limit,
                    offset: params.offset
                };
                return resRepor;
            },
            responseHandler: function(res) {
                if (res.code === '200' && res.result) {
                    return {
                        total: res.result.total,
                        rows: res.result.rows
                    };
                }
                return {
                    total: 0,
                    rows: []
                };
            },
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: 'ID',
                width: 80,
                visible: false,
                formatter: paramsMatter
            }, {
                field: 'survey_title',
                title: '问卷标题',
                width: 300,
                formatter: paramsMatter,
                cellStyle: formatTableUnit
            }, {
                field: 'product_code',
                title: '所属产品',
                width: 150,
                formatter: paramsMatter,
                cellStyle: formatTableUnit
            }, {
                field: 'survey_status',
                title: '状态',
                width: 100,
                align: 'center',
                formatter: statusFormatter
            }, {
                field: 'create_time',
                title: '创建时间',
                width: 180,
                align: 'center',
                formatter: function(value, row, index) {
                    return getMyDate(value);
                }
            }, {
                field: 'operate',
                title: '操作',
                width: 280,
                align: 'center',
                events: operateEvents,
                formatter: operateFormatter
            }]
        });

        var $result = $('#examplebtTableEventsResult');
    })();
})(document, window, jQuery);
