var prefix = ctx + "mapper/data/allBelt";
$(function() {
	var coal = {
		init : function() {
			var _this = this;
			_this.initEvent();
		},
		initEvent : function() {
			var _this = this;
			//搜索按钮控制
			$("#searchInfo").click(function(){
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				if(startTime == "" || endTime ==""){
					layer.msg("请输入开始和结束时间", {icon: 0});
					return;
				}
			});

			//刷新页面
			$("#detailButton").click(function () {
				location.reload();
			});
			
			//查询
			$("#searchInfo").click(function(){
				queryDatas();
			});
			//下载实现
			$("#excel").click(function() {

				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var status = $("#status").val();
				var belt_name = $("#belt_name").val();
				if(startTime == "" || endTime ==""){
					layer.msg("请输入开始和结束时间", {icon: 0});
					return;
				}
				$.post(
						prefix+"/export",{"startTime":startTime,"endTime":endTime,"status":status,"belt_name":belt_name}, function(data) {
						if (data.code == 0) {
							var fileName=data.msg;
							var localurl=prefix+'/download?fileName='+fileName;
							location.href = localurl;
						} else {
							layer.msg(data.meg, {time : 2000});
						}

					})

			});
		}
	};
	coal.init();
});


function queryDatas(){
	var status = $("#status").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var belt_name = $("#belt_name").val();
    $.ajax({
        url: prefix + "/getTotalData", 
        type: "post",
        data: {"startTime": startTime, "endTime": endTime, "status": status,"belt_name":belt_name},
        success: function (data) {
        	var sumWeight = data.sumWeight;
        	var sumVolume = data.sumVolume;
        	$("#sumWeight").html(sumWeight);
        	$("#sumVolume").html(sumVolume);
        }
    });
    return true;
}
function subtrDay(dayNumber, date) {
	date = date ? date : new Date();
	var ms = dayNumber * (1000 * 60 * 60 * 24);
	var yesterday = new Date(date.getTime() - ms);

	return yesterday.Format("yyyy-MM-dd");
}
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, //月份 
		"d+" : this.getDate(), //日 
		"H+" : this.getHours(), //小时 
		"m+" : this.getMinutes(), //分 
		"s+" : this.getSeconds(), //秒 
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度 
		"S" : this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}