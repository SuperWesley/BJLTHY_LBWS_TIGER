var beltLidarChart;

$(function () {
    /**
     * 图表
     */
    //初始化 皮带机 激光雷达 图表
	if(beltLidarChart == undefined){
		initBeltLidarChart();
	}
    /**
     * 数据
     */
    //初始化 皮带机 激光雷达 数据
    initBeltLidarDataWebSocket();
})

//████████████████████  初始化 皮带机激光雷达数据 webSocket  ███████████████████████████████████████████████████████████████████
var wsLidarWebSocket = null, socketNameBeltLidarData = "皮带机 激光雷达数据";
function initBeltLidarDataWebSocket() {
    wsLidarWebSocket = new ReconnectingWebSocket(pcaddress + '/ws/1/beltLidarDataWebSocket');
    wsLidarWebSocket.onopen = function () {
        // console.log(socketNameBeltLidarData + " 客户端已连接");
    }
    wsLidarWebSocket.onmessage = function (evt) {
    	if(evt.data.length>100){
//        	console.log(socketNameBeltLidarData + " 客户端接收消息 ", evt.data);
    		UpdateBeltLidarChart(evt.data);
    	}
        
    }
    wsLidarWebSocket.onclose = function () {
    };
    wsLidarWebSocket.onerror = function (evt) {
        // console.log(socketNameBeltLidarData + " 连接出现错误", evt.e);
    };
}

var initval = [];
for (var i = 0; i <= 360; i++) {
    var t = i / 180 * Math.PI;
    initval.push([1600, i]);
}
var option_g2;
//初始化 皮带机 激光雷达 图表
function initBeltLidarChart() {
	beltLidarChart = echarts.init(document.getElementById('beltLidar'));
	var beltlen = belt.length;
//	var beltStartAngle = beltlen / 2 * 0.5 - 76;
	var beltStartAngle = 0;
    option_g2 = {
        title: {
            text: '',
            top: 'bottom',
            left: 'left',
        },
        legend: {
            data: ['line'],
            top: 'bottom',
            left: 'center'
        },
        polar: {
        	center: ['40%', '10%'],
        	radius:'180%',
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'none'
            }
        },
        angleAxis: {
            type: 'value',
            startAngle: beltStartAngle, //开始角度
            clockwise: true, //正序，倒序显示
            axisLine: { //这个是轴线，最外圈的那个东西
                show: false,
            },
            axisTick: { //这个是外圈上刻度线
                show: false,
            },
            axisLabel: { //这个是外层显示标签 数字
                show: false,
            },
            splitLine: { //角度轴在 grid?区域中的分隔线。 垂直分割线
                show: false,
            },
            splitArea: { //角度轴在 grid?区域中的分隔区域。 垂直分割区域
                show: false,
            },
            axisPointer: { //指示显示框，和显示线
                show: false,
            }
        },
        radiusAxis: { //水平方向分割线
            splitLine: {
                show: false,
            },
            axisLine: {
                show: false,
            },
            axisTick: {
                show: false,
            },
            axisLabel: { //这个是外层显示标签 数字
                show: false,
            }
        },
        series: [{
            coordinateSystem: 'polar',
            showSymbol: false,
            sampling: 'average',
            showAllSymbol: false,
            type: 'line',
            data: belt,
            itemStyle: {
                normal: {
                    color: '#1DA7EE'
                }
            }
        },
        {
            coordinateSystem: 'polar',
            showSymbol: false,
            sampling: 'average',
            showAllSymbol: false,
            type: 'line',
            data: belt,
            itemStyle: {
                normal: {
                    color: 'goldenrod'
                }
            }
        },
        {
            coordinateSystem: 'polar',
            showSymbol: false,
            sampling: 'average',
            showAllSymbol: false,
            type: 'line',
            data: initval,
            itemStyle: {
                normal: {
                    color: '#f0f3f600'
                }
            }
        }
        ]
    };
    beltLidarChart.clear();
    beltLidarChart.setOption(option_g2);
    window.addEventListener("resize", function() {
    	beltLidarChart.resize();
	});
}

//更新皮带机雷达数据
function UpdateBeltLidarChart(data) {
	let val = JSON.parse(data);
    beltLidarChart.setOption({
        series: [
        	{
                coordinateSystem: 'polar',
                showSymbol: false,
                sampling: 'average',
                showAllSymbol: false,
                type: 'line',
                data: val,
                itemStyle: {
                    normal: {
                        color: '#1DA7EE'
                    }
                }
            },
            {
                coordinateSystem: 'polar',
                showSymbol: false,
                sampling: 'average',
                showAllSymbol: false,
                type: 'line',
                data: belt,
                itemStyle: {
                    normal: {
                        color: 'goldenrod'
                    }
                }
            }
        ]
    });
}








