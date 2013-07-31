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
package com.rockagen.commons.annotation;

import java.lang.reflect.Method;

import org.junit.Test;

import com.rockagen.commons.util.ClassUtil;
import com.rockagen.commons.util.CommUtil;

/**
 *
 * @author AGEN
 * @since JDK1.6
 * 
 */
public class OPLogTest {
	
	  @OPLog("Test")
	  public void test()
	  {
	  }

	  @Test
	  public void testDescription()
	    throws SecurityException, NoSuchMethodException
	  {
	    Class<?> classType = getClass();
	    Method method = classType.getMethod("test", new Class[0]);
	    boolean hasAnnotation = method.isAnnotationPresent(OPLog.class);

	    if (hasAnnotation) {
	      OPLog annotation = (OPLog)ClassUtil.getDeclaredMethod(getClass(), false, "test").getAnnotation(OPLog.class);
	      String methodDescp = annotation.description();
	      if(CommUtil.isBlank(methodDescp)){
	    	  methodDescp=annotation.value();
	      }
	     
	      System.out.println("Target method: [" + method.getName() + "]");
	      System.out.println("Value: [" + methodDescp + "]");
	    } else {
	      System.out.println(method.getName());
	    }
	  }

}
