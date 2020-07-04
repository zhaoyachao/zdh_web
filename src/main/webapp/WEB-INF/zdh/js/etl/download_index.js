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

      $('#exampleTableEvents').attr("data-height",$(document.body).height()*0.8)
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
              url : "download_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success")
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : 'download_list'
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #download': function (e, value, row, index) {
              window.open("download_file?id="+row.id);
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="download" name="download" type="button" class="btn btn-outline btn-sm"><i class="glyphicon glyphicon-download-alt" aria-hidden="true"></i>\n' +
              '                                    </button>'
               +
              '</div>'

          ].join('');

      }


    $('#exampleTableEvents').bootstrapTable({
      url: "download_list",
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
            sortable:true
        }, {
            field: 'job_context',
            title: '调度说明',
            sortable:true
        }, {
            field: 'file_name',
            title: '文件路径',
            sortable:true
        }, {
            field: 'down_count',
            title: '下载次数',
            sortable:true
        }, {
            field: 'etl_date',
            title: 'ETL日期',
            sortable:true
        }, {
            field: 'create_time',
            title: '生成时间',
            sortable:true
        }, {
            field: 'operate',
            title: '下载',
            events: operateEvents,//给按钮注册事件
            formatter: operateFormatter //表格中增加按钮
        }]
    });

    var $result = $('#examplebtTableEventsResult');
  })();
})(document, window, jQuery);
