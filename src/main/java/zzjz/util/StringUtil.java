package zzjz.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/** 
* @ClassName: StringUtil 
* @Description: 字符串操作工具类
* @author 梅宏振
* @date 2015年3月4日 下午4:41:57
*/
public class StringUtil {
	private static final int MAX_RECORDS_LIMIT = 800;
	/**
	 * @param <T> 
	* @Title: listConvertToString
	* @Description: 将List转换为以指定符号分隔的字符串 
	* @param list
	* @param separator
	* @return
	* @throws 
	*/
	public static <T> String listConvertToString(List<T> list, String separator) {
		StringBuilder result = new StringBuilder("");
		int i = 0;
		for(T it: list) {
			if(i>0) {
				result.append(separator).append(it);
			} else {
				result.append(it);
			}
			i++;
		}
		return result.toString();
	}
	public static String getInCondition(String filed,List list){
		StringBuilder result = new StringBuilder(" (");
		
		int round = (list.size() % MAX_RECORDS_LIMIT) == 0 ?
				(list.size() / MAX_RECORDS_LIMIT) : 
					(list.size() / MAX_RECORDS_LIMIT) + 1;
		System.out.println("delete round : " + round);
		
		for (int i = 0; i < round; i++) {
			int fromIndex = i * MAX_RECORDS_LIMIT;
			int toIndex = ((i+1) * MAX_RECORDS_LIMIT) > list.size() ? list.size() : ((i+1) * MAX_RECORDS_LIMIT);
			List subList = list.subList(fromIndex, toIndex);
			if(0 == i){
				result.append(filed).append("  in(").append(StringUtil.listConvertToString(subList, ",")).append(") ");
			}else{
				result.append(" or ").append(filed).append("  in(").append(StringUtil.listConvertToString(subList, ",")).append(") ");
			}
		}
		return result.append(")").toString();
	}
	/** 
	* @Title: getStringStream
	* @Description: 字符串转化为输入流
	* @param sInputString
	* @return
	* @throws 
	*/
	public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
						sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	/** 
	* @Title: getStreamString
	* @Description: 输入流转化为字符串 
	* @param tInputStream
	* @return
	* @throws 
	*/
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(
						new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 将字符串首字符转为大写
	 * @param str
	 * @return
	 */
	public static String upperFirstChar(String str) {
		if(StringUtils.isBlank(str)) {
			return "";
		}
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	
}
