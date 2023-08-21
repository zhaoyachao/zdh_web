
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);



      $('#btn_add_group').click(function () {

          parent.layer.open({
              type: 2,
              title: '用户组配置',
              shadeClose: false,
              shade: 0.8,
              area: ['450px', '300px'],
              content: server_context+'/user_group_add_index?id=-1', //iframe的url
              end:function () {
                  parent.layer.closeAll();
              }
          });

      });

      $('#btn_add').click(function () {


          layer.confirm('新增', {
              btn: ['确定','取消'] //按钮
          }, function(index){
              openTabPage(server_context+"/user_add_index.html?id=-1", "用户权限配置");
              layer.close(layer.index);
          }, function(){

          });
      });

      $('#btn_enable').click(function () {

          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请至少选择一个用户!");
              return;
          }

          layer.confirm('一键启用', {
              btn: ['确定','取消'] //按钮
          }, function(index){
              var ids = new Array();// 声明一个数组
              $(rows).each(function() {// 通过获得别选中的来进行遍历
                  ids.push(this.id);// cid为获得到的整条数据中的一列
              });
              console.log(ids)
              enableUsers(ids, "true");
              layer.close(layer.index);
          }, function(){

          });
      });

      $('#btn_disenable').click(function () {
          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请至少选择一个用户!");
              return;
          }

          layer.confirm('一键禁用', {
              btn: ['确定','取消'] //按钮
          }, function(index){
              var ids = new Array();// 声明一个数组
              $(rows).each(function() {// 通过获得别选中的来进行遍历
                  ids.push(this.id);// cid为获得到的整条数据中的一列
              });
              console.log(ids)
              enableUsers(ids, "false");
              layer.close(layer.index);
          }, function(){

          });
      });

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              openTabPage(server_context+"/user_add_index.html?id="+ row.id, "用户权限配置")
          },
          'click #edit_dim': function (e, value, row, index) {
              $("#id").val(row.id);
              parent.layer.open({
                  type: 2,
                  title: '维度配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['100%', '100%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/permission_user_dimension_value_index.html?user_account="+ row.user_account+"&product_code="+row.product_code, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents-table').bootstrapTable('destroy');
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/user_list?"+$("#user_form").serialize()+"&tm="+new Date(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
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
              ' <button id="edit_dim" name="edit_dim" type="button" class="btn btn-outline btn-sm" title="更新维度"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>'
              // ' <button id="copy" name="copy" type="button" class="btn btn-outline btn-sm" title="复制"><i class="glyphicon glyphicon-copyright-mark" aria-hidden="true"></i>\n' +
              // '                                    </button>',
              // ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              // '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              // '                                    </button>'
               +
              '</div>'

          ].join('');

      }

      window.operateEvents2 = {
          'click #btn_enable': function (e, value, row, index) {
              context = "启用用户";
              enable = 'true';
              if(value=="true"){
                  context = "禁用用户";
                  enable = 'false';
              }
              layer.confirm(context, {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组

                  //console.log(row.id)
                  ids.push(row.id);
                  enableUsers(ids,enable);
                  layer.close(layer.index);
              }, function(){

              });
              //window.open(server_context+"/server_log_instance.html?templete_id=" + row.id);
              //openTabPage("task_group_log_instance.html?job_id=" + row.job_id+"&task_log_id="+row.task_log_id, "任务组实例:"+row.job_context)
          }
      };

      function enableUsers(ids,enable) {
          $.ajax({
              url : server_context+"/user_enable",
              data : "ids=" + ids+"&enable="+enable,
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
                      url: server_context+"/user_list?"+$("#user_form").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json"
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
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
          url: "",
          search: true,
          pagination: true,
          showRefresh: true,
          showToggle: true,
          showColumns: true,
          iconSize: 'outline',
          responseHandler:function (res) {
              if(!Array.isArray(res)){
                  layer.msg(res.msg);
                  return res.result;
              }
              return res;
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
                sortable:true
            }, {
                field: 'user_account',
                title: '用户账号',
                sortable:false
            },{
                field: 'user_name',
                title: '用户名',
                sortable:false
            },{
                field: 'email',
                title: '邮箱',
                sortable:false
            }, {
                field: 'phone',
                title: '手机号',
                sortable:false

            }, {
                field: 'product_code',
                title: '产品code',
                sortable:false
            },{
                field: 'enable',
                title: '状态',
                sortable:false,
                events: operateEvents2,//给按钮注册事件
                formatter: function (value, row, index){
                    var context = "未启用";
                    var class_str = "btn-danger btn-xs";

                    if (value == "true") {
                        context = "启用";
                        class_str = "btn-primary  btn-xs"
                    }
                    return [
                        '<div style="text-align:center" >'+
                        '<div class="btn-group">'+
                        '<button id="btn_enable" type="button" class="btn '+class_str+'">'+context+'</button>'+
                        '</div>'+
                        '</div>'
                    ].join('');
                }
            },{
                field: 'operate',
                title: '操作',
                events: operateEvents,//给按钮注册事件
                width:150,
                formatter: operateFormatter //表格中增加按钮
            }]
    });

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

  })();
})(document, window, jQuery);
