package com.niiwoo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间操作类
 * 
 * @author alan
 *
 */
public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyyMM");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat sdfTime2 = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat sdfTimelong = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private final static SimpleDateFormat sdfTimeToMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private final static String registerDate = "EEE MMM dd HH:mm:ss zzz yyyy";

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取今天年月日 YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String gettoDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取前天的年月日 YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDayBeforeYesterDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 2);
		return sdfDay.format(c.getTime());
	}

	/**
	 * 获取昨天年月日 YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getYesterDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		return sdfDay.format(c.getTime());
	}
	
	/**
	 * 获取明天年月日 YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getTomorrowDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		return sdfDay.format(c.getTime());
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @param longTimes
	 * @return
	 */
	public static String getTime(String longTimes, int length) {
		if (length == 8) {
			Date date = new Date();
			// 先把毫秒转日期
			if (longTimes.length() == 13) {
				date.setTime(Long.parseLong(longTimes));
			}
			return sdfDay.format(date);
		} else if (length == 14) {
			Date date = null;
			try {
				date = sdfTime2.parse(longTimes.substring(0, 14));
			} catch (Exception e) {
				date = new Date();
				date.setTime(Long.parseLong(longTimes));
			}
			return sdfTime.format(date);
		} else if (length == 19) {
			Date date = new Date();
			// 先把毫秒转日期
			if (longTimes.length() == 16) {
				date.setTime(Long.parseLong(longTimes.substring(0, 13)));
				return sdfTime.format(date);
			}
			try {
				date = sdfTimelong.parse(longTimes.substring(0, 17));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return sdfTime.format(date);
		} else {
			return longTimes;
		}
	}

	/**
	 * 获取yyyyMMDDHHmmssSSS
	 * 
	 * @return
	 */
	public static String getLongTime() {
		return sdfTimelong.format(new Date());
	}

	/**
	 * 当前时间微秒
	 * 
	 * @return
	 */
	public static Long getTimeMicrosecond() {
		return new Date().getTime() * 1000;
	}

	/**
	 * 获取YYYY-MM-DD HH:mm格式
	 * 
	 * @return
	 */
	public static String getTimeToMin() {
		return sdfTimeToMin.format(new Date());
	}

	public static String getTime(java.sql.Timestamp timestamp) {
		return sdfTime.format(new Date(timestamp.getTime()));
	}

	public static String getTime(java.util.Date date) {
		return sdfTime.format(date);
	}

	public static String getTime(java.sql.Date date) {
		return sdfTime.format(date);
	}

	/**
	 * 获取YYYYMM格式
	 * 
	 * @return
	 */
	public static String getMonth() {
		return sdfMonth.format(new Date());
	}

	/**
	 * 获取当前年月,格式:yyyymm.
	 * 
	 * @param beforeMonth
	 * @return
	 */
	public static String getYearMonth() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return (c.get(Calendar.YEAR)) + "" + (c.get(Calendar.MONTH) + 1);
	}

	/**
	 * 获取当前多少个月之前的年月,格式:yyyymm.
	 * 
	 * @param beforeMonth
	 * @return
	 */
	public static String getYearMonthBeforeMonth(int beforeMonth) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -beforeMonth);
		return (c.get(Calendar.YEAR)) + "" + (c.get(Calendar.MONTH) + 1);
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws @author
	 *             luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * @Title: compareDateTime
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws @author
	 */
	public static boolean compareDateTime(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		try {
			return sdfTime.parse(s).getTime() >= sdfTime.parse(e).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 功能描述：时间相减得到天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getDaySub(Date beginDate, Date endDate) {
		long day = 0;
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 获取当前时间多少天之后的时刻的毫秒数
	 * 
	 * @param day
	 *            多少天
	 * @return
	 */
	public static Long getAfterDayTimeMillis(int day) {
		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, day); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();
		return date.getTime();
	}

	/**
	 * 获取指定多少天之前是否在本月
	 * 
	 * @param day
	 * @return
	 */
	public static boolean getBeforeDayIsNowMonth(int day) {
		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.setTime(new Date());
		int nowMoth = calendar.get(Calendar.MONTH);// 今天是几月
		calendar.add(Calendar.DATE, -day); // 日期减 如果不够减会将月变动
		int resMonth = calendar.get(Calendar.MONTH);// 多少天之前是几月
		return nowMoth == resMonth;// 两个月份是否相等
	}

	/**
	 * 获取当前时间多少天之钱的时刻的微秒数
	 * 
	 * @param day
	 * @return
	 */
	public static Long getBeforeDayTimeMicrosecond(int day) {
		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, -day); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();
		return date.getTime() * 1000;
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 获得当前日期加减指定天数后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDay(int days) {
		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdfd.format(date);
		return dateStr;
	}

	/**
	 * 得到指导定日期n天之后的日期
	 * 
	 * @param inDay
	 *            yyyy-MM-dd
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static String getAfterDayDate(String inDay, String days) throws ParseException {
		int daysInt = Integer.parseInt(days);
		Calendar calendar = Calendar.getInstance(); // java.util包
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		calendar.setTime(sdfd.parse(inDay.length() == 10 ? inDay + " 00:00:00" : inDay));
		calendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar calendar = Calendar.getInstance(); // java.util包
		calendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = calendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * long转日期
	 * 
	 * @param time
	 * @return
	 */
	public static String convertLong2Str(long time) {
		if (time > 0l) {
			Date date = new Date(time);
			return sdfTime.format(date);
		}
		return "";
	}

	/**
	 * 获取下月
	 * 
	 * @param cmonth
	 *            yyyymm
	 * @return
	 */
	public static String getNextMonth(String cmonth) {
		int iYear = Integer.parseInt(cmonth.substring(0, 4));
		int iMonth = Integer.parseInt(cmonth.substring(4, 6));
		String sMonth = "";
		if (iMonth == 12) {
			iYear = iYear + 1;
			iMonth = 1;
		}
		if (iMonth < 10) {
			sMonth = "0" + iMonth;
		} else {
			sMonth = iMonth + "";
		}
		return iYear + sMonth;
	}

	/**
	 * 获取上月
	 * 
	 * @param cmonth
	 *            yyyymm
	 * @return
	 */
	public static String getlastMonth(String cmonth) {
		int iYear = Integer.parseInt(cmonth.substring(0, 4));
		int iMonth = Integer.parseInt(cmonth.substring(4, 6));
		String sMonth = "";
		if (iMonth == 1) {
			iYear = iYear - 1;
			iMonth = 12;
		} else {
			iMonth--;
		}
		if (iMonth < 10) {
			sMonth = "0" + iMonth;
		} else {
			sMonth = iMonth + "";
		}
		return iYear + sMonth;
	}

	/**
	 * 获得当前时间距离今天23:59:59 还有多少秒
	 * 
	 * @return
	 */
	public static int lastZeroSecond() {
		long current = System.currentTimeMillis();// 当前时间毫秒数
		long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();// 今天零点零分零秒的毫秒数
		long twelve = zero + 24 * 60 * 60 * 1000 - 1;// 今天23点59分59秒的毫秒数
		return (int) (twelve - current) / 1000;// 当前时间距离今天23:59:59 还有多少秒
	}

	/**
	 * 获得当天的开始时刻,精确到秒; 如 如 2017-02-05 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayFirstTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return zero;
	}

	/**
	 * 获得当天的结束时刻,精确到秒; 如 如 2017-02-05 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayLastTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date lastTime = calendar.getTime();
		return lastTime;
	}

	/**
	 * 获得当月第一天开始时刻,精确到秒; 如 2017-02-01 00:00:00
	 * 
	 * @return
	 */
	public static Date getMonthFirstDayTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date firstDayTime = calendar.getTime();
		return firstDayTime;
	}

	/**
	 * 获得当月最后一天结束时刻,精确到秒; 如 2017-03-31 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDayTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date lastDayTime = calendar.getTime();
		return lastDayTime;
	}

	/**
	 * 获取时间的月第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String beginTime1 = sdf.format(calendar.getTime());
		return beginTime1;
	}

	/**
	 * 获取时间的月最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endTime1 = sdf.format(calendar.getTime());
		return endTime1;
	}

	/**
	 * 参数日期对应上一个月的日期
	 * 
	 * @return
	 */
	public static String getLastDayByCurrDay(String currdate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = fomatDate(currdate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		date = calendar.getTime();

		return dateFormat.format(date);
	}

	/**
	 * 返回两个日期之间的年月份，格式：yyyyMM
	 * 
	 * @param minstr1
	 * @param maxstr2
	 * @return
	 */
	public static ArrayList<String> betweenDateByParam(String minstr1, String maxstr2) {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");// 格式化为年月

		Date date1 = null;
		Date date2 = null;
		if (minstr1.length() > 7 && minstr1.contains("-")) {
			date1 = fomatDate(minstr1);
		} else {
			date1 = fomatDateToYYYYMM(minstr1);
		}
		if (maxstr2.length() > 7 && maxstr2.contains("-")) {
			date2 = fomatDate(maxstr2);
		} else {
			date2 = fomatDateToYYYYMM(maxstr2);
		}
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
			result.add(sdf.format(date1));
		} else {
			calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 1);
			calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), 2);
			Calendar curr = calendar1;
			while (curr.before(calendar2)) {
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}
		}
		return result;
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDateToYYYYMM(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyyMM");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据传进来的月份增加N个月份之后的日期
	 * 
	 * @param currmonth
	 * @param addmonth
	 * @return
	 */
	public static String getAddMonthByCurrDate(String currmonth, int addmonth) {
		SimpleDateFormat df = null;
		if (currmonth.length() == 6 && !currmonth.contains("-")) {
			df = new SimpleDateFormat("yyyyMM");
		} else if (currmonth.length() == 8 && !currmonth.contains("-")) {
			df = new SimpleDateFormat("yyyyMMdd");
		} else if (currmonth.contains("-") && currmonth.length() > 6) {
			df = new SimpleDateFormat("yyyy-MM-dd");
		}
		Date cumonth = null;
		try {
			cumonth = df.parse(currmonth);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(cumonth);
		cal.add(Calendar.MONTH, addmonth);// 对月份进行计算增加N个月
		Date date = cal.getTime();
		return df.format(date);
	}

	/**
	 * 增加月份
	 * 
	 * @param currTime
	 * @param num
	 * @return
	 */
	public static String getAddMonthAfterDay(String currTime, int monthNum) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");// 格式化为年月
		try {
			Date curr = sdf.parse(currTime);
			calendar.setTime(curr);
			calendar.add(Calendar.MONTH, monthNum);
			return sdf.format(calendar.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return currTime;
	}

	/**
	 * 格式化日期,yyyyMMdd
	 * 
	 * @return
	 */
	public static Date fomatDateToYMD(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回之前天数
	 * 
	 * @param currTime
	 *            当前时间
	 * @param num
	 *            返回之前多少天
	 * @return
	 */
	public static String getBeforeDay(String currTime, int beforeDay) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化为年月
		try {
			Date curr = sdf.parse(currTime);
			calendar.setTime(curr);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - beforeDay);
			return sdf.format(calendar.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return currTime;
	}

	/**
	 * 返回之前的某一月
	 * 
	 * @param currTime
	 *            当前时间
	 * @param num
	 *            返回之前多少月
	 * @return
	 */
	public static String getBeforeMonth(String currTime, int beforeMonth) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");// 格式化为年月
		try {
			Date curr = sdf.parse(currTime);
			calendar.setTime(curr);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - beforeMonth);
			return sdf.format(calendar.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return currTime;
	}

	/**
	 * @Title: 转换为月份yyyyMM
	 * @param dateStr
	 *            yyyy-MM-dd H24:mm:ss 或yyyy-MM-dd 或 yyyyMMdd
	 * @return yyyyMM
	 */
	public static String getMonth(String dateStr) {

		try {
			DateFormat fmt = new SimpleDateFormat("yyyyMM");
			if (dateStr == null || dateStr.length() == 0)// 等于null
				return null;
			if (dateStr.length() < 6) // 小于最小值的长度
				return null;

			if (dateStr.contains("-")) { // 转换
				dateStr = dateStr.replaceAll("-", "").replaceAll("\\s*", "");
				dateStr = dateStr.substring(0, 8);
			}
			Date time = sdfDays.parse(dateStr);
			dateStr = fmt.format(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateStr;
		}
		return dateStr;
	}

	/**
	 * 当前时间 是否在开始时间与结束时间范围内
	 * 
	 * @param beginTime
	 *            yyyyMM | yyyyMMdd | yyyy-MM-dd
	 * @param endTime
	 *            yyyyMM | yyyyMMdd | yyyy-MM-dd
	 * @param currTime
	 *            yyyyMM | yyyyMMdd | yyyy-MM-dd
	 * @return
	 */
	public static boolean isRange(String beginTime, String endTime, String currTime) {
		if (beginTime == null || endTime == null)
			return false;
		if (beginTime.compareTo(currTime) <= 0 && endTime.compareTo(currTime) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获得本周一时间
	 * 
	 * @return
	 */
	public static String getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return sdfDay.format(cal.getTime());
	}

	/**
	 * 获得本周日时间
	 * 
	 * @return
	 */
	public static String getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONDAY), cal1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.setTime(cal1.getTime());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return sdfDay.format(cal.getTime());
	}

	/**
	 * yyyyMMdd 格式日期 转 yyyy-MM-dd
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String strToStrFormatDate(String dateStr) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		String sfstr = "";
		try {
			sfstr = sf2.format(sf1.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	/**
	 * CTS转date
	 * 
	 * @param str
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static String getLocalDateString(String registerDateStr) {
		if (registerDateStr == null) {
			return "";
		} else if ("".equals(registerDateStr.trim())) {
			return "";
		}
		try {
			Date date = new SimpleDateFormat(registerDate, Locale.US).parse(registerDateStr);
			return sdfTime.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}
