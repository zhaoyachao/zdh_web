(function(document, window, $) {
  // ------------------------------
  (function() {

      //初始化产品code根据权限获取用户绑定的维度-dim_product
      function init_dim_product(){
          //$('#product_code').chosen();
          $.ajax({
              type: 'POST',
              url: server_context+"/get_dim_product",
              async:false,
              dataType: 'json',
              data: "",
              //成功返回
              success: function (data) {
                  if(data.code != "200"){
                      layer.msg(data.msg);
                      return ;
                  }
                  var str = '<option value=\"'  + '\" hassubinfo=\"true\">' + '选择归属产品' + '</option>';
                  for (var i = 0; i < data.result.length; i++) {
                      str += '<option value=\"' + data.result[i].dim_value_code + '\" hassubinfo=\"true\">' + data.result[i].dim_value_name + '</option>';
                  }
                  $('#product_code').html(str);
                  $("#product_code").trigger("chosen:updated");
                  $('#product_code').chosen().on("change", function (evt, params) {
                      product_callback();
                  });
              },
              //处理完成
              complete: function () {
              },
              //报错
              error: function (data) {
              }
          });
      }

      init_dim_product();

  })();
})(document, window, jQuery);
