package zzjz.util;

/**
 * 定义系统级别的常量，比如系统名称，系统标识.
 *
 * @author 李飞
 * @version 2015年7月22日 上午10:14:37
 * @ClassName: ConstantUtil
 * @Description: 这里用一句话描述这个类的作用
 */
public class ConstantUtil {

    /**
     * @param key key
     * @return String
     */
    public static String getValue(String key) {
        ConfigUtil.load("system.properties");
        return ConfigUtil.getProperty(key);
    }

    /**
     * 获取系统的超管.
     *
     * @return String
     * @Title: getAdmin
     * @Description: 获取系统的超管
     */
    public static String getAdmin() {
        return getValue("tyqxgl.administrator");
    }

    /**
     * @return String
     * @Title: getSystemName
     * @Description: 系统名称
     */
    public static String getSystemName() {
        return getValue("tyqxgl.systemname");
    }

    /**
     * @return String
     * @Title: getSystemSign
     * @Description: 系统标识
     */
    public static String getSystemSign() {
        return getValue("tyqxgl.systemsign");
    }
}
