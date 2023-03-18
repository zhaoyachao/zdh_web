
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
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/user_group_list2?"+$("#user_group_form").serialize()+"&tm="+new Date(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json"
                  });
              }
          });

      });

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              parent.layer.open({
                  type: 2,
                  title: '权限申请配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/user_group_add_index.html?id="+ row.id, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents-table').bootstrapTable('destroy');
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/user_group_list2?"+$("#user_group_form").serialize()+"&tm="+new Date(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
                      });
                  }
              });
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
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
          url: server_context+"/user_group_list2?"+$("#user_group_form").serialize(),
          dataType: 'json',
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
                sortable:true
            }, {
                field: 'id',
                title: 'ID',
                sortable:true
            }, {
                field: 'group_code',
                title: '用户组code',
                sortable:false
            },{
                field: 'name',
                title: '用户组名',
                sortable:false
            },{
                field: 'operate',
                title: '操作',
                events: operateEvents,//给按钮注册事件
                width:150,
                formatter: operateFormatter //表格中增加按钮
            }]
    });
  })();
})(document, window, jQuery);
