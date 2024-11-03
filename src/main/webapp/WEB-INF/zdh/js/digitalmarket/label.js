
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
              title: '标签配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/label_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/label_list?"+$("#label_form").serialize()+"&tm="+new Date(),
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
            layer.confirm('是否删除标签', {
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
              url : server_context+"/label_delete",
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
                      url: server_context+"/label_list?"+$("#label_form").serialize(),
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
                  title: '标签配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/label_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/label_list?"+$("#label_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
                      });
                  }
              });

          },
          'click #copy': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '标签配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/label_add_index?id="+row.id+"&is_copy=true", //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/label_list?"+$("#label_form").serialize(),
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
                  layer.close(layer.index)
              }, function(){

              });
          },
          'click #tasks': function (e, value, row, index) {
              layer.confirm('是否编辑加工任务(当前功能未开发,可使用ETL能力暂时代替)', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  layer.close(layer.index)
              }, function(){

              });
          },
          'click #edit_value': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '标签配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/label_online_add_value_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/label_list?"+$("#label_form").serialize(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
                      });
                  }
              });

          },
      };

      window.operateEvents3 = {
          'click #btn_status': function (e, value, row, index) {
              layer.confirm('是否启用/禁用', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  $.ajax({
                      url: server_context+"/label_enable",
                      data: "id=" + row.id,
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
                          $('#exampleTableEvents').bootstrapTable('refresh', {
                              url: server_context+"/label_list?"+$("#label_form").serialize(),
                              contentType: "application/json;charset=utf-8",
                              dataType: "json"
                          });
                      },
                      complete: function () {
                          console.info("complete")
                      },
                      error: function (data) {
                          layer.msg('删除失败');
                          console.info("error: " + data.responseText);
                      }

                  });
                  layer.close(layer.index)
              }, function(){

              });
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="copy" name="copy" type="button" class="btn btn-outline btn-sm" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="tasks" name="tasks" type="button" class="btn btn-outline btn-sm" title="标签加工任务">\n' +
              '                                        <i class="glyphicon glyphicon-tasks" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="edit_value" name="edit_value" type="button" class="btn btn-outline btn-sm" title="标签值更新">\n' +
              '                                        <i class="glyphicon glyphicon-camera" aria-hidden="true"></i>\n' +
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
      url: server_context+"/label_list?"+$("#label_form").serialize(),
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
            field:'state',
            sortable:false
        }, {
            field: 'id',
            title: 'ID',
            sortable:false
        }, {
            field: 'label_context',
            title: '标签说明',
            sortable:false
        }, {
            field: 'label_code',
            title: '标签code',
            sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
        },{
            field: 'label_use_type',
            title: '使用场景',
            sortable:true,
            formatter: function (value, row, index) {
                var context = value;
                var class_str = "btn-info btn-xs";
                if(value == "batch"){
                    context = "值查人";
                }
                if(value == "single"){
                    context = "人查值";
                    class_str = "btn-warning  btn-xs"
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
          field: 'param_json',
          title: '标签参数',
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
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            width:150,
            events: operateEvents3,//给按钮注册事件
            formatter: function (value, row, index) {
                var context = "未启用";
                var class_str = "btn-danger btn-xs";
                if (value == "2") {
                    context = "未启用";
                    class_str = "btn-danger  btn-xs"
                }
                if (value == "1") {
                    context = "启用";
                    class_str = "btn-primary  btn-xs"
                }
                return [
                    '<div style="text-align:center" >'+
                    '<div class="btn-group">'+
                    '<button id="btn_status" type="button" class="btn '+class_str+'">'+context+'</button>'+
                    '</div>'+
                    '</div>'
                ].join('');
            }

        }, {
            field: 'operate',
            title: '常用操作按钮事件',
            events: operateEvents,//给按钮注册事件
            width:200,
            formatter: operateFormatter //表格中增加按钮
        }]
    });

  })();
})(document, window, jQuery);
