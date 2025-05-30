
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);



      $('#btn_add').click(function () {

          parent.layer.open({
              type: 2,
              title: '审批人节点配置',
              shadeClose: false,
              shade: 0.8,
              area: ['100%', '100%'],
              content: server_context+'/approval_auditor_flow_add_index?id=-1', //iframe的url
              end:function () {
                  parent.layer.closeAll();
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : server_context+"/approval_auditor_flow_list"
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

              layer.confirm('是否删除记录', {
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
              url : server_context+"/approval_auditor_flow_delete",
              data : "ids=" + ids,
              type : "post",
              dataType : "json",
              success : function(data) {
                  console.info("success");
                  $('#exampleTableEvents').bootstrapTable('refresh', {
                      url : server_context+"/approval_auditor_flow_list"
                  });
              },
              error: function (data) {
                  console.info("error: " + data.responseText);
              }

          });
      }

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {

              parent.layer.open({
                  type: 2,
                  title: '审批人节点更新',
                  shadeClose: false,
                  shade: 0.8,
                  area: ['100%', '100%'],
                  content: server_context+'/approval_auditor_flow_add_index?id='+row.id, //iframe的url
                  end:function () {
                      parent.layer.closeAll();
                      $('#exampleTableEvents').bootstrapTable('refresh', {
                          url : server_context+"/approval_auditor_flow_list"
                      });
                  }
              });
          }
      };

      function operateFormatter(value, row, index) {
          var edit_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var copy_class = "btn btn-outline btn-sm "+ get_edit_class(row);
          var del_class = "btn btn-outline btn-sm "+ get_del_class(row);
          return [
              ' <div class="btn-group" id="exampleTableEventsToolbar" role="group">' +
              ' <button id="edit" name="edit" type="button" class="'+edit_class+'" title="更新"><i class="glyphicon glyphicon-edit" aria-hidden="true"></i>\n' +
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

      var columns = [{
          checkbox: true,
          field:'state',
          sortable:false
      }, {
          field: 'id',
          title: 'ID',
          sortable:true
      }, {
          field: 'flow_context',
          title: '审批流',
          sortable:true
      },{
          field: 'flow_code',
          title: '审批节点',
          sortable:true
      },{
          field: 'product_code',
          title: '产品代码',
          sortable:true
      },{
          field: 'operate',
          title: '操作',
          events: operateEvents,//给按钮注册事件
          width:150,
          formatter: operateFormatter //表格中增加按钮
      }];
      var bootstrapTableConf = getTablePageCommon(server_context+"/approval_auditor_flow_list?tm="+new Date());
      bootstrapTableConf['columns'] = columns;

      $('#exampleTableEvents').bootstrapTable(bootstrapTableConf);

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

    var $result = $('#examplebtTableEventsResult');
  })();
})(document, window, jQuery);
