var weightChart = null;
var volumeChart = null;
var beltWeekChart = null;
var weight = null;
var maxVal = 8000;//圆环极大值
//var addrPort = "ws://172.16.2.110:8090/mapper";
//var addrPort = "ws://4v71729g61.zicp.vip/mapper";
var appAddress = null;
$(function () {
	//初始化IP地址
	initIP();
    //初始化 APP 数据
    initBeltAPPWebSocket();
})
//初始化IP地址
function initIP(){
	$.ajax({
		url : "/lbss/lbss/config/pcconfig/getIP",
		type : "post",
		dataType : "json",
		async:false,  //同步执行
		success : function(data) {
			appAddress = "ws://"+data.MapperIP+"/mapper";
		}
	})
}

//████████████████████  初始化 APP数据 webSocket  ███████████████████████████████████████████████████████████████████
var wsBeltAppWebSocket = null;

function initBeltAPPWebSocket() {
    wsBeltAppWebSocket = new WebSocket(appAddress + '/ws/beltAppWebSocket');
    wsBeltAppWebSocket.onopen = function () {
        // console.log(socketNameBeltLidarData + " 客户端已连接");
    }
    wsBeltAppWebSocket.onmessage = function (evt) {
    	updataData(JSON.parse(evt.data));
        
    }
    wsBeltAppWebSocket.onclose = function () {
        // console.log(socketNameBeltLidarData + " 客户端已断开连接");
    };
    wsBeltAppWebSocket.onerror = function (evt) {
        // console.log(socketNameBeltLidarData + " 连接出现错误", evt.e);
    };
    
}

//初始化界面图表
function initChart(){
	//初始化瞬时过煤重量图表信息
	beltWeight();
//	//初始化瞬时过煤体积图表信息
	beltVolume();
	//初始化周图表信息
	beltWeek();
	
}
//更新数据信息
function updataData(data){
	//皮带信息
	$("#totalweight").text(parseFloat(data.realBelt.totalWeight).toFixed(2)+" t");
	$("#monthWeight").text(parseFloat(data.month_weight).toFixed(2)+" t");
	$("#nigWeight").text(parseFloat(data.realBelt.nig_weight).toFixed(2)+" t");
	$("#morWeight").text(parseFloat(data.realBelt.mor_weight).toFixed(2)+" t");
	$("#aftWeight").text(parseFloat(data.realBelt.aft_weight).toFixed(2)+" t");
	updateChart(data);
}
//更新界面图表
function updateChart(data){
	
//	//更新瞬时过煤重量图表信息
	updateWeight(data);
//	//更新瞬时过煤体积图表信息
	updateVolume(data);
	//更新周图表信息
	updateWeek(data);
}

/**------------------------- 更新图表数据信息  start----------------------------*/

function updateWeight(data){
	var weight = parseFloat(data.realBelt.weight).toFixed(2);
	weightChart.setOption({
        series: [{
        	data :  [{//系列中的数据内容数组
	            value: weight,
	         // 单个扇区的标签配置
                label: {
	                normal: {
	                    // 是显示标签
	                    show: true,
	                    position: 'center',
	                    fontSize: 18,
	                    // 标签内容格式器，支持字符串模板和回调函数两种形式，字符串模板与回调函数返回的字符串均支持用 \n 换行
	                    formatter:'{c}t/h',
	                }
           
        		},
	            itemStyle: {
	                normal: {
	                    color: changecolor(weight)
	                }
	            }
		        }, 
		        {
		            value: maxVal - weight,
		        }
	        ]
        }]
    })
}

function updateVolume(data){
    var volume = parseFloat(data.realBelt.volume).toFixed(2);
	volumeChart.setOption({
        series: [{
        	data :  [{//系列中的数据内容数组
	            value: volume,
	         // 单个扇区的标签配置
                label: {
	                normal: {
	                    // 是显示标签
	                    show: true,
	                    position: 'center',
	                    fontSize: 18,
	                    // 标签内容格式器，支持字符串模板和回调函数两种形式，字符串模板与回调函数返回的字符串均支持用 \n 换行
	                    formatter:'{c}m³/h',
	                }
           
        		},
	            itemStyle: {
	                normal: {
	                    color: changecolor(volume)
	                }
	            }
		        }, 
		        {
		            value: maxVal - volume,
		        }
	        ]
        }]
    })
}


function updateWeek(data){
	
	beltWeekChart.setOption({
		yAxis : [{
			data : data.weekTime
		}],
		series : [{
			data : data.weights
		}]
    })
	
}

/**------------------------- 更新图表数据信息  end----------------------------*/
//根据数值在定义区间valObj中范围改变，显示colorObj中对应颜色
function changecolor(percent){
	// 指定图表的配置项和数据
    var valObj = {min: 3000, max: 5000};//自定义区间值
    var colorObj = {min: '#4fd74d', safe: '#eea736', max: '#f91414'};//颜色对应以上区间值定义
    var color;
    if(percent < valObj.min){
        color = colorObj.min;
    } else if (percent >= valObj.min && percent <= valObj.max){
        color = colorObj.safe;
    } else {
        color = colorObj.max;
    }
    return color;
} 
