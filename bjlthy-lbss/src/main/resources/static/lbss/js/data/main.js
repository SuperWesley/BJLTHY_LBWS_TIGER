var beltInstallChart = null;
var beltTotalChart = null;
var flag = 1;
$(function () {
	//初始化图表
	initChart();
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
				var status ="正常";
				var color ="#13f408";
				if (data.status == "0"){
					status ="异常";
					color ="#f40808";
				}
				$("#dataTab tbody").append(
					"<tr>"+
					"<td class='id' width='11%' class='mainTableStyle'><a href='http://"+data.ip+":8090/mapper/frontMain/openBeltPage?belt_name="+data.belt_name+"' target='_blank' class='mainTableStyle' style='font-size:17px;margin-left: 22%;color: #FFFFFF'>"+ data.belt_name +"</button></td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867; line-height: 38px; font-size:18px; text-align:center\">"+ data.weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; line-height: 38px; font-size:18px; text-align:center\">"+ data.hourWeight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867;line-height: 38px; font-size:18px; text-align:center\">"+ data.density +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; line-height: 38px; font-size:18px; text-align:center\">"+ data.gangueRatio +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867;line-height: 38px; font-size:18px; text-align:center\">"+ data.speed +"</td>"+

					"<td width=\"11%\" style=\"color:#ffffff; line-height: 38px; font-size:18px; text-align:center\">"+ data.nig_weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867; line-height: 38px; font-size:18px; text-align:center\">"+ data.mor_weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; line-height: 38px; font-size:18px; text-align:center\">"+ data.aft_weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867; line-height: 38px; font-size:18px; text-align:center\">"+ data.day_weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; line-height: 38px; font-size:18px; text-align:center\">"+ data.month_weight +"</td>"+
					"<td width=\"11%\" style=\"color:#ffffff; background-color: #284867; line-height: 38px; font-size:18px; text-align:center\">"+ data.year_weight +"</td>"+
					"<td width=\"11%\" style=\"color:"+color+"; line-height: 38px; font-size:18px; text-align:center\">"+ status +"</td>"+
					"</tr>"
				);

        	}
			//皮带信息
			updateChart(result);
		}
	})
}

//初始化界面图表
function initChart(){
	//初始化瞬时图表信息
	if(beltInstallChart == undefined){
		beltBeltInstall();
	}
	//初始化统计图表信息
	if(beltTotalChart == undefined){
		beltBeltTotal();
	}
	
}

//更新界面图表
function updateChart(data){
	//更新瞬时过煤重量图表信息
	updateBeltInstall(data);
	var val = $(".inpBtu:eq("+flag+")").val();
	if(val == "周"){
		//更新统计图表信息
		updateBeltTotal(data);
	}
}

/**------------------------- 更新图表数据信息  start----------------------------*/
function updateBeltInstall(data){
	
	var realWeightList = [];
    for(let i=0;i<data.realWeightArr.length;i++){
        //push向series1数组添加数据
    	let realWeightArr = data.realWeightArr[i];
    	realWeightList.push(
            {
               data:realWeightArr,
            }
         );
    }
    
	beltInstallChart.setOption({
		xAxis : [{
			data : data.timeList
		}],
		series: realWeightList
    })
	
}


function updateBeltTotal(data){
	var totalWeights = [];
    for(let i=0;i<data.totalWeightArr.length;i++){
        //push向series1数组添加数据
    	let totalWeightArr = data.totalWeightArr[i];
    	totalWeights.push(
            {
               data:totalWeightArr,
            }
         );
    }
    
	beltTotalChart.setOption({
		xAxis : [{
			data : data.days
		}],
		series : totalWeights
		
    })
	
}

/**------------------------- 更新图表数据信息  end----------------------------*/

var fontSize = 16;
var x_p = '60%';
//皮带瞬时过煤重量
function beltBeltInstall() {
	
	var realInfo = [];
    for(let i=0;i<workingNames.length;i++){
        //push向series1数组添加数据
    	realInfo.push(
            {
               name: workingNames[i],
               data:[],
	           type: 'line',
	           smooth:false,
	           symbolSize: 8,
            }
         );
    }
    
    if(workingNames.length >= 3){
    	fontSize = 16- (workingNames.length- 2)*2;
    }
	// 基于准备好的dom，初始化echarts实例
	beltInstallChart = echarts.init(document.getElementById('instantCharts'));

	option = {
			color:['#ef7b1c','#ff0000','#33b5ff'],
	        legend: {
	            data: workingNames,
	            right:'10%', 
	            textStyle:{//图例文字的样式
	                color:'#ccc',
	                fontSize:fontSize
	            }
	        },
	        title: {
	        	text:'瞬时过煤量（t/h）',
	        	left: 20,
	        	textStyle:{
	        		fontSize: 15,
		        	fontWeight: 'bold',
		        	color: '#00EAFF'
	        	}
	        },
	        grid: {
	        	top: '10%', // 等价于 y: '16%'
	            left: '2%',
	            right: '10%',
	            bottom: '15%',
	            containLabel: true
	        },
	        // 提示框
	        tooltip : {
	            trigger: 'axis',
	            axisPointer: {
	                type: 'cross',
	                label: {
	                    backgroundColor: '#6a7985'
	                }
	            }
	        },
	        xAxis: {
	            name: '时间',
	            type: 'category',
	            axisLine: {
	                lineStyle: {
	                    // 设置x轴颜色
	                    color: '#00EAFF'
	                }
	            },
	            
	            boundaryGap: false,
	            data: ['10:20:00','10:20:03','10:20:06','10:20:09','10:21:00']
	        },

	        yAxis: {
	            type: 'value',
	            axisLine: {
	                lineStyle: {
	                    // 设置y轴颜色
	                    color: '#00EAFF',
	                }
	            },
	            axisLable: {
	            	fontSize: 14
	            },
	            splitLine: {
	                show: true,
	                lineStyle:{
	                   color: ['#315070'],
	                   width: 1,
	                   type: 'solid'
	              }
	        　　	},
	            nameTextStyle: {
	            	fontSize : 16,      //更改坐标轴文字大小     
	            	padding: [0,0,0,100]
	            }
	        },
	        series:realInfo,

	    };

	beltInstallChart.clear();
	// 使用刚指定的配置项和数据显示图表。
	beltInstallChart.setOption(option);
	window.addEventListener("resize", function() {
		beltInstallChart.resize();
	});
}

