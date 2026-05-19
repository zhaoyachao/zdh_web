(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);
      
      $('#add').click(function () {
          parent.layer.open({
              type: 2,
              title: '推送配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['100%', '100%'],
              content: server_context+"/push_config_add_index?id=-1",
              end : function () {
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/push_config_list_by_page?"+$("#push_config_form").serialize()+"&tm="+new Date(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
                      queryParams: function (params) {
                          let resRepor = {
                              limit: params.limit,
                              offset: params.offset
                          };
                          return resRepor;
                      }
                  });
              }
          });
      });

      $('#edit').click(function () {
        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');
        if (rows.length == 0) {
            layer.msg("请先选择要编辑的记录!");
            return;
        } else if (rows.length > 1) {
            layer.msg("只能选择一条记录进行编辑!");
            return;
        } else {
            var row = rows[0];
            parent.layer.open({
                type: 2,
                title: '推送配置',
                shadeClose: false,
                resize: true,
                fixed: false,
                maxmin: true,
                shade: 0.1,
                area : ['100%', '100%'],
                content: server_context+"/push_config_add_index?id="+row.id,
                end:function () {
                    $('#exampleTableEvents').bootstrapTable('refresh', {
                        url : server_context+'/push_config_list_by_page?'+$("#push_config_form").serialize(),
                        contentType: "application/json;charset=utf-8",
                        dataType: "json",
                        queryParams: function (params) {
                            let resRepor = {
                                limit: params.limit,
                                offset: params.offset
                            };
                            return resRepor;
                        },
                    });
                }
            });
        }
    });

      $('#remove').click(function () {
        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');
        if (rows.length == 0) {
            layer.msg("请先选择要删除的记录!");
            return;
        } else {
            layer.confirm('是否删除配置', {
                btn: ['确定','取消']
            }, function(index){
                var ids = new Array();
                $(rows).each(function() {
                    ids.push(this.id);
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
              url : server_context+"/push_config_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  if(data.code != '200'){
                      parent.layer.msg("执行失败");
                      return ;
                  }
                  parent.layer.msg("执行成功");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/push_config_list_by_page?"+$("#push_config_form").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
                      queryParams: function (params) {
                          let resRepor = {
                              limit: params.limit,
                              offset: params.offset
                          };
                          return resRepor;
                      }
                  });
              },
              error : function (data) {
                  console.info("error: " + data.responseText);
              }
          });
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '推送配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['100%', '100%'],
                  content: server_context+"/push_config_add_index?id="+row.id,
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/push_config_list_by_page?'+$("#push_config_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json",
                          queryParams: function (params) {
                              let resRepor = {
                                  limit: params.limit,
                                  offset: params.offset
                              };
                              return resRepor;
                          },
                      });
                  }
              });
          },
          'click #del': function (e, value, row, index) {
              layer.confirm('是否删除配置', {
                  btn: ['确定','取消']
              }, function(index){
                  var ids = new Array();
                  ids.push(row.id);
                  deleteMs(ids);
                  layer.close(layer.index)
              }, function(){

              });
          },
      };

      function operateFormatter(value, row, index) {
          var edit_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var del_class = "btn btn-outline btn-sm "+ get_del_class(row);
          return [
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="'+edit_class+'" title="编辑"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="'+del_class+'" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
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

      function getMyDate(str){
          var oDate = new Date(str),
              oYear = oDate.getFullYear(),
              oMonth = oDate.getMonth()+1,
              oDay = oDate.getDate(),
              oHour = oDate.getHours(),
              oMin = oDate.getMinutes(),
              oSen = oDate.getSeconds(),
              oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +" "+getzf(oHour)+":"+getzf(oMin)+":"+getzf(oSen);
          return oTime;
      };

      function getzf(num){
          if(parseInt(num) < 10){
              num = '0'+num;
          }
          return num;
      }

      function configTypeFormatter(value, row, index) {
          if (value == 'switch') {
              return '<span class="label label-primary">开关配置</span>';
          } else if (value == 'number') {
              return '<span class="label label-info">数字配置</span>';
          } else {
              return '<span class="label label-default">未知类型</span>';
          }
      }

      function configFormatter(value, row, index) {
          // 使用 config_json 字段（map 类型）
          var configJson = row.config_json || {};
          var status = configJson.status === 1 ? '开启' : '关闭';
          var threadSize = configJson.thread_size || 1;
          var scheduleTime = configJson.schedule_time || '';
          
          var result = '状态: ' + status + '<br>线程数: ' + threadSize;
          if (scheduleTime) {
              result += '<br>时间段: ' + scheduleTime;
          }
          return result;
      }

      $('#exampleTableEvents').bootstrapTable('destroy').bootstrapTable({
      method: "POST",
      url: server_context+"/push_config_list_by_page?"+$("#push_config_form").serialize(),
          dataType: 'json',
          search: true,
          pagination: true,
          showRefresh: true,
          showToggle: true,
          showColumns: true,
          showLoading: true,
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
      queryParams: function (params) {
          let resRepor = {
              limit: params.limit,
              offset: params.offset
          };
          return resRepor;
      },
     responseHandler: res => {
              layer.msg(res.msg);
              return {
                  "total":res.result.total,
                  "rows": res.result.rows
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
            field:'state',
            sortable:false
        }, {
            field: 'id',
            title: 'ID',
            sortable:true
        }, {
            field: 'config_key',
            title: '配置Key',
            sortable:true
        }, {
            field: 'config_name',
            title: '配置名称',
            sortable:true
        }, {
            field: 'config_type',
            title: '配置类型',
            sortable:true,
            formatter: configTypeFormatter
        }, {
            field: 'config_json',
            title: '配置详情',
            sortable:true,
            formatter: configFormatter
        }, {
            field: 'description',
            title: '描述',
            sortable:true
        }, {
            field: 'create_time',
            title: '创建时间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        },{ 
            field: 'operate',
            title: '操作',
            events: operateEvents,
            width:150,
            formatter: operateFormatter
        }]
    });

  })();
})(document, window, jQuery);
