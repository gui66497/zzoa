package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年3月4日 下午4:41:57
 * @ClassName: StringUtil
 * @Description: 字符串操作工具类
 */
public class StringUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    private static final int MAX_RECORDS_LIMIT = 800;

    /**
     * @param <T> T
     * @param list list
     * @param separator separator
     * @return String
     * @throws
     * @Title: listConvertToString
     * @Description: 将List转换为以指定符号分隔的字符串
     */
    public static<T> String listConvertToString(List<T> list, String separator) {
        StringBuilder result = new StringBuilder("");
        int i = 0;
        for (T it : list) {
            if (i > 0) {
                result.append(separator).append(it);
            } else {
                result.append(it);
            }
            i++;
        }
        return result.toString();
    }

    /**
     *
     * @param filed filed
     * @param list list
     * @return String
     */
    public static String getInCondition(String filed, List list) {
        StringBuilder result = new StringBuilder(" (");

        int round = (list.size() % MAX_RECORDS_LIMIT) == 0 ?
                (list.size() / MAX_RECORDS_LIMIT) :
                (list.size() / MAX_RECORDS_LIMIT) + 1;
        LOGGER.info("delete round : " + round);

        for (int i = 0; i < round; i++) {
            int fromIndex = i * MAX_RECORDS_LIMIT;
            int toIndex = ((i + 1) * MAX_RECORDS_LIMIT) > list.size() ? list.size() : ((i + 1) * MAX_RECORDS_LIMIT);
            List subList = list.subList(fromIndex, toIndex);
            if (0 == i) {
                result.append(filed).append("  in(").append(StringUtil.listConvertToString(subList, ",")).append(") ");
            } else {
                result.append(" or ").append(filed).append("  in(").append(StringUtil.listConvertToString(subList, ",")).append(") ");
            }
        }
        return result.append(")").toString();
    }

    /**
     * @param sInputString sInputString
     * @return InputStream
     * @throws
     * @Title: getStringStream
     * @Description: 字符串转化为输入流
     */
    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
                        sInputString.getBytes("utf8"));
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tInputStream tInputStream
     * @return String
     * @throws
     * @Title: getStreamString
     * @Description: 输入流转化为字符串
     */
    public static String getStreamString(InputStream tInputStream) {
        if (tInputStream != null) {
            try {
                InputStreamReader isr = new InputStreamReader(tInputStream, "UTF-8");
                BufferedReader tBufferedReader = new BufferedReader(isr);
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine = "";
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
     * 将字符串首字符转为大写.
     *
     * @param str str
     * @return String
     */
    public static String upperFirstChar(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
