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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author AGEN
 * @since JDK1.6
 * 
 */
public class ReflexUtilTest {

	@Test
	public void testGetInstance() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "ROCKAGEN");
		map.put("age", 21);
		map.put("email", null);
		map.put("addr", "");
		System.out.println(ReflexUtil
				.getBasicInstance(TestVo.class, map, false));
	}

	@Test
	public void testGetInstance2() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "ROCKAGEN");
		map.put("age", 21);
		map.put("email", null);
		map.put("addr", "");

		System.out.println(ReflexUtil
				.getBasicInstance(TestVo2.class, map, true));
	}

	@Test
	public void testGetInstance3() throws IllegalArgumentException,
			IllegalAccessException {

		Class<?>[] clazz = new Class<?>[] { String.class, int.class,
				String.class };
		Object[] obj = new Object[] { "ROCKAGEN", 21, "agen@rockagen.com" };
		Object[] obj2 = new Object[] { "ROCKAGEN", 21, "agen@rockagen.com",
				"CHINA" };
		System.out.println(ClassUtil.getInstance(TestVo2.class, true, clazz,
				obj));
		System.out.println(ClassUtil.getInstance(TestVo2.class, true, clazz,
				obj2));
		// System.out.println(ClassUtil.getInstance(TestVo2.class, false,
		// clazz,obj));
		TestVo2 vo2 = ClassUtil.getInstance(TestVo2.class, true, clazz, obj);

		System.out.println(ReflexUtil.getFieldValue(vo2, "name", false));
		System.out.println(ReflexUtil.getFieldValue(vo2, "email", false));
		System.out.println(ReflexUtil.getFieldValue(vo2, "addr", false));

		ReflexUtil.setFieldValue(vo2, "name", "ROCKAGEN-USA", false);
		ReflexUtil.setFieldValue(vo2, "email", "rockagen@gmail.com", false);
		ReflexUtil.setFieldValue(vo2, "addr", "USA", false);
		System.out.println("========================================");
		System.out.println(ReflexUtil.getFieldValue(vo2, "name", false));
		System.out.println(ReflexUtil.getFieldValue(vo2, "email", false));
		System.out.println(ReflexUtil.getFieldValue(vo2, "addr", false));
	}

	@Test
	public void testGetAnnotations() {
		System.out.println("Method: "
				+ Arrays.toString(ClassUtil.getAnnotatedDeclaredMethods(
						TestVo3.class, Deprecated.class, true)));
		System.out.println("Constructor: "
				+ Arrays.toString(ClassUtil.getAnnotatedDeclaredConstructors(
						TestVo3.class, Deprecated.class, true)));
		System.out.println("Field: "
				+ Arrays.toString(ClassUtil.getAnnotatedDeclaredFields(
						TestVo3.class, Deprecated.class, true)));
	}

	@Test
	public void testClassUtils() {
		System.out.println("Field: "
				+ Arrays.toString(ClassUtil.getDeclaredFields(TestVo3.class,
						true)));
		System.out.println("Constructor: "
				+ Arrays.toString(ClassUtil.getDeclaredConstructors(
						TestVo3.class, true)));
		System.out.println("Method: "
				+ Arrays.toString(ClassUtil.getDeclaredMethods(TestVo3.class,
						true)));

		System.out
				.println("==============================================================");
		System.out.println("Constructor: "
				+ ClassUtil.getDeclaredConstructor(TestVo3.class, true));
		System.out.println("Method: "
				+ ClassUtil.getDeclaredMethod(TestVo3.class, true, "setName",
						new Class[] { String.class }));
	}

}
