
function strategy_base_param(s) {

    if($("#rule_id").length){
        if($("#rule_id").is("select")){
            $("#rule_id").val(s["rule_id"]);
            $("#rule_id").trigger("chosen:updated");
        }else{
            $("#rule_id").val(s["rule_id"]);
        }
    }

    if($("#rule_context").length){
        $('#rule_context').val(is_empty(s["rule_context"])?"":s["rule_context"]);
    }

    if($("#is_base").length){
        $("#is_base").val(s.is_base);
        $("#is_base").trigger("chosen:updated");
    }

    $("#operate").val(s["operate"]);
    $("#operate").trigger("chosen:updated");

    $("#data_status").val(s["data_status"]);
    $("#data_status").trigger("chosen:updated");

    $("#depend_level").val(s["depend_level"]);
    $("#depend_level").trigger("chosen:updated");
    if (!s["time_out"] && typeof(s["time_out"])!="undefined"){
        $("#time_out").val(s["time_out"]);
    }

    $("#touch_type").val(s.touch_type);
    $("#touch_type").trigger("chosen:updated");

    var is_disenable=s['is_disenable'];
    if(is_disenable=="true"){
        $('#is_disenable').iCheck('check');
    }

    $("#project_code").val(s["project_code"]);
    $("#project_code").trigger("chosen:updated");
    var project_id = $('#project_code').find("option:selected").attr("mytype");
    project_scene(project_id);

    $("#project_scene").val(s["project_scene"]);
    $("#project_scene").trigger("chosen:updated");

    $('#version_tag').val(s.version_tag);

    $("#is_async").val(s.is_async);
    $("#is_async").trigger("chosen:updated");

    $("#plan_retry_count").val(s.plan_retry_count);

}

function get_strategy_base_param(more_task, rule_id, rule_context){
    var operate=$('#operate').find("option:selected").val();
    var depend_level=$('#depend_level').find("option:selected").val();
    var touch_type=$('#touch_type').find("option:selected").val();
    var time_out=$('#time_out').val();
    var is_base=$('#is_base').find("option:selected").val();
    var data_status=$('#data_status').find("option:selected").val();
    var project_code=$('#project_code').find("option:selected").val();
    var project_scene=$('#project_scene').find("option:selected").val();
    var version_tag=$('#version_tag').val();
    var is_async=$('#is_async').find("option:selected").val();
    var plan_retry_count=$('#plan_retry_count').val();

    var is_disenable = "false";
    if($('#is_disenable').is(':checked')){
        is_disenable="true"
    }

    const  data={
        "more_task": more_task,
        "operate": operate,
        "touch_type": touch_type,
        "is_disenable":is_disenable,
        "depend_level":depend_level,
        "time_out": time_out,
        "is_base": is_base,
        "data_status": data_status,
        "project_code": project_code,
        "project_scene": project_scene,
        "version_tag": is_empty(version_tag)?"":version_tag,
        "is_async": is_empty(is_async)?"false":is_async,
        "plan_retry_count": is_empty(plan_retry_count)?"0":plan_retry_count,

        "rule_id": rule_id,
        "rule_context":is_empty(rule_context)?"":rule_context,
    };

    return data;
}

(function(document, window, $) {
  // ------------------------------
  (function() {



  })();
})(document, window, jQuery);
