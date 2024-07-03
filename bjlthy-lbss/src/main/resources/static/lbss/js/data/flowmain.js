var beltInstallChart = null;
var beltTotalChart = null;
var flag = 1;
var greenLight = '-webkit-gradient(linear, left top, left bottom, from(#00FF00), to(#00FF00))';
var redLight = '-webkit-gradient(linear, left top, left bottom, from(#FF0000), to(#FF0000))';
var jingImageUrls = [
	"url(../mapper/img/jing/xyp.png)",
	"url(../mapper/img/jing/zy3pd.png)",
	"url(../mapper/img/jing/zy2pd.png)",
	"url(../mapper/img/jing/zy1pd.png)",
	"url(../mapper/img/jing/101pd.png)",
	"url(../mapper/img/jing/103pd.png)",
	"url(../mapper/img/jing/120pd.png)",
	"url(../mapper/img/jing/2201pd.png)",
];
var dongImageUrls = [
	"url(../mapper/img/dong/xyp.gif)",
	"url(../mapper/img/dong/zy3.gif)",
	"url(../mapper/img/dong/zy2.gif)",
	"url(../mapper/img/dong/zy1.gif)",
	"url(../mapper/img/dong/101.gif)",
	"url(../mapper/img/dong/103.gif)",
	"url(../mapper/img/dong/120.gif)",
	"url(../mapper/img/dong/2201.gif)",
];
$(function () {
	//初始化图表
	// initChart();
	initDatagrid();
	//定时更新数据信息
	setInterval(function() {
		initDatagrid();
	}, 5000)
})

var prefix = ctx + "mapper/data/allBelt";
function initDatagrid(){
	$.ajax({
		url : prefix + "/getDetail",
		type : "post",
		dataType : "json",
		success : function(result) {
			$("#dataTab tbody").html("");
			for(var i=0;i < result.list.length;i++){
        		var data = result.list[i];
				var href = "http://"+data.ip+":8090/mapper/frontMain/openBeltPage?belt_name="+data.belt_name;
				var info = parseFloat(data.speed).toFixed(2)+"m/s、"+parseFloat(data.weight*3600).toFixed(2)+"t/h";
				if (data.belt_name == "主运一部"){
					if(data.speed > 0 ){
						$(".breathe-belt").eq(4).css('background-image', greenLight);
						$("#zy1").css('background-image', dongImageUrls[3]);
					}else{
						$(".breathe-belt").eq(4).css('background-image', redLight);
						$("#zy1").css('background-image', jingImageUrls[3]);
					}
					// $("#name_5").html(href);
					$('#name_5').attr('href',href);
					$("#info_5").text(info);
					if (data.status == "1"){
						$('#name_5').css("color","#13f408");
						$("#info_5").css("color","#13f408");
					}else {
						$(".breathe-belt").eq(4).css('background-image', redLight);
						$('#name_5').css("color","#f40808");
						$("#info_5").css("color","#f40808");
					}
				}else if (data.belt_name == "主运二部"){
					// $("#name_6").html(href);
					if(data.speed > 0 ){
						$(".breathe-belt").eq(2).css('background-image', greenLight);
						$("#zy2").css('background-image', dongImageUrls[2]);
					}else{
						$(".breathe-belt").eq(2).css('background-image', redLight);
						$("#zy2").css('background-image', jingImageUrls[2]);
					}
					$('#name_6').attr('href',href);
					$("#info_6").text(info);
					if (data.status == "1"){
						$('#name_6').css("color","#13f408");
						$("#info_6").css("color","#13f408");
					}else {
						$(".breathe-belt").eq(2).css('background-image', redLight);
						$('#name_6').css("color","#f40808");
						$("#info_6").css("color","#f40808");
					}
				}

        	}
		}
	})
}