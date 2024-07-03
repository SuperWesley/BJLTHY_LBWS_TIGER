package com.bjlthy.lbss.tool;

import com.bjlthy.common.utils.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 日期工具类
 * @date 2021年2月25日 下午10:57:37
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class DateUtil extends DateUtils{

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm","yyyyMMddHHmmss","yyyyMMddHHmmssS" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "yyyyMMddHHmmssS");
	}

	public static void main(String[] args) {
		System.out.println(getTime());
	}
	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss.SSS）
	 */
	public static String getDateTimeMS() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd_HH）
	 */
	public static String getDateHour() {
		return formatDate(new Date(), "yyyy-MM-dd_HH");
	}
	
	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 功能描述：返回小时
	 * @return 返回小时
	 */
	public static String getHour() {

		return formatDate(new Date(), "HH");
	}

	/**
	 * 功能描述：返回分
	 *
	 * @return 返回分钟
	 */
	public static String getMinute() {
		return formatDate(new Date(), "mm");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
	
	/**
	 * 判断字符串是否是日期
	 * @param timeString
	 * @return
	 */
	public static boolean isDate(String timeString){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		try{
			format.parse(timeString);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 格式化时间
	 * @param timestamp
	 * @return
	 */
	public static String dateFormat(Date timestamp){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(timestamp);
	}
	
	/**
	 * 获取系统时间Timestamp
	 * @return
	 */
	public static Timestamp getSysTimestamp(){
		return new Timestamp(new Date().getTime());
	}
	
	/**
	 * 获取系统时间Date
	 * @return
	 */
	public static Date getSysDate(){
		return new Date();
	}
	
	/**
	 * 生成时间随机数 
	 * @return
	 */
	public static String getDateRandom(){
		String s=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		return s;
	}
	
	 /**
     * 
     * @author 张宁
     * @description 判断当前日期是星期几
     * @param 参数
     * @date 2020年12月19日 下午6:06:56
     */
 	public static String dayForWeek(String date)  {  

         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
         String[] weekDays = {"星期日","星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
         int w=1;
 		try {
 			Date tmpDate = format.parse(date);
 			Calendar cal = Calendar.getInstance(); 
 			cal.setTime(tmpDate);
 			w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
 			if (w < 0){
 				w = 0;
 			}
 			
 		} catch (Exception e1) {
 			e1.printStackTrace();
 		}  
 		return weekDays[w];

     }  
 	
 	 /**
     * 计算相差分钟
     */
    public static long getDiffMin(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long min = diff / nm;

        return min;
    }

	/**
	 * 根据自定义格式格式化字符串为Date
	 *
	 * @param dateString
	 * @param datePattern
	 * @return java.util.Date
	 * @Description 根据自定义格式格式化字符串为Date
	 * @author LiFeng
	 * @date 2018/8/22 18:52
	 */
	public static Date formatStringToDate(String dateString, String datePattern) {
		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		try {
			return format.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串转日期
	 *
	 * @param dateString
	 * @return java.util.Date
	 * @Description 字符串转日期
	 * @author LiFeng
	 * @date 2018/8/22 18:52
	 */
	public static Date formatStringToDate(String dateString) {
		if (StringUtils.isBlank(dateString)) {
			return null;
		}
		if (dateString.matches("^\\d{4}-\\d{1,2}$")) {
			return formatStringToDate(dateString, "yyyy-MM");
		} else if (dateString.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
			return formatStringToDate(dateString, "yyyy-MM-dd");
		} else if (dateString.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
			return formatStringToDate(dateString, "yyyy-MM-dd HH:mm");
		} else if (dateString.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
			return formatStringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
		} else if (dateString.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}$")) {
			return formatStringToDate(dateString.substring(0, dateString.indexOf(".")), "yyyy-MM-dd HH:mm:ss");
		} else if (dateString.matches("^\\d{4}-\\d{1,2}-\\d{1,2},{1}\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{3}$")) {
			dateString = dateString.replace(",", " ");
			return formatStringToDate(dateString.substring(0, dateString.indexOf(".")), "yyyy-MM-dd HH:mm:ss");
		} else {
			return formatStringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
		}
	}

	public static long getSec(String time){

		String[] my =time.split(":");

		int hour =Integer.parseInt(my[0]);

		int min =Integer.parseInt(my[1]);

		int sec =Integer.parseInt(my[2]);

		long zong =hour*3600+min*60+sec;

		return zong;
	}

}