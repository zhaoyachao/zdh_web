
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
              title: '日志任务配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/etl_task_log_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/etl_task_log_list?"+$("#etl_task_form").serialize()+"&tm="+new Date(),
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
              url : server_context+"/etl_task_log_delete",
              data : "ids=" + ids,
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
                      url: server_context+"/etl_task_log_list?"+$("#etl_task_form").serialize(),
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
                  title: '日志任务配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/etl_task_log_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/etl_task_log_list?"+$("#etl_task_form").serialize(),
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
                  title: '日志任务配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/etl_task_log_add_index?id="+row.id+"&is_copy=true", //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/etl_task_log_list?"+$("#etl_task_form").serialize(),
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
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
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
      url: server_context+"/etl_task_log_list?"+$("#etl_task_form").serialize(),
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
            field: 'log_context',
            title: '日 志 中 文 描 述 及 说 明',
            sortable:false
        },{
          field: 'data_sources_choose_output',
          title: '输出数据源ID',
          sortable:false
      },{
          field: 'data_source_type_output',
          title: '输出数据源类型',
          sortable:true
      },{
          field: 'data_sources_output',
          title: '输出数据源',
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

      function openTabPage(url, title) {
          var wpd = $(window.parent.document);
          var mainContent = wpd.find('.J_mainContent');
          var thisIframe = mainContent.find("iframe[data-id='" + url + "']");
          var pageTabs = wpd.find('.J_menuTabs .page-tabs-content ');
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
