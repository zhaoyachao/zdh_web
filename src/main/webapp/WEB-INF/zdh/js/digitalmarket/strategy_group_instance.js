
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
            layer.confirm('当前不支持删除操作', {
                btn: ['确定','取消'] //按钮
            }, function(index){
                layer.close(index);
            }, function(){
            });
        }
    });

      $('#refash_small_flow').click(function () {

          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请先选择要同步小流量的记录!");
              return;
          } else {
              layer.confirm('确定同步小流量', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组
                  $(rows).each(function () {// 通过获得别选中的来进行遍历
                      ids.push(this.id);// job_id为获得到的整条数据中的一列
                  });
                  console.log(ids);
                  refashFlows(ids);
                  layer.close(layer.index);
              }, function(){
              });
          }
      });

      function refashFlows(ids) {
          $.ajax({
              url: server_context+"/small_flow_rate_refash",
              data: "ids=" + ids,
              type: "post",
              async:false,
              dataType: "json",
              success: function (data) {
                  console.info("success");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/strategy_group_instance_list?"+$("#strategy_group_form").serialize()
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      $('#priority').click(function () {

          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请先选择要修改优先级的记录!");
              return;
          } else {

              var ids2 = new Array();// 声明一个数组
              $(rows).each(function () {// 通过获得别选中的来进行遍历
                  ids2.push(this.id);// job_id为获得到的整条数据中的一列
              });

              layer.use('extend/layer.ext.js', function () {
                  layer.prompt({title: '确定修改优先级(值越大优先级越高)', formType: 2}, function(pass, index){
                      layer.close(index);
                      $.ajax({
                          url: server_context+"/strategy_instance_priority",
                          data: "ids="+ids2 +"&priority="+ pass,
                          type: "post",
                          async:false,
                          dataType: "json",
                          success: function (data) {
                              console.info("success");
                              $('#exampleTableEvents').bootstrapTable('refresh', {
                                  url: server_context+"/strategy_group_instance_list?"+$("#strategy_group_form").serialize()
                              });
                          },
                          error: function (data) {
                              console.info("error: " + data.responseText);
                          }

                      });

                  });

              });
          }
      });

      window.operateEvents = {
          'click #log_txt': function (e, value, row, index) {
              window.open(server_context+"/log_txt.html?job_id=" + row.job_id+"&task_log_id="+row.id + "&start_time=" + row.run_time + "&update_time=" + row.update_time);
              //openTabPage("log_txt.html?job_id=" + row.job_id+"&task_log_id="+row.id + "&start_time=" + row.start_time + "&update_time=" + row.update_time, row.job_context + "日志")
          },
          'click #strategy_instance': function (e, value, row, index) {
              window.open(server_context+"/strategy_instance_index.html?strategy_group_instance_id="+row.id)
              //openTabPage("task_log_instance.html?group_id="+row.id, "任务组:"+row.job_context + ":子任务日志")
          },
          'click #retry': function (e, value, row, index) {

              parent.layer.open({
                  type: 2,
                  title: '重试任务组',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['60%', '80%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/strategy_group_retry_detail_index?id="+row.id, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/strategy_group_instance_list?"+$("#strategy_group_form").serialize()
                      });
                  }
              });

          },
          'click #kill': function (e, value, row, index) {
//                if (row.status != "running") {
//                    layer.msg("当前任务状态不支持杀死")
//                    return
//                }
              layer.confirm('确定杀死任务吗?', {
                  btn: ['确定', '取消'] //按钮
              }, function (index) {

                  $.ajax({
                      url: server_context+"/killStrategyGroup",
                      data: "id=" + row.id,
                      type: "post",
                      dataType: "json",
                      success: function (data) {
                          console.info("success");
                          if(data.code != "200"){
                              layer.msg(data.msg);
                              return
                          }
                          layer.close(index)
                      },
                      error: function (data) {
                          console.info("error: " + data.responseText);
                      }

                  });
                  layer.msg("杀死中")
              }, function () {

              });
          },
          'click #small_flow_rate': function (e, value, row, index) {
              var small_flow_rate = row.small_flow_rate;
              if(is_empty(small_flow_rate)){
                  small_flow_rate = '1,1';
              }
              parent.layer.open({
                  type: 2,
                  title: '小流量配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['60%', '80%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/small_flow_rate_index?id="+row.id+"&small_flow_rate="+small_flow_rate, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/strategy_group_instance_list?"+$("#strategy_group_form").serialize()
                      });
                  }
              });
          },
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
              '                                    </button>',
              ' <button id="strategy_group_execute" name="strategy_group_execute" type="button" class="btn btn-outline btn-sm" title="执行策略"><i class="glyphicon glyphicon-refresh" aria-hidden="true"></i>\n' +
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

      var url = location.search; //这一条语句获取了包括问号开始到参数的最后，不包括前面的路径
      var params = url.substr(1);//去掉问号
      var pa = params.split("&");
      var s = new Object();//定义一个全局变量-存储任务ID
      for (var i = 0; i < pa.length; i++) {
          s[pa[i].split("=")[0]] = unescape(pa[i].split("=")[1]);
      }

      $('#group_id').val(s.id);

      $('#exampleTableEvents').bootstrapTable('destroy').bootstrapTable({
      method: "POST",
      dataType: 'json',
      url: server_context+"/strategy_group_instance_list?"+$("#strategy_group_form").serialize(),
      search: true,
      pagination: true,
      showRefresh: true,
      showToggle: true,
      showColumns: true,
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
          loadIndex = layer.load(1);
          let resRepor = {
              //服务端分页所需要的参数
              limit: params.limit,
              offset: params.offset
          };
          return resRepor;
      },
      // 请求完成回调 可处理请求到的数据
      responseHandler: res => {
          // 关闭加载层
          layer.close(loadIndex);
          layer.msg(res.msg);
          return {
              "total":res.result.total,
              "rows": res.result.rows
          }
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
            field: 'group_context',
            title: '策略组说明',
            sortable:false
        }, {
            field: 'create_time',
            title: '任 务 创 建 时 间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        }, {
            field: 'update_time',
            title: '任 务 更 新 时 间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        }, {
            field: 'small_flow_rate',
            title: '小流量比例',
            sortable:true,
            formatter: function (value, row, index) {
                var context = "小流量";
                if(is_empty(value)){
                    value = '0,0';
                }
                var start = value.split(",")[0];
                var end = value.split(",")[1];
                var process = end - start;
                var process2 = 100 - end;
                var class_str = "progress-bar progress-bar-success";
                var div_1 = "";
                var div_2 =   '<div title="'+start+ '%-'+ end + '%" style="width: '+ process + '%"  role="progressbar" class="' + class_str + '">' + '</div>';
                var div_3 = "";
                if(start > 1 ){
                    div_1 =  '<div title="'+0+ '%-'+ (start-1) + '%" style="width: '+ start + '%"  role="progressbar" class="progress-bar progress-bar-warning">' + '</div>';
                }
                if(end < 100 && start > 0){
                    div_3 =  '<div title="'+(end-1+2)+ '%-'+ 100 + '%" style="width: '+ process2 + '%" role="progressbar" class="progress-bar progress-bar-warning">'  + '</div>';
                }
                if(value == '0,0'){
                    div_2 =   '<div title="'+start+ '%-'+ end + '%" style="width: '+ 100 + '%"  role="progressbar" class="progress-bar progress-bar-warning">' + '</div>';
                }
                return [
                    '<small>' + context + start+ '%-'+ end + '%</small>' +
                    '<div class="progress progress-mini progress-striped">' +
                    div_1 +
                    div_2 +
                    div_3 +
                    '</div>'
                ].join('');
            }
        }, {
            field: 'small_flow_status',
            title: '小流量状态',
            sortable:true,
            formatter: function (value, row, index) {
                var context = value;
                var class_str = "btn-primary btn-xs";
                if(value == "on"){
                    context = "开启";
                }
                if(value == "off"){
                    class_str = "btn-danger btn-xs";
                    context = "关闭";
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
            field: 'process',
            title: '进度',
            sortable: true,
            formatter: function (value, row, index) {
                var context = "执行中";
                var process = 10;
                var class_str = "progress-bar progress-bar-success";
                if (row.status == "error" || row.status == "killed") {
                    class_str = "progress-bar progress-bar-danger";
                    process = 100;
                    context = "失败"
                }
                if (row.status == "finish") {
                    class_str = "progress-bar progress-bar-success";
                    process = 100;
                    context = "完成"
                }
                return [
                    '<small>' + context + process + '%</small>' +
                    '<div class="progress progress-mini">' +
                    '<div style="width: ' + process + '%" aria-valuemax="100" aria-valuemin="0" aria-valuenow="35" role="progressbar" class="' + class_str + '">' +
                    '</div>' +
                    '</div>'
                ].join('');
            }
        }, {
            field: 'status',
            title: '任务执行状态',
            sortable: true,
            width:260,
            events: operateEvents,//给按钮注册事件
            formatter: function (value, row, index) {
                var class_str = "btn-primary  btn-xs";
                var class_str2 = "btn-primary  ";
                var context = "未运行";
                var btn_retry_str='<button id="retry" type="button" class="btn btn-primary btn-xs">重试</button>';
                var btn_kill_str='<button id="kill" type="button" class="btn btn-danger btn-xs">杀死</button>';
                if(row.is_retryed==1){
                    btn_retry_str='<button disabled="disabled" id="retry" type="button" class="btn btn-primary btn-xs">重试</button>'
                }

                var process=100;

                if (value == "finish") {
                    context = "完成";
                    class_str = "btn-primary btn-xs";
                    btn_retry_str='';
                    btn_kill_str='';
                }
                if (value == "etl" || value == "dispatch" || value == "wait_retry" || value=="check_dep" || value=="check_dep_finish" || value=="sub_task_dispatch") {
                    context = "运行中";
                    class_str = "btn-primary  btn-xs";
                    btn_retry_str='';
                }
                if (value == "error") {
                    context = "失败";
                    class_str = "btn-danger btn-xs";
                    btn_kill_str='';
                }
                if (value == "retry") {
                    context = "重试";
                    class_str = "btn-danger btn-xs"
                }
                if (value == "kill" ){
                    context = "杀死中";
                    class_str = "btn-danger btn-xs";
                    btn_kill_str='';
                    btn_retry_str='';
                }
                if (value=="killed" ){
                    context = "已杀死";
                    class_str = "btn-danger btn-xs";
                    btn_kill_str=''
                }
                if (value == "create") {
                    context = "创建成功";
                    class_str = "btn-primary  btn-xs";
                    btn_retry_str='';
                }

                if(context=="未运行"){
                    btn_retry_str='';
                    btn_kill_str='';
                }
                var url=server_context+"/log_txt.html?job_id=" + row.job_id+"&task_log_id="+row.id + "&run_time=" + row.run_time + "&update_time=" + row.update_time;
                var title=row.job_context + "日志";
                return [
                    '<div style="text-align:center" >'+
                    '<div class="btn-group">'+
                    '<button type="button" class="btn '+class_str+'" >'+context+'</button>'+
                    btn_retry_str +
                    btn_kill_str +
                    '<button type="button" id="log_txt" class="btn btn-warning btn-xs">组日志</button>'+
                    '<button type="button" id="strategy_instance" class="btn btn-info btn-xs">子任务</button>'+
                    '<button type="button" id="small_flow_rate" class="btn btn-primary btn-xs">小流量</button>'+
                    '</div>'+
                    '</div>'
                ].join('');
            }
        }]
    });

  })();
})(document, window, jQuery);
