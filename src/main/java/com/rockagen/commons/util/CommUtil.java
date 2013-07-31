/*
 * Copyright 2002-2013 the original author or authors.
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author AGEN
 * @since JDK1.6
 */
public class CommUtil {
	
	
	// ~ Instance fields ==================================================

	/**
	 * 0~9 A~Z cha array
	 */
	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	
	
	//~ Constructors ==================================================
	
	/**
	 */
	private CommUtil(){
		
	}
	// ~ Methods ==================================================

	/**
	 * Generates a (0 to 9 and the combination of A ~ Z) String, the string length is specified by the len
	 * 
	 * @param len
	 *            length
	 * @return randomCode
	 */
	public static String generateRandomCode(int len) {
		if (len < 1){
			len=4;
		}
		String temp = RandomStringUtils.random(len, 0, 36, true, true, CHARS,
				new Random(System.currentTimeMillis()));
		return temp;
	}

	/**
	 * Generate a  (0 to 9 and A ~ Z) String list, the string length is specified by the len,list size is specified size
	 * 
	 * @param len
	 * @param size
	 * @return list
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	public static boolean hasSpecialChar(String src) {
		// 判断是否包含特殊字符
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
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNum(String number){
		int retval=getPhoneNumberType(number);
		if(retval >0 && retval<4){
			return true;
		}
		return false;
	}
	
	
	/**
	 *Get Mobile operator
	 * <li>0： number error</li> <li>1： china mobile(phone)</li> <li>2：china unicom(phone)</li> <li>
	 * 3：China Telecom(phone)</li> <li>4：  Tel</li>
	 * 
	 * @param number
	 * @return
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
	 * @return
	 */
	public static String date2String(Date date,String pattern) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				pattern);
			String str_date = dateFormat.format(date);
		return str_date;
	}
	
	/**
	 * Date ---> String (yyyyMMddHHmmss)
	 * @param time
	 * @return
	 */
	public static String dateTime2StringFs(Date date){
		
		return date2String(date,"yyyyMMddHHmmss");
	}
	
	/**
	 * 
	 * Date ---> String (yyyyMMddHHmmssSSS)
	 * 
	 * @param time
	 */
	public static String dateTime2StringFS(Date date) {
		
		return date2String(date,"yyyyMMddHHmmssSSS");
		
	}

	/**
	 * String --> Date
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date string2Date(String dateStr,String pattern) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
				pattern);
		Date date =new Date();
		
		 try {
			 return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			return date;
		}
	}

	/**
	 * String --> Date (yyyyMMddHHmmss)
	 * @param dateStr
	 * @return
	 */
	public static Date string2DateFs(String dateStr){
		
		return string2Date(dateStr,"yyyyMMddHHmmss");
	}
	
	/**
	 * 
	 * String --> Date (yyyyMMddHHmmssSSS)
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date string2DateFS(String dateStr) {
		
		return string2Date(dateStr,"yyyyMMddHHmmssSSS");
		
	}
	

	/**
	 * Return the next day's date and time 00:00:00 start date
	 * 
	 * @param date
	 * @return
	 */
	public static Date nextDayBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
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
	 * @return
	 */
	public static Date nextMonthBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1); //  next month's am O0:00:00
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
	 * @return
	 */
	public static Date nextMonth(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	

	/**
	 * Return the next day's date
	 * @param date
	 * @return
	 */
	public static Date nextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	/**
	 * Return the next hour's date(24H)
	 * @param date
	 * @return
	 */
	public static Date nextHour(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, 1);
		return cal.getTime();
	}
	/**
	 * Return the current year 
	 * @param date
	 * @return
	 */
	public static int getCurrentYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	
	/**
	 * Return the current month of year (1~12)
	 * @param date
	 * @return
	 */
	public static int getCurrentMonthOfYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+1;
	}
	
	/**
	 * Return the current day of month
	 * @param date
	 * @return
	 */
	public static int getCurrentDayOfMounth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * Return the current hour of day (24H)
	 * @param date
	 * @return
	 */
	public static int getCurrentHourOfDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}



	/**
	 * md5 encrypt
	 * 
	 * @param src
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String md5(String src) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = null;
		
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(src.getBytes("UTF-8"));
			
		

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	/**
	 * return a ArrayList from comma-separated string 
	 * 
	 * @param access
	 * @return
	 */
	public static java.util.List<String> createListFromCommaDelimitedString(
			String access){
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
	 * @param src
	 * @return
	 */
	public static double extractNumber(String src) {
		String temp="0.0";
		Pattern p = Pattern.compile("\\d+\\.?\\d+");
		Matcher m = p.matcher(src);
		while (m.find()) {
			temp = m.group();
		}
		return Double.valueOf(temp);
	}
	
	/**
    *  Returns a new string that is a substring of this string. The
     * substring begins at the specified <code>startIndex</code> and
     * extends to the character at index <code>endIndex - 1</code>.
     * and if remaining character use <code>postfix</code> instead
     * Thus the length of the substring is <code>startIndex-endIndex + postfix</code>.
     * <p>
     * Examples:
     * <blockquote><pre>
     * "hamburger".subPostfix(0, 4,"...") returns "hamb..."
     * </pre></blockquote>
     * </p>
	 * @param str
	 * @param startIndex
	 * @param endIndex
	 * @param postfix
	 * @return
	 */
	public static String  subPostfix(String str, int startIndex, int endIndex, String postfix) {
		 if (StringUtils.isBlank(str)) return "";
     int length = str.length();
     if (length <= startIndex){
    	 return str;
     }
     else if (length <= endIndex){
    	 return str.substring(startIndex, length);
     }
     else{
    	 return str.substring(startIndex, endIndex) + postfix;
     }
	}
	/**
	 * base64 encode
	 * @param plainText
	 * @return
	 */
	public static String encodeBase64(String plainText){
		byte[] b=Base64.encodeBase64(plainText.getBytes(), true);;
		String s=new String(b);
		return s;
	}
	
	/**
	 * base 64 decode
	 * @param signature
	 * @return
	 */
	public static String decodeBase64(String signature){
		byte[] b = Base64.decodeBase64(signature.getBytes());
		String s=new String(b);
		return s;
	}

    /**
	 * return a  <code>len</code> not repeat random number String, len must be less than 10 
	 * @param len must less than 10
	 * @return
	 */
	public static String getRandomNumber(int len){
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for(int i=0;i<len;i++){
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num)+""), "");
		}
		return sb.toString();
	}
	
	/**
	 * Array to a comma-delimited string
	 * 
	 * @param src
	 * @return
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
	 * json object into JAVA List object
	 * @param jsonBuff
	 * @param clazz
	 * @param list
	 * @param classMap
	 */
	@SuppressWarnings("unchecked")
	public static <T> void jsonToList(String jsonBuff,Class<T> clazz,List<T> list, Map<String,Object> classMap){
		
		if(clazz == null)return;
		
		if(null == list){
			list=new ArrayList<T>();
		}
		JSONArray array = JSONArray.fromObject(jsonBuff); 
		
		Iterator<?> iter = array.iterator();
        while (iter.hasNext()) {
        	   JSONObject jsonObject = (JSONObject)iter.next();   
               list.add((T)JSONObject.toBean(jsonObject, clazz,classMap));   
        }
	}
	
	/**
	 * see StringUtils.isBlank(cs)
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs){
		return StringUtils.isBlank(cs);
	}
	
	/**
	 * see StringUtils.capitalize(src)
	 * 
	 * @param cs
	 * @return
	 */
	public static String capitalize(String src){
		return StringUtils.capitalize(src);
	}
	/**
	 * see StringUtils.uncapitalize(src)
	 * 
	 * @param cs
	 * @return
	 */
	public static String uncapitalize(String src){
		return StringUtils.uncapitalize(src);
	}
	
	/**
	 * see StringUtils.trim(String)
	 * 
	 * @param src
	 * @return
	 */
	public static String trim(String src) {
		if(isBlank(src)){
			return src;
		}
		
		return StringUtils.trim(src);
	};
	
	/**
	 * escape CSV  see StringEscapeUtils.escapeCsv(str)
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeCsv(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.escapeCsv(str);
	}
	
	/**
	 * <p>escape HTML  see StringEscapeUtils.escapeHtml(str)</p>
     * <p>
     * For example:
     * </p> 
     * <p><code>"bread" & "butter"</code></p>
     * becomes:
     * <p>
     * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
     * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.escapeHtml(str);
	}
	/**
	 * escape JAVA  see StringEscapeUtils.escapeJava(str)
     * For example:
     * </p> 
     * <p><code>"中国"</code></p>
     * becomes:
     * <p>
     * <code>\u4E2D\u56FD</code>.
     * </p>
	 *  --> 
	 * @param str
	 * @return
	 */
	public static String escapeJava(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.escapeJava(str);
	}
	
	/**
	 * escape SQL  see StringEscapeUtils.escapeSql(str)
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeSql(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.escapeSql(str);
	}
	
	/**
	 * escape XML  see StringEscapeUtils.escapeXml(str)
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeXml(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.escapeXml(str);
		
	}
	
	
	/**
	 * unescape CSV  see StringEscapeUtils.unescapeCsv(str)
	 * 
	 * @param str
	 * @return
	 */
	public static String unescapeCsv(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.unescapeCsv(str);
	}
	
	/**
	 * <p>unescape HTML  see StringEscapeUtils.unescapeHtml(str)</p>
	 * <p>
	 * For example:
	 * </p> 
	 * <p><code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code></p>
	 * becomes:
	 * <p>
	 * <code>"bread" & "butter"</code>.
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static String unescapeHtml(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.unescapeHtml(str);
	}
	/**
	 * unescape JAVA  see StringEscapeUtils.unescapeJava(str)
	 * For example:
	 * </p> 
	 * <p><code>\u4E2D\u56FD</code></p>
	 * becomes:
	 * <p>
	 * <code>"中国"</code>.
	 * </p>
	 *  --> 
	 * @param str
	 * @return
	 */
	public static String unescapeJava(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.unescapeJava(str);
	}
	
	
	/**
	 * unescape XML  see StringEscapeUtils.unescapeXml(str)
	 * 
	 * @param str
	 * @return
	 */
	public static String unescapeXml(String str){
		if(isBlank(str)){
			return str;
		}
		return StringEscapeUtils.unescapeXml(str);
	}
	

}
