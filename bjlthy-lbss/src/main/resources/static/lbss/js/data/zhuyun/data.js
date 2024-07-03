var beltSpeedChart = null;
var beltWeightChart = null;
var beltVolumeChart = null;
var beltShiftChart = null;
var beltWeekChart = null;
var beltMonthChart = null;
var serverIP = null;
$(function () {
	//初始化图表
	initChart();
	// initDatagrid();
	
})

var prefix = ctx + "mapper/weight";
function initDatagrid(){
	$.ajax({
		url : prefix + "/getDetail",
		type : "post",
		dataType : "json",
		success : function(data) {
			//皮带信息
			$("#totalweight").text("当日过煤重量： "+parseFloat(data.realBelt.totalWeight).toFixed(2)+" t");
			$("#totalvolume").text("当日过煤体积： "+parseFloat(data.realBelt.totalVolume).toFixed(2)+" m³");
			$("#hourWeight").text("整时累计过煤重量： "+parseFloat(data.realBelt.hourWeight).toFixed(2)+" t");
			$("#hourVolume").text("整时累计过煤体积： "+parseFloat(data.realBelt.hourVolume).toFixed(2)+" m³");
			$("#gangueRatio").text("含矸率： "+parseFloat(data.realBelt.gangueRatio).toFixed(2)+" %");
			$("#hunmei").text("混煤密度： "+parseFloat(data.realBelt.density*1.33).toFixed(2)+" t/m³");
			$("#density").text("松散密度： "+parseFloat(data.realBelt.density).toFixed(2)+" t/m³");
			updateChart(data);
		}
	})
}

//初始化界面图表
function initChart(){
	//初始化皮带速度图表信息
	if(beltSpeedChart == undefined){
		beltSpeed(); 
	}
	//初始化瞬时过煤重量图表信息
	if(beltWeightChart == undefined){
		beltWeight();
	}
	//初始化瞬时过煤体积图表信息
	if(beltVolumeChart == undefined){
		beltVolume();
	}
	//初始化班次图表信息
	if(beltShiftChart == undefined){
		beltShift();
	}
	//初始化周图表信息
	if(beltWeekChart == undefined){
		beltWeek();
	}
	//初始化月图表信息
	if(beltMonthChart == undefined){
		beltMonth();
	}
	
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
			data : data.weights
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

//皮带机速度
function beltSpeed() {
	// 基于准备好的dom，初始化echarts实例
	beltSpeedChart = echarts.init(document.getElementById('beltSpeed'));
	var colorTemplate1 = [
	   [1, new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
            offset: 0.1,
            color: "#13ace8"
	    }, {
	        offset: 0.6,
	        color: "#0167e8"
	    }, {
	        offset: 1,
	        color: "#0B95FF"
	    }])]
	];
	option = {
		series : [
			{
				name : '皮带机速度',
				min : 0,
				max : 10,
				type : 'gauge',
				radius : "90%",
				axisLine: { // 仪表盘轴线(轮廓线)相关配置。
		                show: true, // 是否显示仪表盘轴线(轮廓线),默认 true。
		                lineStyle: { // 仪表盘轴线样式。
		                    color: colorTemplate1, //仪表盘的轴线可以被分成不同颜色的多段。每段的  结束位置(范围是[0,1]) 和  颜色  可以通过一个数组来表示。默认取值：[[0.2, '#91c7ae'], [0.8, '#63869e'], [1, '#c23531']]
		                    opacity: 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
		                    width: 15, //轴线宽度,默认 30。
		                    shadowBlur: 0, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。
		                    shadowColor: "#fff", //阴影颜色。支持的格式同color。
		                }
		            },
				axisTick: { // 刻度(线)样式。
	                show: true, // 是否显示刻度(线),默认 true。
	                splitNumber: 1, // 分隔线之间分割的刻度数,默认 5。
	                length: 6, // 刻度线长。支持相对半径的百分比,默认 8。
	                lineStyle: { // 刻度线样式。
	                    color: "#0167E8", //线的颜色,默认 #eee。
	                    opacity: 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
	                    width: 1, //线度,默认 1。
	                    type: "solid", //线的类型,默认 solid。 此外还有 dashed,dotted
	                    shadowBlur: 0, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。
	                    shadowColor: "#fff", //阴影颜色。支持的格式同color。
	                },
	            },
	            axisLabel: { // 刻度标签。
	                show: true, // 是否显示标签,默认 true。
	                distance: 1, // 标签与刻度线的距离,默认 5。
	                color: "#fff", // 文字的颜色,默认 #fff。
	                fontSize: 18, // 文字的字体大小,默认 5。
	                formatter: "{value}", // 刻度标签的内容格式器，支持字符串模板和回调函数两种形式。 示例:// 使用字符串模板，模板变量为刻度默认标签 {value},如:formatter: '{value} kg'; // 使用函数模板，函数参数分别为刻度数值,如formatter: function (value) {return value + 'km/h';}
	            },
	       
				splitLine : { // 分隔线
					length : 25, // 属性length控制线长
					lineStyle : { // 属性lineStyle（详见lineStyle）控制线条样式
						color : 'auto',
						width : 3
					}
				},
				
				detail : {
					show : true, // 是否显示详情,默认 true。
					offsetCenter : [ 0, "90%" ], // 相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
					fontSize : 30, // 文字的字体大小,默认 15。
					formatter : "{value}m/s", // 格式化函数或者字符串
					color : '#f36202',
				},
				data : [0.00],
			}
		]
	};

	beltSpeedChart.clear();
	// 使用刚指定的配置项和数据显示图表。
	beltSpeedChart.setOption(option);
	window.addEventListener("resize", function() {
		beltSpeedChart.resize();
	});
}

