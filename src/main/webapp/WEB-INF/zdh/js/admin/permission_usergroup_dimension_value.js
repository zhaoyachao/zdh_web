
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
          var product_code = $('#product_code').val();
          var group_code = $('#group_code').val();
          parent.layer.open({
              type: 2,
              title: '用户组绑定维度配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['100%', '100%'],
              //area: ['450px', '500px'],
              content: server_context+"/permission_usergroup_dimension_value_add_index?id=-1&product_code="+product_code+"&group_code="+group_code, //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/permission_usergroup_dimension_list?"+$("#permission_dimension_form").serialize()+"&tm="+new Date(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
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

              layer.confirm('是否删除数据标识', {
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
              url : server_context+"/permission_dimension_delete",
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
                      url: server_context+"/permission_dimension_list?"+$("#permission_dimension_form").serialize(),
                      contentType: "application/json;charset=utf-8",
                      dataType: "json",
                      queryParams: function (params) {
                          // 此处使用了LayUi组件 是为加载层
                          loadIndex = layer.load(1);
                          let resRepor = {
                              //服务端分页所需要的参数
                              limit: params.limit,
                              offset: params.offset
                          };
                          return resRepor;
                      }
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #edit_value': function (e, value, row, index) {
              var product_code = $('#product_code').val();
              var group_code = $('#group_code').val();
              var dim_code = row.dim_code;
              parent.layer.open({
                  type: 2,
                  title: '数据标识配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['100%', '100%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/permission_usergroup_dimension_value_add_index.html?id="+dim_code+"&product_code="+product_code+"&group_code="+group_code+"&dim_code="+dim_code, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents-table').bootstrapTable('destroy');
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/permission_usergroup_dimension_list?"+$("#permission_dimension_form").serialize()+"&tm="+new Date(),
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
              ' <button id="edit_value" name="edit_value" type="button" class="btn btn-outline btn-sm" title="更新维度值"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>\n'+
              '</div>'

          ].join('');

      }

      var columns =  [{
          checkbox: true,
          field:'state',
          sortable:true
      }, {
          field: 'dim_code',
          title: '维度code',
          sortable:false
      },{
          field: 'dim_name',
          title: '维度名称',
          sortable:false
      },{
          field: 'operate',
          title: '操作',
          events: operateEvents,//给按钮注册事件
          width:150,
          formatter: operateFormatter //表格中增加按钮
      }];

      var bootstrapTableConf = getTableCommon("");
      bootstrapTableConf['columns'] = columns;
      $('#exampleTableEvents').bootstrapTable(bootstrapTableConf);
  })();
})(document, window, jQuery);
