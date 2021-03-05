package utils;

import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
	
	/**
	 * 将string按需要格式化,前面加缩进符,后面加换行符
	 * @param tabNum 缩进量
	 * @param srcString
	 * @return
	 */
	public static String formatSingleLine(int tabNum, String srcString) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<tabNum; i++) {
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
		for(String s : src.trim().split(" ")) {
			sb.append(firstToUpperCase(s));
		}
		return sb.toString();
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
			if(c >= 'A' && c <= 'Z') {
				// 一旦遇到大写单词，保存之前已有字符组成的单词
				if(sbWord.length() > 0) {
					if(sb.length() > 0) {
						sb.append("_");
					}
					sb.append(sbWord.toString());
				}
				sbWord = new StringBuilder();
			}
			sbWord.append(c);
		}

		if(sbWord.length() > 0) {
			if(sb.length() > 0) {
				sb.append("_");
			}
			sb.append(sbWord.toString());
		}

		return sb.toString();
	}

	public static boolean hasChinese(String s) {
		String regexChinese = "[\u4e00-\u9fa5]+";
		Pattern patternChinese = Pattern.compile(regexChinese);
		return patternChinese.matcher(s).find();
	}

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