//皮带瞬时过煤重量
function beltWeight() {
	// 基于准备好的dom，初始化echarts实例
	beltWeightChart = echarts.init(document.getElementById('beltWeight'));

	option = {
	        animation: true,
	        visualMap: {
	        	show: false,
	        	pieces:[
	        		{
	                    gt: 0,
	                    lte: 3000,
	                    color: '#4fd74d'
	                  },
	                  {
	                    gt: 3000,
	                    lte: 5000,
	                    color: '#eea736'
	                  },
	                  {
	                      gt: 5000,
	                      color: '#f91414'
	                    }
	        	],
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
	            top: '15%', // 等价于 y: '16%'
	            left: '3%',
	            right: '5%',
	            bottom: '10%',
	            containLabel: true
	        },
	        // 提示框
	        tooltip: {
	            trigger: 'axis'
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
	            data: []
	        },

	        yAxis: {
	            type: 'value',
	            min:0,
	            max:8000,
	            interval : 2000,
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

	        series: [{
	            name: '实际',
	            data: [],
	            type: 'line',
	            // 设置折线上圆点大小
	            // symbolSize: 5,
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    // 拐点上显示数值
	                    label: {
	                        show: true
	                    },
	                    borderColor: 'red', // 拐点边框颜色
	                    lineStyle: {
	                        width: 3, // 设置线宽
	                        type: 'solid' //'dotted'虚线 'solid'实线
	                    }
	                }
	            }
	        }
	        ],

	        color: ['#CDB44F']
	    };

	beltWeightChart.clear();
	// 使用刚指定的配置项和数据显示图表。
	beltWeightChart.setOption(option);
	window.addEventListener("resize", function() {
		beltWeightChart.resize();
	});
}

