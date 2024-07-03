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

    //初始化 WEB 数据
	initDatagrid();
    // initBeltAPPWebSocket();
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
			belt = [[1361.0,60.5],[1362.0,61.0],[1365.0,61.5],[1367.0,62.0],[1357.0,62.5],[1354.0,63.0],[1367.0,63.5],[1367.0,64.0],[1363.0,64.5],[1370.0,65.0],[1375.0,65.5],[1372.0,66.0],[1386.0,66.5],[1381.0,67.0],[1385.0,67.5],[1390.0,68.0],[1396.0,68.5],[1404.0,69.0],[1405.0,69.5],[1419.0,70.0],[1400.0,70.5],[1409.0,71.0],[1423.0,71.5],[1427.0,72.0],[1424.0,72.5],[1419.0,73.0],[1434.0,73.5],[1435.0,74.0],[1430.0,74.5],[1446.0,75.0],[1452.0,75.5],[1456.0,76.0],[1463.0,76.5],[1451.0,77.0],[1442.0,77.5],[1465.0,78.0],[1461.0,78.5],[1448.0,79.0],[1454.0,79.5],[1458.0,80.0],[1466.0,80.5],[1468.0,81.0],[1461.0,81.5],[1465.0,82.0],[1470.0,82.5],[1470.0,83.0],[1474.0,83.5],[1470.0,84.0],[1469.0,84.5],[1471.0,85.0],[1459.0,85.5],[1465.0,86.0],[1465.0,86.5],[1458.0,87.0],[1460.0,87.5],[1462.0,88.0],[1456.0,88.5],[1467.0,89.0],[1471.0,89.5],[1466.0,90.0],[1458.0,90.5],[1476.0,91.0],[1466.0,91.5],[1460.0,92.0],[1473.0,92.5],[1461.0,93.0],[1468.0,93.5],[1466.0,94.0],[1463.0,94.5],[1459.0,95.0],[1468.0,95.5],[1470.0,96.0],[1458.0,96.5],[1455.0,97.0],[1458.0,97.5],[1462.0,98.0],[1450.0,98.5],[1460.0,99.0],[1444.0,99.5],[1446.0,100.0],[1444.0,100.5],[1448.0,101.0],[1443.0,101.5],[1439.0,102.0],[1434.0,102.5],[1424.0,103.0],[1417.0,103.5],[1420.0,104.0],[1415.0,104.5],[1417.0,105.0],[1414.0,105.5],[1400.0,106.0],[1403.0,106.5],[1401.0,107.0],[1401.0,107.5],[1389.0,108.0],[1392.0,108.5],[1386.0,109.0],[1398.0,109.5],[1390.0,110.0],[1386.0,110.5],[1383.0,111.0],[1373.0,111.5],[1378.0,112.0],[1367.0,112.5],[1370.0,113.0],[1373.0,113.5],[1381.0,114.0],[1383.0,114.5],[1387.0,115.0],[1387.1,115.5]];
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
    if (3 != websocket.readyState) {
        websocket.close();
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