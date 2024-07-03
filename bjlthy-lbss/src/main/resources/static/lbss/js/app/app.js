var weightChart = null;
var volumeChart = null;
var beltWeekChart = null;
var weight = null;
var maxVal = 8000;//圆环极大值

$(function () {
	//初始化图表
	initChart();
	initDatagrid();
	
})

function initDatagrid(){
	$.ajax({
		url : "/lbss/lbss/weight/getDetail",
		type : "post",
		dataType : "json",
		success : function(data) {
			//皮带信息
			$("#totalweight").text(parseFloat(data.realBelt.totalWeight).toFixed(2)+" t");
			$("#monthWeight").text(parseFloat(data.month_weight).toFixed(2)+" t");
			$("#nigWeight").text(parseFloat(data.realBelt.nig_weight).toFixed(2)+" t");
			$("#morWeight").text(parseFloat(data.realBelt.mor_weight).toFixed(2)+" t");
			$("#aftWeight").text(parseFloat(data.realBelt.aft_weight).toFixed(2)+" t");
			updateChart(data);
		}
	})
}

//初始化界面图表
function initChart(){
	//初始化瞬时过煤重量图表信息
	beltWeight();
	//初始化瞬时过煤体积图表信息
	beltVolume();
	//初始化周图表信息
	beltWeek();
	
}

//更新界面图表
function updateChart(data){
	
	//更新瞬时过煤重量图表信息
	updateWeight(data);
	//更新瞬时过煤体积图表信息
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

//皮带瞬时过煤重量
function beltWeight() {
	// 基于准备好的dom，初始化echarts实例
	weightChart = echarts.init(document.getElementById('weight'));

	var option = {
	     title: {
	         x: 'center',
	         y: 'center',
	         itemGap: 15,//描述文字与上面数值单位的垂直距离
	         textStyle: {
	             color: '#000',
	             fontSize: 16,
	         },
	     },
	     series: [{
	         type: 'pie',//饼图类型
	         radius: ['80%', '90%'],//饼图的内、外半径
	         hoverAnimation: false,
	         label: {
	             normal: {
	                 show: false,
	             }
	         },
	         itemStyle: {
	            normal: {
	                color: '#f4f4f4',
	            }
	        },
	        data: [{//系列中的数据内容数组
	            value: 1000,
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
	                    color: changecolor(1000)
	                }
	            }
	        }, {
	            value: maxVal - 1000,
	        }
	        ]
	     }]
	 };
	
	// 使用刚指定的配置项和数据显示图表。
	weightChart.setOption(option);
	window.addEventListener("resize", function() {
		weightChart.resize();
	});
}

//皮带瞬时过煤体积
function beltVolume() {
	// 基于准备好的dom，初始化echarts实例
	volumeChart = echarts.init(document.getElementById('volume'));

	//根据数值在定义区间valObj中范围改变，显示colorObj中对应颜色
	var option = {
	     title: {
	         x: 'center',
	         y: 'center',
	         itemGap: 15,//描述文字与上面数值单位的垂直距离
	         textStyle: {
	             color: '#000',
	             fontSize: 16,
	         },
	     },
	    series: [{
	         type: 'pie',//饼图类型
	         radius: ['80%', '90%'],//饼图的内、外半径
	         hoverAnimation: false,
	         label: {
	             normal: {
	                 show: false,
	             }
	         },
	         itemStyle: {
	            normal: {
	                color: '#f4f4f4',
	            }
	        },
	        data: [{//系列中的数据内容数组
	            value: 1000,
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
	                    color: changecolor(1000)
	                }
	            }
	        }, {
	            value: maxVal - 1000,
	        }
	        ]
	     }]
	 };
	// 使用刚指定的配置项和数据显示图表。
	volumeChart.setOption(option);
	window.addEventListener("resize", function() {
		volumeChart.resize();
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
            top: 8,
            bottom: 20,
            left: 70,
            right: 70
        },
        xAxis: {
        	min:0,
        	max:90000,
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
                    color: '#828282',
                    fontSize: 15,
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
                    color: '#828282',
                    fontSize: 13,
                },
                formatter: function (value) {
                    return value.data+" t";
                }
            },
            barWidth: 15,   //柱状宽度
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
	beltWeekChart.setOption(option);
	window.addEventListener("resize", function() {
		beltWeekChart.resize();
	});
}

