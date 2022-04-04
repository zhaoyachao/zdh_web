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

      $('#add').click(function () {
          parent.layer.open({
              type: 2,
              title: '规则配置',
              shadeClose: false,
              resize: true,
              fixed: false,
              maxmin: true,
              shade: 0.1,
              area : ['45%', '60%'],
              //area: ['450px', '500px'],
              content: server_context+"/quality_rule_add_index?id=-1", //iframe的url
              end : function () {
                  console.info("弹框结束")
                  $('#exampleTableEvents-table').bootstrapTable('destroy');
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+"/quality_rule_list?"+$("#quality_rule_form").serialize()+"&tm="+new Date(),
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
              layer.msg("当前不支持删除功能");
              // layer.confirm('是否删除SSH任务', {
              //     btn: ['确定','取消'] //按钮
              // }, function(index){
              //     var ids = new Array();// 声明一个数组
              //     $(rows).each(function() {// 通过获得别选中的来进行遍历
              //         ids.push(this.id);// cid为获得到的整条数据中的一列
              //     });
              //     console.log(ids)
              //     deleteMs(ids)
              //     layer.close(layer.index);
              // }, function(){
              //
              // });
          }

      });

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

      function getRuleTypeName(rule_type){
          if(rule_type == "1"){
              return "SQL表达式";
          }
          if(rule_type == "2"){
              return "正则表达式";
          }
          if(rule_type == "3"){
              return "ZDH内置";
          }
          return rule_type;
      }
      window.operateEvents = {

          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);
              parent.layer.open({
                  type: 2,
                  title: '规则配置',
                  shadeClose: false,
                  resize: true,
                  fixed: false,
                  maxmin: true,
                  shade: 0.1,
                  area : ['45%', '60%'],
                  //area: ['450px', '500px'],
                  content: server_context+"/quality_rule_add_index?id="+row.id, //iframe的url
                  end:function () {
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+'/quality_rule_list'
                      });
                  }
              });

          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div style="text-align:center" >' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>' +
              '</div>'
          ].join('');
      }

    $('#exampleTableEvents').bootstrapTable({
      url: "",
      search: true,
      pagination: true,
      showRefresh: true,
      showToggle: true,
      showColumns: true,
      iconSize: 'outline',
      responseHandler:function (res) {
          if(!Array.isArray(res)){
              if(res.code == "201"){
                  layer.msg(res.msg);
              }else{
                  layer.msg("未返回有效数据");
              }
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
            field: 'rule_code',
            title: '规则code',
            sortable:true
        }, {
            field: 'rule_name',
            title: '规则名称',
            sortable:true
        }, {
            field: 'rule_type',
            title: '规则类型(正则,sql表达式)',
            sortable:true,
            formatter: function (value, row, index) {
                return getRuleTypeName(value);
            }
        }, {
            field: 'rule_expr',
            title: '规则内容',
            sortable:true
        }, {
            field: 'create_time',
            title: '生成时间',
            sortable:true,
           formatter: function (value, row, index) {
           return getMyDate(value);
           }
        }, {
            field: 'operate',
            title: '常用操作按钮事件',
            events: operateEvents,//给按钮注册事件
            width:150,
            formatter: operateFormatter //表格中增加按钮
        }]
    });

  })();
})(document, window, jQuery);
