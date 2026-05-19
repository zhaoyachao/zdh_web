(function(document, window, $) {
  // ------------------------------
  (function() {

      //初始化调度器id,获取当前系统下的所有调度器id

      $.ajax({
          type: 'POST',
          url: server_context+"/dispatch_executor_list",
          async:false,
          dataType: 'json',
          data: "",
          //成功返回
          success: function (data) {
              if(data.code != "200"){
                  layer.msg(data.msg);
                  return ;
              }
              var str = '<option value="" hassubinfo=\"true\">' + "不指定" + '</option>';
              for (var i = 0; i < data.result.length; i++) {
                  str += '<option value=\"' + data.result[i].instance_name + '\" hassubinfo=\"true\">' + data.result[i].instance_name + '</option>';
              }
              $('#schedule_id').html(str);
              $("#schedule_id").trigger("chosen:updated");
              $('#schedule_id').chosen();
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
