
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);

      $('#remove').click(function () {

        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
        if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
            layer.msg("请先选择要删除的记录!");
            return;
        } else {
            layer.confirm('是否删除非结构化任务', {
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
              url : server_context+"/etl_task_unstructure_log_delete",
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
                      url: server_context+"/etl_task_unstructure_log_list?"+$("#etl_task_unstructure_from").serialize()+"&unstructure_id="+row.unstructure_id,
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
          'click #log': function (e, value, row, index) {
              $("#id").val(row.id);
              var log_template = "                    <div style='height: 100%'><textarea id=\"textarea_logs\" name=\"textarea_logs\" class=\"form-control\" style=\"background-color: #333333;color: #44ee44; height: 100%\"\n" +
              "                               readonly>msg</textarea></div>";
              var html = log_template.replaceAll("msg", row.msg);
              parent.layer.open({
                  type: 1,
                  title: '非结构化任务日志',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: html, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/etl_task_unstructure_log_list'+"?unstructure_id="+row.unstructure_id
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
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="log" name="log" type="button" class="btn btn-outline btn-sm" title="日志"><i class="glyphicon glyphicon-file" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
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
      method: "POST",
      url: server_context+"/etl_task_unstructure_log_list",
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
            sortable:true
        }, {
            field: 'id',
            title: 'ID',
            sortable:false
        }, {
            field: 'unstructure_context',
            title: '非结构化 中 文 描 述 及 说 明',
            sortable:false
        },{
            field: 'project_id',
            title: '项目名',
            sortable:false
        }, {
            field: 'data_sources_choose_file_output',
            title: '文件输出源',
            sortable:false
        }, {
            field: 'data_sources_choose_jdbc_output',
            title: '元数据输出源',
            sortable:false
        },{
            field: 'etl_sql',
            title: '数据处理逻辑',
            sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
        }, {
          field: 'unstructure_params_output',
          title: '参数',
          sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
      }, {
            field: 'status',
            title: '执行状态',
            sortable: true,
            width:150,
            formatter: function (value, row, index) {
                var context = "失败";
                var class_str = "btn-danger btn-xs";
                if (value == "true") {
                    context = "成功";
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

        }, {
            field: 'unstructure_id',
            title: '非结构任务id',
            sortable:false
        },{
            field: 'create_time',
            title: '任 务 创 建 时 间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
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
