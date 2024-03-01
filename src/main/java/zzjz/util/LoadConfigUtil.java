package zzjz.util;

import java.util.Properties;

/**
 * @author 梅宏振
 * @version 2017-10-19
 * @ClassName: LoadConfigUtil
 * @Description: 加载配置文件工具类
 */
public class LoadConfigUtil {

    private static Properties props = new Properties();

    static {
        /*try {
            //props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("xtInfor.properties"));
			String configPath = new File(System.getProperty("user.dir")).getParent()+File.separator+"system.properties";
			System.out.println("configPath = " + configPath);
			File file = new File(configPath);
			FileReader fileReader = new FileReader(file);
			props.load(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        Properties props1 = new Properties();
        try {
            props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("props/system.properties"));

        /*String configPath = new File(System.getProperty("user.dir")).getParent()+File.separator+"system.properties";
            System.out.println("configPath = " + configPath);
			File file = new File(configPath);
			FileReader fileReader = new FileReader(file);
			props.load(fileReader);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return  props.getProperty(propertyKey);
    }

    /**
     * 根据KEY读取配置.
     *
     * @param key key
     * @return String
     */
    public static String getValue(String key) {
        return props.getProperty(key);
    }

    /**
     * 修改配置.
     *
     * @param key   key
     * @param value value
     */
    public static void updateProperties(String key, String value) {
        props.setProperty(key, value);
    }


    /*public static void main(String args[]) {
        System.out.println(LoadConfigUtil.getValue("url"));
    }*/

    /**
     * @param fileName    fileName
     * @param propertyKey propertyKey
     * @return String
     */
    public static String readFileConfig(String fileName, String propertyKey) {
        Properties props1 = new Properties();
        try {

            props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
        } catch (Exception e) {
            //
            e.printStackTrace();
        }
        return props1.getProperty(propertyKey);
    }
}
