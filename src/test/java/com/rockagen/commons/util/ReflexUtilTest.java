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

import java.sql.Ref;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author RA
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
        TestVo obj=ReflexUtil.getBasicInstance(TestVo.class, map, false);
        Assert.assertEquals(21, obj.getAge());
	}


	@Test
	public void testGetInstance2() throws IllegalArgumentException,
			IllegalAccessException {

		Class<?>[] clazz = new Class<?>[] { String.class, int.class,
				String.class };
		Object[] obj = new Object[] { "ROCKAGEN", 21, "agen@rockagen.com" };
		TestVo2 vo2 = ClassUtil.getInstance(TestVo2.class, true, clazz, obj);

		Assert.assertEquals("ROCKAGEN",
				ReflexUtil.getFieldValue(vo2, "name", false));
		Assert.assertEquals("agen@rockagen.com",
				ReflexUtil.getFieldValue(vo2, "email", false));
		Assert.assertEquals("CHINA",
				ReflexUtil.getFieldValue(vo2, "addr", false));

		ReflexUtil.setFieldValue(vo2, "name", "ROCKAGEN-USA", false);
		ReflexUtil.setFieldValue(vo2, "email", "rockagen@gmail.com", false);
		ReflexUtil.setFieldValue(vo2, "addr", "USA", false);

		Assert.assertEquals("ROCKAGEN-USA",
				ReflexUtil.getFieldValue(vo2, "name", false));
		Assert.assertEquals("rockagen@gmail.com",
				ReflexUtil.getFieldValue(vo2, "email", false));
		Assert.assertEquals("USA", ReflexUtil.getFieldValue(vo2, "addr", false));
	}

	@Test
    @Ignore
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
	@Ignore
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
                new Class[]{String.class}));
	}
    
    @Test
    public void testIntefaceGenericTypeClass(){
        Class<?>[] clazz=ReflexUtil.getInterfacesGenricClasses(TestInClass.class);
        Class[] exp={String.class,Long.class,Integer.class};
        Assert.assertArrayEquals(clazz, exp);

    }   
    @Test
    public void testSuperClassesGenericTypeClass(){
        Class<?>[] clazz=ReflexUtil.getSuperClassGenricClasses(TestInClass.class);
        Class[] exp={String.class,Long.class};
        Assert.assertArrayEquals(clazz, exp);
    }
    
    interface TestInterface<A,B,C>{
        
    }
    class SuperTestClass<A,B>{
        
    }
    class TestInClass extends SuperTestClass<String,Long> implements TestInterface<String,Long,Integer>{
    }

}
