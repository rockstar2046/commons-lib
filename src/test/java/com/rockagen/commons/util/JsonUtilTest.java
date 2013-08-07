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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 *
 * @author AGEN
 * @since JDK1.6
 */
public class JsonUtilTest {
	
	@Test
	public void testJson(){
		
		String jsonStr1="[{\"city\":\"CHINA 0\",\"testVo\":{\"name\":\"ROCKAGEN 0\",\"age\":0,\"email\":\"agen@rockagen.com 0\"}},"
				+ "{\"city\":\"Beijing 0\",\"testVo\":{\"name\":\"ROCKAGEN2 0\",\"age\":0,\"email\":\"agen@rockagen.com2 0\"}}]";
		TestVo4[] tvs=JsonUtil.toBean(jsonStr1, TestVo4[].class);
		for(TestVo4 tv : tvs)
 		System.out.println(tv.getTestVo().getName()+" "+tv.getTestVo().getAge()+" "+tv.getTestVo().getEmail()+" "+tv.getCity());
		 
	}
	@Test
	public void testToJson(){
		for(int i=0; i<10;i++){
		TestVo vo1=new TestVo();
		vo1.setAge(i);
		vo1.setName("ROCKAGEN "+i);
		vo1.setEmail("agen@rockagen.com "+i);
		vo1.setBir(new Date());
		TestVo vo2=new TestVo();
		vo2.setAge(i);
		vo2.setName("ROCKAGEN2 "+i);
		vo2.setEmail("agen@rockagen.com2 "+i);
		
		TestVo4 vo4=new TestVo4();
		vo4.setCity("CHINA "+i);
		vo4.setTestVo(vo1);
		
		TestVo4 vo5=new TestVo4();
		vo5.setCity("Beijing "+i);
		vo5.setTestVo(vo2);
		System.out.println(JsonUtil.toJson(new TestVo4[]{vo4,vo5}));
		}
		
	}
	
	@Test
	public void testCollectionToJson(){
			TestVo vo1=new TestVo();
			vo1.setAge(20);
			vo1.setName("ROCKAGEN");
			vo1.setEmail("agen@rockagen.com ");
			TestVo vo2=new TestVo();
			vo2.setAge(21);
			vo2.setName("ROCKAGEN2");
			vo2.setEmail("agen@rockagen.com2");
			
			TestVo4 vo4=new TestVo4();
			vo4.setCity("CHINA");
			vo4.setTestVo(vo1);
			
			TestVo4 vo5=new TestVo4();
			vo5.setCity("中国");
			vo5.setTestVo(vo2);
			List<TestVo4> list=new ArrayList<TestVo4>();
			list.add(vo4);
			list.add(vo5);
			System.out.println(JsonUtil.toJson(list));
			
			Map<String, TestVo4> map=new HashMap<String,TestVo4>();
			map.put("testVo1", vo4);
			map.put("testVo2", vo5);
			System.out.println(JsonUtil.toJson(map));
	}
	
	@Test
	public void testJsontoCollection(){
		String jsonStr1="[{\"city\":\"CHINA 0\",\"testVo\":{\"name\":\"ROCKAGEN 0\",\"age\":0,\"email\":\"agen@rockagen.com 0\"}},"
				+ "{\"city\":\"Beijing 0\",\"testVo\":{\"name\":\"ROCKAGEN2 0\",\"age\":0,\"email\":\"agen@rockagen.com2 0\"}}]";
		
		for(TestVo4 tv : JsonUtil.toBean(jsonStr1,new TypeReference<List<TestVo4>>(){})){
 		System.out.println(tv.getTestVo().getName()+" "+tv.getTestVo().getAge()+" "+tv.getTestVo().getEmail()+" "+tv.getTestVo().getEmail());
		}
		
		String jsonStr2="{\"testVo1\":{\"city\":\"CHINA\",\"testVo\":{\"name\":\"ROCKAGEN\",\"age\":20,\"email\":\"agen@rockagen.com \"}},\"testVo2\":{\"city\":\"Beijing\",\"testVo\":{\"name\":\"ROCKAGEN2\",\"age\":21,\"email\":\"agen@rockagen.com2\"}}}";
		 for (Map.Entry<String, TestVo4> entry :JsonUtil.toBean(jsonStr2,new TypeReference<Map<String,TestVo4>>(){}).entrySet()) {
			   System.out.println( entry.getKey().toString()+" "+entry.getValue().getCity()+" "+entry.getValue().getTestVo().getEmail()+" "+entry.getValue().getTestVo().getEmail());
		 }
	}
	
		

}
