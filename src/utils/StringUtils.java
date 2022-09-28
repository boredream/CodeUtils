package utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static void main(String[] args) {
        Integer integer = getNumber("产品1箱数");
        System.out.println(integer);
    }

    /**
     * 将string按需要格式化,前面加缩进符,后面加换行符
     *
     * @param tabNum    缩进量
     * @param srcString
     * @return
     */
    public static String formatSingleLine(int tabNum, String srcString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tabNum; i++) {
            sb.append("\t");
        }
        sb.append(srcString);
        sb.append("\n");
        return sb.toString();
    }

    public static String firstToUpperCase(String key) {
        return key.substring(0, 1).toUpperCase(Locale.CHINA) + key.substring(1);
    }

    public static String firstToLowerCase(String key) {
        return key.substring(0, 1).toLowerCase(Locale.CHINA) + key.substring(1);
    }

    public static String gapToCamel(String src) {
        StringBuilder sb = new StringBuilder();
        for (String s : src.trim().split(" ")) {
            sb.append(firstToUpperCase(s));
        }
        return sb.toString();
    }

    public static String _ToCamel(String src) {
        StringBuilder sb = new StringBuilder();
        for (String s : src.trim().split("_")) {
            sb.append(firstToUpperCase(s));
        }
        return firstToLowerCase(sb.toString());
    }

    /**
     * 驼峰转下划线命名
     */
    public static String camelTo_(String src) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbWord = new StringBuilder();
        char[] chars = src.trim().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                // 一旦遇到大写单词，保存之前已有字符组成的单词
                if (sbWord.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append("_");
                    }
                    sb.append(sbWord.toString());
                }
                sbWord = new StringBuilder();
            }
            sbWord.append(c);
        }

        if (sbWord.length() > 0) {
            if (sb.length() > 0) {
                sb.append("_");
            }
            sb.append(sbWord.toString());
        }

        return sb.toString().toLowerCase();
    }

    public static boolean hasChinese(String s) {
        String regexChinese = "[\u4e00-\u9fa5]+";
        Pattern patternChinese = Pattern.compile(regexChinese);
        return patternChinese.matcher(s).find();
    }

    public static boolean hasNumber(String s) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(s).find();
    }

    // 提取第一个数字
    public static Integer getNumber(String s) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Integer num = null;
        try {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String group = matcher.group();
                num = Integer.parseInt(group);
            }
        } catch (Exception e) {
            //
        }
        return num;
    }

    public static String regexGet(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String guessEncoding(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String getStringOrEmpty(String str, String def) {
        return StringUtils.isEmpty(str) ? def : str;
    }
}
