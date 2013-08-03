/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.commons.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Commons Utils
 * 
 * @author AGEN
 * @since JDK1.6
 * @since commons.lang3
 */
public class CommUtil extends StringUtils {

	// ~ Instance fields ==================================================

	/**
	 * 0~9 A~Z cha array
	 */
	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	/**
	 * Single byte character set
	 */
	private static final char[] SBC_CASE = { '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'-', '_', '=', '+', '\\', '|', '[', ']', ';', ':', '\'', '"', ',',
			'<', '.', '>', '/', '?' };

	/**
	 * Double byte character set
	 */
	private static final char[] DBC_CASE = { '１', '２', '３', '４', '５', '６', '７',
			'８', '９', '０', '！', '＠', '＃', '＄', '％', '︿', '＆', '＊', '（', '）',
			'ａ', 'ｂ', 'ｃ', 'ｄ', 'ｅ', 'ｆ', 'ｇ', 'ｈ', 'ｉ', 'ｊ', 'ｋ', 'ｌ', 'ｍ',
			'ｎ', 'ｏ', 'ｐ', 'ｑ', 'ｒ', 'ｓ', 'ｔ', 'ｕ', 'ｖ', 'ｗ', 'ｘ', 'ｙ', 'ｚ',
			'Ａ', 'Ｂ', 'Ｃ', 'Ｄ', 'Ｅ', 'Ｆ', 'Ｇ', 'Ｈ', 'Ｉ', 'Ｊ', 'Ｋ', 'Ｌ', 'Ｍ',
			'Ｎ', 'Ｏ', 'Ｐ', 'Ｑ', 'Ｒ', 'Ｓ', 'Ｔ', 'Ｕ', 'Ｖ', 'Ｗ', 'Ｘ', 'Ｙ', 'Ｚ',
			'－', '＿', '＝', '＋', '＼', '｜', '【', '】', '；', '：', '‘', '“', '，',
			'《', '。', '》', '／', '？' };
	/**
	 * WEEK DAYS
	 */
	private static final String[] WEEK_DAYS = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

	// ~ Constructors ==================================================

	/**
	 */
	private CommUtil() {

	}

	// ~ Methods ==================================================

	/**
	 * Generates a (0 to 9 and the combination of A ~ Z) String, the string
	 * length is specified by the len
	 * 
	 * @param len
	 *            length
	 * @return a length is len randomCode String
	 */
	public static String generateRandomCode(int len) {
		if (len < 1) {
			len = 4;
		}
		String temp = RandomStringUtils.random(len, 0, 36, true, true, CHARS,
				new Random(System.currentTimeMillis()));
		return temp;
	}

	/**
	 * Generate a (0 to 9 and A ~ Z) String list, the string length is specified
	 * by the len,list size is specified size
	 * 
	 * @param len
	 * @param size
	 * @return size list
	 */
	public static List<String> generateRandomCodes(int len, int size) {
		if (len < 1 || size < 1) {
			return null;
		}
		List<String> codes = new ArrayList<String>();

		for (int i = 0; i < size; i++) {
			codes.add(RandomStringUtils.random(len, 0, 36, true, true, CHARS,
					new Random(i)));
		}
		return codes;

	}

	/**
	 * mask the Last char to "*"
	 * 
	 * @param name
	 * @return lastMask String
	 */
	public static String lastMask(String name) {

		if (!StringUtils.isBlank(name) && name.indexOf("*") < 0) {

			return name.substring(0, name.length() - 1) + '*';
		}
		return name;
	}

