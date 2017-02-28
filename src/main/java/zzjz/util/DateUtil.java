package zzjz.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/** 
* @ClassName: DateUtil 
* @Description: 日期操作工具类
* @author 梅宏振
* @date 2015年3月4日 下午1:37:28
*/
public class DateUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final  String DATE_FORMATE1 = "yyyy-MM-dd HH:mm:ss";
	
	/** 
	* @Title: StringToDate
	* @Description: 字符串转日期
	* @param dateStr
	* @param dateFormate
	* @return 如果转换失败返回null
	* @throws 
	*/
	public static Date StringToDate(String dateStr,String dateFormate){
		SimpleDateFormat sdft = new SimpleDateFormat(dateFormate);
		Date date = null;
		try {
			date = sdft.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	/** 
	* @Title: StringToDate
	* @Description: 按照 yyyy-MM-dd HH:mm:ss格式解析字符串为Date
	* @param dateStr
	* @return
	* @throws 
	*/
	public static Date StringToDate(String dateStr){
		SimpleDateFormat sdft = new SimpleDateFormat(DATE_FORMATE1);
		Date date = null;
		try {
			date = sdft.parse(dateStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage() + "时间的格式不正确，应为：yyyy-MM-dd HH:mm:ss");
		}
		return date;
	}
	
	/** 
	* @Title: StringToDate
	* @Description: 按照 yyyy-MM-dd HH:mm:ss格式解析字符串为Date
	* @param dateStr
	* @return
	* @throws 
	*/
	public static Timestamp StringToTimestamp(String dateStr){
		SimpleDateFormat sdft = new SimpleDateFormat(DATE_FORMATE1);
		long time = 0;
		try {
			time = sdft.parse(dateStr).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(time);
	}
	
	/** 
	* @Title: dateToString
	* @Description: 日期转字符串
	* @param date
	* @param dateFormate
	* @return
	* @throws 
	*/
	public static String dateToString(Date date,String dateFormate){
		SimpleDateFormat sdft = new SimpleDateFormat(dateFormate);
		if(null == date){
			date = new Date(0);
		}
		return sdft.format(date);
	}
	
	/** 
	* @Title: dateToString
	* @Description: 日期转字符串,默认格式为yyyy-MM-dd HH:mm:ss
	* @param date
	* @return
	* @throws 
	*/
	public static String dateToString(Date date){
		return dateToString(date,DATE_FORMATE1);
	}
	
	/** 
	* @Title: getInitDate
	* @Description: 获取日期的初始化时间("1970-01-01 00:00:00") 
	* @return
	* @throws 
	*/
	public static Date getInitDate() {
		Date date = new Date();
		try {
			date = sdf.parse("1970-01-01 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.getInitDate());
	}
	/** 
	* @Title: getDate
	* @Description: 取得当前日期的某个日期
	* @param dateStr  日期字符串
	* @param addYear  要转变的年数
	* @param addMonth 要转变的月数
	* @param addDate  要转变的日数
	* @return
	* @throws Exception
	* @throws 
	*/
	public static String changeDate(String dateStr, int addYear, int addMonth,
			int addDate) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date sourceDate = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sourceDate);
			cal.add(Calendar.YEAR, addYear);
			cal.add(Calendar.MONTH, addMonth);
			cal.add(Calendar.DATE, addDate);

			SimpleDateFormat returnSdf = new SimpleDateFormat(
					"yyyy-MM-dd");
			String dateTmp = returnSdf.format(cal.getTime());
//			java.util.Date returnDate = returnSdf.parse(dateTmp);
			return dateTmp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 生成随机文件名：当前年月日时分秒+五位随机数
	 *
	 * @return
	 */
	public static String getRandomFileName() {
		SimpleDateFormat simpleDateFormat;
		simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String str = simpleDateFormat.format(date);
		Random random = new Random();
		int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
		return rannum + str;// 当前时间
	}

	public static String getNowDate(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String dateStr = dateFormat.format(now);
		return dateStr;
	}
	public static void main(String args1[],String args2[]){
		System.out.println(DateUtil.dateToString(null));
	}
}
