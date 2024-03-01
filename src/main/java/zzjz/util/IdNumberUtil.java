package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 身份证号工具类.
 *
 * @author fgt
 * @version 2017/5/11 8:43
 */
public class IdNumberUtil extends StringUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(IdNumberUtil.class);

    private final static int INT3 = 3;

    private final static int INT4 = 4;

    private final static int INT5 = 5;

    private final static int INT6 = 6;

    private final static int INT7 = 7;

    private final static int INT8 = 8;

    private final static int INT9 = 9;

    private final static int INT10 = 10;

    private final static int INT11 = 11;

    private final static int INT12 = 12;

    private final static int INT13 = 13;

    private final static int INT14 = 14;

    private final static int INT15 = 15;

    private final static int INT16 = 16;

    private final static int INT17 = 17;

    private final static int INT18 = 18;

    private final static int INT19 = 19;

    private final static int INT20 = 20;

    private final static int INT21 = 21;

    private final static int INT22 = 22;

    private final static int INT23 = 23;

    private final static int INT24 = 24;

    private final static int INT25 = 25;

    private final static int INT26 = 26;

    private final static int INT27 = 27;

    private final static int INT28 = 28;

    private final static int INT29 = 29;

    private final static int INT30 = 30;

    private final static int INT31 = 31;

    private final static int INT32 = 32;

    private final static int INT33 = 33;

    private final static int INT34 = 34;

    private final static int INT35 = 35;

    private final static int INT55 = 55;

    private final static int INT100 = 100;

    private final static int INT400 = 400;

    private final static int INT522 = 522;

    /**
     * 中国公民身份证号码最小长度.
     */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度.
     */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 省、直辖市代码表.
     */
    public static final String[] CITY_CODE = {
        "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
        "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
        "81", "82", "91"
    };

    /**
     * 每位加权因子.
     */
    private static final int[] POWER = {
        7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };

    /**
     * 第18位校检码.
     */
    public static final String[] VERIFY_CODE = {
        "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };

    /**
     * 最低年限.
     */
    public static final int MIN = 1930;

    /**
     * 城市编码.
     */
    private static final Map<String, String> CITYCODES = new HashMap<String, String>();

    /**
     * 台湾身份首字母对应数字.
     */
    private static final Map<String, Integer> TWFIRSTCODE = new HashMap<String, Integer>();

    /**
     * 香港身份首字母对应数字.
     */
    private static final Map<String, Integer> HKFIRSTCODE = new HashMap<String, Integer>();

    static {
        CITYCODES.put("11", "北京");
        CITYCODES.put("12", "天津");
        CITYCODES.put("13", "河北");
        CITYCODES.put("14", "山西");
        CITYCODES.put("15", "内蒙古");
        CITYCODES.put("21", "辽宁");
        CITYCODES.put("22", "吉林");
        CITYCODES.put("23", "黑龙江");
        CITYCODES.put("31", "上海");
        CITYCODES.put("32", "江苏");
        CITYCODES.put("33", "浙江");
        CITYCODES.put("34", "安徽");
        CITYCODES.put("35", "福建");
        CITYCODES.put("36", "江西");
        CITYCODES.put("37", "山东");
        CITYCODES.put("41", "河南");
        CITYCODES.put("42", "湖北");
        CITYCODES.put("43", "湖南");
        CITYCODES.put("44", "广东");
        CITYCODES.put("45", "广西");
        CITYCODES.put("46", "海南");
        CITYCODES.put("50", "重庆");
        CITYCODES.put("51", "四川");
        CITYCODES.put("52", "贵州");
        CITYCODES.put("53", "云南");
        CITYCODES.put("54", "西藏");
        CITYCODES.put("61", "陕西");
        CITYCODES.put("62", "甘肃");
        CITYCODES.put("63", "青海");
        CITYCODES.put("64", "宁夏");
        CITYCODES.put("65", "新疆");
        CITYCODES.put("71", "台湾");
        CITYCODES.put("81", "香港");
        CITYCODES.put("82", "澳门");
        CITYCODES.put("91", "国外");
        TWFIRSTCODE.put("A", INT10);
        TWFIRSTCODE.put("B", INT11);
        TWFIRSTCODE.put("C", INT12);
        TWFIRSTCODE.put("D", INT13);
        TWFIRSTCODE.put("E", INT14);
        TWFIRSTCODE.put("F", INT15);
        TWFIRSTCODE.put("G", INT16);
        TWFIRSTCODE.put("H", INT17);
        TWFIRSTCODE.put("J", INT18);
        TWFIRSTCODE.put("K", INT19);
        TWFIRSTCODE.put("L", INT20);
        TWFIRSTCODE.put("M", INT21);
        TWFIRSTCODE.put("N", INT22);
        TWFIRSTCODE.put("P", INT23);
        TWFIRSTCODE.put("Q", INT24);
        TWFIRSTCODE.put("R", INT25);
        TWFIRSTCODE.put("S", INT26);
        TWFIRSTCODE.put("T", INT27);
        TWFIRSTCODE.put("U", INT28);
        TWFIRSTCODE.put("V", INT29);
        TWFIRSTCODE.put("X", INT30);
        TWFIRSTCODE.put("Y", INT31);
        TWFIRSTCODE.put("W", INT32);
        TWFIRSTCODE.put("Z", INT33);
        TWFIRSTCODE.put("I", INT34);
        TWFIRSTCODE.put("O", INT35);
        HKFIRSTCODE.put("A", 1);
        HKFIRSTCODE.put("B", 2);
        HKFIRSTCODE.put("C", INT3);
        HKFIRSTCODE.put("R", INT18);
        HKFIRSTCODE.put("U", INT21);
        HKFIRSTCODE.put("Z", INT26);
        HKFIRSTCODE.put("X", INT24);
        HKFIRSTCODE.put("W", INT23);
        HKFIRSTCODE.put("O", INT15);
        HKFIRSTCODE.put("N", INT14);
    }

    /**
     * 将15位身份证号码转换为18位.
     *
     * @param idCard 15位身份编码
     * @return 18位身份编码
     */
    public static String conver15CardTo18(String idCard) {
        String idCard18 = "";
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return null;
        }
        if (isNum(idCard)) {
            // 获取出生年月日
            String birthday = idCard.substring(INT6, INT12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            // 获取出生年(完全表现形式,如：2010)
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, INT6) + sYear + idCard.substring(INT8);
            // 转换字符数组
            char[] cArr = idCard18.toCharArray();
            if (cArr != null) {
                int[] iCard = converCharToInt(cArr);
                int iSum17 = getPowerSum(iCard);
                // 获取校验位
                String sVal = getCheckCode18(iSum17);
                if (sVal.length() > 0) {
                    idCard18 += sVal;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return idCard18;
    }

    /**
     * 验证身份证是否合法.
     *
     * @param idCard 身份证号
     * @return boolean
     */
    public static boolean validateCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        String card = idCard.trim();
        if (validateIdCard18(card)) {
            return true;
        }
        if (validateIdCard15(card)) {
            return true;
        }
        String[] cardval = validateIdCard10(card);
        if (cardval != null) {
            if (cardval[2].equals("true")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证18位身份编码是否合法.
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard18(String idCard) {
        boolean bTrue = false;
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            // 前17位
            String code17 = idCard.substring(0, INT17);
            // 第18位
            String code18 = idCard.substring(INT17, CHINA_ID_MAX_LENGTH);
            if (isNum(code17)) {
                char[] cArr = code17.toCharArray();
                if (cArr != null) {
                    int[] iCard = converCharToInt(cArr);
                    int iSum17 = getPowerSum(iCard);
                    // 获取校验位
                    String val = getCheckCode18(iSum17);
                    if (val.length() > 0 && val.equalsIgnoreCase(code18)) {
                        bTrue = true;
                    }
                }
            }
        }
        return bTrue;
    }

    /**
     * 验证15位身份编码是否合法.
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard15(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH) {
            return false;
        }
        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (CITYCODES.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(INT6, INT12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null) {
                cal.setTime(birthDate);
            }
            if (!valiDate(cal.get(Calendar.YEAR), Integer.parseInt(birthCode.substring(2, INT4)),
                    Integer.parseInt(birthCode.substring(INT4, INT6)))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 验证10位身份编码是否合法.
     *
     * @param idCard 身份编码
     * @return 身份证信息数组
     * <p>
     * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
     * 若不是身份证件号码则返回null
     * </p>
     */
    public static String[] validateIdCard10(String idCard) {
        String[] info = new String[INT3];
        String card = idCard.replaceAll("[\\(|\\)]", "");
        if (card.length() != INT8 && card.length() != INT9 && idCard.length() != INT10) {
            return null;
        }
        if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            LOGGER.info("11111");
            String char2 = idCard.substring(1, 2);
            if ("1".equals(char2)) {
                info[1] = "M";
                LOGGER.info("MMMMMMM");
            } else if ("2".equals(char2)) {
                info[1] = "F";
                LOGGER.info("FFFFFFF");
            } else {
                info[1] = "N";
                info[2] = "false";
                LOGGER.info("NNNN");
                return info;
            }
            info[2] = validateTWCard(idCard) ? "true" : "false";
        } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
        } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = validateHKCard(idCard) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证台湾身份证号码.
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateTWCard(String idCard) {
        String start = idCard.substring(0, 1);
        String mid = idCard.substring(1, INT9);
        String end = idCard.substring(INT9, INT10);
        Integer iStart = TWFIRSTCODE.get(start);
        Integer sum = iStart / INT10 + (iStart % INT10) * INT9;
        char[] chars = mid.toCharArray();
        Integer iflag = INT8;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * iflag;
            iflag--;
        }
        return (sum % INT10 == 0 ? 0 : (INT10 - sum % INT10)) == Integer.parseInt(end) ? true : false;
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查).
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateHKCard(String idCard) {
        String card = idCard.replaceAll("[\\(|\\)]", "");
        Integer sum = 0;
        if (card.length() == INT9) {
            sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - INT55) * INT9
                    + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - INT55) * INT8;
            card = card.substring(1, INT9);
        } else {
            sum = INT522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - INT55) * INT8;
        }
        String mid = card.substring(1, INT7);
        String end = card.substring(INT7, INT8);
        char[] chars = mid.toCharArray();
        Integer iflag = INT7;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * iflag;
            iflag--;
        }
        if (end.toUpperCase().equals("A")) {
            sum = sum + INT10;
        } else {
            sum = sum + Integer.parseInt(end);
        }
        return (sum % INT11 == 0) ? true : false;
    }

    /**
     * 将字符数组转换成数字数组.
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值.
     *
     * @param iArr iArr
     * @return 身份证编码。
     */
    public static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断.
     *
     * @param iSum iSum
     * @return 校验位
     */
    public static String getCheckCode18(int iSum) {
        String sCode = "";
        switch (iSum % INT11) {
            case INT10:
                sCode = "2";
                break;
            case INT9:
                sCode = "3";
                break;
            case INT8:
                sCode = "4";
                break;
            case INT7:
                sCode = "5";
                break;
            case INT6:
                sCode = "6";
                break;
            case INT5:
                sCode = "7";
                break;
            case INT4:
                sCode = "8";
                break;
            case INT3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                break;
        }
        return sCode;
    }

    /**
     * 根据身份编号获取年龄.
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String year = idCard.substring(INT6, INT10);
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.parseInt(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日.
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(INT6, INT14);
    }

    /**
     * 根据身份编号获取生日年.
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(INT6, INT10));
    }

    /**
     * 根据身份编号获取生日月.
     *
     * @param idCard 身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(INT10, INT12));
    }

    /**
     * 根据身份编号获取生日天.
     *
     * @param idCard 身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        Integer len = idCard.length();
        if (len < CHINA_ID_MIN_LENGTH) {
            return null;
        } else if (len == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return Short.valueOf(idCard.substring(INT12, INT14));
    }

    /**
     * 根据身份编号获取性别.
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "N";
        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String sCardNum = idCard.substring(INT16, INT17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "M";
        } else {
            sGender = "F";
        }
        return sGender;
    }

    /**
     * 根据身份编号获取户籍省份.
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvinceByIdCard(String idCard) {
        int len = idCard.length();
        String sProvince = null;
        String sProvinNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sProvinNum = idCard.substring(0, 2);
        }
        sProvince = CITYCODES.get(sProvinNum);
        return sProvince;
    }

    /**
     * 数字验证.
     *
     * @param val val
     * @return 提取的数字。
     */
    public static boolean isNum(String val) {
        return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
    }

    /**
     * 验证小于当前日期 是否有效.
     *
     * @param iYear  待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate  待验证日期(日)
     * @return 是否有效
     */
    public static boolean valiDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > INT12) {
            return false;
        }
        switch (iMonth) {
            case INT4:
            case INT6:
            case INT9:
            case INT11:
                datePerMonth = INT30;
                break;
            case 2:
                boolean dm = ((iYear % INT4 == 0 && iYear % INT100 != 0) || (iYear % INT400 == 0))
                        && (iYear > MIN && iYear < year);
                datePerMonth = dm ? INT29 : INT28;
                break;
            default:
                datePerMonth = INT31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }

    /**
     * 获取uuid.
     * @return 获取uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /*public static void main(String[] args) {
        boolean s1 = IdNumberUtil.validateCard("320201197406120042");
        boolean s2 = IdNumberUtil.validateCard("32020119740612004X");
        320211197406120042
        LOGGER.info(IdNumberUtil.getBirthByIdCard("32128119921030501X"));
        LOGGER.info(IdNumberUtil.getBirthByIdCard("320201197406120042"));
        LOGGER.info(IdNumberUtil.getGenderByIdCard("32128119921030501X"));
    }*/
}

