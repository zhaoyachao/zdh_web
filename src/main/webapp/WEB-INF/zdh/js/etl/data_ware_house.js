
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);
      function deleteMs(id) {
          $.ajax({
              url: server_context+"/data_ware_house_del",
              data: "id=" + id,
              type: "post",
              async:false,
              dataType: "json",
              success: function (data) {
                  console.info("success");
                  if(data.code != "200"){
                      layer.msg(data.msg);
                      return
                  }
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url: server_context+'/data_ware_house_list2'
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              openTabPage(server_context+"/data_ware_house_detail_index.html?id=" + row.id, "表信息:"+row.issue_context)
          },
          // 'click #del': function (e, value, row, index) {
          //     layer.confirm('是否删除数据模型', {
          //         btn: ['确定','取消'] //按钮
          //     }, function(index){
          //         deleteMs(row.id);
          //         layer.close(layer.index);
          //     }, function(){
          //
          //     });
          // },
          'click #apply': function (e, value, row, index) {
              layer.confirm('申请数据', {
                  btn: ['确定','取消'] //按钮
              }, function(index){
                  $.ajax({
                      url: server_context+"/data_apply_add",
                      data: "issue_id=" + row.id,
                      type: "post",
                      async:false,
                      dataType: "json",
                      success: function (data) {
                          console.info(data.msg);
                          layer.msg(data.msg)
                      },
                      error: function (data) {
                          console.info("error: " + data.msg);
                      }

                  });
              }, function(){

              });
          }
      };

      function operateFormatter(value, row, index) {
          return [
              ' <div class="btn-group hidden-xs" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="btn btn-outline btn-sm" title="查看"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
              '                                    </button>',
              // ' <button id="del" name="del" type="button" class="btn btn-outline btn-sm" title="删除">\n' +
              // '                                        <i class="glyphicon glyphicon-trash" aria-hidden="true"></i>\n' +
              // '                                    </button>',
              ' <button id="apply" name="apply" type="button" class="btn btn-outline btn-sm" title="申请">\n' +
              '                                        <i class="glyphicon glyphicon-open" aria-hidden="true"></i>\n' +
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
      url: server_context+"/data_ware_house_list2",
      search: false,
      pagination: false,
      showRefresh: false,
      showToggle: false,
      showColumns: false,
      iconSize: 'outline',
      toolbar: '#exampleTableEventsToolbar',
      icons: {
        refresh: 'glyphicon-repeat',
        toggle: 'glyphicon-list-alt',
        columns: 'glyphicon-list'
      },
          responseHandler:function (res) {
              if(res.code != "200"){
                  layer.msg(res.msg);
                  return ;
              }
              return res.result;
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
            field: 'issue_context',
            title: '发布数据说明',
            sortable:false
        },{
            field: 'data_source_type_input',
            title: '输入数据源类型',
            sortable:false
        }, {
            field: 'data_sources_table_name_input',
            title: '输入数据源表',
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

    var $result = $('#examplebtTableEventsResult');

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
