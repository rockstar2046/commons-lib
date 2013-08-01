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

import org.junit.Test;

/**
 * @author AGEN
 * @since JDK1.6
 */
public class CommUtilTest {
	
	@Test
	public void testEscape(){
		System.out.print(CommUtil.escapeJava("ÖÐ¹ú"));
	}
	
	@Test
	public void testString(){
		System.out.println(CommUtil.subPostfix(CommUtil.generateRandomCode(12),0,4,"*"));
		System.out.println(CommUtil.extractNumber(CommUtil.generateRandomCode(12)));
		System.out.println(CommUtil.extractNumber(CommUtil.subPostfix(CommUtil.generateRandomCode(12),0,4,"*")));
		
		System.out.println(CommUtil.toCommaDelimitedString(new String[]{"tom","jack","axl","duff","slash","Izzy"," Adler"}));
	}
	
	@Test
	public void testRandomString(){
		System.out.println(CommUtil.generateRandomCode(12));
		System.out.println(CommUtil.getRandomNumber(6));
	}
	@Test
	public void testDigest(){
		System.out.println(MDUtil.sha1Hex("hello"));
		System.out.println(MDUtil.md5Hex("hello"));
		System.out.println(MDUtil.md5("hello"));
	}

}
