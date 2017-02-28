package zzjz.util;

/** 定义系统级别的常量，比如系统名称，系统标识
* @ClassName: ConstantUtil 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 李飞
* @date 2015年7月22日 上午10:14:37
*/
public class ConstantUtil {

	public static String getValue(String key){
		ConfigUtil.load("system.properties");
		return ConfigUtil.getProperty(key);
	}
	
	/** 获取系统的超管
	* @Title: getAdmin
	* @Description:  获取系统的超管
	* @return
	* @throws 
	*/
	public static String getAdmin(){
		return getValue("tyqxgl.administrator");
	}
	
	/** 
	* @Title: getSystemName
	* @Description: 系统名称
	* @return
	* @throws 
	*/
	public static String getSystemName(){
		return getValue("tyqxgl.systemname");
	}
	
	/** 
	* @Title: getSystemSign
	* @Description: 系统标识
	* @return
	* @throws 
	*/
	public static String getSystemSign(){
		return getValue("tyqxgl.systemsign");
	}
}
