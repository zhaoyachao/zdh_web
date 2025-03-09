
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
              title: '权限申请配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/permission_apply_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束");
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/permission_apply_list?"+$("#permission_apply_form").serialize()+"&tm="+new Date(),
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

              layer.confirm('是否删除权限申请', {
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
              url : server_context+"/permission_apply_delete",
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
                      url: server_context+"/permission_apply_list?"+$("#permission_apply_form").serialize(),
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
              //openTabPage(server_context+"/permission_apply_add_index.html?id="+ row.id, "权限申请配置");
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
                  content: server_context+"/permission_apply_add_index.html?id="+ row.id, //iframe的url
                  end : function () {
                      console.info("弹框结束");
                      $('#exampleTableEvents-table').bootstrapTable('destroy');
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url: server_context+"/permission_apply_list?"+$("#permission_apply_form").serialize()+"&tm="+new Date(),
                          contentType: "application/json;charset=utf-8",
                          dataType: "json"
                      });
                  }
              });
          },
          'click #del': function (e, value, row, index) {
              $("#id").val(row.id);
              var ids = new Array();// 声明一个数组
              ids.push(row.id);
              deleteMs(ids)
          }
      };

      function operateFormatter(value, row, index) {
          var edit_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var copy_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var del_class = "btn btn-outline btn-sm "+ get_del_class(row);
          return [
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="'+edit_class+'" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>\n'+
              ' <button id="del" name="del" type="button" class="'+del_class+'" title="删除"><i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              '                                    </button>\n'+
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
          url: server_context+"/permission_apply_list",
          search: true,
          pagination: true,
          showRefresh: true,
          showToggle: true,
          showColumns: true,
          iconSize: 'outline',
          toolbar: '#exampleTableEventsToolbar',
          responseHandler:function (res) {
                if(!Array.isArray(res)){
                    layer.msg(res.msg);
                    return res.result;
                }
                return res;
          },
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
                  field: 'product_code',
                  title: '产品code',
                  sortable:false
              },{
                  field: 'owner',
                  title: '申请人',
                  sortable:false
              },{
                  field: 'apply_type',
                  title: '申请类型',
                  sortable:false
              },{
                  field: 'product_code',
                  title: '申请对象',
                  sortable:false
              },{
                  field: 'reason',
                  title: '申请原因',
                  sortable:false
              },{
              field: 'status_name',
              title: '状态',
              sortable:false,
              formatter: function (value, row, index){
                  var context = value;
                  var class_str = "btn-danger btn-xs";

                  if (row.status == "0") {
                      class_str = "btn-warning  btn-xs"
                  }
                  if (row.status == "1") {
                      class_str = "btn-default  btn-xs"
                  }
                  if (row.status == "2") {
                      class_str = "btn-danger  btn-xs"
                  }
                  if (row.status == "3") {
                      class_str = "btn-primary  btn-xs"
                  }
                  if (row.status == "4") {
                      class_str = "btn-danger  btn-xs"
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
                  title: '操作',
                  events: operateEvents,//给按钮注册事件
                  width:150,
                  formatter: operateFormatter //表格中增加按钮

        }]
    });

    var $result = $('#examplebtTableEventsResult');
  })();
})(document, window, jQuery);
