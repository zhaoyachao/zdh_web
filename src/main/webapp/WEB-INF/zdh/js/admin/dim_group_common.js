(function(document, window, $) {
  // ------------------------------
  (function() {

      //初始化维度code
      function init_dim_group(){
          //$('#dim_group').chosen();
          $.ajax({
              type: 'POST',
              url: server_context+"/get_dim_group",
              async:false,
              dataType: 'json',
              data: "",
              //成功返回
              success: function (data) {
                  if(data.code != "200"){
                      layer.msg(data.msg);
                      return ;
                  }
                  var str = '<option value=\"'  + '\" hassubinfo=\"true\">' + '选择归属组' + '</option>';
                  for (var i = 0; i < data.result.length; i++) {
                      str += '<option value=\"' + data.result[i].dim_value_code + '\" hassubinfo=\"true\">' + data.result[i].dim_value_name + '</option>';
                  }
                  console.info(data.result.length);
                  $('#dim_group').html(str);
                  $("#dim_group").trigger("chosen:updated");
                  $('#dim_group').chosen();
              },
              //处理完成
              complete: function () {
              },
              //报错
              error: function (data) {
              }
          });
      }

      init_dim_group();

  })();
})(document, window, jQuery);
