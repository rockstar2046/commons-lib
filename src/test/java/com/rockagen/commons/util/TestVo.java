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

import java.util.Date;

import com.rockagen.commons.annotation.OPLog;

/**
 * @author AGEN
 * @since JDK1.6
 */
public class TestVo {

	private String name;
	private int age;
	private String email;
	private Date bir;
	

	public TestVo(String name, int age, String email,Date bir) {
		super();
		this.name = name;
		this.age = age;
		this.email = email;
		this.bir=bir;
	}
	public TestVo(){};

	@OPLog
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBir() {
		return bir;
	}
	public void setBir(Date bir) {
		this.bir = bir;
	}
	@Override
	public String toString() {
		return "TestVo [name=" + name + ", age=" + age + ", email=" + email
				+ ", bir=" + bir + "]";
	}


	
}
