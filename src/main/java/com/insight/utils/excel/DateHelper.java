package com.insight.utils.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author 宣炳刚
 * @date 2020/10/23
 * @remark
 */
public final class DateHelper {

    /**
     * 日期格式字典
     */
    private static final Map<String, String> FORMAT_MAP = new HashMap<>(16);

    static {
        FORMAT_MAP.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");
        FORMAT_MAP.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd-HH-mm");
        FORMAT_MAP.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd-HH");
        FORMAT_MAP.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd");
        FORMAT_MAP.put("^\\d{4}\\D+\\d{1,2}$", "yyyy-MM");
        FORMAT_MAP.put("^\\d{4}$", "yyyy");
        FORMAT_MAP.put("^\\d{14}$", "yyyyMMddHHmmss");
        FORMAT_MAP.put("^\\d{12}$", "yyyyMMddHHmm");
        FORMAT_MAP.put("^\\d{10}$", "yyyyMMddHH");
        FORMAT_MAP.put("^\\d{8}$", "yyyyMMdd");
        FORMAT_MAP.put("^\\d{6}$", "yyyyMM");
        FORMAT_MAP.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");
        FORMAT_MAP.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");
    }

    /**
     * 格式化日期时间字符串为yyyy-MM-dd HH-mm-ss格式
     *
     * @param date 输入的日期时间字符串
     * @return 格式化为yyyy-MM-dd HH-mm-ss格式的字符串
     */
    public static String dateFormat(String date) throws ParseException {
        for (Map.Entry<String, String> entry : FORMAT_MAP.entrySet()) {
            if (Pattern.compile(entry.getKey()).matcher(date).matches()) {
                SimpleDateFormat formatter = new SimpleDateFormat(entry.getValue());
                String result = date.replaceAll("\\D+", "-");
                Date dateValue = formatter.parse(result);

                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateValue);
            }
        }

        return null;
    }
}
