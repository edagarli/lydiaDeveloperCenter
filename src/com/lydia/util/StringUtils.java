package com.lydia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	 public static String fillStringByArgs(String str, String... arr) {
	        Matcher m = Pattern.compile("\\{(\\d)\\}").matcher(str);
	        while (m.find()) {
	            // str = str.replace(m.group(),
	            // URLEncoder.encode(arr[Integer.parseInt(m.group(1))]));
	            str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
	        }
	        return str;
	    }

	    public static boolean isBlank(String str) {
	        if (str == null || "".equals(str)) {
	            return true;
	        }
	        return false;
	    }
}
