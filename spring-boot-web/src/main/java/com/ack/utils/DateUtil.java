/**
 *
 */
package com.ack.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间工具类-使用getSdf获取的format对象才是线程安全的
 * @author chenzhao @date Oct 16, 2019
 */
public class DateUtil {

	/** 锁对象 */
	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>>();

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 *
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					// 使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	public synchronized static Date parseDate(String date1) throws Exception {

		Date resultDate = null;
		try {
			resultDate = getSdf("yyyy-MM-dd HH:mm:ss").parse(date1);
		} catch (Exception e) {
			try {
				resultDate = getSdf("yyyyMMddHHmmss").parse(date1);
			} catch (Exception e1) {
				try {
					resultDate = getSdf("yyyy-MM-dd").parse(date1);
				} catch (Exception e2) {
					try {
						resultDate = getSdf("yyyyMMdd").parse(date1);
					} catch (Exception e3) {
						throw new Exception("#Date parse exception");
					}
				}
			}
		}
		return resultDate;
	}

	/**
	 * 日期大小比较，支持yyyy-MM-dd hh:mm:ss + yyyyMMddhhmmss + yyyy-MM-dd + yyyyMMdd格式
	 * 参数支持string和date任意格式组合
	 * 
	 * @param date1
	 * @param date2
	 * @return # date1大于date2:1 # 等于:0 # 小于:-1
	 * @throws Exception
	 */
	public static int compareDate(Object date1, Object date2) throws Exception {
		Date dt1 = null, dt2 = null;
		// #参数自动处理
		if (date1 instanceof String) {
			dt1 = parseDate(date1 + "");
		}
		if (date2 instanceof String) {
			dt2 = parseDate(date2 + "");
		}
		if (date1 instanceof Date) {
			dt1 = (Date) date1;
		}
		if (date2 instanceof Date) {
			dt2 = (Date) date2;
		}

		if (dt1 != null && dt2 != null && dt1.getTime() > dt2.getTime()) {
			return 1;
		} else if (dt1 != null && dt2 != null && dt1.getTime() < dt2.getTime()) {
			return -1;
		} else if (dt1 != null && dt2 != null && dt1.getTime() == dt2.getTime()) {
			return 0;
		}
		return 0;
	}

	/**
	 * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return getSdf(pattern).format(date);
	}

	public static Date parse(String dateStr, String pattern) throws ParseException {
		return getSdf(pattern).parse(dateStr);
	}

	/**
	 * 判断当前时间是否在【beginTime】和【endTime】之间
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static Boolean validateValidDate(Object beginTime, Object endTime) throws Exception {
		Date date = new Date();
		// date1大于date2:1 # 等于:0 # 小于:-1
		if (compareDate(beginTime, date) == -1 && compareDate(endTime, date) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断middleDate是否在时间区间内，包左不包右 validateValidDate(2015-05-05,
	 * 2015-05-06,2015-05-05) = true validateValidDate(2015-05-05,
	 * 2015-05-06,2015-05-06) = false
	 * 
	 * @author chenzhao @date May 5, 2019
	 * @param beginTime
	 * @param endTime
	 * @param middleDate
	 * @return
	 * @throws Exception
	 */
	public static Boolean validateValidDate(Object beginTime, Object endTime, Date middleDate) throws Exception {
		// date1大于date2:1 # 等于:0 # 小于:-1
		if (compareDate(beginTime, middleDate) <= 0 && compareDate(endTime, middleDate) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算2个日期的毫秒差值，支持string和date类型混参
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static long calcInterval(Object beginTime, Object endTime) throws Exception {
		Date dt1 = null, dt2 = null;
		if (beginTime instanceof String) {
			dt1 = parseDate(beginTime + "");
		} else {
			dt1 = (Date) beginTime;
		}
		if (endTime instanceof String) {
			dt2 = parseDate(endTime + "");
		} else {
			dt2 = (Date) endTime;
		}
		return dt2.getTime() - dt1.getTime();
	}

	public static void main(String[] args) throws Exception {
		Date dt = new Date();
		dt.setHours(23);
		dt.setMinutes(59);
		dt.setSeconds(59);
		System.out.println(calcInterval("2018-04-23 05:08:59", dt));
		System.out.println(calcInterval("2018-04-23 05:08:59", "2018-04-23 23:59:59"));

	}

}