//皮带煤量统计信息
function beltBeltTotal() {
	
	var totalInfo = [];
	var countMax=0;
    for(var key in workingNames){
        //push向series1数组添加数据
    	totalInfo.push(
            {
               name: workingNames[countMax],
               type: 'bar',
               data: [],
            }
         );
        countMax++;
    }
    
	// 基于准备好的dom，初始化echarts实例
	beltTotalChart = echarts.init(document.getElementById('totalCharts'));

	var xDataWeekly = [0.00,0.00,0.00,0.00,0.00,0.00,0.00];
    var yDataWeekly = ['周日','周六','周五','周四','周三','周二','周一'];
    option = {
		color:['#ef7b1c','#ff0000','#33b5ff'],
        legend: {
            data: workingNames,
            right:'10%', 
            textStyle:{//图例文字的样式
                color:'#ccc',
                fontSize:fontSize
            }
        },
        grid: {
        	top: '10%', // 等价于 y: '16%'
            left: '2%',
            right: '10%',
            bottom: '15%',
            containLabel: true
        },
        // 提示框
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        xAxis: {
            type: 'category',
            axisLabel : {
				fontSize : "13",
				color : '#fff'
			},
            data: []
        },

        yAxis: {
            type: 'value',
            axisLine: {
                lineStyle: {
                    // 设置y轴颜色
                    color: '#00EAFF',
                }
            },
            axisLable: {
            	fontSize: 12
            	
            },
            splitLine: {
                show: true,
                lineStyle:{
                   color: ['#315070'],
                   width: 1,
                   type: 'solid'
              }
        　　	},
            nameTextStyle: {
            	fontSize : 14,      //更改坐标轴文字大小     
            	padding: [0,0,0,100]
            }
        },
        series:totalInfo
    };

	// 使用刚指定的配置项和数据显示图表。
    beltTotalChart.clear();
    beltTotalChart.setOption(option);
	window.addEventListener("resize", function() {
		beltTotalChart.resize();
	});
}

$("#dayInfo").click(function(){
	flag = 0;
	$(".inpBtu").css('color','#fff');
	$("#dayInfo").css('color','#01f5f1');
	$.ajax({
		url : prefix + "/getDayData",
		type : "post",
		dataType : "json",
		success : function(data) {
			var totalWeights = [];
		    for(let i=0;i<data.totalWeightArr.length;i++){
		        //push向series1数组添加数据
		    	let totalWeightArr = data.totalWeightArr[i];
		    	totalWeights.push(
		            {
		               data:totalWeightArr,
		            }
		         );
		    }
		    
			beltTotalChart.setOption({
				xAxis : [{
					data : data.days
				}],
				series :totalWeights

			})
		}
	});
});
$("#weekInfo").click(function(){
	flag = 1;
	$(".inpBtu").css('color','#fff');
	$("#weekInfo").css('color','#01f5f1');
	$.ajax({
		url : prefix + "/getWeekData",
		type : "post",
		dataType : "json",
		success : function(data) {
			var totalWeights = [];
		    for(let i=0;i<data.totalWeightArr.length;i++){
		        //push向series1数组添加数据
		    	let totalWeightArr = data.totalWeightArr[i];
		    	totalWeights.push(
		            {
		               data:totalWeightArr,
		            }
		         );
		    }
		    
			beltTotalChart.setOption({
				xAxis : [{
					data : data.days
				}],
				series :totalWeights
		    })
		}
	});
});
$("#monthInfo").click(function(){
	flag = 2;
	$(".inpBtu").css('color','#fff');
	$("#monthInfo").css('color','#01f5f1');
	$.ajax({
		url : prefix + "/getMonthData",
		type : "post",
		dataType : "json",
		success : function(data) {
			var totalWeights = [];
		    for(let i=0;i<data.totalWeightArr.length;i++){
		        //push向series1数组添加数据
		    	let totalWeightArr = data.totalWeightArr[i];
		    	totalWeights.push(
		            {
		               data:totalWeightArr,
		            }
		         );
		    }
		    
			beltTotalChart.setOption({
				xAxis : [{
					data : data.days
				}],
				series :totalWeights
		    })
		}
	});
});
$("#yearInfo").click(function(){
	flag =3;
	$(".inpBtu").css('color','#fff');
	$("#yearInfo").css('color','#01f5f1');
	$.ajax({
		url : prefix + "/getYearData",
		type : "post",
		dataType : "json",
		success : function(data) {
			var totalWeights = [];
		    for(let i=0;i<data.totalWeightArr.length;i++){
		        //push向series1数组添加数据
		    	let totalWeightArr = data.totalWeightArr[i];
		    	totalWeights.push(
		            {
		               data:totalWeightArr,
		            }
		         );
		    }
		    
			beltTotalChart.setOption({
				xAxis : [{
					data : data.days
				}],
				series :totalWeights
		    })
		}
	});
});