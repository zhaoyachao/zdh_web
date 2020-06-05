$(function () {

    // var barChart = echarts.init(document.getElementById("echarts-bar-chart"));
    var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));

    function requestEcharts(){
        var barChart = echarts.init(document.getElementById("echarts-bar-chart"));
        $.get('/etlEcharts', function(res) {

            var etl_date_datas = new Array();// 声明一个数组
            var running_datas = new Array();// 声明一个数组
            var error_datas = new Array();// 声明一个数组
            var finish_datas = new Array();// 声明一个数组
            for (i = 0; i < res.length; i++) {
                etl_date_datas.push(res[i].etl_date)
                running_datas.push(res[i].running)
                error_datas.push(res[i].error)
                finish_datas.push(res[i].finish)
            }

            var baroption = {
                title : {
                    text: '调度任务总计'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['正在执行','已完成','失败']
                },
                grid:{
                    x:30,
                    x2:40,
                    y2:24
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : etl_date_datas
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'正在执行',
                        type:'bar',
                        data:running_datas
                    },
                    {
                        name:'已完成',
                        type:'bar',
                        data:finish_datas
                    },
                    {
                        name:'失败',
                        type:'bar',
                        data:error_datas
                    }
                ]
            };
            barChart.setOption(baroption,true);
           // $(window).resize(barChart.resize);
           // window.onresize = barChart.resize;
        })


    }

    function requestCurrentEcharts() {

        $.get('/etlEchartsCurrent', function(res) {

            var etl_date_datas = new Array();// 声明一个数组
            var running_datas = new Array();// 声明一个数组
            var error_datas = new Array();// 声明一个数组
            var finish_datas = new Array();// 声明一个数组
            for (i = 0; i < res.length; i++) {
                etl_date_datas.push(res[i].etl_date)
                running_datas.push(res[i].running)
                error_datas.push(res[i].error)
                finish_datas.push(res[i].finish)
            }

            var pieoption = {
                title : {
                    text: '任务状态分配',
                    subtext: '调度任务',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'left',
                    data:['正在执行','执行成功','失败']
                },
                calculable : true,
                series : [
                    {
                        name:'任务状态',
                        type:'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:running_datas, name:'正在执行'},
                            {value:finish_datas, name:'执行成功'},
                            {value:error_datas, name:'失败'}
                        ]
                    }
                ]
            };
            pieChart.setOption(pieoption);
            $(window).resize(pieChart.resize);

        })
    }

    requestEcharts()
    // setInterval(function () {
    //     requestEcharts()
    //
    // },10000)


});
