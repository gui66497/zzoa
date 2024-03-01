package zzjz.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * PinyinUtil.
 *
 * @author fgt
 * @version 2017/6/1 16:38
 */
public class PinyinUtil {
    private static PinyinUtil instance;

    private static final int INT128 = 128;

    private static final int TWO = 2;

    private static final int THREE = 3;

    private static final int FOUR = 4;

    /**
     *
     * @return PinyinUtil
     */
    public static PinyinUtil getInstance() {
        if (instance == null) {
            instance = new PinyinUtil();
        }
        return instance;
    }

    private HanyuPinyinOutputFormat outputFormat;

    private HanyuPinyinOutputFormat getOutputFormat() {
        if (outputFormat == null) {
            outputFormat = new HanyuPinyinOutputFormat();
            outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }
        return outputFormat;
    }

    /**
     * 获取字符串中所有字符首字母.
     *
     * @param str str
     * @return String
     */
    public String getStringInitial(String str) {
        StringBuffer sbf = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                sbf.append(getCharInitial(str.charAt(i)));
            }
        }
        return sbf.toString();
    }

    /**
     * 获取中文字符首字母.
     *
     * @param c char
     * @return String
     */
    public String getCharInitial(char c) {
        try {
            String[] strs = PinyinHelper.toHanyuPinyinStringArray(c,
                    getOutputFormat());
            String initial = "";
            if (strs != null && strs.length > 0) {
                String str = strs[0];
                if (str != null && str.length() > 0) {
                    initial += str.charAt(0);
                }
            }
            return initial;
        } catch (Exception ex) {
            ex.printStackTrace();
            return c + "";
        }
    }

    /**
     *
     * @param chines chines
     * @return String
     */
    public String getStringPinyin(String chines) {

        char[] nameChar = chines.toCharArray();
        StringBuffer pinyinStr = new StringBuffer("");
        for (int i = 0; i < nameChar.length; i++) {
            try {
                char cha = nameChar[i];
                if (nameChar[i] > INT128) {
                    pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(cha, getOutputFormat())[0]);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                pinyinStr.append(nameChar[i]);
            }
        }
        return pinyinStr.toString();
    }

    /**
     * 获取字符全拼.
     *
     * @param c char
     * @return String
     */
    public String getCharPinyin(char c) {

        try {
            String[] strs = PinyinHelper.toHanyuPinyinStringArray(c,
                    getOutputFormat());
            String str = "";
            if (strs != null && strs.length > 0) {
                str = strs[0];
            }
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
            return c + "";
        }
    }

    /**
     * 通过名字得到直真君智邮箱.
     * @param name 名字
     * @return 邮箱
     */
    public String getZZEmailByName(String name) {
        if (name.length() == TWO) {
            return getInstance().getStringPinyin(name.substring(1)) + "." + getInstance().getStringPinyin(name.substring(0, 1)) + "@zzjunzhi.com";
        } else if (name.length() == THREE) {
            return getInstance().getStringPinyin(name.substring(1)) + "." + getInstance().getStringPinyin(name.substring(0, 1)) + "@zzjunzhi.com";
        } else if (name.length() == FOUR) {
            return getInstance().getStringPinyin(name.substring(2)) + "." + getInstance().getStringPinyin(name.substring(0, 2)) + "@zzjunzhi.com";
        }
        return null;
    }

    /*public static void main(String[] args) {
        String stringInitial = PinyinUtil.getInstance().getStringInitial("玥是个生僻字一般的拼音码解析不了");
        System.out.println(stringInitial);

        String stringPinyin = PinyinUtil.getInstance().getStringPinyin("玥是个生僻字一般的拼音码解析不了");
        System.out.println(stringPinyin);

    }*/
}
