package zzjz.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author 彭鹏.
 * @version 2017-10-18
 */
public class ConfigUtil {

    private static Properties properties = new Properties();

    /**
     * @param fileName fileName
     * @return Properties
     */
    public static Properties load(String fileName) {
        properties.clear();
        InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (null == in) {
            return properties;
        }
        try {
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            properties.load(isr);
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    /**
     * @param key key
     * @return String
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /*public static void main(String[] args) {
        ConfigUtil.load("system.properties");
        String value = ConfigUtil.getProperty("validation_query");
        System.out.println(value);
    }*/
}
