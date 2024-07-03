$('document').ready(function () {
    init();
})

function init() {
    $("#startTime").val(getRecentDay(-1));
    $("#endTime").val(getRecentDay(0));
}

$("#excel").click(function () {
    var prefix = ctx + "" +"/mapper/data/sumweightDay";
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var belt_name = $("#belt_name").val();
    var status = $("#status").val();
    if (startTime == "" || endTime == "") {
        layer.msg("请输入开始和结束时间", {icon: 3});
        return;
    }
    $.post(
        prefix + "/export", {"startTime": startTime, "endTime": endTime,"belt_name":belt_name,"status":status}, function (data) {
            if (data.code == 0) {
                var fileName = data.msg;
                var localurl = prefix + '/download?fileName=' + fileName;
                location.href = localurl;
            } else {
                layer.msg(data.meg, {icon: 2, time: 2000});
            }

        });
});

function queryDatas(){
    var prefix = ctx + "" +"/mapper/data/sumweightDay";
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

function getRecentDay(day) {
    var today = new Date();
    var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds);
    var tYear = today.getFullYear();
    var tMonth = today.getMonth();
    var tDate = today.getDate();
    tMonth = doHandleMonth(tMonth + 1);
    tDate = doHandleMonth(tDate);
    return tYear + "-" + tMonth + "-" + tDate + " 00:00:00";
}

function doHandleMonth(month) {
    var m = month;
    if (month.toString().length == 1) {
        m = "0" + month;
    }
    return m;
}


