function getInputTime(id) {
    var time = $('#' + id).val();
    if (time == undefined || time == "") {
        return '1970-01-01';
    } else {
        return time;
    }
}

/*将时间字符串转换成date对象*/
function getDate(dateStr) {
    /* 若日期是使用-分割的，全部转换成/
     因为只有日期时，js会将-分割的字符串基准时区设置为GMT，与当前时区相差8小时 */
    dateStr = dateStr.replace(/-/g, '/');
    return new Date(dateStr);
}

/*随机生成十六进制颜色数组*/
function randomColorArr(length) {
    let arr = [];
    for (let i = 0; i < length; i++) {
        arr.push(color16());
    }
    return arr;
}

/*随机生成数组*/
function randomArr(length) {
    let arr = [];
    for (let i = 0; i < length; i++) {
        arr.push(parseInt(Math.random() * 10 + 1));
    }
    return arr;
}

function color16() {//十六进制颜色随机
    let r = Math.floor(Math.random() * 256);
    let g = Math.floor(Math.random() * 256);
    let b = Math.floor(Math.random() * 256);
    let color = '#' + r.toString(16) + g.toString(16) + b.toString(16);
    return color;
}


/**
 * 获取当前月期号
 * 返回格式: YYYY-mm
 * */
function getCurrentMonthIssue(date) {
    let month = parseInt(date.getMonth() + 1);
    if (month < 10) {
        month = '0' + month
    }
    return date.getFullYear() + "-" + month;
}

/**
 * 获取当前的日期
 * 返回格式: YYYY-mm-dd
 * */
function getCurrentDate(date) {
    let month = parseInt(date.getMonth() + 1);
    let day = date.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取本周的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-23
 * */
function getCurrentWeekFirstDay(date) {
    let weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000)
    let firstMonth = Number(weekFirstDay.getMonth()) + 1

    if (firstMonth < 10) {
        firstMonth = '0' + firstMonth
    }
    let weekFirstDays = weekFirstDay.getDate();
    if (weekFirstDays < 10) {
        weekFirstDays = '0' + weekFirstDays;
    }
    return weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
}

/**
 * 获取本周的最后一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-29
 * */
function getCurrentWeekLastDay(date) {
    let weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000)
    let weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000)
    let lastMonth = Number(weekLastDay.getMonth()) + 1
    if (lastMonth < 10) {
        lastMonth = '0' + lastMonth
    }
    let weekLastDays = weekLastDay.getDate();
    if (weekLastDays < 10) {
        weekLastDays = '0' + weekLastDays;
    }
    return weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
}

/**
 * 获取当前月的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-01
 * */
function getCurrentMonthFirstDay(date) {
    // let date = new Date();
    date.setDate(1);
    let month = parseInt(date.getMonth() + 1);
    let day = date.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 返回当年的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-01-01
 * */
function getCurrentYearFirstDay(date) {
    // let date = new Date();
    date.setDate(1);
    date.setMonth(0);
    let month = parseInt(date.getMonth() + 1);
    let day = date.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取当前月的最后一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-30
 * */
function getCurrentMonthLastDay() {
    let date = new Date();
    let currentMonth = date.getMonth();
    let nextMonth = ++currentMonth;
    let nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    let oneDay = 1000 * 60 * 60 * 24;
    let lastTime = new Date(nextMonthFirstDay - oneDay);
    let month = parseInt(lastTime.getMonth() + 1);
    let day = lastTime.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取下个月的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-30
 * */
function nextMonthFirstDay(dateStr) {
    let date = new Date(dateStr);
    let currentYear = date.getFullYear();
    let currentMonth = date.getMonth();
    let nextMonth = ++currentMonth;
    let nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
    let lastTime = new Date(nextMonthFirstDay);
    let month = parseInt(lastTime.getMonth() + 1);
    let nextYear;
    if(nextMonth == 12){
    	++currentYear;
    }
    let day = lastTime.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return currentYear + '-' + month + '-' + day;
}

/**
 * 获取下一年的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2021-01-01
 * */
function nextYearFirstDay(dateStr) {
    let date = new Date(dateStr);
    let currentYear = date.getFullYear();
    let nextYear = ++currentYear;
    let nextYearFirstDay = new Date(nextYear, 0, 1);
    let lastTime = new Date(nextYearFirstDay);
    let month = parseInt(lastTime.getMonth() + 1);
    let day = lastTime.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return lastTime.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取下周的第一天
 * 返回格式: YYYY-mm-dd
 *    例子: 当日为: 2020-11-27
 *      返回日期为: 2020-11-29
 * */
function nextWeekFirstDay(dateStr) {
    let date = new Date(dateStr);
    let weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000)
    let lastDay = new Date((weekFirstDay / 1000 + 7 * 86400) * 1000)
    let lastMonth = Number(lastDay.getMonth()) + 1
    if (lastMonth < 10) {
        lastMonth = '0' + lastMonth
    }
    let weekLastDays = lastDay.getDate();
    if (weekLastDays < 10) {
        weekLastDays = '0' + weekLastDays;
    }
    return weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
}

/*日期格式化*/
function dateFormat(fmt, date) {
    let ret;
    const opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
    };
    for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
            fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
        ;
    }
    ;
    return fmt;
}