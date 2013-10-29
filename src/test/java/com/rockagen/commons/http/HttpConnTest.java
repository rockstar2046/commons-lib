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
package com.rockagen.commons.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author AGEN
 * @since JDK1.6
 */
public class HttpConnTest {
	
	
	private static Header[] headers={
			new BasicHeader("Accept","image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*"),
			new BasicHeader("Accept-Language","zh-cn"),
			new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72"),
			new BasicHeader("Pragma","no-cache")
	};
	
	private static List<NameValuePair> nvPairs=new ArrayList<NameValuePair>();
	
	@BeforeClass
	public static void initNvPairs(){
		nvPairs.add(new BasicNameValuePair("username","hello"));
		nvPairs.add(new BasicNameValuePair("password","shit"));
	}
	
	
	@Test
	public void testSendGet() throws IOException {
//		System.out.println(HttpConn.sendGet(httpHost, "/"));
//		System.out.println(HttpConn.sendGet((HttpHost)obj[0], (String)obj[1]));
//		System.out.println(HttpConn.sendGet((HttpHost)obj2[0], (String)obj2[1]));
//		System.out.println(HttpConn.sendGet((HttpHost)obj2[0], (String)obj2[1],headers));
		
	}
	@Test
	public void testSendGetProxy() throws IOException{
		HttpHost httpHost = new HttpHost("www.sex.com", 80, "http");
		HttpHost proxy = new HttpHost("localhost", 8087, "http");
		System.out.println(HttpConn.sendGet(httpHost,proxy, "/"));
	}
	
	@Test
	public void testSendGet4BasicAuth() throws IOException{
//		HttpHost httpHost = new HttpHost("www.rockagen.com", 80, "http");
//		System.out.println(HttpConn.sendGet(HttpConn.getUPC("admin","admin"),httpHost, "/NginxStatus"));
	}
	
	
	@Test
	public void testSendPost() throws IOException {
		
//		HttpHost httpHost = new HttpHost("www.rockagen.com", 80, "http");
//		Object[] obj=HttpConn.resolveString("http://www.rockagen.com");
//		Object[] obj2=HttpConn.resolveString("https://www.rockagen.com");
//		System.out.println(HttpConn.sendPost(httpHost, "/"));
//		System.out.println(HttpConn.sendPost((HttpHost)obj[0], (String)obj[1]));
//		System.out.println(HttpConn.sendPost((HttpHost)obj2[0], (String)obj2[1]));
//		System.out.println(HttpConn.sendPost((HttpHost)obj2[0], (String)obj2[1],headers));
	}
	
	@Test
	public void testSendPostProxy() throws IOException{
		HttpHost httpHost = new HttpHost("www.sex.com", 80, "http");
		HttpHost proxy = new HttpHost("localhost", 8087, "http");
//		System.out.println(HttpConn.sendPost(httpHost,proxy, "/"));
	}
	
	@Test
	public void testSendPost4BasicAuth() throws IOException{
//		HttpHost httpHost = new HttpHost("localhost", 80, "http");
//		System.out.println(HttpConn.sendPost(HttpConn.getUPC("admin","FUCK_TX_TEAM"),httpHost, "/ajax/open/orderExceptionTipAction.action"));
	}
	
	// ...



}
