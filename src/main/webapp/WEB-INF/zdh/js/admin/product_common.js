(function(document, window, $) {
  // ------------------------------
  (function() {

      //初始化产品code,获取当前用户下-管理的产品(必须是产品的管理员)

      $.ajax({
          type: 'POST',
          url: server_context+"/product_tag_list",
          async:false,
          dataType: 'json',
          data: "",
          //成功返回
          success: function (data) {
              if(data.code != "200"){
                  layer.msg(data.msg);
                  return ;
              }
              var str = '';
              for (var i = 0; i < data.result.length; i++) {
                  str += '<option value=\"' + data.result[i].product_code + '\" hassubinfo=\"true\">' + data.result[i].product_name + '</option>';
              }
              $('#product_code').html(str);
              $("#product_code").trigger("chosen:updated");
              $('#product_code').chosen();
          },
          //处理完成
          complete: function () {
          },
          //报错
          error: function (data) {
          }
      });

  })();
})(document, window, jQuery);