//皮带瞬时过煤体积
function beltVolume() {
	// 基于准备好的dom，初始化echarts实例
	beltVolumeChart = echarts.init(document.getElementById('beltVolume'));

	option = {
	        animation: true,
	        visualMap: {
	        	show: false,
	        	pieces:[
	        		{
	                    gt: 0,
	                    lte: 3000,
	                    color: '#4fd74d'
	                  },
	                  {
	                    gt: 3000,
	                    lte: 5000,
	                    color: '#eea736'
	                  },
	                  {
	                      gt: 5000,
	                      color: '#f91414'
	                    }
	        	],
	        },
	        title: {
	        	text:'瞬时体积（m³/h）',
	        	left: 20,
	        	textStyle:{
	        		fontSize: 15,
		        	fontWeight: 'bold',
		        	color: '#00EAFF'
	        	}
	        },
	        grid: {
	            top: '15%', // 等价于 y: '16%'
	            left: '3%',
	            right: '5%',
	            bottom: '10%',
	            containLabel: true
	        },
	        // 提示框
	        tooltip: {
	            trigger: 'axis'
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
	            data: []
	        },

	        yAxis: {
	            type: 'value',
	            min:0,
	            max:8000,
	            interval : 2000,
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

	        series: [{
	            name: '实际',
	            data: [],
	            type: 'line',
	            // 设置折线上圆点大小
	            // symbolSize: 5,
	            smooth:true,
	            itemStyle: {
	                normal: {
	                    // 拐点上显示数值
	                    label: {
	                        show: true
	                    },
	                    borderColor: 'red', // 拐点边框颜色
	                    lineStyle: {
	                        width: 3, // 设置线宽
	                        type: 'solid' //'dotted'虚线 'solid'实线
	                    }
	                }
	            }
	        }
	        ],

	        color: ['#CDB44F']
	    };

	beltVolumeChart.clear();
	// 使用刚指定的配置项和数据显示图表。
	beltVolumeChart.setOption(option);
	window.addEventListener("resize", function() {
		beltVolumeChart.resize();
	});
}

//今日班次煤量信息
function beltShift() {
	// 基于准备好的dom，初始化echarts实例
	beltShiftChart = echarts.init(document.getElementById('beltShift'));

	var xDataBanci = [0.00,0.00,0.00];
	option = {
			grid: {
				top: 10,
				bottom: 15,
				left: 70,
				right: 85
			},
			xAxis: {
				min:0,
				max:80000,
				axisLine: {show: false},
				axisLabel: {show: false},
				axisTick: {show: false},
				splitLine: {show: false},
			},
			yAxis: {
				type: 'category',
				data: ['中班','早班','晚班'],
				axisLine: {show: false},
				axisLabel: {
					show: true,
					textStyle: {
						color: '#00EAFF',
						fontSize: 18,
					}
				},
				axisTick: {show: false},
				splitLine: {show: false},
			},
			series: [{
				realtimeSort: true,
				type: 'bar',
				data: xDataBanci,
				label: {
					show: true,
					position: 'right',
					textStyle: {
						color: '#00EAFF',
						fontSize: 18,
					},
					formatter: function (value) {
						return value.data+" t";
					}
				},
				barWidth: 15,   //柱状宽度
				itemStyle: {    //柱状颜色和圆角
					color: new echarts.graphic.LinearGradient(0, 0, 1, 0,
							[
								{offset: 0, color: '#0072ff'},
								{offset: 1, color: '#00EAFF'}
					])
				},
			}]
	};

	beltShiftChart.clear();
	// 使用刚指定的配置项和数据显示图表。
	beltShiftChart.setOption(option);
	window.addEventListener("resize", function() {
		beltShiftChart.resize();
	});
}

//本周煤量信息
function beltWeek() {
	// 基于准备好的dom，初始化echarts实例
	beltWeekChart = echarts.init(document.getElementById('beltWeek'));

	var xDataWeekly = [0.00,0.00,0.00,0.00,0.00,0.00,0.00];
    var yDataWeekly = ['周日','周六','周五','周四','周三','周二','周一'];
    option = {
        grid: {
            top: 10,
            bottom: 10,
            left: 70,
            right: 85
        },
        xAxis: {
        	min:0,
        	max:120000,
            axisLine: {show: false},
            axisLabel: {show: false},
            axisTick: {show: false},
            splitLine: {show: false},
        },
        yAxis: {
            type: 'category',
            data: yDataWeekly,
            axisLine: {show: false},
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#00EAFF',
                    fontSize: 17,
                }
            },
            axisTick: {show: false},
            splitLine: {show: false},
        },
        series: [{
            type: 'bar',
            data: xDataWeekly,
            label: {
                show: true,
                position: 'right',
                textStyle: {
                    color: '#00EAFF',
                    fontSize: 17,
                },
                formatter: function (value) {
                    return value.data+" t";
                }
            },
            barWidth: 17,   //柱状宽度
            itemStyle: {    //柱状颜色和圆角
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0,
                    [
                        {offset: 0, color: '#0072ff'},// 0%处的颜色为蓝色
                        {offset: 1, color: '#F5A930'} // 100%处的颜色为黄
                    ])
            },
        }]
    };

	// 使用刚指定的配置项和数据显示图表。
    beltWeekChart.clear();
	beltWeekChart.setOption(option);
	window.addEventListener("resize", function() {
		beltWeekChart.resize();
	});
}

//本月煤量信息
function beltMonth() {
	// 基于准备好的dom，初始化echarts实例
	beltMonthChart = echarts.init(document.getElementById('beltMonth'));
	var xDataWeekly = [0.00];
    var yDataWeekly = ['12'];
	option = {
		grid : {
			top : 15,
			bottom : 15,
			left : 60,
			right : 85
		},
		xAxis : {
			min : 0,
			max : 3000000,
			axisLine : {
				show : false
			},
			axisLabel : {
				show : false
			},
			axisTick : {
				show : false
			},
			splitLine : {
				show : false
			},
		},
		yAxis : {
			type : 'category',
			data: yDataWeekly,
			axisLine : {
				show : false
			},
			axisLabel : {
				show : true,
				textStyle : {
					color : '#00EAFF',
					fontSize : 15,
				}
			},
			axisTick : {
				show : false
			},
			splitLine : {
				show : false
			},
		},
		series : [ {
			type : 'bar',
			data: xDataWeekly,
			label : {
				show : true,
				position : 'right',
				textStyle : {
					color : '#00EAFF',
					fontSize : 15,
				},
				formatter : function(value) {
					return value.data + " t";
				}
			},
			barWidth : 20, //柱状宽度
			itemStyle : { //柱状颜色和圆角
				color : new echarts.graphic.LinearGradient(0, 0, 1, 0,
					[
						{
							offset : 0,
							color : '#0072ff'
						}, // 0%处的颜色为蓝色
						{
							offset : 1,
							color : '#F5A930'
						} // 100%处的颜色为黄
					])
			},
		} ]
	};

	// 使用刚指定的配置项和数据显示图表。
	beltMonthChart.clear();
	beltMonthChart.setOption(option);
	window.addEventListener("resize", function() {
		beltMonthChart.resize();
	});
}
