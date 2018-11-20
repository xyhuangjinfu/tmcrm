package cn.hjf.tmcrm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

	public static final String PATTERN_1 = "yyyy-MM-dd HH:mm:ss";

	public static String format(String pattern, long currentTimeMillis) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(new Date(currentTimeMillis));
	}
}
