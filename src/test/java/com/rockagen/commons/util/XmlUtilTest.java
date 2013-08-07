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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rockagen.commons.util.XmlUtil.XAlias;
import com.rockagen.commons.util.XmlUtil.XAliasAttribute;

/**
 * 
 * @author AGEN
 * @since JDK1.6
 */
public class XmlUtilTest {

	public String getContent() throws IOException {
		File file = new File(getClass().getResource("TestVo.xml").getFile());
		return FileUtil.readFileToString(file);

	}

	@Test
	public void testFromXml() throws IOException {

		System.out.println(XmlUtil.getEncoding(getContent()));
		// System.out.println(XMlUtil.formatCompact(data));
		System.out.println(XmlUtil.formatPretty(getContent()));

	}

	@Test
	public void testToXml() throws IOException {

		File file = new File("/home/agen/a.xml");
		FileUtil.touch(file);

		TestVo tv = new TestVo();
		tv.setName("ROCKAGEN");
		tv.setAge(22);
		tv.setEmail("agen@rockagen.com");
		TestVo4 tv4 = new TestVo4();
		tv4.setCity("Beijing");
		tv4.setTestVo(tv);

		TestVo4 tv5 = new TestVo4();
		tv5.setCity("Shanghai");
		tv5.setTestVo(tv);

		List<TestVo4> list = new ArrayList<TestVo4>();
		list.add(tv4);
		list.add(tv5);

		XAlias[] xa = { new XAlias("TestVo4", TestVo4.class),
				new XAlias("Beans", List.class) };
		XAliasAttribute[] xaa = { new XAliasAttribute("City", TestVo4.class,
				"city") };
		String xmlString = XmlUtil.formatPretty(XmlUtil.toXml(list, xa, null,
				xaa));
		System.err.println(xmlString);
		FileUtil.writeStringToFile(file, xmlString, true);
	}

	@Test
	public void testToBean() throws IOException {

		TestVo tv = new TestVo();
		tv.setName("ROCKAGEN");
		tv.setAge(22);
		tv.setEmail("agen@rockagen.com");
		TestVo4 tv4 = new TestVo4();
		tv4.setCity("Beijing");
		tv4.setTestVo(tv);

		TestVo4 tv5 = new TestVo4();
		tv5.setCity("Shanghai");
		tv5.setTestVo(tv);

		List<TestVo4> list = new ArrayList<TestVo4>();
		list.add(tv4);
		list.add(tv5);

		XAlias[] xa = { new XAlias("TestVo4", TestVo4.class),
				new XAlias("Beans", List.class) };
		XAliasAttribute[] xaa = { new XAliasAttribute("City", TestVo4.class,
				"city") };
		String xmlString = XmlUtil.formatPretty(XmlUtil.toXml(list, xa, null,
				xaa));
		System.err.println(xmlString);
		List<TestVo4> list2 = (List<TestVo4>) XmlUtil.toBean(xmlString, xa,
				null, xaa);
		for (TestVo4 tvl : list2) {
			System.out.println(tvl);
		}

	}

}
