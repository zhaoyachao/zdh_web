/*!
 * Remark (http://getbootstrapadmin.com/remark)
 * Copyright 2015 amazingsurge
 * Licensed under the Themeforest Standard Licenses
 */
function cellStyle(value, row, index) {
  var classes = ['active', 'success', 'info', 'warning', 'danger'];

  if (index % 2 === 0 && index / 2 < classes.length) {
    return {
      classes: classes[index / 2]
    };
  }
  return {};
}

function rowStyle(row, index) {
  var classes = ['active', 'success', 'info', 'warning', 'danger'];

  if (index % 2 === 0 && index / 2 < classes.length) {
    return {
      classes: classes[index / 2]
    };
  }
  return {};
}

function scoreSorter(a, b) {
  if (a > b) return 1;
  if (a < b) return -1;
  return 0;
}

function nameFormatter(value) {
  return value + '<i class="icon wb-book" aria-hidden="true"></i> ';
}

function starsFormatter(value) {
  return '<i class="icon wb-star" aria-hidden="true"></i> ' + value;
}

function queryParams() {
  return {
    type: 'owner',
    sort: 'updated',
    direction: 'desc',
    per_page: 100,
    page: 1
  };
}

function buildTable($el, cells, rows) {
  var i, j, row,
    columns = [],
    data = [];

  for (i = 0; i < cells; i++) {
    columns.push({
      field: '字段' + i,
      title: '单元' + i
    });
  }
  for (i = 0; i < rows; i++) {
    row = {};
    for (j = 0; j < cells; j++) {
      row['字段' + j] = 'Row-' + i + '-' + j;
    }
    data.push(row);
  }
  $el.bootstrapTable('destroy').bootstrapTable({
    columns: columns,
    data: data,
    iconSize: 'outline',
    icons: {
      columns: 'glyphicon-list'
    }
  });
}

(function(document, window, $) {
  'use strict';

  // Example Bootstrap Table Large Columns
  // -------------------------------------
  buildTable($('#exampleTableLargeColumns'), 50, 50);


  // Example Bootstrap Table Events
  // ------------------------------
  (function() {

      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);
      $('#remove').click(function () {

          layer.msg("当前不支持删除");
        var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
        if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
            layer.msg("请先选择要删除的记录!");
            return;
        } else {

            layer.confirm('是否删除下载记录', {
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
              url : server_context+"/notice_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : 'notice_list2'
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }
          });
      }

      $('#is_see').click(function () {

          var rows = $("#exampleTableEvents").bootstrapTable('getSelections');// 获得要删除的数据
          if (rows.length == 0) {// rows 主要是为了判断是否选中，下面的else内容才是主要
              layer.msg("请先选择要删除的记录!");
              return;
          } else {

              var index = layer.confirm('标记为已读', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  var ids = new Array();// 声明一个数组
                  $(rows).each(function() {// 通过获得别选中的来进行遍历
                      ids.push(this.id);// cid为获得到的整条数据中的一列
                  });
                  console.log(ids);
                  $.ajax({
                      url : server_context+"/notice_update_see",
                      data : "ids=" + ids+"&is_see=true",
                      type : "get",
                      dataType : "json",
                      async:false,
                      success : function(data) {
                          console.info("success");
                          $('#exampleTableEvents').bootstrapTable('refresh', {
                              url : 'notice_list2'
                          });
                      },
                      error: function (data) {
                          console.info("error: " + data.responseText);
                      }
                  });
                  layer.close(index);
              }, function(){

              });
          }
      });

      window.operateEvents = {
          'click #detail': function (e, value, row, index) {
              window.open(server_context+"/notice_detail_index?id="+row.id);
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div style="text-align:center">' +
              ' <button id="detail" name="detail" type="button" class="btn btn-outline btn-sm"><i class="glyphicon glyphicon-envelope" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
              '</div>'

          ].join('');

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
      var loadIndex;
    $('#exampleTableEvents').bootstrapTable({
        method: "POST",
        url: server_context+"/notice_list2",
      search: true,
      pagination: true,
      showRefresh: true,
      showToggle: true,
      showColumns: true,
        pageList:[10,50,100,200,500],
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
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
      responseHandler: res => {
             // 关闭加载层
             layer.close(loadIndex);
             return {                            //return bootstrap-table能处理的数据格式
                     "total":res.result.total,
                     "rows": res.result.rows
                     }
      },
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
            sortable:false
        }, {
            field: 'id',
            title: 'ID',
            sortable:true
        }, {
            field: 'msg_type',
            title: '通知类型',
            sortable:true
        }, {
            field: 'msg_title',
            title: '主题',
            sortable:true
        },{
            field: 'owner',
            title: '通知账号',
            sortable:true
        }, {
            field: 'create_time',
            title: '生成时间',
            sortable:true,
            formatter: function (value, row, index) {
                return getMyDate(value);
            }
        },  {
            field: 'is_see',
            title: '是否已读',
            sortable:true,
            formatter: function (value, row, index) {
                var context = "未读";
                var class_str = "btn-danger btn-xs";
                if (value == "true") {
                    context = "已读";
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
            field: 'operate',
            title: '查看',
            events: operateEvents,//给按钮注册事件
            formatter: operateFormatter //表格中增加按钮
        }]
    });

    var $result = $('#examplebtTableEventsResult');
  })();
})(document, window, jQuery);
