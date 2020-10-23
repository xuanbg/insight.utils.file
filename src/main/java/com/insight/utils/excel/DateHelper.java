package com.insight.utils.excel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private static final Map<String, String> formatMap = new HashMap<>(16);

    /**
     * 格式化日期时间字符串为yyyy-MM-dd HH-mm-ss格式
     *
     * @param date 输入的日期时间字符串
     * @return 格式化为yyyy-MM-dd HH-mm-ss格式的字符串
     */
    public static String dateFormat(String date) {
        if (formatMap.isEmpty()) {
            formatMap.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");
            formatMap.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd-HH-mm");
            formatMap.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd-HH");
            formatMap.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}$", "yyyy-MM-dd");
            formatMap.put("^\\d{4}\\D+\\d{1,2}$", "yyyy-MM");
            formatMap.put("^\\d{4}$", "yyyy");
            formatMap.put("^\\d{14}$", "yyyyMMddHHmmss");
            formatMap.put("^\\d{12}$", "yyyyMMddHHmm");
            formatMap.put("^\\d{10}$", "yyyyMMddHH");
            formatMap.put("^\\d{8}$", "yyyyMMdd");
            formatMap.put("^\\d{6}$", "yyyyMM");
            formatMap.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");
            formatMap.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");
        }

        DateTimeFormatter formatter = null;
        for (String key : formatMap.keySet()) {
            if (Pattern.compile(key).matcher(date).matches()) {
                formatter = DateTimeFormatter.ofPattern(formatMap.get(key));
                break;
            }
        }

        if (formatter == null) {
            return null;
        }

        String result = date.replaceAll("\\D+", "-");
        LocalDateTime dateValue = LocalDateTime.parse(result, formatter);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return formatter.format(dateValue);
    }
}
