
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {

      $('#add').click(function () {
          parent.layer.open({
              type: 2,
              title: 'ETL任务配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: "dispatch_task_add_index?id=-1", //iframe的url
              end:function () {
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : 'dispatch_task_list'
                  });
              }
          });
      })

      $('#remove').click(function () {

        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
        if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
            alert("请先选择要删除的记录!");
            return;
        } else {
            var ids = new Array();// 声明一个数组
            $(rows).each(function() {// 通过获得别选中的来进行遍历
                ids.push(this.id);// cid为获得到的整条数据中的一列
            });
            console.log(ids)
            deleteMs(ids)
        }

    })

      function deleteMs(ids) {
          $.ajax({
              url : "dispatch_task_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success")
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : 'dispatch_task_list'
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      function executeMs(id) {
          $('#execute').attr({disabled: "disabled"});
          layer.msg('开始执行');
          $.ajax({
              url : "dispatch_task_execute",
              data : "id=" + id,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success")
                  layer.msg('执行成功');
                  $("#execute").removeAttr('disabled');
              },
              error: function (data) {
                  $("#execute").removeAttr('disabled');
                  layer.msg('执行失败');
                  console.info("error: " + data.responseText);
              }

          });
      }


      window.operateEvents = {
          'click #edit': function (e, value, row, index) {

              $("#id").val(row.id)
              top.layer.open({
                  type: 2,
                  title: '调度任务配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: "dispatch_task_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : 'dispatch_task_list'
                      });
                  }
              });

          },
          'click #del': function (e, value, row, index) {
              var ids = new Array();// 声明一个数组
              ids.push(row.id)
              deleteMs(ids)
          },
          'click #execute': function (e, value, row, index) {
              executeMs(row.id)
          },
          'click #log_txt': function (e, value, row, index) {
              openTabPage("log_txt.html?id="+row.id,"日志")
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="execute" name="execute" type="button" class="btn btn-outline btn-sm">\n' +
              '                                        <i class="glyphicon glyphicon-play" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm">\n' +
              '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>',
              ' <button id="log_txt" name="log_txt" type="button" class="btn btn-outline btn-sm">\n' +
              '                                        <i class="glyphicon glyphicon-file" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
              '</div>'

          ].join('');

      }


    $('#exampleTableEvents').bootstrapTable({
      url: "dispatch_task_list",
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
        columns: [{
            checkbox: true,
            field:'state',
            sortable:true
        }, {
            field: 'id',
            title: 'ID',
            sortable:false
        }, {
            field: 'dispatch_context',
            title: '调度说明',
            sortable:false
        }, {
            field: 'etl_task_id',
            title: 'ETL任务ID',
            sortable:false
        }, {
            field: 'etl_context',
            title: 'ETL任务说明',
            sortable:false
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
          var thisIframe = mainContent.find("iframe[data-id='"+ url +"']");
          var pageTabs = wpd.find('.J_menuTabs .page-tabs-content ')
          pageTabs.find(".J_menuTab.active").removeClass("active");
          mainContent.find("iframe").css("display", "none");
          if(thisIframe.length > 0){	// 选项卡已打开
              thisIframe.css("display", "inline");
              pageTabs.find(".J_menuTab[data-id='"+ url +"']").addClass("active");
          }else{
              var menuItem = wpd.find("a.J_menuItem[href='"+ url +"']");
              var dataIndex = title == undefined ? menuItem.attr("data-index") : '9999';
              var _title = title == undefined ? menuItem.find('.nav-label').text() : title;
              var iframe = '<iframe class="J_iframe" name="iframe'+ dataIndex +'" width="100%" height="100%" src="' + url + '" frameborder="0" data-id="' + url
                  + '" seamless="" style="display: inline;"></iframe>';
              pageTabs.append(
                  ' <a href="javascript:;" class="J_menuTab active" data-id="'+url+'">' + _title + ' <i class="fa fa-times-circle"></i></a>');
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
