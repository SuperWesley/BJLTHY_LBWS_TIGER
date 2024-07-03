package com.bjlthy.lbss.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateHelper {
    /**
     * @author: 张宁
     * @description: 获取当前日期 最近一周的日期
     * @param: 参数
     * @return: List
     * @throws:
     */
     public static List<String> getWeekDays(Date todayDate) {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         List<String> list = new ArrayList<>();
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(todayDate);
         calendar.add(Calendar.DATE, -8);
         for (int i = 0; i < 7; i++) {
             calendar.add(Calendar.DATE, 1);
             list.add(sdf.format(calendar.getTime()));
         }
         return list;
     }

     /**
      * @author: 张宁
      * @description: 根据当前日期获得所在周的日期
      * @param: 参数
      * @return: List
      * @throws:
      */
	public static List<String> getWeekDays(String todayDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		try {
			Date mdate = dateFormat.parse(todayDate);
			int b = mdate.getDay();
			if(b == 0){
				b =7;
			}
			Date fdate;
			Long fTime = mdate.getTime() - b * 24 * 3600000;
			for (int a = 1; a <= 8; a++) {
				fdate = new Date();
				fdate.setTime(fTime + (a * 24 * 3600000));
				String day = dateFormat.format(fdate);
				list.add(day);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
    
	
    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
//            tempEnd.add(Calendar.DATE, 1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取两个日期之间的所有月份
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getMonthDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
        Calendar tempStart = Calendar.getInstance();
        Calendar tempEnd = Calendar.getInstance();
        try {
            
            tempStart.setTime(sdf.parse(startTime));
            tempStart.set(tempStart.get(Calendar.YEAR), tempStart.get(Calendar.MONTH), 1);
           
            tempEnd.setTime(sdf.parse(endTime));
            tempEnd.set(tempEnd.get(Calendar.YEAR), tempEnd.get(Calendar.MONTH), 1);
//            tempEnd.add(Calendar.DATE, 1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(sdf.format(tempStart.getTime()));
                tempStart.add(Calendar.MONTH, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tempStart = null;tempEnd=null;
        return days;
    }
    
    /**
     * 获取两个日期之间的所有年份
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return
     */
    public static List<String> getYearDays(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");//格式化为年月1
        Calendar tempStart = Calendar.getInstance();
        Calendar tempEnd = Calendar.getInstance();
        try {
            
            tempStart.setTime(sdf.parse(startTime));
            int startYear = tempStart.get(Calendar.YEAR);
            
            tempEnd.setTime(sdf.parse(endTime));
//            tempEnd.set(tempEnd.get(Calendar.YEAR), 1996);
            int endYear = tempEnd.get(Calendar.YEAR);
            // tempEnd.add(Calendar.DATE, 1);// 日期加1(包含结束)
            while (startYear<=endYear){
            	days.add(startYear+"");
            	startYear++;
//                tempStart.add(Calendar.YEAR, 1);
            }
            /*while (tempStart.before(tempEnd)) {
                days.add(sdf.format(tempStart.getTime()));
                tempStart.add(Calendar.YEAR, 1);
            }*/

        } catch (ParseException e) {
            e.printStackTrace();
        }
        tempStart = null;tempEnd=null;
        return days;
    }

    /**
     * @author: 张宁
     * @description: 根据当前日期获得所在月的日期
     * @param: 参数
     * @return: List
     * @throws:
     */
	public static List<String> getMonthDays(String todayDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		try {
			Date mdate = dateFormat.parse(todayDate);
			int b = mdate.getDay();
			Date fdate;
			Long fTime = mdate.getTime() - b * 24 * 3600000;
			for (int a = 1; a <= 7; a++) {
				fdate = new Date();
				fdate.setTime(fTime + (a * 24 * 3600000));
				String day = dateFormat.format(fdate);
				list.add(day);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

    /**
     * @author: 张宁
     * @description: 获取当前日期 最近一年
     * @param: 参数
     * @return: List
     * @throws:
     */
    public static List<String> getYearMonths(Date todayDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(sdf.format(calendar.getTime()));
        }
        return list;
    }

    /**
     * @author: 张宁
     * @description: 获取当前月第一天
     * @param: 参数
     * @return: String
     * @throws:
     */
    public static String getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = date.getMonth();
        // 设置月份
        calendar.set(Calendar.MONTH, month);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 00:00:00";
    }

    /**
     * @author: 张宁
     * @description: 获取下个月第一天
     * @param: 参数
     * @return: String
     * @throws:
     */
    public static String getNextDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = date.getMonth()+1;
        // 设置月份
        calendar.set(Calendar.MONTH, month);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 00:00:00";
    }
    /**
     * @author: 张宁
     * @description: 获取当前月最后一天
     * @param: 参数
     * @return: String
     * @throws:
     */
    public static String getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = date.getMonth();
        // 设置月份
        calendar.set(Calendar.MONTH, month);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (month == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 23:59:59";
    }

    /**
     * @author: 张宁
     * @description: 获取当前月最后一天
     * @param: 参数
     * @return: String
     * @throws:
     */
    public static String getLastDayOfMonth2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = date.getMonth();
        // 设置月份
        calendar.set(Calendar.MONTH, month);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (month == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
    
    /**
     * @author: 张宁
     * @description: 获取周一到周日
     * @param: 参数
     * @return: Map
     * @throws:
     */
    public static Map<String, Integer> getweekOneToFive() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("周一", 0);
        map.put("周二", 0);
        map.put("周三", 0);
        map.put("周四", 0);
        map.put("周五", 0);
        map.put("周六", 0);
        map.put("周日", 0);
        return map;
    }
    
    /**
     * 获取当前日期前几天的日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getLastDay(String startTime,int day){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.DATE,-day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前日期后几天的日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getNextDay(String startTime,int day){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.DATE,+day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前日期前几天的日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getBeforDay(String startTime,int day){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.DATE,-day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前时间前几小时的时间
     * @param startTime
     * @param day
     * @return
     */
    public static String getNextHour(String startTime,int hour){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.HOUR,hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前时间前几小时的时间
     * @param startTime
     * @param day
     * @return
     */
    public static String getBeforeHour(String startTime,int hour){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.HOUR,hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前月份后几个月日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getNextMonth(String startTime,int month){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.MONTH,month);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前月份前几个月日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getLastMonth(String startTime,int month){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.MONTH,-month);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前年份前几年日期
     * @param startTime
     * @param day
     * @return
     */
    public static String getNextYear(String startTime,int year){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.YEAR,year);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一天日期
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static String getYearLast(int year){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        String yearLast = sdf.format(currYearLast); 
        return yearLast;
    }
    
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static String getYearFirst(int year){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        String yearFirst = sdf.format(currYearFirst); 
        return yearFirst;
    }
    
    /**
     * 获取当前日期前几秒时间
     * @param startTime
     * @param day
     * @return
     */
    public static String getLastSecond(String startTime,int second){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.SECOND,-second);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一秒时间
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }

    /**
     * 获取当前日期后几秒时间
     * @param startTime
     * @param day
     * @return
     */
    public static String getNextSecond(String startTime,int second){

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(sdf.parse(startTime));
            calendar.add(calendar.SECOND,+second);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //获取下一秒时间
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    /**
     * 获取当前日期前几小时时间
     * @param startTime
     * @param day
     * @return
     */
    public static String getLastHour(String startTime,int hour){
    	
    	Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			calendar.setTime(sdf.parse(startTime));
			calendar.add(calendar.HOUR,-hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //获取下一秒时间
        String endTime = sdf.format(calendar.getTime());
        return endTime;
    }
    
    public static void main(String[] args) {
		int year = Integer.valueOf(DateUtil.getYear());
		String day = getYearLast(year);
		System.out.println(day);
	}
}
