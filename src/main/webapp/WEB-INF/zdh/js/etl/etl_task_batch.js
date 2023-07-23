
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);
      $('#add').click(function () {
          parent.layer.open({
              type: 2,
              title: '批量任务配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/etl_task_batch_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/etl_task_batch_list?"+$("#etl_task_form").serialize()+"&tm="+new Date(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json"
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

            layer.confirm('是否删除单源ETL任务', {
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
              url : server_context+"/etl_task_batch_delete",
              data : "ids=" + ids,
              type : "post",
              async:false,
              dataType : "json",
              success : function(data) {
                  if(data.code != '200'){
                      console.error(data.msg);
                      layer.msg("执行失败");
                      return ;
                  }
                  layer.msg("执行成功");

                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/etl_task_batch_list?"+$("#etl_task_form").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json"
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
                  title: '批量任务配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/etl_task_batch_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/etl_task_batch_list'
                      });
                  }
              });

          },
          'click #copy': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '批量任务配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/etl_task_batch_add_index?id="+row.id+"&is_copy=true", //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/etl_task_batch_list?"+$("#etl_task_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
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
                  layer.close(layer.index);
              }, function(){

              });
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="copy" name="copy" type="button" class="btn btn-outline btn-sm" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
              '</div>'

          ].join('');

      }

      window.operateEvents3 = {
          'click #create_etl': function (e, value, row, index) {

              layer.confirm('是否生成批量ETL任务', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  $.ajax({
                      url : server_context+"/etl_task_batch_create",
                      data : "id=" + row.id,
                      type : "post",
                      async:false,
                      dataType : "json",
                      success : function(data) {
                          if(data.code != '200'){
                              console.error(data.msg);
                              parent.layer.msg("执行失败");
                              return ;
                          }
                          parent.layer.msg("执行成功");

                          $('#exampleTableEvents').bootstrapTable('refresh', {
                              url: server_context+"/etl_task_batch_list?"+$("#etl_task_form").serialize(),
                              contentType: "application/json;charset=utf-8",
                              dataType: "json"
                          });
                      },
                      error: function (data) {
                          console.info("error: " + data.responseText);
                      }

                  });

                  layer.close(layer.index);
              }, function(){

              });

          }
      };

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


      $('#exampleTableEvents').bootstrapTable({
      method: "POST",
      url: server_context+"/etl_task_batch_list",
      search: true,
      pagination: true,
      showRefresh: true,
      showToggle: true,
      showColumns: true,
      iconSize: 'outline',
          responseHandler:function (res) {
              if(res.code != "200"){
                  layer.msg(res.msg);
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
            field:'state',
            sortable:true
        }, {
            field: 'id',
            title: 'ID',
            sortable:false
        }, {
            field: 'etl_pre_context',
            title: 'etl前缀说 明',
            sortable:false
        },{
            field: 'etl_suffix_context',
            title: 'etl后缀说 明',
            sortable:false
        },{
            field: 'data_sources_choose_input',
            title: '输入数据源ID',
            sortable:false,
            visible: false
        }, {
            field: 'data_source_type_input',
            title: '输入数据源类型',
            sortable:false
        },{
            field: 'data_sources_file_name_input',
            title: '输入数据源文件',
            sortable:false
        }, {
          field: 'data_sources_params_input',
          title: '输入数据源参数',
          visible: false,
          sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
      }, {
            field: 'data_sources_filter_input',
            title: '输入数据源过滤条件',
            sortable:false,
            visible: false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
        },{
          field: 'data_sources_choose_output',
          title: '输出数据源ID',
          sortable:false
      },{
          field: 'data_source_type_output',
          title: '输出数据源类型',
          sortable:true
      },{
          field: 'data_sources_table_name_output',
          title: '输出数据源表',
          visible: false,
          sortable:false
      },{
          field: 'data_sources_file_name_output',
          title: '输出数据源文件',
          visible: false,
          sortable:false
      },{
          field: 'data_sources_params_output',
          title: '输出数据源参数',
          visible: false,
          sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
      },{
            field: 'create_time',
            title: '任 务 创 建 时 间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        },{
            field: 'data_sources_clear_output',
            title: '输出数据删除条件',
            visible: false,
            sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
        },{
            field: 'status',
            title: '批量操作',
            sortable: true,
            width:250,
            events: operateEvents3,//给按钮注册事件
            formatter: function (value, row, index) {
                var context = "未执行";
                var class_str = "btn-warning btn-xs";
                if (value == "0") {
                    context = "未执行";
                    class_str = "btn-warning  btn-xs"
                }
                if (value == "1") {
                    context = "执行中";
                    class_str = "btn-info  btn-xs"
                }
                if (value == "2") {
                    context = "执行失败";
                    class_str = "btn-danger  btn-xs"
                }
                if (value == "3") {
                    context = "执行完成";
                    class_str = "btn-info  btn-xs"
                }

                return [
                    '<div style="text-align:center" >'+
                    '<div class="btn-group">'+
                    '<button type="button" class="btn '+class_str+'">'+context+'</button>'+
                    '<button type="button" id="create_etl" class="btn btn-primary btn-xs">生成任务</button>'+
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

    var $result = $('#examplebtTableEventsResult');
  })();
})(document, window, jQuery);
