(function(document, window, $) {

  (function() {
      
      function getUrlParam(name) {
          var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
          var r = window.location.search.substr(1).match(reg);
          if (r != null) return unescape(r[2]);
          return null;
      }

      var id = getUrlParam('id');
      
      if (id != '-1' && id != null) {
          loadConfigData(id);
      }

      function loadConfigData(id) {
          $.ajax({
              url: server_context + "/push_config_detail",
              data: "id=" + id,
              type: "post",
              dataType: "json",
              success: function(data) {
                  if (data.code == '200') {
                      var config = data.result;
                      $('#id').val(config.id);
                      $('#config_key').val(config.config_key);
                      $('#config_name').val(config.config_name);
                      $('#config_type').val(config.config_type);
                      $('#description').val(config.description);
                      
                      // 使用 config_json 字段（map 类型）
                      var configJson = config.config_json || {};
                      
                      // 设置 status
                      var status = configJson.status || 1;
                      if (status == 1) {
                          $('#status_1').prop('checked', true);
                      } else {
                          $('#status_0').prop('checked', true);
                      }
                      
                      // 设置 thread_size
                      $('#thread_size').val(configJson.thread_size || 1);
                      
                      // 设置 schedule_time
                      $('#schedule_time').val(configJson.schedule_time || '');
                  } else {
                      layer.msg("加载配置失败：" + data.msg);
                  }
              },
              error: function(data) {
                  console.info("error: " + data.responseText);
                  layer.msg("加载配置失败");
              }
          });
      }

      function saveConfig() {
          var id = $('#id').val();
          var productCode = $('#product_code').val();
          var configKey = $('#config_key').val();
          var configName = $('#config_name').val();
          var configType = $('#config_type').val();
          var description = $('#description').val();
          
          if (!configKey || configKey.trim() == '') {
              layer.msg("配置Key不能为空");
              return;
          }
          
          if (!configName || configName.trim() == '') {
              layer.msg("配置名称不能为空");
              return;
          }
          
          // 获取 status
          var status = 0;
          if ($('#status_1').is(':checked')) {
              status = 1;
          } else if ($('#status_0').is(':checked')) {
              status = 0;
          } else {
              layer.msg("请选择状态");
              return;
          }
          
          // 获取 thread_size
          var threadSize = $('#thread_size').val();
          if (!threadSize || threadSize.trim() == '') {
              layer.msg("请输入线程数");
              return;
          }
          if (parseInt(threadSize) < 1) {
              layer.msg("线程数必须大于等于1");
              return;
          }
          
          // 获取 schedule_time
          var scheduleTime = $('#schedule_time').val();
          
          // 构建 config_json 对象
          var configJson = {
              "status": status,
              "thread_size": parseInt(threadSize),
              "schedule_time": scheduleTime
          };
          
          var formData = {
              'id': id,
              'product_code': productCode,
              'config_key': configKey,
              'config_name': configName,
              'config_type': configType,
              'config_json': configJson,
              'description': description
          };
          
          $.ajax({
              url: server_context + "/push_config_save",
              data: JSON.stringify(formData),
              type: "post",
              contentType: "application/json;charset=utf-8",
              dataType: "json",
              success: function(data) {
                  if (data.code == '200') {
                      layer.msg("保存成功");
                      setTimeout(function() {
                          var index = parent.layer.getFrameIndex(window.name);
                          parent.layer.close(index);
                      }, 1000);
                  } else {
                      layer.msg("保存失败：" + data.msg);
                  }
              },
              error: function(data) {
                  console.info("error: " + data.responseText);
                  layer.msg("保存失败");
              }
          });
      }

      // 页面加载完成后绑定事件
      $(document).ready(function() {
          // 绑定保存按钮点击事件
          $('#push_config_add_form').on('click', '.btn-primary', function() {
              saveConfig();
          });
      });

  })();
})(document, window, jQuery);
