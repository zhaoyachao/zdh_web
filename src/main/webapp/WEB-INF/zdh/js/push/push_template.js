
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
              title: '模板配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['100%', '100%'],//[宽度,高度]
              //area: ['450px', '500px'],
              content: server_context+"/push_template_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/push_template_list_by_page?"+$("#push_template_form").serialize()+"&tm="+new Date(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
                      queryParams: function (params) {
                          // 此处使用了LayUi组件 是为加载层
                          //loadIndex = layer.load(1);
                          let resRepor = {
                              //服务端分页所需要的参数
                              limit: params.limit,
                              offset: params.offset
                          };
                          return resRepor;
                      }
                  });
              }
          });
      });

      $('#remove').click(function () {

        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
        if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
            layer.msg("请先选择要删除的记录!");
            return;
        } else {
            layer.confirm('是否删除规则', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                var ids = new Array();// 声明一个数组
                $(rows).each(function() {// 通过获得别选中的来进行遍历
                    ids.push(this.id);// cid为获得到的整条数据中的一列
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
              url : server_context+"/push_template_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  if(data.code != '200'){
                      console.error(data.msg);
                      parent.layer.msg("执行失败");
                      return ;
                  }
                  parent.layer.msg("执行成功");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/push_template_list_by_page?"+$("#push_template_form").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
                      queryParams: function (params) {
                          // 此处使用了LayUi组件 是为加载层
                          //loadIndex = layer.load(1);
                          let resRepor = {
                              //服务端分页所需要的参数
                              limit: params.limit,
                              offset: params.offset
                          };
                          return resRepor;
                      }
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '模板配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['100%', '100%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/push_template_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/push_template_list_by_page?'+$("#push_template_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json",
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
                      });
                  }
              });

          },
          'click #copy': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '模板配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['100%', '100%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/push_template_add_index?id="+row.id+"&is_copy=true", //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/push_template_list_by_page?"+$("#push_template_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json",
                          queryParams: function (params) {
                              // 此处使用了LayUi组件 是为加载层
                              //loadIndex = layer.load(1);
                              let resRepor = {
                                  //服务端分页所需要的参数
                                  limit: params.limit,
                                  offset: params.offset
                              };
                              return resRepor;
                          }
                      });
                  }
              });

          },
          'click #del': function (e, value, row, index) {
              layer.confirm('是否删除任务', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组
                  ids.push(row.id);
                  deleteMs(ids);
                  layer.close(layer.index)
              }, function(){

              });
          },
          'click #push_template_execute': function (e, value, row, index) {
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
                  content: server_context+"/push_template_task_exe_detail_index?id="+row.id, //iframe的url
                  end : function () {
                      console.info("弹框结束")
                  }
              });
          }
      };

      function operateFormatter(value, row, index) {
          var edit_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var copy_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var del_class = "btn btn-outline btn-sm "+ get_del_class(row);
          return [
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="'+edit_class+'" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="copy" name="copy" type="button" class="'+copy_class+'" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="'+del_class+'" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="push_template_execute" name="push_template_execute" type="button" class="'+edit_class+'" title="执行策略"><i class="glyphicon glyphicon-refresh" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
              '</div>'

          ].join('');

      }

      //表格超出宽度鼠标悬停显示td内容
      function paramsMatter(value, row, index) {
          var span = document.createElement("span");
          span.setAttribute("title", value);
          span.innerHTML = value;
          return span.outerHTML;
      }
      //td宽度以及内容超过宽度隐藏
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


      $('#exampleTableEvents').bootstrapTable('destroy').bootstrapTable({
      method: "POST",
      url: server_context+"/push_template_list_by_page?"+$("#push_template_form").serialize(),
          dataType: 'json',
          search: true,
          pagination: true,
          showRefresh: true,
          showToggle: true,
          showColumns: true,
          showLoading: true,
          sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
          pageNumber: 1,                       //初始化加载第一页，默认第一页
          pageSize: 10,                       //每页的记录行数（*）
          iconSize: 'outline',
          toolbar: '#exampleTableEventsToolbar',
          icons: {
              refresh: 'glyphicon-repeat',
              toggle: 'glyphicon-list-alt',
              columns: 'glyphicon-list'
          },
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
     responseHandler: res => {
              // 关闭加载层
              //layer.close(loadIndex);
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
            field: 'template_name',
            title: '模板名称',
            sortable:true,
        },  {
            field: 'template_id',
            title: '模板ID',
            sortable:true
        },  {
            field: 'push_type',
            title: '消息类型',
            sortable:true,
            formatter: function (value, row, index) {
                var context = value;
                var class_str = "btn-primary btn-xs";
                if(value == "1"){
                    context = "营销";
                }
                if(value == "2"){
                    context = "通知";
                }
                if(value == "3"){
                    context = "验证码";
                }
                if(value == "4"){
                    class_str = "btn-danger btn-xs";
                    context = "告警";
                }
                if(value == "5"){
                    context = "其他";
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
            field: 'create_time',
            title: '任 务 创 建 时 间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        }, {
            field: 'status',
            title: '状态及执行记录',
            sortable: true,
            width:150,
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
                    '<button type="button" id="strategy_group_instance" class="btn btn-warning btn-xs">执行记录</button>'+
                    '</div>'+
                    '</div>'
                ].join('');
            }

        },{
            field: 'operate',
            title: '常用操作按钮事件',
            events: operateEvents,//给按钮注册事件
            width:150,
            formatter: operateFormatter //表格中增加按钮
        }]
    });

  })();
})(document, window, jQuery);
