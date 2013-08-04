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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 * @author AGEN
 * @since JDK1.6
 * @see JACKSON 1.9.13
 */
public class JsonUtil {

	// ~ Instance fields ==================================================

	/**
	 * 
	 */
	private static final Log log = LogFactory.getLog(JsonUtil.class);

	/**
	 * ObjectMapper Single instance
	 */
	private static volatile ObjectMapper MAPPER;
	/**
	 * JsonFactory
	 */
	private static volatile JsonFactory JSONFACTORY;

	// ~ Constructors ==================================================

	/**
	 */
	private JsonUtil() {

	}

	// ~ Methods ==================================================

	/**
	 * return a ObjectMapper if exist,else new ObjectMapper
	 * 
	 * @return ObjectMapper
	 */
	public static ObjectMapper getMapper() {
		if (MAPPER == null) {
			synchronized (ObjectMapper.class) {
				MAPPER = new ObjectMapper();
				MAPPER.configure(
						org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
				MAPPER.configure(
						org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
						false);
				MAPPER.configure(
						org.codehaus.jackson.map.SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE,
						true);
			}
		}
		return MAPPER;
	}

	/**
	 * return a JsonFactory if exist,else new JsonFactory
	 * 
	 * @return JsonFactory
	 */
	public static JsonFactory getJsonFactory() {
		if (JSONFACTORY == null) {
			JSONFACTORY = new JsonFactory();
		}
		return JSONFACTORY;
	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonStr,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonStr, MyBean[])
	 * 
	 * toBean(jsonStr, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonStr
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(String jsonStr, TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonStr == null)
			return null;
		return toBean(new MyJsonParser(jsonStr), valueTypeRef);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonBytes,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonBytes, MyBean[])
	 * 
	 * toBean(jsonBytes, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonBytes
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(byte[] jsonBytes, TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonBytes == null)
			return null;
		return toBean(new MyJsonParser(jsonBytes), valueTypeRef);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonReader,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonReader, MyBean[])
	 * 
	 * toBean(jsonReader, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonReader
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(Reader jsonReader, TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonReader == null)
			return null;
		return toBean(new MyJsonParser(jsonReader), valueTypeRef);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonURL,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonURL, MyBean[])
	 * 
	 * toBean(jsonURL, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonURL
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(URL jsonURL, TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonURL == null)
			return null;
		return toBean(new MyJsonParser(jsonURL), valueTypeRef);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonFile,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonFile, MyBean[])
	 * 
	 * toBean(jsonFile, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonFile
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(File jsonFile, TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonFile == null)
			return null;
		return toBean(new MyJsonParser(jsonFile), valueTypeRef);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * call {@link MyJsonParser#getJsonParser()} return a JsonParser
	 * </p>
	 * 
	 * @param myJsonParser
	 * @param valueTypeRef
	 * @see #toBean(JsonParser, TypeReference)
	 * @return bean
	 */
	protected static <T> T toBean(MyJsonParser myJsonParser,
			TypeReference<T> valueTypeRef) {
		if (myJsonParser.getJsonParser() == null)
			return null;
		return toBean(myJsonParser.getJsonParser(), valueTypeRef);
	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * List,Array,Map
	 * </p>
	 * <code><pre>
	 * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
	 * 
	 * e.g:
	 * toBean(jsonParser,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
	 * 
	 * toBean(jsonParser, MyBean[])
	 * 
	 * toBean(jsonParser, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
	 * </code></p>
	 * 
	 * @param jsonParser
	 * @param valueTypeRef
	 * @return bean
	 */
	public static <T> T toBean(JsonParser jsonParser,
			TypeReference<T> valueTypeRef) {
		if (valueTypeRef == null || jsonParser == null)
			return null;

		T obj = null;
		try {
			obj = getMapper().readValue(jsonParser, valueTypeRef);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (jsonParser != null) {
				try {
					jsonParser.close();
				} catch (IOException e) {
				}
			}
		}
		return obj;
	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(String, TypeReference)}
	 * </p>
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(String jsonStr, Class<T> clazz) {
		if (clazz == null || jsonStr == null)
			return null;
		return toBean(new MyJsonParser(jsonStr), clazz);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(byte[], TypeReference)}
	 * </p>
	 * 
	 * @param jsonBytes
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(byte[] jsonBytes, Class<T> clazz) {
		if (clazz == null || jsonBytes == null)
			return null;
		return toBean(new MyJsonParser(jsonBytes), clazz);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(Reader, TypeReference)}
	 * </p>
	 * 
	 * @param jsonReader
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(Reader jsonReader, Class<T> clazz) {
		if (clazz == null || jsonReader == null)
			return null;
		return toBean(new MyJsonParser(jsonReader), clazz);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(URL, TypeReference)}
	 * </p>
	 * 
	 * @param jsonURL
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(URL jsonURL, Class<T> clazz) {
		if (clazz == null || jsonURL == null)
			return null;
		return toBean(new MyJsonParser(jsonURL), clazz);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(File, TypeReference)}
	 * </p>
	 * 
	 * @param jsonFile
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(File jsonFile, Class<T> clazz) {
		if (clazz == null || jsonFile == null)
			return null;
		return toBean(new MyJsonParser(jsonFile), clazz);

	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * call {@link MyJsonParser#getJsonParser()} return a JsonParser
	 * </p>
	 * 
	 * @param myJsonParser
	 * @param clazz
	 * @see #toBean(JsonParser, Class)
	 * @return bean
	 */
	protected static <T> T toBean(MyJsonParser myJsonParser, Class<T> clazz) {
		if (myJsonParser.getJsonParser() == null)
			return null;
		return toBean(myJsonParser.getJsonParser(), clazz);
	}

	/**
	 * Json string to java bean <br/>
	 * <p>
	 * e.g:
	 * </p>
	 * <code><pre>
	 * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
	 * </pre></code>
	 * <p>
	 * List,Array,Map... see {@link #toBean(String, TypeReference)}
	 * </p>
	 * 
	 * @param jsonParser
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(JsonParser jsonParser, Class<T> clazz) {
		if (clazz == null || jsonParser == null)
			return null;

		T obj = null;
		try {
			obj = getMapper().readValue(jsonParser, clazz);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if (jsonParser != null) {
				try {
					jsonParser.close();
				} catch (IOException e) {
				}
			}
		}
		return obj;

	}

	/**
	 * Bean to json string
	 * 
	 * @param obj
	 * @return json string
	 */
	public static <T> String toJson(T obj) {
		StringWriter writer = new StringWriter();
		String jsonStr = "";
		JsonGenerator gen = null;
		try {
			gen = getJsonFactory().createJsonGenerator(writer);
			getMapper().writeValue(gen, obj);
			writer.flush();
			jsonStr = writer.toString();

		} catch (IOException e) {
			log.error(e);
		} finally {
			if (gen != null) {
				try {
					gen.close();
				} catch (IOException e) {
				}
			}

		}
		return jsonStr;
	}

	/**
	 * JsonParser Helper</br> provide to
	 * {@link JsonUtil#toBean(MyJsonParser, Class)} and
	 * {@link JsonUtil#toBean(MyJsonParser, TypeReference)}
	 */
	protected static class MyJsonParser {

		// ~ Instance fields ==================================================

		/**
		 * JsonParser
		 */
		private JsonParser jsonParser;

		// ~ Constructors ==================================================

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(File)}
		 * 
		 * @param obj
		 */
		public MyJsonParser(File obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(Reader)}
		 * 
		 * @param obj
		 */
		public MyJsonParser(Reader obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(byte[])}
		 * 
		 * @param obj
		 */
		public MyJsonParser(byte[] obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(String)}
		 * 
		 * @param obj
		 */
		public MyJsonParser(String obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(URL)}
		 * 
		 * @param obj
		 */
		public MyJsonParser(URL obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		/**
		 * Set jsonParser from {@link JsonFactory#createJsonParser(InputStream)}
		 * 
		 * @param obj
		 */
		public MyJsonParser(InputStream obj) {
			try {
				this.jsonParser = getJsonFactory().createJsonParser(obj);
			} catch (JsonParseException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}

		// ~ Methods ==================================================

		/**
		 * Return a JsonParser by Constructors
		 * 
		 * @return jsonParser
		 */
		public JsonParser getJsonParser() {
			return jsonParser;
		}

	}

}
