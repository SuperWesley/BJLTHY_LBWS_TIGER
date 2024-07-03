var beltLidarChart = null;
var scraperLidarChart = null;
var greenLight = '-webkit-gradient(linear, left top, left bottom, from(#00FF00), to(#00FF00))';
var redLight = '-webkit-gradient(linear, left top, left bottom, from(#FF0000), to(#FF0000))';
var yellowLight = '-webkit-gradient(linear, left top, left bottom, from(#dfe70a), to(#dfe70a))';
$(function () {
    /**
     * 状态
     */
    //初始化 激光雷达 状态
    initLidarStateWebSocket();
    //初始化 皮带机 状态
    initBeltStateWebSocket();
})

//████████████████████  初始化 皮带机状态 webSocket  ███████████████████████████████████████████████████████████████████
var wsBeltStateWebSocket = null, socketNameBeltState = "皮带机状态";
let beltHeartCheck;
function initBeltStateWebSocket() {
    wsBeltStateWebSocket = new ReconnectingWebSocket(pcaddress + '/ws/zy/beltStateWebSocket/'+belt_name_en);
    wsBeltStateWebSocket.onopen = function () {
        // console.log(socketNameBeltState + " 客户端已连接");
        beltHeartCheck = setInterval(function() {
            wsBeltStateWebSocket.send('ping');
        }, 3000);
    }
    wsBeltStateWebSocket.onmessage = function (evt) {
        // console.log(socketNameBeltState + " 客户端接收消息 ", evt.data);
        var obj = JSON.parse(evt.data);
        if (obj["belt_name"]==belt_name && obj["code"] != 1000 ){
            clearInterval(beltHeartCheck);
        }
    	changeBeltState(obj);
    }
    wsBeltStateWebSocket.onclose = function () {
    	 
    	 wsBeltStateWebSocket.close();
        clearInterval(beltHeartCheck);
    	
    };
    wsBeltStateWebSocket.onerror = function (evt) {
        // console.log(socketNameBeltState + " 连接出现错误", evt.e);
    	// wsBeltStateWebSocket.close();
    };
}

function changeBeltState(obj) {
	var name = obj["belt_name"];
    if(name == belt_name){
    	if (obj["msg"] != "" && obj["msg"] != "正常"){
    		$(".breathe-belt").eq(1).css('background-image', redLight);
    		$(".belt_yuan:eq(1) span").text(obj["msg"]);
    	}else {
    		$(".breathe-belt").eq(1).css('background-image', greenLight);
    		$(".belt_yuan:eq(1) span").text("正常");
    		
    	}
    }else if(name == "主运一部"){
        if (obj["msg"] != "" && obj["msg"] != "正常"){
            $(".breathe-belt").eq(2).css('background-image', redLight);
            $(".belt_yuan:eq(2) span").text(obj["msg"]);
        }else {
            $(".breathe-belt").eq(2).css('background-image', greenLight);
            $(".belt_yuan:eq(2) span").text("正常");

        }
    }
}

//████████████████████  初始化 激光雷达状态 webSocket  ███████████████████████████████████████████████████████████████████
var wsLidarStateWebSocket = null, socketNameLidarState = "激光雷达状态";
let lidarHeartCheck;
function initLidarStateWebSocket() {
    wsLidarStateWebSocket = new ReconnectingWebSocket(pcaddress + '/ws/zy/lidarStateWebSocket/'+belt_name_en);
    wsLidarStateWebSocket.onopen = function () {
        // console.log(socketNameLidarState + " 客户端已连接");
        lidarHeartCheck = setInterval(function() {
            wsLidarStateWebSocket.send('ping');
        }, 3000);
    }
    wsLidarStateWebSocket.onmessage = function (evt) {
        // console.log(socketNameLidarState + " 客户端接收消息 ", evt.data);

        var obj = JSON.parse(evt.data);
        if (obj["code"] != 1000){
            clearInterval(lidarHeartCheck);
        }
        changeLidarState(obj);
    }
    wsLidarStateWebSocket.onclose = function () {
        // console.log(socketNameLidarState + " 客户端已断开连接");
    	
    	 wsLidarStateWebSocket.close();
        clearInterval(lidarHeartCheck);
    };
    wsLidarStateWebSocket.onerror = function (evt) {
        // console.log(socketNameLidarState + " 连接出现错误", evt.e);
    	//  wsLidarStateWebSocket.close();
    	
    };
}

//更新雷达状态
function changeLidarState(obj) {
    var name = obj["belt_name"];
    if(name == belt_name){
        if (obj["msg"] != "" && obj["msg"] != "正常" && obj["code"] != "2"){
            $(".breathe-belt").eq(0).css('background-image', redLight);
            $(".belt_yuan:eq(0) span").text("异常");

        }else {

            if(obj["code"] == "2"){
                $("#lidarWarning").text("激光雷达防爆玻璃表面有污渍请擦拭");
            }else{
                $("#lidarWarning").text("");
            }
            $(".breathe-belt").eq(0).css('background-image', greenLight);
            $(".belt_yuan:eq(0) span").text("正常");
        }
    }
}

