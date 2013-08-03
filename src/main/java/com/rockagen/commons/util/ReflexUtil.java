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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reflex Utils
 * 
 * @author AGEN
 * @since JDK1.6
 */
public class ReflexUtil {

	
	
	//~ Instance fields ==================================================
	
	/**
	 * 
	 */
	private static final  Log log = LogFactory.getLog(ReflexUtil.class);

	
	//~ Constructors ==================================================
	
	/**
	 */
	private ReflexUtil() {
	}

	//~ Methods ==================================================
	
	/**
	 * 
	 * Direct reading the object attribute values,the private / protected modifiers will be ignoring.if getter function exist,return getter function value</br>
	 * If recursively is true, will looking from all class hierarchy
	 * 
	 * @param object
	 * @param fieldName
	 * @param recursively
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @return object
	 */
	public static Object getFieldValue(final Object object, String fieldName,boolean recursively) throws IllegalArgumentException, IllegalAccessException {
		Field field = ClassUtil.getDeclaredField(object.getClass(), fieldName,recursively);
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		String methodName="get"+CommUtil.capitalize(fieldName);
		Method method=ClassUtil.getDeclaredMethod(object.getClass(), recursively, methodName);
		if(method!=null){
			try {
				makeAccessible(method);
				return method.invoke(object);
			} catch (InvocationTargetException e) {
				// do not
			}
		}
		makeAccessible(field);
		Object result = null;
		result = field.get(object);
		return result;
	}

	
	/**
	 * Direct writing the object attribute values, the private / protected modifiers will be ignoring,if setter function exist,return setter function value.</br>
	 * If recursively is true, will looking from all class hierarchy
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 * @param recursively
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException 
	 */
	public static void setFieldValue(final Object object, String fieldName, final Object value,boolean recursively)throws IllegalArgumentException, IllegalAccessException {
		Field field = ClassUtil.getDeclaredField(object.getClass(), fieldName,recursively);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		String methodName="set"+CommUtil.capitalize(fieldName);

		try {
		Method method=ClassUtil.getDeclaredMethod(object.getClass(), recursively, methodName,value.getClass());
		if(method!=null){
			
				makeAccessible(method);
				method.invoke(object,value);
				return ;
			
		}
		} catch (InvocationTargetException e) {
			// do not
		}catch (NullPointerException e){
			log.warn(object+" field: ["+fieldName+"] is null.");
		}
		makeAccessible(field);

		field.set(object, value);
	}


	
	/**
	 * set attribute is accessible
	 * @param field
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}
	/**
	 * set method is accessible
	 * @param method
	 */
	public static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}
	
	/**
	 * set constructor is accessible
	 * @param constructor
	 */
	public static <T> void makeAccessible(Constructor<T> constructor) {
		if (!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
			constructor.setAccessible(true);
		}
	}
	
	/**
	 * obtained the parent class generic parameter types</br>
	 * <p>for exmaple:</p>
	 * <code>
	 *  ClassB&lt;T&gt;extends ClassA&ltT&gt;
	 * </code>
	 * @param clazz 
	 * @return Type
	 */
	public static Type[] getSuperClassGenricType(final Class<?> clazz) {

		Type[] temp={Object.class};
		// eg: ClassA<T>
		Type type = clazz.getGenericSuperclass();
		
		if (type instanceof ParameterizedType) {
			
			Type[] params = ((ParameterizedType) type).getActualTypeArguments();

			return  params;
			
		}else{
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return temp;
		}


	}
	
	/**
	 * Create new instance of specified class and type from a map</br>
	 * <b>note: clazz must write setter</b>
	 * @param clazz
	 * @param paramsMap attributes
	 * @param accessible
	 * @return instance
	 */
	public static <T> T getBasicInstance(Class<T> clazz,Map<String,Object> paramsMap,boolean accessible){
		
		if(clazz !=null && paramsMap !=null && paramsMap.size()>0 ){
			
			T instance = ClassUtil.getInstance(clazz,accessible);
			// No automatic processing attributes
			StringBuffer bufnp = new StringBuffer("No automatic processing attributes: [");
			
			 for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
				 String key =entry.getKey();
				 if(CommUtil.isBlank(key)){
					 continue;
				 }
				 key=CommUtil.uncapitalize(key);
				 
				 try {
					setFieldValue(instance,key, entry.getValue(), false);
				} catch (IllegalArgumentException e1) {
					bufnp.append(key).append(',');
				} catch (IllegalAccessException e1) {
					bufnp.append(key).append(',');
				}
					
			 }
			 bufnp.append("]");
			 log.warn(CommUtil.removeEnd(bufnp.toString(), ","));	
			 return instance;
		}else{
			return null;
		}
			
	}	
	
}
