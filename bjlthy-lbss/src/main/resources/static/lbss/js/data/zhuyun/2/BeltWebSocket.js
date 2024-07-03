var beltSpeedChart = null;
var beltWeightChart = null;
var beltVolumeChart = null;
var beltShiftChart = null;
var beltWeekChart = null;
var beltMonthChart = null;
var pcaddress = null;
var belt_name_en = $("#belt_name_en").val();
var belt_name = $("#belt_name").val();
var belt = null;
$(function () {
	
	//初始化IP地址
	initIP();

    //初始化 APP 数据
    // initBeltAPPWebSocket();
	initDatagrid();
	//定时更新数据信息
	setInterval(function() {
		initDatagrid();
	}, 3000)
})

//初始化IP地址
function initIP(){
	$.ajax({
		url : ctx + "lbss/config/ipconfig/getIP",
		type : "post",
		dataType : "json",
		async:false,  //同步执行
		success : function(data) {
			pcaddress = "ws://"+data.IPv4+"/mapper";
			// belt_name = data.belt_name;
			serverIP = data.endpointUrl;
			belt = [[1088.0,60.5],[1074.0,61.0],[1083.0,61.5],[1090.0,62.0],[1089.0,62.5],[1091.0,63.0],[1100.0,63.5],[1106.0,64.0],[1105.0,64.5],[1105.0,65.0],[1110.0,65.5],[1107.0,66.0],[1120.0,66.5],[1119.0,67.0],[1117.0,67.5],[1128.0,68.0],[1132.0,68.5],[1125.0,69.0],[1120.0,69.5],[1139.0,70.0],[1137.0,70.5],[1139.0,71.0],[1140.0,71.5],[1136.0,72.0],[1154.0,72.5],[1145.0,73.0],[1155.0,73.5],[1155.0,74.0],[1177.0,74.5],[1173.0,75.0],[1166.0,75.5],[1171.0,76.0],[1185.0,76.5],[1174.0,77.0],[1176.0,77.5],[1165.0,78.0],[1167.0,78.5],[1161.0,79.0],[1164.0,79.5],[1167.0,80.0],[1164.0,80.5],[1167.0,81.0],[1171.0,81.5],[1162.0,82.0],[1166.0,82.5],[1164.0,83.0],[1163.0,83.5],[1160.0,84.0],[1163.0,84.5],[1172.0,85.0],[1156.0,85.5],[1170.0,86.0],[1185.0,86.5],[1168.0,87.0],[1163.0,87.5],[1168.0,88.0],[1170.0,88.5],[1159.0,89.0],[1171.0,89.5],[1166.0,90.0],[1184.0,90.5],[1185.0,91.0],[1176.0,91.5],[1172.0,92.0],[1165.0,92.5],[1156.0,93.0],[1147.0,93.5],[1161.0,94.0],[1164.0,94.5],[1155.0,95.0],[1150.0,95.5],[1150.0,96.0],[1147.0,96.5],[1143.0,97.0],[1133.0,97.5],[1136.0,98.0],[1122.0,98.5],[1119.0,99.0],[1123.0,99.5],[1121.0,100.0],[1125.0,100.5],[1137.0,101.0],[1118.0,101.5],[1115.0,102.0],[1110.0,102.5],[1111.0,103.0],[1117.0,103.5],[1111.0,104.0],[1107.0,104.5],[1098.0,105.0],[1084.0,105.5],[1101.0,106.0],[1089.0,106.5],[1084.0,107.0]];
		}
	})
	
}

//████████████████████  初始化 APP数据 webSocket  ███████████████████████████████████████████████████████████████████
var wsBelteWebSocket = null;

function initBeltAPPWebSocket() {

	wsBelteWebSocket = new ReconnectingWebSocket(pcaddress + '/ws/zy/beltWebSocket/'+belt_name_en);
	wsBelteWebSocket.onopen = function () {
	}
	wsBelteWebSocket.onmessage = function (evt) {
    	updataData(JSON.parse(evt.data));
    	checkPageChange();
    }
	wsBelteWebSocket.onclose = function () {
//    	onLineCheck();
        // console.log(socketNameBeltLidarData + " 客户端已断开连接");
    };
    wsBelteWebSocket.onerror = function (evt) {
        // console.log(socketNameBeltLidarData + " 连接出现错误", evt.e);
    };
    window.onbeforeunload = function () {
		closeWebsocket();
	};
}

//关闭websocket
function closeWebsocket() {
    //3代表已经关闭
    if (3 != wsBelteWebSocket.readyState) {
		wsBelteWebSocket.close();
    } else {
        alert("websocket之前已经关闭");
    }
}
var flag = 0;
function checkPageChange() {
	document.addEventListener('visibilitychange',function(){
		if(document.visibilityState=='hidden'){
			//切离该页面时执行
			flag = 1;
		}else {
			//切换到该页面时执行
			if(flag != 0){
				flag = 0;
				window.location.reload();
			}
		}
	});
}

