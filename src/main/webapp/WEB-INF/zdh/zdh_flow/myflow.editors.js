(function($){
var myflow = $.myflow;

$.extend(true, myflow.editors, {

    inputEditorId : function(idName){
        var _props,_k,_div,_src,_r;
        this.init = function(props, k, div, src, r){
            _props=props; _k=k; _div=div; _src=src; _r=r;

            $('<input id='+idName+' style="width:100%;"/>').val(props[_k].value).change(function(){
                props[_k].value = $(this).val();
            }).appendTo('#'+_div);

            $('#'+_div).data('editor', this);
        }
        this.destroy = function(){
            $('#'+_div+' input').each(function(){
                _props[_k].value = $(this).val();
            });
        }
    },

	inputEditor : function(){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;
			
			$('<input style="width:100%;"/>').val(props[_k].value).change(function(){
				props[_k].value = $(this).val();
			}).appendTo('#'+_div);
			
			$('#'+_div).data('editor', this);
		}
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		}
	},
    inputEditor2 : function(){
        var _props,_k,_div,_src,_r;
        this.init = function(props, k, div, src, r){
            _props=props; _k=k; _div=div; _src=src; _r=r;

            $('<input id="dataSourcesType" style="width:100%;" readonly />').val(props[_k].value).change(function(){
                props[_k].value = $(this).val();
            }).appendTo('#'+_div);

            $('#'+_div).data('editor', this);
        }
        this.destroy = function(){
            $('#'+_div+' input').each(function(){
                _props[_k].value = $(this).val();
            });
        }
    },

    inputEditor3 : function(){
        var _props,_k,_div,_src,_r;
        this.init = function(props, k, div, src, r){
            _props=props; _k=k; _div=div; _src=src; _r=r;

            $('<input id="columns" style="width:100%;"/>').val(props[_k].value).change(function(){
                props[_k].value = $(this).val();
            }).appendTo('#'+_div);

            $('#'+_div).data('editor', this);
        }
        this.destroy = function(){
            $('#'+_div+' input').each(function(){
                _props[_k].value = $(this).val();
            });
        }
    },

	selectEditor : function(arg){
		var _props,_k,_div,_src,_r;
		this.init = function(props, k, div, src, r){
			_props=props; _k=k; _div=div; _src=src; _r=r;

            $('<input id="dataId" type="hidden" style="width:100%;" readonly />\n').appendTo('#'+_div);
            var sle=$('<input id="mySelect" list="dataSelect" style="width:100%;" dataId=""/>\n'+
            '<datalist id="dataSelect">\n' +
            '  <option value="">\n' +
            '</datalist>'
            ).val(props[_k].value).change(function(){
                props[_k].value = $(this).val();
                var dataId=$("#dataSelect option[value=" + this.value + "]").attr("dataId");
                $("#dataId").val(dataId)
                var dataSourcesType=$("#dataSelect option[value=" + this.value + "]").attr("dataSourcesType");
                $("#dataSourcesType").val(dataSourcesType)

				//数据源改变
                    $.ajax({
                        type: "GET",
                        dataType : "json",
						data:"id="+dataId,
                        url: "etl_task_tables",
                        success: function(data){
                            var opts = eval(data);
                            if(opts && opts.length){
                                for(var idx=0; idx<opts.length; idx++){
                                    $("#tableSelect").append('<option value="'+opts[idx]+'">'+opts[idx]+'</option>');
                                }
                                //sle2.val(_props[_k].value);
                                // sle.attr("id",_props[_k].id);
                            }
                        }
                    });
            }).appendTo('#'+_div);

				$.ajax({
				   type: "GET",
                   dataType : "json",
				   url: arg,
				   success: function(data){
					  var opts = eval(data);
					 if(opts && opts.length){
						for(var idx=0; idx<opts.length; idx++){
							sle.append('<option value="'+opts[idx].data_source_context+'"dataId="'+opts[idx].id+'"dataSourcesType="'+opts[idx].data_source_type+'">'+opts[idx].data_source_context+'</option>');
						}
						sle.val(_props[_k].value);
                         var dataId=$("#dataSelect option[value=" + _props[_k].value + "]").attr("dataId");
                         $("#dataId").val(dataId)

					 }
				   }
				});

			$('#'+_div).data('editor', this);
		};
		this.destroy = function(){
			$('#'+_div+' input').each(function(){
				_props[_k].value = $(this).val();
			});
		};
	},
    selectEditor2 : function(arg){
        var _props,_k,_div,_src,_r;
        this.init = function(props, k, div, src, r){
            _props=props; _k=k; _div=div; _src=src; _r=r;

                $('<input id="tableId" type="hidden" style="width:100%;" readonly />\n').appendTo('#'+_div);
                var sle2=$('<input id="mySelect2" list="tableSelect" style="width:100%;"/>\n'+
                    '<datalist id="tableSelect">\n' +
                    '  <option value="">\n' +
                    '</datalist>'
                ).val(props[_k].value).on("mousedown",function () {
                    //alert("dataId:"+$("#dataId").val())
                    var dataId=$("#dataId").val()
                     if(dataId != '' && $('#dataSourcesType')=='JDBC'){
                         $.ajax({
                             type: "GET",
                             dataType : "json",
                             data:"id="+dataId,
                             url: "etl_task_tables",
                             success: function(data){
                                 var opts = eval(data);
                                 if(opts && opts.length){
                                     $("#tableSelect").html("")
                                     $("#tableSelect").append('<option value=""></option>');
                                     for(var idx=0; idx<opts.length; idx++){
                                         $("#tableSelect").append('<option value="'+opts[idx]+'">'+opts[idx]+'</option>');
                                     }
                                     //sle2.val(_props[_k].value);
                                     // sle.attr("id",_props[_k].id);
                                 }
                             }
                         });
					 }
                }).on("change",function () {
                    props[_k].value = $(this).val();
                    //查询对应的表字段
                    var dataId=$("#dataId").val()

                    $.ajax({
                        type: "GET",
                        dataType : "json",
                        data:"id="+dataId+"&table_name="+$(this).val(),
                        url: "etl_task_schema",
                        success: function(data){
                            var opts = eval(data);
                            $('#columns').val(opts);
                            if(opts && opts.length){
                                // $("#tableSelect").html("")
                                // $("#tableSelect").append('<option value=""></option>');
                                // for(var idx=0; idx<opts.length; idx++){
                                //     $("#tableSelect").append('<option value="'+opts[idx]+'">'+opts[idx]+'</option>');
                                // }
                                //sle2.val(_props[_k].value);
                                // sle.attr("id",_props[_k].id);
                            }
                        }
                    });


                }).appendTo('#'+_div);



            $('#'+_div).data('editor', this);
        };
        this.destroy = function(){
            $('#'+_div+' input').each(function(){
                _props[_k].value = $(this).val();
            });
        };
    }
});

})(jQuery);