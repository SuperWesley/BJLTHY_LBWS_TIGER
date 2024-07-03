var startTime = "";
var endTime = "";
var prefix = "/mapper/mapper/data/sumweight";
layui.use('laydate', function(){})
$(function() {
	var coal = {
		init : function() {
			var _this = this;
			_this.initTime();
			_this.initDatagrid();
			_this.initEvent();
		},
		initTime : function() {
			 layui.use('laydate', function () {
		        var laydate = layui.laydate;
		        laydate.render({
		            elem: '#startTime',
		            trigger: 'click', //  触发方式
		            done: function (value, date) {

		            }
		        });
		    });
			layui.use('laydate', function () {
		        var laydate = layui.laydate;
		        laydate.render({
		            elem: '#endTime',
		            trigger: 'click', //  触发方式
		            done: function (value, date) {

		            }
		        });
		    });
			//昨天的时间 
			var startTime = getYesterDayDate(new Date());
			$("#startTime").val(startTime);
			//今天的时间 
			var endTime = getCurrentDate(new Date());
			$("#endTime").val(endTime);
		},
		initEvent : function() {
			var _this = this;
			//刷新本页面
			$("#weekButton").click(function () {
				location.reload();
			});
			
		},
		initDatagrid : function() {
			showScraperStatis(null, 'S');
		}
	};
	coal.init();
});

function showScraperStatis(btn, type) {
    if (btn != null) {
        $(btn).parent().find(".btn-primary").each(function () {
            $(this).css("background-color", "#1ab394");
            $(this).css("border-color", "#1ab394");
        })
        $(btn).css("background-color", "#f8ac59");
        $(btn).css("border-color", "#f8ac59");
    }else{
    	$(".btn-primary").eq(0).css('background-color', "#f8ac59");
    	$(".btn-primary").eq(0).css('border-color', "#f8ac59");
    }
    let url = "";
    //获取起始时间值
    var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
    let colw = 10;
    if (startTime == "" || endTime == "") {
        alert("请输入起始时间后再进行查询！");
        return 0;
    }
    if (type === 'S') {
    	endTime = addDay(startTime ,1);
    	$("#endTime").val(endTime);
        colw = 100;
        url = prefix + "/getShiftData";
    } else if (type === 'D') {
        colw = 40;
        url = prefix + "/getMonthData";
    } else if (type === 'W') {
    	endTime = addDay(startTime ,7);
    	$("#endTime").val(endTime);
        colw = 40;
        url = prefix + "/getWeekData";
    } else if (type === 'M') {
        colw = 10;
        url = prefix + "/getYearData";
    } else if (type === 'Y') {
        colw = 5;
        url = prefix + "/getAllYearData";
    } else {
        return 0;
    }
    queryDatas(url, startTime, endTime, type, colw);
}

function queryDatas(url, startTime, endTime, type, colw){
    $.ajax({
        url: url, type: "post",
        data: {"startTime": startTime, "endTime": endTime, type: type, colw: colw},
        success: function (data) {
            //汇总信息展示
            $("#totalTab tbody").html("");
            $("#totalTab tbody").append(
       			"<tr>"+
    				"<td width=\"20%\" style=\"color:#000; font-size:0.7rem; text-align:center;white-space:pre\">"+ data.day +"</td>"+
    				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.totalWeight).toFixed(2) +"</td>"+
    				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.totalVolume).toFixed(2) +"</td>"+
    				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.density).toFixed(2) +"</td>"+
    				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.gangueRatio).toFixed(2) +"</td>"+
    			"</tr>"
            );
            //详细信息展示
            $("#dataTab tbody").html("");
            $("#dataTab thead").html("");
            var date=null;
        	for(var i=0;i < data.timeList.length;i++){
        		if(i==0){
        			if(type === 'S'){
        				date = "班次";
        			}else if(type === 'W' || type === 'D'){
        				date = "日期";
        			}else if(type === 'M'){
        				date = "月份";
        			}else if(type === 'Y'){
        				date = "年份";
        			}
        			$("#dataTab thead").append(
        					"<tr>"+
        					"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ date +"</td>"+
        					"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">重量(t)</td>"+
        					"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">体积(m³)</td>"+
        					"<td width=\"12%\" style=\"color:#000; font-size:0.8rem; text-align:center\">松散密度(t/m³)</td>"+
        					"<td width=\"9%\" style=\"color:#000; font-size:0.8rem; text-align:center\">含矸率(%)</td>"+
        					"</tr>"
        			);
        		}
        		if(type === 'D' || type === 'W'){
        			$("#dataTab tbody").append(
    					"<tr>"+
        				"<td width=\"12%\" style=\"color:#000; font-size:0.7rem; text-align:center\"><a onclick='queryShift(\"" + data.timeList[i] + "\")'>"+data.timeList[i]+"</a></td>"+
        				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.weightList[i]).toFixed(2) +"</td>"+
        				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.volumeList[i]).toFixed(2) +"</td>"+
        				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.densityList[i]).toFixed(2) +"</td>"+
        				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.gangueRatioList[i]).toFixed(2) +"</td>"+
        				"</tr>"
        			);		
        		}else{
        			$("#dataTab tbody").append(
	        			"<tr>"+
	    				"<td width=\"12%\" style=\"color:#000; font-size:0.7rem; text-align:center\">"+ data.timeList[i] +"</td>"+
	    				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.weightList[i]).toFixed(2) +"</td>"+
	    				"<td width=\"11%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.volumeList[i]).toFixed(2) +"</td>"+
	    				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.densityList[i]).toFixed(2) +"</td>"+
	    				"<td width=\"10%\" style=\"color:#000; font-size:0.8rem; text-align:center\">"+ parseFloat(data.gangueRatioList[i]).toFixed(2) +"</td>"+
	    				"</tr>"
    				);
				}

				}
        }
    });
    
}

function queryShift(obj){
	$(".btn-primary").eq(0).css('background-color', "#f8ac59");
	$(".btn-primary").eq(0).css('border-color', "#f8ac59");
	
	$(".btn-primary").eq(1).css("background-color", "#1ab394");
	$(".btn-primary").eq(1).css("border-color", "#1ab394");
	
	$(".btn-primary").eq(2).css("background-color", "#1ab394");
	$(".btn-primary").eq(2).css("border-color", "#1ab394");
	
	var startTime = obj;
	var endTime = addDay(startTime ,1);
	$("#startTime").val(startTime);
	$("#endTime").val(endTime);
	let colw = 100;
	let url = prefix + "/getShiftData";
	queryDatas(url, startTime, endTime, 'S', colw);
}

