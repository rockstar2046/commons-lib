/*
 * Copyright 2014 the original author or authors.
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
import java.io.StringReader;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

/**
 * XML UTIL
 * 
 * @author RA
 * @since JDK1.6
 * @since DOM4J 1.6.1
 * @since XSTREAM 1.4.5
 */
public class XmlUtil {

	// ~ Instance fields ==================================================

	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);
	/**
	 * Default Encoding
	 */
	private static final String ENCODING = "UTF-8";

	// ~ Constructors ==================================================

	/**
	 */
	private XmlUtil() {

	}

	// ~ Methods ==================================================

	/**
	 * Format xml {@link OutputFormat#createPrettyPrint()}
	 * 
	 * @param xmlStr
	 * @param enc
	 * @return pretty String
	 */
	public static String formatPretty(String xmlStr, String enc) {
		return formatPretty(xmlStr, enc, false);
	}

	/**
	 * Format xml {@link OutputFormat#createPrettyPrint()}
	 * 
	 * @param xmlStr
	 * @param enc
	 * @param isSuppressDeclaration
	 * @return pretty String
	 */
	public static String formatPretty(String xmlStr, String enc,
			boolean isSuppressDeclaration) {

		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		OutputFormat formater = OutputFormat.createPrettyPrint();
		formater.setEncoding(enc);
		formater.setSuppressDeclaration(isSuppressDeclaration);
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
		return formatCompact(xmlStr, enc, false);
	}

	/**
	 * Format xml {@link OutputFormat#createCompactFormat()}
	 * 
	 * @param xmlStr
	 * @param enc
	 * @param isSuppressDeclaration
	 * @return Compact xml String
	 */
	public static String formatCompact(String xmlStr, String enc,
			boolean isSuppressDeclaration) {

		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		OutputFormat formater = OutputFormat.createCompactFormat();
		formater.setEncoding(enc);
		formater.setSuppressDeclaration(isSuppressDeclaration);
		return format(xmlStr, formater, enc);
	}

	/**
	 * Format xml
	 * 
	 * @param xmlStr
	 * @param formater
	 *            {@link OutputFormat}
	 * @param enc
	 * @return format String
	 */
	public static String format(String xmlStr, OutputFormat formater, String enc) {
		if (CommUtil.isBlank(xmlStr))
			return xmlStr;

		if (enc == null)
			enc = ENCODING;

		SAXReader reader = new SAXReader();
		StringReader sr = new StringReader(xmlStr);

		Document doc = null;
		XMLWriter writer = null;
		StringWriter sw = new StringWriter();
		try {
			doc = reader.read(sr);
			writer = new XMLWriter(sw, formater);
			writer.write(doc);
			return sw.toString();
		} catch (DocumentException e) {
			log.error("{}",e.getMessage(),e);
		} catch (IOException e) {
			log.error("{}",e.getMessage(),e);
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
	 * @param xmlStr
	 * @return encoding value
	 */
	public static String getEncoding(String xmlStr) {
		String result = null;

		String xml = xmlStr.trim();

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

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from serialize objects to XML.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  
	 *  then,
	 *  
	 * 	toXml(bean,xa,xaf,xaa,xf);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#NO_REFERENCES}
	 * 
	 * @param obj
	 *            bean
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @return xml String
	 * @since XSTREAM 1.4.5
	 */
	public static <T> String toXml(T obj, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields) {

		return toXml(obj, xAlias, xAliasFields, xAliasAttributes, xOmitFields,
				null, null, null);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from serialize objects to XML.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  
	 *  then,
	 *  
	 * 	toXml(bean,xa,xaf,xaa,xf);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#ID_REFERENCES}
	 * 
	 * @param obj
	 *            bean
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @return xml String
	 * @since XSTREAM 1.4.5
	 */
	public static <T> String toXmlWithIdRef(T obj, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields) {

		return toXmlWithIdRef(obj, xAlias, xAliasFields, xAliasAttributes,
				xOmitFields, null, null, null);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from serialize objects to XML.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  XImplicitCollection[] xic=null;
	 *  XImmutableType[] xit=null;
	 *  XConverter[] xc=null;
	 *  then,
	 *  
	 * 	toXml(bean,xa,xaf,xaa,xf,xic,xit,xc);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#NO_REFERENCES}
	 * 
	 * @param obj
	 *            bean
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @param xImplicitCollection
	 *            Adds implicit collection which is used for all items of the
	 *            given element name defined by itemFieldName.
	 * @param xImmutableTypes
	 *            Add immutable types. The value of the instances of these types
	 *            will always be written into the stream even if they appear
	 *            multiple times.
	 * @param xConverters
	 *            register converter
	 * @return xml String
	 * @since XSTREAM 1.4.5
	 */
	public static <T> String toXml(T obj, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {
		return (String) parse(0, XStream.NO_REFERENCES, obj, xAlias,
				xAliasFields, xAliasAttributes, xOmitFields,
				xImplicitCollection, xImmutableTypes, xConverters);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from serialize objects to XML.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  XImplicitCollection[] xic=null;
	 *  XImmutableType[] xit=null;
	 *  XConverter[] xc=null;
	 *  then,
	 *  
	 * 	toXml(bean,xa,xaf,xaa,xf,xic,xit,xc);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#ID_REFERENCES}
	 * 
	 * @param obj
	 *            bean
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @param xImplicitCollection
	 *            Adds implicit collection which is used for all items of the
	 *            given element name defined by itemFieldName.
	 * @param xImmutableTypes
	 *            Add immutable types. The value of the instances of these types
	 *            will always be written into the stream even if they appear
	 *            multiple times.
	 * @param xConverters
	 *            register converter
	 * @return xml String
	 * @since XSTREAM 1.4.5
	 */
	public static String toXmlWithIdRef(Object obj, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {

		return (String) parse(0, XStream.ID_REFERENCES, obj, xAlias,
				xAliasFields, xAliasAttributes, xOmitFields,
				xImplicitCollection, xImmutableTypes, xConverters);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from xml to serialize objects.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  
	 *  
	 *  then,
	 *  
	 * 	toBean(xmlStr,xa,xaf,xaa,xf);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#NO_REFERENCES}
	 * 
	 * @param xmlStr
	 *            xml String
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @since XSTREAM 1.4.5
	 * 
	 * @return Object
	 */
	public static Object toBean(String xmlStr, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields) {
		return toBean(xmlStr, xAlias, xAliasFields, xAliasAttributes,
				xOmitFields, null, null, null);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from XML to serialize objects.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")}
	 *  
	 *  
	 *  then,
	 *  
	 * 	toBean(xmlStr,xa,xaf,xaa,xf);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#ID_REFERENCES}
	 * 
	 * @param xmlStr
	 *            xml String
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @since XSTREAM 1.4.5
	 * 
	 * @return Object
	 */
	public static Object toBeanWithIdRef(String xmlStr, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields) {
		return toBeanWithIdRef(xmlStr, xAlias, xAliasFields, xAliasAttributes,
				xOmitFields, null, null, null);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from XML to serialize objects.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")};
	 *  XImplicitCollection[] xic=null;
	 *  XImmutableType[] xit=null;
	 *  XConverter[] xc=null;
	 *  then,
	 *  
	 * 	toBean(xmlStr,xa,xaf,xaa,xf,xic,xit,xc);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#NO_REFERENCES}
	 * 
	 * @param xmlStr
	 *            xml String
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @param xImplicitCollection
	 *            Adds implicit collection which is used for all items of the
	 *            given element name defined by itemFieldName.
	 * @param xImmutableTypes
	 *            Add immutable types. The value of the instances of these types
	 *            will always be written into the stream even if they appear
	 *            multiple times.
	 * @param xConverters
	 *            register converter
	 * @since XSTREAM 1.4.5
	 * 
	 * @return Object
	 */
	public static Object toBean(String xmlStr, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {
		return parse(1, XStream.NO_REFERENCES, xmlStr, xAlias, xAliasFields,
				xAliasAttributes, xOmitFields, xImplicitCollection,
				xImmutableTypes, xConverters);
	}

	/**
	 * <P>
	 * Bean to xml.
	 * <P>
	 * <P>
	 * Using XStream library from xml to serialize objects.
	 * </P>
	 * <p>
	 * example:
	 * </p>
	 * <code><pre>
	 * 
	 *  XAlias[] xa={new XAlias("Foo",Foo.class)};
	 *  XAliasField[] xaf={new XAliasField("Bar",Bar.class,"bar")};
	 *  XAliasAttribute[] xaa={new XAliasAttribute("Name",User.class,"name")};
	 *  XOmitField[] xf={new XOmitField(V.class,"v")};
	 *  XImplicitCollection[] xic=null;
	 *  XImmutableType[] xit=null;
	 *  XConverter[] xc=null;
	 *  then,
	 *  
	 * 	toBean(xmlStr,xa,xaf,xaa,xf,xic,xit,xc);
	 * </pre></code> <b>Note: XStream Mode is {@link XStream#ID_REFERENCES}
	 * 
	 * @param xmlStr
	 *            xml String
	 * @param xAlias
	 *            Alias a Class to a shorter name to be used in XML elements.
	 * @param xAliasFields
	 *            Create an alias for a field name.
	 * @param xAliasAttributes
	 *            Create an alias for an attribute.
	 * @param xOmitFields
	 *            Prevents a field from being serialized.
	 * @param xImplicitCollection
	 *            Adds implicit collection which is used for all items of the
	 *            given element name defined by itemFieldName.
	 * @param xImmutableTypes
	 *            Add immutable types. The value of the instances of these types
	 *            will always be written into the stream even if they appear
	 *            multiple times.
	 * @param xConverters
	 *            register converter
	 * @since XSTREAM 1.4.5
	 * 
	 * @return Object
	 */
	public static Object toBeanWithIdRef(String xmlStr, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {

		return parse(1, XStream.ID_REFERENCES, xmlStr, xAlias, xAliasFields,
				xAliasAttributes, xOmitFields, xImplicitCollection,
				xImmutableTypes, xConverters);
	}

	/**
	 * Parser
	 * 
	 * @param parseMod
	 *            0 is toXml
	 * @param mode
	 *            XStream.ID_REFERENCES or XStream.NO_REFERENCES
	 * @param value
	 * @param xAlias
	 * @param xAliasFields
	 * @param xAliasAttributes
	 * @param xOmitFields
	 * @param xImplicitCollection
	 * @param xImmutableTypes
	 * @param xConverters
	 * @return Object
	 */
	private final static Object parse(int parseMod, int mode, Object value,
			XAlias[] xAlias, XAliasField[] xAliasFields,
			XAliasAttribute[] xAliasAttributes, XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {

		if (value == null) {
			return null;
		}
		final Object src = value;
		if (src instanceof String) {
			if (src.equals("")) {
				return null;
			}
		}

		final XStream xstream = new XStream();
		xstream.setMode(mode);
		initXstream(xstream, xAlias, xAliasFields, xAliasAttributes,
				xOmitFields, xImplicitCollection, xImmutableTypes, xConverters);

		if (parseMod == 0) {
			return xstream.toXML(src);
		} else {
			return xstream.fromXML((String) src);
		}

	}

	/**
	 * XStream Alias
	 * 
	 * @param xstream
	 * @param xAlias
	 * @param xAliasFields
	 * @param xAliasAttributes
	 * @param xOmitFields
	 * @param xImplicitCollection
	 * @param xImmutableTypes
	 * @param xConverters
	 */
	protected static void initXstream(XStream xstream, XAlias[] xAlias,
			XAliasField[] xAliasFields, XAliasAttribute[] xAliasAttributes,
			XOmitField[] xOmitFields,
			XImplicitCollection[] xImplicitCollection,
			XImmutableType[] xImmutableTypes, XConverter[] xConverters) {
		
		if (xOmitFields != null) {
			for (XOmitField xof : xOmitFields) {
				xstream.omitField(xof.classType, xof.fieldName);
			}
		}
		if (xImplicitCollection != null) {
			for (XImplicitCollection xic : xImplicitCollection) {
				xstream.addImplicitCollection(xic.ownerType, xic.fieldName,
						xic.itemFieldName, xic.itemType);
			}
		}
		if (xImmutableTypes != null) {
			for (XImmutableType xit : xImmutableTypes) {
				xstream.addImmutableType(xit.type);
			}
		}
		if (xConverters != null) {
			for (XConverter xc : xConverters) {
				xstream.registerConverter(xc.converter, xc.priority);
			}
		}
		
		if (xAlias != null) {
			for (XAlias xa : xAlias) {
				xstream.alias(xa.aliasName, xa.classType);
			}
		}
		if (xAliasFields != null) {
			for (XAliasField xaf : xAliasFields) {
				xstream.aliasField(xaf.aliasName, xaf.fieldType, xaf.fieldName);
			}
		}
		if (xAliasAttributes != null) {
			for (XAliasAttribute xaa : xAliasAttributes) {
				xstream.useAttributeFor(xaa.attributeType, xaa.attributeName);
				xstream.aliasAttribute(xaa.attributeType, xaa.attributeName,
						xaa.aliasName);
			}
		}

	}

	/**
	 * See {@link XStream#alias(String, Class, Class)}
	 */
	public static class XAlias {
		private final String aliasName;
		private final Class<?> classType;

		public XAlias(String aliasName, Class<?> classType) {
			this.aliasName = aliasName;
			this.classType = classType;
		}

		public String getAliasName() {
			return aliasName;
		}

		public Class<?> getClassType() {
			return classType;
		}

	}

	/**
	 * See {@link XStream#omitField(Class, String)}
	 */
	public static class XOmitField {
		private final String fieldName;
		private final Class<?> classType;

		public XOmitField(Class<?> classType, String fieldName) {
			this.fieldName = fieldName;
			this.classType = classType;
		}

		public String getFieldName() {
			return fieldName;
		}

		public Class<?> getClassType() {
			return classType;
		}

	}

	/**
	 * See {@link XStream#addImmutableType(Class)}
	 */
	public static class XImmutableType {
		private final Class<?> type;

		public XImmutableType(Class<?> type) {
			this.type = type;
		}

		public Class<?> getType() {
			return type;
		}

	}

	/**
	 * See {@link XStream#registerConverter(Converter,int)}
	 */
	public static class XConverter {

		private final Converter converter;
		private final int priority;

		public XConverter(Converter converter, int priority) {
			this.converter = converter;
			this.priority = priority;
		}

		public Converter getConverter() {
			return converter;
		}

		public int getPriority() {
			return priority;
		}

	}

	/**
	 * See {@link XStream#addImplicitCollection(Class, String , String , Class)}
	 */
	public static class XImplicitCollection {

		private final Class<?> ownerType;
		private final String fieldName;
		private final String itemFieldName;
		private final Class<?> itemType;

		public XImplicitCollection(Class<?> ownerType, String fieldName,
				String itemFieldName, Class<?> itemType) {
			this.ownerType = ownerType;
			this.fieldName = fieldName;
			this.itemFieldName = itemFieldName;
			this.itemType = itemType;
		}

		public Class<?> getOwnerType() {
			return ownerType;
		}

		public String getFieldName() {
			return fieldName;
		}

		public String getItemFieldName() {
			return itemFieldName;
		}

		public Class<?> getItemType() {
			return itemType;
		}

	}

	/**
	 * See {@link XStream#aliasField(String, Class, String)}
	 */
	public static class XAliasField {

		private final String aliasName;
		private final Class<?> fieldType;
		private final String fieldName;

		public XAliasField(String aliasName, Class<?> fieldType,
				String fieldName) {
			this.aliasName = aliasName;
			this.fieldType = fieldType;
			this.fieldName = fieldName;
		}

		public String getAliasName() {
			return aliasName;
		}

		public Class<?> getFieldType() {
			return fieldType;
		}

		public String getFieldName() {
			return fieldName;
		}

	}

	/**
	 * See {@link XStream#aliasAttribute(Class, String, String)}
	 */
	public static class XAliasAttribute {

		private final String aliasName;
		private final Class<?> attributeType;
		private final String attributeName;

		public XAliasAttribute(String aliasName, Class<?> attributeType,
				String attributeName) {
			this.aliasName = aliasName;
			this.attributeType = attributeType;
			this.attributeName = attributeName;
		}

		public String getAliasName() {
			return aliasName;
		}

		public Class<?> getAttributeType() {
			return attributeType;
		}

		public String getAttributeName() {
			return attributeName;
		}

	}

}
