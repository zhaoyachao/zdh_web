
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
              title: '参数配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/param_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/param_list?"+$("#param_from").serialize()+"&tm="+new Date(),
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
            layer.confirm('是否删除SSH任务', {
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

      $('#refresh').click(function () {

          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请先选择要同步的记录!");
              return;
          } else {
              layer.confirm('是否批量同步redis', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组
                  $(rows).each(function() {// 通过获得别选中的来进行遍历
                      ids.push(this.id);// cid为获得到的整条数据中的一列
                  });
                  toRedis(ids);
                  layer.close(layer.index)
              }, function(){

              });
          }

      });

      function deleteMs(ids) {
          $.ajax({
              url : server_context+"/param_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success");
                  if(data.code != "200"){
                      parent.layer.msg(data.msg);
                      return
                  }
                  parent.layer.msg(data.msg);
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/param_list?"+$("#param_from").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json"
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      function toRedis(ids) {
          $.ajax({
              url : server_context+"/param_to_redis",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success");
                  if(data.code != "200"){
                      parent.layer.msg(data.msg);
                      return
                  }
                  parent.layer.msg(data.msg);
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              parent.layer.open({
                  type: 2,
                  title: '参数配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/param_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/param_list'
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
          'click #toredis': function (e, value, row, index) {

              layer.confirm('是否同步redis', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组
                  ids.push(row.id);
                  toRedis(ids);
                  layer.close(layer.index)
              }, function(){

              });
          },
          'click #merge': function (e, value, row, index) {

              layer.use('extend/layer.ext.js', function () {
                  layer.prompt({title: '同步当前参数到指定版本,为空标识同步默认版本,同步后当前参数会删除',
                      formType: 2,
                      value:"default",
                      btn: ['合并','合并后删除', '取消'],
                      btn2: function(index, elem){
                          //合并后删除逻辑
                          var value = $('#layui-layer'+index + " .layui-layer-input").val();
                          $.ajax({
                              url: server_context+"/param_merge",
                              data: "id="+row.id +"&version="+ value+"&is_delete=true",
                              type: "post",
                              async:false,
                              dataType: "json",
                              success: function (data) {
                                  console.info("success");
                                  $('#exampleTableEvents').bootstrapTable('refresh', {
                                      url: server_context+"/param_list"
                                  });
                              },
                              error: function (data) {
                                  console.info("error: " + data.responseText);
                              }

                          });
                      },
                  }, function(pass, index){

                      $.ajax({
                          url: server_context+"/param_merge",
                          data: "id="+row.id +"&version="+ pass,
                          type: "post",
                          async:false,
                          dataType: "json",
                          success: function (data) {
                              if(data.code != "200"){
                                  parent.layer.msg(data.msg);
                                  return ;
                              }
                              $('#exampleTableEvents').bootstrapTable('refresh', {
                                  url: server_context+"/param_list"
                              });
                              layer.close(index);
                          },
                          error: function (data) {
                              console.info("error: " + data.responseText);
                          }

                      });

                  });

              });
          },
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="toredis" name="toredis" type="button" class="btn btn-outline btn-sm" title="同步redis">\n' +
              '                                        <i class="glyphicon glyphicon-refresh" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="merge" name="merge" type="button" class="btn btn-outline btn-sm" title="合并参数">\n' +
              '                                        <i class="glyphicon glyphicon-refresh" aria-hidden="true"></i>\n' +
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


      $('#exampleTableEvents').bootstrapTable({
          method: 'POST',
      url: server_context+"/param_list",
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
      responseHandler:function (res) {
          if(res.code != "200"){
              layer.msg(res.msg);
              return ;
          }
          return res.result;
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
            field: 'param_name',
            title: '参数名称',
            sortable:false
        }, {
            field: 'param_context',
            title: '参数说明',
            sortable:false
        }, {
            field: 'param_value',
            title: '参数值',
            sortable:false
        }, {
            field: 'param_type_name',
            title: '参数类型',
            sortable:false
        }, {
            field: 'owner',
            title: '创建人',
            sortable:false
        },{
            field: 'status',
            title: '状态',
            sortable:false,
            //events: operateEvents3,//给按钮注册事件
            formatter: function (value, row, index) {
                var context = "未启用";
                var class_str = "btn-danger btn-xs";
                if (value == "on") {
                    context = "启用";
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
