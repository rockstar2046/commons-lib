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
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author AGEN
 * @since JDK1.6
 */
public class JsonUtil {
	
	
	
	//~ Instance fields ==================================================
	
	/**
	 * 
	 */
	private static final  Log log = LogFactory.getLog(JsonUtil.class);

	
	/**
	 * ObjectMapper Single instance
	 */
	private static volatile ObjectMapper MAPPER;
	/**
	 * JsonFactory
	 */
	private static volatile JsonFactory JSONFACTORY;
	
	//~ Constructors ==================================================
	
	/**
	 */
	private JsonUtil(){
		
	}
	
	
	//~ Methods ==================================================
	
	/**
	 * return a ObjectMapper if exist,else new ObjectMapper
	 * @return ObjectMapper
	 */
	public static ObjectMapper getMapper(){
		if(MAPPER==null){
			synchronized (ObjectMapper.class) {
				MAPPER = new ObjectMapper();
				MAPPER.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES , false);
				MAPPER.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS  , false);
			}
		}
		return MAPPER;
	}
	/**
	 * return a JsonFactory if exist,else new JsonFactory
	 * @return JsonFactory
	 */
	public static JsonFactory getJsonFactory(){
		if(JSONFACTORY==null){
				JSONFACTORY = new JsonFactory();
		}
		return JSONFACTORY;
	}
	

	
	 /** 
     * Json string to java bean
     * <br/>
     * <p>List,Array,Map</p>
     * <code><pre>
     * clazz= new TypeReference&lt;List&lt;MyBean&gt;&gt;(){};
     * 
     * e.g:
     * toBeran(jsonStr,new TypeReference&lt;List&lt;MyBean&gt;&gt;(){})
     * 
     * toBeran(jsonStr, MyBean[])
     * 
     * toBeran(jsonStr, new TypeReference&lt;Map&lt;String, Map&lt;String, MyBean&gt;&gt;(){})
     * </code></p>
     * @param jsonStr 
     * @param valueTypeRef 
     * @return bean
     */  
    public static <T> T toBean(String jsonStr, TypeReference<T> valueTypeRef){ 
    	if(valueTypeRef==null) return null;
    	
    	T obj=null;
		try {
			obj = getMapper().readValue(jsonStr, valueTypeRef);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
    	 return obj;
    	 
    }  
    /** 
     * Json string to java bean
     * <br/>
     * <p>e.g:</p>
     * <code><pre>
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}} 
     * </pre></code>
     * <p>List,Array,Map... see {@link #toBean(String, TypeReference)}</p>
     * @param jsonStr 
     * @param clazz 
     * @return bean
     */  
    public static <T> T toBean(String jsonStr, Class<T> clazz){ 
    	if(clazz==null) return null;
    	
    	T obj=null;
    	try {
    		obj = getMapper().readValue(jsonStr, clazz);
    	} catch (JsonParseException e) {
    		log.error(e);
    	} catch (JsonMappingException e) {
    		log.error(e);
    	} catch (IOException e) {
    		log.error(e);
    	}
    	return obj;
    	
    }  
    
    
    /**
     * Bean to json string
     * @param obj
     * @return json string
     */
    public static <T> String toJson(T obj) {   
            StringWriter writer = new StringWriter();   
            String jsonStr="";
            JsonGenerator gen=null;
			try {
				gen = getJsonFactory().createJsonGenerator(writer);
				  getMapper().writeValue(gen, obj);   
		           jsonStr= writer.toString();   
		          
			} catch (IOException e) {
				log.error(e);
			}finally{
				if(gen!=null){
					try {
						gen.close();
					} catch (IOException e) {
					}
				}
				
			}
            return jsonStr;   
        }  
      
  

}
