package com.zxj.tools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zxj on 2016/6/1.
 */
public class StringUtils {
    /**
     * 是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组是否为空
     *
     * @param values
     * @return
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }
    /**
     * 判断是否符合邮箱格式
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail.trim());
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断输入是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String strPattern = "[0-9]*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str.trim());
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 匹配身份证
     *
     * @param idCard
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isIDCard(String idCard) {
        String pattern = "^\\d{10}|\\d{13}|\\d{15}|\\d{17}(\\d|x|X)$";
        return idCard.matches(pattern);
    }

    /**
     * 将字母转换成数字_1
     * @param input
     * @return
     */
    public static String letterToNum(String input) {
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();
        if (null != input && !"".equals(input)) {
            for (char c : input.toCharArray()) {
                if (String.valueOf(c).matches(reg)) {
                    strBuf.append(c - 96);
                } else {
                    strBuf.append(c);
                }
            }
            return strBuf.toString();
        } else {
            return input;
        }
    }

    /** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }
    /** 车牌号码正则 */
    static Pattern carPlatePattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
    static Pattern EnglishOrNumberPattern = Pattern.compile("^[A-Z_0-9_a-z]{0,}$");
    public static boolean isCarPlate(String input)
    {
        return carPlatePattern.matcher(input).matches();
    }
    public static boolean isEnglishOrNumber(String trim)
    {
        return EnglishOrNumberPattern.matcher(trim).matches();
    }

}
