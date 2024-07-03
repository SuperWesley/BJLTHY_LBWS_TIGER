var workingNames = null;
var workingList = null;
$(function () {
	initCompanyName();
});
function initCompanyName(){
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
				$(".logo_img").css('background-image','url(/mapper/mapper/img/longtian.png)');
			}
			$("#system_name").text(system_name);
			
			workingNames = data.workingNames;
			workingList = data.workingList;
		}
	})
}