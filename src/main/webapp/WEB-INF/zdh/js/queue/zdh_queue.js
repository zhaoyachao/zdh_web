
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
              title: '新增消息',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/zdh_queue_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/zdh_queue_list?"+$("#label_form").serialize()+"&tm="+new Date(),
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
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              top.layer.open({
                  type: 2,
                  title: '优先级配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/zdh_queue_add_index?queue_name="+row.queue_name+"&msg="+row.msg+"&priority="+row.priority, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/zdh_queue_list?'+$("#label_form").serialize()+"&tm="+new Date()
                      });
                  }
              });

          },
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="优先级"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
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
      url: server_context+"/zdh_queue_list?"+$("#label_form").serialize()+"&tm="+new Date(),
      search: true,
      pagination: true,
      showRefresh: true,
      showToggle: true,
      showColumns: true,
      sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
      pageNumber: 1,                       //初始化加载第一页，默认第一页
      pageSize: 10,                       //每页的记录行数（*）
      iconSize: 'outline',
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
            field: 'queue_name',
            title: '队列名',
            sortable:false
        }, {
            field: 'msg',
            title: '内容',
            sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
        }, {
          field: 'priority',
          title: '优先级',
          sortable:false,
            cellStyle: formatTableUnit,
            formatter: paramsMatter
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
