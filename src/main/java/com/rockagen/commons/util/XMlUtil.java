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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML UTIL
 * 
 * @author AGEN
 * @since JDK1.6
 */
public class XMlUtil {

	// ~ Instance fields ==================================================

	/**
	 * 
	 */
	private static final Log log = LogFactory.getLog(XMlUtil.class);
	/**
	 * Default Encoding
	 */
	private static final String ENCODING = "UTF-8";

	// ~ Constructors ==================================================

	/**
	 */
	private XMlUtil() {

	}

	// ~ Methods ==================================================

	/**
	 * Format xml {@link OutputFormat#createPrettyPrint()}
	 * 
	 * @param xmlStr
	 * @param enc
	 * @return
	 */
	public static String formatPretty(String xmlStr, String enc) {

		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		OutputFormat formater = OutputFormat.createPrettyPrint();
		formater.setEncoding(enc);

		return format(xmlStr, formater, enc);
	}

	/**
	 * Format xml {@link OutputFormat#createCompactFormat()}
	 * 
	 * @param xmlStr
	 * @param enc
	 * @return Compact xml String
	 */
	public static String formatCompact(String xmlStr, String enc) {

		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		OutputFormat formater = OutputFormat.createCompactFormat();
		formater.setEncoding(enc);

		return format(xmlStr, formater, enc);
	}

	/**
	 * Format xml
	 * 
	 * @param xmlStr
	 * @param formater
	 *            {@link OutputFormat}
	 * @param enc
	 * @return
	 */
	public static String format(String xmlStr, OutputFormat formater, String enc) {
		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		SAXReader reader = new SAXReader();

		Document doc = null;
		XMLWriter writer = null;
		try {
			doc = reader.read(xmlStr);
			writer = new XMLWriter(formater);
			writer.write(doc);
			return writer.toString();
		} catch (DocumentException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}

		}

		return xmlStr;
	}

	/**
	 * Format xml, use xml file encoding value as OutputFormat encoding ,if not
	 * exist encoding attribute use UTF-8 by default
	 * {@link OutputFormat#createPrettyPrint()}
	 * 
	 * @param xmlStr
	 * @return format xml string
	 */
	public static String formatPretty(String xmlStr) {
		return formatPretty(xmlStr, getEncoding(xmlStr));
	}

	/**
	 * Format xml, use xml file encoding value as OutputFormat encoding,if not
	 * exist encoding attribute use UTF-8 by default
	 * {@link OutputFormat#createPrettyPrint()}
	 * 
	 * @param xmlStr
	 * @return format xml string
	 */
	public static String formatCompact(String xmlStr) {
		return formatCompact(xmlStr, getEncoding(xmlStr));
	}

	/**
	 * Obtain xml file encoding attribute
	 * 
	 * @param text
	 * @return encoding value
	 */
	public static String getEncoding(String xmlText) {
		String result = null;

		String xml = xmlText.trim();

		if (xml.startsWith("<?xml")) {
			int end = xml.indexOf("?>");
			int encIndex = xml.indexOf("encoding=");
			if (encIndex != -1) {
				String sub = xml.substring(encIndex + 9, end);
				result = CommUtil.substringBetween(sub, "\"", "\"");
				return result;
			}
		}
		return result;
	}

}
