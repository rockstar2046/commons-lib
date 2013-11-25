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

import java.util.BitSet;

import org.junit.Test;

/**
 * @author AGEN
 * @since JDK1.6
 */
public class CommUtilTest {

	
	private void println(Object o){
		System.out.println(o);
	}
	@Test
	public void testEscape() {
		println(CommUtil.escapeJava("中国"));
		println(CommUtil.hasSpecialChar("d$3d"));
		println(CommUtil.hasSpecialChar(""));
	}

	@Test
	public void testString() {
		println(CommUtil.subPostfix(CommUtil.generateRandomCode(12),
				0, 4, "*"));
		println(CommUtil.extractNumber(CommUtil
				.generateRandomCode(12)));
		println(CommUtil.extractNumber(CommUtil.subPostfix(
				CommUtil.generateRandomCode(12), 0, 4, "*")));

		println(CommUtil.toCommaDelimitedString(new String[] {
				"tom", "jack", "axl", "duff", "slash", "Izzy", " Adler" }));
	}

	@Test
	public void testRandomString() {
		println(CommUtil.generateRandomCode(12));
		println(CommUtil.getRandomNumber(6));
	}

	@Test
	public void testDigest() {
		println(MDUtil.sha1Hex("hello"));
		println(MDUtil.md5Hex("hello"));
		println(MDUtil.md5("hello"));
	}

	@Test
	public void testSameChars() {
		println(CommUtil.repeatChar('b', 10));
		println(CommUtil.repeatChar('b', 1));
		println(CommUtil.repeatChar('b', 0));
		println(CommUtil.repeatChar('b', -1));

	}

	@Test
	public void testBC() {
		println(CommUtil.toDBC("!?<>\"'D"));
		println(CommUtil.toSBC("。<@《%“"));

	}
	
	@Test
	public void testPrettyTable(){
		  String[] headers={"num","name","age","where"};
		  Object[][] values=
		{
		  {1,Integer.MAX_VALUE,22,"USA"},
		  {2,"joe",40,"USA","ddd"},
		};		
		println(CommUtil.prettyTable(headers,values));
	}

	@Test
	public void testPrettyHexDump(){
		println(CommUtil.prettyHexdump("0256~".getBytes()));
	}
	@Test
	public void testHex(){
		println(CommUtil.hexdump("0256艹".getBytes()));
	}
	@Test
	public void testBitSet(){
		println(CommUtil.bitSet("0256".getBytes()));
	}
	@Test
	public void testBitValue(){
		String bs="0256";
		BitSet b=CommUtil.bitSet(bs.getBytes());
		println(b.toString());
		println(CommUtil.hexdump(bs.getBytes()));
		println(CommUtil.hexdump(CommUtil.bitValue(b)));
	}
}
