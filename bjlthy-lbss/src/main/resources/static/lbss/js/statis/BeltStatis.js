$('document').ready(function () {
    init();
})

function init() {
    init_laydate();
    showScraperStatis(null, 'D');
    $(".button_div").find(".btn-primary").eq(0).css("background-color", "#f8ac59");
    $(".button_div").find(".btn-primary").eq(0).css("border-color", "#f8ac59");
}

function init_laydate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#time',
            trigger: 'click', //  触发方式
            done: function (value, date) {

            }
        });
    });
    $("#time").val(dateFormat("YYYY-mm-dd", new Date()));
}

function showScraperStatis(btn, type) {
    if (btn != null) {
        $(btn).parent().find(".btn-primary").each(function () {
            $(this).css("background-color", "#1ab394");
            $(this).css("border-color", "#1ab394");
        })
        $(btn).css("background-color", "#f8ac59");
        $(btn).css("border-color", "#f8ac59");
    }
    let url = "";
    //获取起始时间值
    let time = getInputTime('time');
    let colw = 10;
    if (time == undefined || time == "") {
        alert("请输入起始时间后再进行查询！");
        return 0;
    }
    let startTime = getDate(time);
    //若起始时间为空，设置默认值，默认查询月统计
    if (startTime == undefined || startTime == '') {
        startTime = getCurrentMonthFirstDay(new Date());
    }
    let endTime;
    if (type === 'D') {
        startTime = time;
        colw = 100;
        url = "/mapper/data/sumweightHour/getShiftData?belt_name="+belt_name;
    } else if (type === 'W') {
        startTime = getCurrentWeekFirstDay(startTime);
        endTime = nextWeekFirstDay(startTime);
        colw = 40;
        url = "/mapper/data/sumweightDay/getWeekData?belt_name="+belt_name;
    } else if (type === 'M') {
        startTime = getCurrentMonthFirstDay(startTime);
        endTime = nextMonthFirstDay(startTime);
        colw = 10;
        url = "/mapper/data/sumweightDay/getMonthData?belt_name="+belt_name;
    } else if (type === 'Y') {
        startTime = getCurrentYearFirstDay(startTime);
        endTime = nextYearFirstDay(startTime);
        colw = 5;
        url = "/mapper/data/sumweightDay/getYearData?belt_name="+belt_name;
    } else {
        return 0;
    }
    queryRecords(url, startTime, endTime, type, colw);
}

function queryRecords(url, startTime, endTime, type, colw) {
    $.ajax({
        url: ctx + "" +url,
        type: "post",
        data: {"startTime": startTime, "endTime": endTime, type: type, colw: colw},
        success: function (data) {
            $("#day").html(data.day);
            $("#totalWeight").html(parseFloat(data.totalWeight).toFixed(2));
            $("#totalVolume").html(parseFloat(data.totalVolume).toFixed(2));
            $("#density").html(parseFloat(data.density).toFixed(2));
            $("#gangueRatio").html(parseFloat(data.gangueRatio).toFixed(2));
            $("#height").html(parseFloat(data.height).toFixed(2));
            initEcharts(data, type);
        }
    });
}


function initEcharts(data, type) {
    var myChart = echarts.init(document.getElementById("main"));
    var size;
    var max = data.max;
    var interval = data.interval;
    if (type === 'D') {
        size = "20";
//        max = 30000;
//        interval = 6000;
    } else if (type === 'W') {
        size = "13";
//        max = 50000;
//        interval = 10000;
    } else if (type === 'M') {
        size = "10";
//        max = 50000;
//        interval = 10000;
    } else if (type === 'Y') {
        size = "13";
//        max = 3000000;
//        interval = 600000;
    }
    // 指定图表的配置项和数据
    var colors = ['#0167E8', '#FFDC1C'];
    var option = {
        color: colors,
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
            }
        },
        // 图例
        legend: {
            data: ['过煤重量', '过煤体积'],
            textStyle: {
                fontSize: "15",
                color: 'auto'
            }
        },
        // 工具箱
        toolbox: {
            feature: {
                saveAsImage: {backgroundColor: "#0c1d30"}
            }
        },
        grid: {
            x: 80,
            y: 55,
            x2: 110,
            y2: 45,
            borderWidth: 1
        },

        // x轴
        xAxis: [
            {
                type: 'category',
                data: data.timeList,
                axisLabel: {
                    fontSize: size,
                    color: '#D9E9F1',
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '过煤重量',
                min: 0,
                max: max,
                interval: interval,
                position: 'left',
                axisLine: {
                    lineStyle: {
                        color: colors[0]
                    }
                },
                axisLabel: {
                    formatter: '{value} t',
                    fontgangueRatio: "10",
                    fontWeight: 'bold'
                },
            },
            {
                type: 'value',
                name: '过煤体积',
                min: 0,
                max: max,
                interval: interval,
                position: 'right',
                axisLine: {
                    lineStyle: {
                        color: colors[1]
                    }
                },
                axisLabel: {
                    formatter: '{value} m³',
                    fontWeight: 'bold'
                },
            }
        ],
        // 数据
        series: [{
            name: '过煤重量',
            type: 'bar',
            barWidth: '20%',
            data: data.weightList,
            itemStyle: {
                color: {
                    type: 'linear', // 线性渐变
                    x: 1,
                    y: 1,
                    x2: 0,
                    y2: 0,
                    colorStops: [{
                        offset: 0,
                        color: '#0167E8' // 0%处的颜色为蓝色
                    }, {
                        offset: 1,
                        color: '#13ACE8' // 100%处的颜色为浅蓝
                    }]
                }
            }
        },
            {
                name: '过煤体积',
                type: 'bar',
                yAxisIndex: 1,
                barWidth: '20%',
                data: data.volumeList,
                itemStyle: {
                    color: {
                        type: 'linear', // 线性渐变
                        x: 0,
                        y: 0,
                        x2: 1,
                        y2: 1,
                        colorStops: [{
                            offset: 0,
                            color: '#FFDC1C' // 0%处的颜色为黄色
                        }, {
                            offset: 1,
                            color: '#DB750D' // 100%处的颜色为暗红
                        }]
                    }
                }
            }]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

