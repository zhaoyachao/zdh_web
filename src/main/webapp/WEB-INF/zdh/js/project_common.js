
function project_scene(id) {
    $.ajax({
        type: 'POST',
        url: server_context+"/project_detail",
        async:false,
        dataType: 'json',
        data: {"id": id},
        //成功返回
        success: function (data) {
            if(data.code != "200"){
                layer.msg(data.msg);
                return ;
            }
            if(data.result == null){
                return ;
            }
            if(data.result.project_json_object.length>0){
                var str = '<option value=\"\" mytype=\"\" hassubinfo=\"true\"></option>';
                for(var i=0;i<data.result.project_json_object.length;i++){
                    str += '<option value=\"' + data.result.project_json_object[i].enum_value + '\" hassubinfo=\"true\">' + data.result.project_json_object[i].enum_value_context + '</option>';
                }
                $('#project_scene').html(str);
                $("#project_scene").trigger("chosen:updated");
                $('#project_scene').chosen();
            }
        },
        //处理完成
        complete: function () {
        },
        //报错
        error: function (data) {
        }
    });
}

(function(document, window, $) {
  // ------------------------------
  (function() {

      //初始化项目code,获取当前用户
      $('#project_code').chosen();
      $('#project_scene').chosen();

      $.ajax({
          type: 'POST',
          url: server_context+"/project_list",
          async:false,
          dataType: 'json',
          data: "",
          //成功返回
          success: function (data) {
              if(data.code != "200"){
                  layer.msg(data.msg);
                  return ;
              }
              var str = '<option value=\"\" mytype=\"\" hassubinfo=\"true\"></option>';
              for (var i = 0; i < data.result.length; i++) {
                  str += '<option value=\"' + data.result[i].project_code + '\" hassubinfo=\"true\" mytype='+data.result[i].id+'>' + data.result[i].project_name + '</option>';
              }
              $('#project_code').html(str);
              $("#project_code").trigger("chosen:updated");
              $('#project_code').chosen().on("change", function (evt, params) {
                  // alert($('#data_sources_choose_input').val())
                  var project_id = $('#project_code').find("option:selected").attr("mytype");
                  // 加载
                  project_scene(project_id);

              });
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
