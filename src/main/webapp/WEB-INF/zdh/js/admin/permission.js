
(function(document, window, $) {

  // Example Bootstrap Table Events
  // ------------------------------
  (function() {
      var height=400;
      if($(document.body).height()*0.8>height){
          height=$(document.body).height()*0.8
      }
      $('#exampleTableEvents').attr("data-height",height);

      window.operateEvents = {
          'click #edit': function (e, value, row, index) {
              $("#id").val(row.id);

              openTabPage(server_context+"/permission_add_index.html?id="+ row.id, "用户权限配置")

              // top.layer.open({
              //     type: 2,
              //     title: '用户权限配置',
              //     shadeClose: false,
              //     resize: true,
              //     fixed: false,
              //     maxmin: true,
              //     shade: 0.1,
              //     area : ['45%', '60%'],
              //     //area: ['450px', '500px'],
              //     content: "permission_add_index?id="+row.id, //iframe的url
              //     end:function () {
              //         $('#exampleTableEvents').bootstrapTable('refresh', {
              //             url : 'user_list'
              //         });
              //     }
              // });

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
          url: server_context+"/user_list?"+$("#user_form").serialize(),
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
            sortable:false
        }, {
            field: 'id',
            title: 'ID',
            sortable:false
        }, {
            field: 'userName',
            title: '用户账号',
            sortable:false
        },{
            field: 'email',
            title: '邮箱',
            sortable:false
        }, {
            field: 'phone',
            title: '手机号',
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
