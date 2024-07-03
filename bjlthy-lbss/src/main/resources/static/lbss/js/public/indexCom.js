var workingNames = null;
var workingList = null;
var belt_name = null;
$(function () {
	initCompanyName();
	initIP();
});
function initCompanyName(){
	belt_name = $("#belt_name").val();
	$.ajax({
		url : ctx + "lbss/config/sysconfig/getCompanyName",
		type : "post",
		dataType : "json",
		async:false,  //同步执行
		success : function(data) {
			var company_name = data.company_name;
			var system_name = data.system_name;
			$("#company_name").text(company_name);
			if(company_name.indexOf("北京龙田")>-1){
				$(".logo").css('background-image','url(/mapper/mapper/img/longtian.png)');
			}
			$("#system_name").text(belt_name+system_name);
			
			workingNames = data.workingNames;
			// workingList = data.workingList;
		}
	})
}

//初始化IP地址
function initIP(){
	$.ajax({
		url : ctx + "lbss/config/ipconfig/getIP",
		type : "post",
		dataType : "json",
		async:false,  //同步执行
		success : function(data) {
			pcaddress = "ws://"+data.IPv4+"/mapper";
		}
	})
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
    var url = "";
    switch (selectName) {
        case "实时":
            url = "/mapper/front/BeltRealtime";
            break;
        case "皮带统计":
            url = "/mapper/front/BeltStatis";
            break;
        case "皮带详细":
            url = "/mapper/front/BeltDetail";
            break;
    }
    return url;
}

function checkZeroInfo(){
	var url = "/mapper/zero/info";
	window.open(url);
}
function pageSwitch(){
	var name = $("#manage").val();
	if(name == ""){
		return;
	}
    var url = "";
    switch (name) {
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