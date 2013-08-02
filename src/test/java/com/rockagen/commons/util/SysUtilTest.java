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
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 *
 * @author AGEN
 * @since JDK1.6
 */
public class SysUtilTest {
	
	
	@Test
	public void openUrl() throws IllegalArgumentException, ClassNotFoundException, IllegalAccessException, InterruptedException, InvocationTargetException, IOException, NoSuchMethodException{
		System.out.println(SysUtil.OS_NAME);
		
		SysUtil.browse("http://www.google.com");
	}

}