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
package com.rockagen.commons.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.rockagen.commons.util.FileUtil;

/**
 * @author RA
 * @since JDK1.6
 */
public class HttpConnTest {
	
	
	private static Map<String,String> headers=new HashMap<String, String>();
	
	
	@BeforeClass
	public static void initNvPairs(){
		headers.put("Accept","image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*");
		headers.put("Accept-Language","zh-cn");
		headers.put("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
		headers.put("Pragma","no-cache");
	}
	
	@Test
	@Ignore
	public void testSendGet() throws IOException{
		InputStream in=FileUtil.openInputStream(new File("/home/ra/var/cacerts"));
		System.out.println(HttpConn.send(in, "changeit".toCharArray(), "https://rooool.com", RequestMethod.GET));
		System.out.println(HttpConn.send("https://www.google.com", "http://127.0.0.1:8118",RequestMethod.GET));
	}
	
	@Test
	@Ignore
	public void testSendGet4BasicAuth() throws IOException{
		InputStream in=FileUtil.openInputStream(new File("/home/ra/var/cacerts"));
		System.out.println(HttpConn.send(HttpConn.getUPC("admin", "youseeajb"), in, "changeit".toCharArray(), "https://rooool.com/status",RequestMethod.PUT));
	}
	
	
	@Test
	@Ignore
	public void testSendPost() throws IOException {
		InputStream in=FileUtil.openInputStream(new File("/home/ra/var/cacerts"));
		Map<String, String> map=new HashMap<String, String>();
		System.out.println(HttpConn.sendBody(in, "changeit".toCharArray(), "https://rooool.com/api/", map,RequestMethod.POST));
		//System.out.println(HttpConn.sendPost(in,"changeit".toCharArray(),"https://rooool.com/api/",map,"http://127.0.0.1:8118"));
	}

}
