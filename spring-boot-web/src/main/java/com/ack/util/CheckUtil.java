/**
 *
 */
package com.ack.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 数据校验工具类
 * 
 * @author chen.zhao @DATE: 2018年3月7日
 */
public class CheckUtil {

	final static String EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	final static String PHONE = "^1[\\d]{10}$";
	/**
	 * 正整数
	 */
	final static String POSITIVE_INTEGER = "^[0-9]*[1-9][0-9]*$";
	
	final static String INTEGER="^(0|[1-9][0-9]*|-[1-9][0-9]*)$";

	/**
	 * 邮箱校验
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		return str.matches(EMAIL);
	}

	/**
	 * 手机号校验
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str) {
		return str.matches(PHONE);
	}

	/**
	 * 正整数校验
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPositiveInteger(String str) {
		return str.matches(POSITIVE_INTEGER);
	}
	/**
	 * 正整数+0+负整数
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		return str.matches(INTEGER);
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
	 * 解析str日期，支持格式：yyyy-MM-dd yyyyMMdd yyyy-MM-dd hh:mm:ss yyyy-MM-dd hh:mm:ss
	 * 
	 * @param date1
	 * @return
	 * @throws Exception
	 */
	private static Date parseDate(String date1) throws Exception {
		DateFormat dfyyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfyyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		DateFormat dfyyyy_MM_ddhhmmss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat dfyyyyMMddhhmmss = new SimpleDateFormat("yyyyMMddhhmmss");
		Date resultDate = null;
		try {
			resultDate = dfyyyy_MM_dd.parse(date1);
		} catch (Exception e) {
			try {
				resultDate = dfyyyyMMdd.parse(date1);
			} catch (Exception e1) {
				try {
					resultDate = dfyyyy_MM_ddhhmmss.parse(date1);
				} catch (Exception e2) {
					try {
						resultDate = dfyyyyMMddhhmmss.parse(date1);
					} catch (Exception e3) {
						throw new Exception("#Date parse exception");
					}
				}
			}
		}
		return resultDate;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(compareDate("2018-03-07", "2018-03-07"));
		System.out.println(compareDate("2018-03-10", new Date()));
		System.out.println(compareDate("2018-03-08", "2018-03-07"));
		System.out.println(compareDate("20180307172559", "20180307172557"));
		Date nowDate=new Date();
		System.out.println(nowDate);
	}

}
