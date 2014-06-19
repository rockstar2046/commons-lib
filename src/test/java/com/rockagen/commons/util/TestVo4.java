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

/**
 *
 * @author RA
 * @since JDK1.6
 */
public class TestVo4 {
	
	private String city;
	private TestVo testVo;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public TestVo getTestVo() {
		return testVo;
	}
	public void setTestVo(TestVo testVo) {
		this.testVo = testVo;
	}
	@Override
	public String toString() {
		return "TestVo4 [city=" + city + ", testVo=" + testVo + "]";
	}
	
	

}
