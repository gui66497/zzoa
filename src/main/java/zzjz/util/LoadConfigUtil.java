package zzjz.util;

import java.util.Properties;

/** 
 * @ClassName: LoadConfigUtil 
 * @Description: 加载配置文件工具类
 * @Author: 梅宏振 
 */
public class LoadConfigUtil {

	private static Properties props = new Properties();
	
	static{
/*		try {
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
		
		Properties props = new Properties();		
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties"));
			
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
	 * 根据KEY读取配置
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		return props.getProperty(key);
	}

    /**
     * 修改配置
     * @param key
     * @param value
     */
    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    } 
	
    
    public static void main(String args[]) {
    	System.out.println(LoadConfigUtil.getValue("url"));
    }
    
    public static String readFileConfig(String fileName,String propertyKey){
		Properties props = new Properties();		
		try {
			
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		return  props.getProperty(propertyKey);
	}
}
