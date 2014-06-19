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

import java.net.SocketException;
import java.util.Arrays;
import java.util.BitSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author RA
 * @since JDK1.6
 */
public class CommUtilTest {

	
	private void println(Object o){
		System.out.println(o);
	}
	@Test
	public void testEscape() {
		Assert.assertEquals("\\u4E2D\\u56FD", CommUtil.escapeJava("中国"));
		Assert.assertTrue(CommUtil.hasSpecialChar("d$3d"));
		Assert.assertFalse(CommUtil.hasSpecialChar(""));
	}

	@Test
	public void testString() {
		Assert.assertEquals("rock*",CommUtil.subPostfix("rockagen",
				0, 4, "*"));
		Assert.assertEquals(22,CommUtil.extractNumber("ra22"),0);
        Assert.assertEquals("tom,jack,axl,duff,slash,izzy, adler", CommUtil.toCommaDelimitedString(new String[] {
				"tom", "jack", "axl", "duff", "slash", "izzy", " adler" }));
	}

	@Test
	@Ignore
	public void testRandomString() {
		println(CommUtil.generateRandomCode(12));
		println(CommUtil.getRandomNumber(6));
	}

	@Test
	public void testDigest() {
		Assert.assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", MDUtil.sha1Hex("hello"));
		Assert.assertEquals("5d41402abc4b2a76b9719d911017c592", MDUtil.md5Hex("hello"));
	}

	@Test
	public void testSameChars() {
		Assert.assertEquals("bbbbbbbbbb",CommUtil.repeatChar('b', 10));
		Assert.assertEquals("b",CommUtil.repeatChar('b', 1));
		Assert.assertEquals("",CommUtil.repeatChar('b', 0));
		Assert.assertEquals("",CommUtil.repeatChar('b', -1));

	}

	@Test
	public void testBC() {
		Assert.assertEquals("！？《》“‘Ｄ",CommUtil.toDBC("!?<>\"'D"));
		Assert.assertEquals(".<@<%\"",CommUtil.toSBC("。<@《%“"));

	}
	
	@Test
	@Ignore
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
	@Ignore
	public void testPrettyHexDump(){
		println(CommUtil.prettyHexdump("0256~".getBytes()));
	}
	
	@Test
	public void testHex(){
		Assert.assertEquals("30323536E889B9", CommUtil.hexdump("0256艹".getBytes()));
	}
	
	@Test
	@Ignore
	public void testBitSet(){
		println(CommUtil.bitSet("0256".getBytes()));
	}
	@Test
	public void testBitValue(){
		String bs="0256";
		BitSet b=CommUtil.bitSet(bs.getBytes());
		
		Assert.assertEquals("3032353600000000", CommUtil.hexdump(CommUtil.bitValue(b)));
	}
	
	@Test
	@Ignore
	public void testMacs() throws SocketException{
		System.out.println(Arrays.toString(CommUtil.getMacAddrs()));
	}
}