var prefix = ctx + "mapper/weight";
function initDatagrid(){
	$.ajax({
		url : prefix + "/getDetail",
		type : "post",
		data : {"belt_name":belt_name},
		dataType : "json",
		success : function(result) {
			//皮带信息
			updataData(result);
			checkPageChange();
		}
	})
}

//更新数据信息
function updataData(data){
	//皮带信息
	$("#totalweight").text("当日过煤重量： "+parseFloat(data.realBelt.totalWeight).toFixed(2)+" t");
	$("#totalvolume").text("当日过煤体积： "+parseFloat(data.realBelt.totalVolume).toFixed(2)+" m³");
	$("#hourWeight").text("整时累计过煤重量： "+parseFloat(data.realBelt.hourWeight).toFixed(2)+" t");
	$("#hourVolume").text("整时累计过煤体积： "+parseFloat(data.realBelt.hourVolume).toFixed(2)+" m³");
	$("#gangueRatio").text("含矸率： "+parseFloat(data.realBelt.gangueRatio*100).toFixed(2)+" %");
	$("#hunmei").text("混煤密度： "+parseFloat(data.realBelt.density).toFixed(2)+" t/m³");
	$("#density").text("松散密度： "+parseFloat(data.realBelt.density/1.33).toFixed(2)+" t/m³");
	if (data.zeroInfo != null){
		$("#zeroResult").html(data.zeroInfo.remark);
		$("#zeroResult").css("color","#13f408");
		$("#zeroTime").html(data.zeroInfo.time);
	}else{
		$("#zeroResult").html("");
		$("#zeroTime").html("");
	}

	$("#hourWeight_warning").text(data.hourWeight_warning);
	updateChart(data);
}
//更新界面图表

function updateChart(data){
	
	
	//更新皮带速度图表信息
	updateSpeed(data); 
	//更新瞬时过煤重量图表信息
	updateWeight(data);
	//更新瞬时过煤体积图表信息
	updateVolume(data);
	//更新班次图表信息
	updateShift(data);
	//更新周图表信息
	updateWeek(data);
	//更新月图表信息
	updateMonth(data);
}

/**------------------------- 更新图表数据信息  start----------------------------*/
function updateSpeed(data){
	beltSpeedChart.setOption({
        series: [{
        	data : [data.speed]
        }]
    });
}

function updateWeight(data){
	beltWeightChart.setOption({
		xAxis : [{
			data : data.timeList
		}],
        series: [{
        	data : data.weightList
        }]
    })
	
}

function updateVolume(data){
	beltVolumeChart.setOption({
		xAxis : [{
			data : data.timeList
		}],
        series: [{
        	data : data.volumeList
        }]
    })
}

function updateShift(data){
	beltShiftChart.setOption({
		yAxis:[{
			data:data.shiftNames
		}],
        series: [{
        	data : data.shiftList
        }]
    })
}

function updateWeek(data){
	beltWeekChart.setOption({
		yAxis : [{
			data : data.weekTime
		}],
		series : [{
			data : data.weekWeights
		}]
    })
	
}

function updateMonth(data){
	beltMonthChart.setOption({
		yAxis : [{
			data : [data.month+" 月"]
		}],
		series : [{
			data : [data.month_weight]
		}]
    })
	
}

/**------------------------- 更新图表数据信息  end----------------------------*/
function checkZeroInfo(){
	var url = "/mapper/zero/info";
	window.open(url);
}

function queryDetailInfo(){
	var name = $("#detail").val();
	if(name == ""){
		return;
	}
	var url = getUrlBySelectName(name);
	window.open(url);
}
function getUrlBySelectName(selectName) {
	var belt_name = $("#belt_name").val();
	var url = "";
	switch (selectName) {
		case "实时":
			url = "/mapper/front/BeltRealtime?belt_name="+belt_name;
			break;
		case "皮带统计":
			url = "/mapper/front/BeltStatis?belt_name="+belt_name;
			break;
		case "皮带详细":
			url = "/mapper/front/BeltDetail?belt_name="+belt_name;
			break;
	}
	return url;
}

function pageSwitch(){
	var name = $("#manage").val();
	if(name == ""){
		return;
	}
	var url = "";
	switch (name) {
		case "汇总":
			url = "http:"+serverIP+":8090/mapper/frontMain";
			break;
		case "后台":
			url = "/mapper/login";
			break;
		case "关闭":
			window.opener=null;
			window.open('','_self');
			window.close();
			return;
	}
	window.open(url);
}