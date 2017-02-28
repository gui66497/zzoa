package zzjz.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigUtil {

	private static Properties properties = new Properties();
	
	public static Properties load(String fileName){
		properties.clear();
		InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
		if(null == in){
			return properties;
		}
		try {
			
			properties.load(new InputStreamReader(in, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
	
	public static void main(String[] args) {
		ConfigUtil.load("system.properties");
		String value = ConfigUtil.getProperty("validation_query");
		System.out.println(value);
	}
}