	/**
	 * Verify the email
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmail(String str) {
		// email regular
		boolean validate = str
				.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		if (validate) {
			return true;
		}
		return false;
	}

	/**
	 * Verify special characters
	 * 
	 * @param src
	 * @return boolean
	 */
	public static boolean hasSpecialChar(String src) {

		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(src);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Verify phone number
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean isPhoneNum(String number) {
		int retval = getPhoneNumberType(number);
		if (retval > 0 && retval < 4) {
			return true;
		}
		return false;
	}

	/**
	 * Get Mobile operator <li>0: number error</li> <li>1: china mobile(phone)</li>
	 * <li>2: china unicom(phone)</li> <li>
	 * 3: China Telecom(phone)</li> <li>4: Tel</li>
	 * 
	 * @param number
	 * @return 0,1,2,3,4
	 */
	public static int getPhoneNumberType(String number) {
		// ~ phone
		String cm = "^(((13[5-9])|(147)|(15[012789])|(18[23478]))\\d{8})|(134[0-8])\\d{7}$";
		String cu = "^((13[0-2])|(145)|(15[5-6])|(18[5-6]))\\d{8}$";
		String ct = "^(((133)|(153)|(18[019]))\\d{8})|(1349)\\d{7}$";

		String tel = "^((0[12]\\d{1}\\d{8})|(0[3-9]\\d{2}\\d{7,8}))$";

		int flag = 0;

		if (number.matches(cm)) {
			flag = 1;
		} else if (number.matches(cu)) {
			flag = 2;
		} else if (number.matches(ct)) {
			flag = 3;
		} else if (number.matches(tel)) {
			flag = 4;
		} else {
			// TODO
		}
		return flag;
	}

	/**
	 * Date --> String
	 * 
	 * @param date
	 * @param pattern
	 * @return date String
	 */
	public static String date2String(Date date, String pattern) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				pattern);
		String str_date = dateFormat.format(date);
		return str_date;
	}

	/**
	 * Date ---> String (yyyyMMddHHmmss)
	 * 
	 * @param date
	 * @return date String
	 */
	public static String dateTime2StringFs(Date date) {

		return date2String(date, "yyyyMMddHHmmss");
	}

	/**
	 * 
	 * Date ---> String (yyyyMMddHHmmssSSS)
	 * 
	 * @param date
	 * @return date String
	 */
	public static String dateTime2StringFS(Date date) {

		return date2String(date, "yyyyMMddHHmmssSSS");

	}

	/**
	 * String --> Date
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return date
	 */
	public static Date string2Date(String dateStr, String pattern) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				pattern);
		Date date = new Date();

		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * String --> Date (yyyyMMddHHmmss)
	 * 
	 * @param dateStr
	 * @return date
	 */
	public static Date string2DateFs(String dateStr) {

		return string2Date(dateStr, "yyyyMMddHHmmss");
	}

	/**
	 * 
	 * String --> Date (yyyyMMddHHmmssSSS)
	 * 
	 * @param dateStr
	 * @return date
	 */
	public static Date string2DateFS(String dateStr) {

		return string2Date(dateStr, "yyyyMMddHHmmssSSS");

	}

	/**
	 * Return the next day's date and time 00:00:00 start date
	 * 
	 * @param date
	 * @return next day begin
	 */
	public static Date nextDayBegin(Date date) {
		Calendar cal =getCalendar(date);
		cal.add(Calendar.DATE, 1); // next day's am O0:00:00
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * Return the next month's date and time 00:00:00 start date
	 * 
	 * @param date
	 * @return next month begin
	 */
	public static Date nextMonthBegin(Date date) {
		Calendar cal = getCalendar(date);
		cal.set(Calendar.DAY_OF_MONTH, 1); // next month's am O0:00:00
		cal.add(Calendar.MONTH, 1);

		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * Return the next month's date
	 * 
	 * @param date
	 * @return next month
	 */
	public static Date nextMonth(Date date) {
		Calendar cal = getCalendar(date);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}

	/**
	 * Return the next day's date
	 * 
	 * @param date
	 * @return next day
	 */
	public static Date nextDay(Date date) {
		Calendar cal =getCalendar(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * Return the next hour's date(24H)
	 * 
	 * @param date
	 * @return next hour
	 */
	public static Date nextHour(Date date) {
		Calendar cal = getCalendar(date);
		cal.add(Calendar.HOUR_OF_DAY, 1);
		return cal.getTime();
	}

	/**
	 * Return the current year
	 * 
	 * @param date
	 * @return current year
	 */
	public static int getCurrentYear(Date date) {
		Calendar cal =getCalendar(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Return the current month of year (1~12)
	 * 
	 * @param date
	 * @return month of year (1~12)
	 */
	public static int getCurrentMonthOfYear(Date date) {
		Calendar cal =getCalendar(date);
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Return the current week of year 
	 * 
	 * @param date
	 * @return current week of year 
	 */
	public static int getCurrentWeekOfYear(Date date) {
		Calendar cal = getCalendar(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * Return the current week of month 
	 * 
	 * @param date
	 * @return current week of month 
	 */
	public static int getCurrentWeekOfMonth(Date date) {
		Calendar cal = getCalendar(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}
	
	 /**
     * Get the current day of the week<br>
     * 
     * @param date Date
     * @return current day of week String. e.g:SUNDAY,MONDAY...
     */
    public static String getCurrentDayOfWeekS(Date date) {
        Calendar cal =getCalendar(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return WEEK_DAYS[w];
    }
	
	/**
	 * Get Calendar if date is null ,return new Date
	 * @param date
	 * @return calendar
	 */
	public static Calendar getCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		if(date==null){
			date=new Date();
		}
		cal.setTime(date);
		return cal;
	}

	/**
	 * Return the current day of month
	 * 
	 * @param date
	 * @return current day
	 */
	public static int getCurrentDayOfMounth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * Return the current day of week
	 * 
	 * @param date
	 * @return current day of week
	 */
	public static int getCurrentDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Return the current hour of day (24H)
	 * 
	 * @param date
	 * @return current hour
	 */
	public static int getCurrentHourOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * return a ArrayList from comma-separated string
	 * 
	 * @param access
	 * @return list
	 */
	public static java.util.List<String> createListFromCommaDelimitedString(
			String access) {
		if (access == null || access.equals("")) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] args = access.split(",");
		for (String arg : args) {
			if (!arg.equals("")) {
				list.add(arg);
			}
		}
		return list;

	}

	/**
	 * return a double value from String
	 * 
	 * @param src
	 * @return double
	 */
	public static double extractNumber(String src) {
		String temp = "0.0";
		Pattern p = Pattern.compile("\\d+\\.?\\d+");
		Matcher m = p.matcher(src);
		while (m.find()) {
			temp = m.group();
		}
		return Double.valueOf(temp);
	}

	/**
	 * Returns a new string that is a substring of this string. The substring
	 * begins at the specified <code>startIndex</code> and extends to the
	 * character at index <code>endIndex - 1</code>. and if remaining character
	 * use <code>postfix</code> instead Thus the length of the substring is
	 * <code>startIndex-endIndex + postfix</code>.
	 * <p>
	 * Examples: <blockquote>
	 * 
	 * <pre>
	 * "hamburger".subPostfix(0, 4,"...") returns "hamb..."
	 * </pre>
	 * 
	 * </blockquote>
	 * </p>
	 * 
	 * @param str
	 * @param startIndex
	 * @param endIndex
	 * @param postfix
	 * @return string
	 */
	public static String subPostfix(String str, int startIndex, int endIndex,
			String postfix) {
		if (StringUtils.isBlank(str))
			return "";
		int length = str.length();
		if (length <= startIndex) {
			return str;
		} else if (length <= endIndex) {
			return str.substring(startIndex, length);
		} else {
			return str.substring(startIndex, endIndex) + postfix;
		}
	}

	/**
	 * base64 encode
	 * 
	 * @param plainText
	 * @return base64 string
	 */
	public static String encodeBase64(String plainText) {
		byte[] b = Base64.encodeBase64(plainText.getBytes(), true);
		;
		String s = new String(b);
		return s;
	}

	/**
	 * base 64 decode
	 * 
	 * @param signature
	 * @return decode Base64 string
	 */
	public static String decodeBase64(String signature) {
		byte[] b = Base64.decodeBase64(signature.getBytes());
		String s = new String(b);
		return s;
	}

	/**
	 * return a <code>len</code> not repeat random number String, len must be
	 * less than 10
	 * 
	 * @param len
	 *            must less than 10
	 * @return random number
	 */
	public static String getRandomNumber(int len) {
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < len; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}
		return sb.toString();
	}

	/**
	 * Array to a comma-delimited string
	 * 
	 * @param src
	 * @return string
	 */
	public static String toCommaDelimitedString(String[] src) {
		StringBuffer sb = new StringBuffer();
		for (String str : src) {
			sb.append(str);
			sb.append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

	/**
	 * escape CSV see StringEscapeUtils.escapeCsv(str)
	 * 
	 * @param str
	 * @return string
	 */
	public static String escapeCsv(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.escapeCsv(str);
	}

	/**
	 * <p>
	 * escape HTML see StringEscapeUtils.escapeHtml3(str)
	 * </p>
	 * <p>
	 * Supports all known HTML 3.0 entities, including funky accents. Note that
	 * the commonly used apostrophe escape character (&amp;apos;) is not a legal
	 * entity and so is not supported).
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>"bread" & "butter"</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>
	 * .
	 * </p>
	 * 
	 * @param str
	 * @return string
	 */
	public static String escapeHtml3(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.escapeHtml3(str);
	}

	/**
	 * <p>
	 * escape HTML see StringEscapeUtils.escapeHtml4(str)
	 * </p>
	 * <p>
	 * Supports all known HTML 4.0 entities, including funky accents. Note that
	 * the commonly used apostrophe escape character (&amp;apos;) is not a legal
	 * entity and so is not supported).
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>"bread" & "butter"</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>
	 * .
	 * </p>
	 * 
	 * @param str
	 * @return string
	 */
	public static String escapeHtml4(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.escapeHtml4(str);
	}

	/**
	 * escape JAVA see StringEscapeUtils.escapeJava(str) For example: </p>
	 * <p>
	 * <code>"中国"</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>\u4E2D\u56FD</code>.
	 * </p>
	 * -->
	 * 
	 * @param str
	 * @return string
	 */
	public static String escapeJava(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.escapeJava(str);
	}

	/**
	 * escape XML see StringEscapeUtils.escapeXml(str)
	 * 
	 * @param str
	 * @return string
	 */
	public static String escapeXml(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.escapeXml(str);

	}

	/**
	 * unescape CSV see StringEscapeUtils.unescapeCsv(str)
	 * 
	 * @param str
	 * @return string
	 */
	public static String unescapeCsv(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.unescapeCsv(str);
	}

	/**
	 * <p>
	 * unescape HTML see StringEscapeUtils.unescapeHtml3(str)
	 * </p>
	 * <p>
	 * Supports all known HTML 3.0 entities, including funky accents. Note that
	 * the commonly used apostrophe escape character (&amp;apos;) is not a legal
	 * entity and so is not supported).
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>"bread" & "butter"</code>.
	 * </p>
	 * 
	 * @param str
	 * @return string
	 */
	public static String unescapeHtml3(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.unescapeHtml3(str);
	}

	/**
	 * <p>
	 * unescape HTML see StringEscapeUtils.unescapeHtml4(str)
	 * </p>
	 * <p>
	 * Supports all known HTML 4.0 entities, including funky accents. Note that
	 * the commonly used apostrophe escape character (&amp;apos;) is not a legal
	 * entity and so is not supported).
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>"bread" & "butter"</code>.
	 * </p>
	 * 
	 * @param str
	 * @return string
	 */
	public static String unescapeHtml4(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.unescapeHtml4(str);
	}

	/**
	 * unescape JAVA see StringEscapeUtils.unescapeJava(str) For example: </p>
	 * <p>
	 * <code>\u4E2D\u56FD</code>
	 * </p>
	 * becomes:
	 * <p>
	 * <code>"中国"</code>.
	 * </p>
	 * -->
	 * 
	 * @param str
	 * @return string
	 */
	public static String unescapeJava(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.unescapeJava(str);
	}

	/**
	 * unescape XML see StringEscapeUtils.unescapeXml(str)
	 * 
	 * @param str
	 * @return string
	 */
	public static String unescapeXml(String str) {
		if (isBlank(str)) {
			return str;
		}
		return StringEscapeUtils.unescapeXml(str);
	}

	/**
	 * wrap a map to String by connector
	 * <p>
	 * Here are some more examples of how strings can be used: <blockquote> if
	 * connector is & :
	 * 
	 * <pre>
	 *     a=1&b=2&c=3
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <block
	 * 
	 * @param map
	 * @param connector
	 * @return string
	 */
	public static String joinMapValue(Map<String, String> map, String connector) {
		StringBuffer b = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			b.append(entry.getKey());
			b.append('=');
			if (entry.getValue() != null) {
				b.append(entry.getValue());
			}
			b.append(connector);
		}
		return removeEnd(b.toString(), connector);
	}

	/**
	 * <p>
	 * split and return a hashMap ,outerRegex is ' ' ,innerRegex is '=' by
	 * default.
	 * </p>
	 * <p>
	 * if str is null or "",return null
	 * </p>
	 * <p>
	 * <blockquote> if outRegex is '&' and innerRegex is '='
	 * 
	 * <pre>
	 * str = &quot;a=1&amp;b=2&amp;c=3&amp;d=&quot;;
	 * Map&lt;String, String&gt; map = new HashMap&lt;String, String&gt;();
	 * map.put(&quot;a&quot;, &quot;1&quot;);
	 * map.put(&quot;b&quot;, &quot;2&quot;);
	 * map.put(&quot;c&quot;, &quot;3&quot;);
	 * map.put(&quot;d&quot;, &quot;&quot;);
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * 
	 * @param outRegex
	 * @param innerRegex
	 * @param str
	 * @return string
	 */
	public static Map<String, String> toMap(String outRegex,
			String innerRegex, String str) {
		if (isBlank(str)) {
			return null;
		}
		if (isBlank(outRegex)) {
			outRegex = " ";
		}
		if (isBlank(innerRegex)) {
			innerRegex = "=";
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] args = str.split(outRegex);
		for (String arg : args) {
			String[] temp = arg.split(innerRegex);
			if (temp.length == 1) {
				map.put(temp[0], "");
				continue;
			}
			if (temp.length == 2) {
				map.put(temp[0], temp[1]);
			}

		}
		return map;
	}

	/**
	 * Generate len same chars
	 * 
	 * @param cs
	 * @param len
	 * @return String
	 */
	public static String genSameChars(String cs, int len) {
		if (isBlank(cs) || len < 1)
			return cs;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(cs);
		}
		return sb.toString();
	}

	/**
	 * Change double byte character set String to Single byte character set
	 * 
	 * @param dbcString
	 * @return single byte character set string
	 */
	public static String toSBC(String dbcString) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < dbcString.length(); i++) {

			int index = ArrayUtil.indexOf(DBC_CASE, dbcString.charAt(i));

			if (index != -1) {
				sb.append(SBC_CASE[index]);
			} else {
				sb.append(dbcString.charAt(i));
			}
		}
		return sb.toString();

	}

	/**
	 * Change single byte character set String to double byte character set
	 * 
	 * @param sbcString
	 * @return double byte character set string
	 */
	public static String toDBC(String sbcString) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < sbcString.length(); i++) {

			int index = ArrayUtil.indexOf(SBC_CASE, sbcString.charAt(i));

			if (index != -1) {
				sb.append(DBC_CASE[index]);
			} else {
				sb.append(sbcString.charAt(i));
			}
		}
		return sb.toString();

	}

}
