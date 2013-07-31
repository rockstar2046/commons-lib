/*
 * Copyright 2002-2013 the original author or authors.
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
 * @author AGEN
 * @since JDK1.6
 */
public class TestVo3 {

	@Deprecated
	private String name;
	@Deprecated
	private int age;
	@Deprecated
	private String email;
	
	@Deprecated
	private TestVo3(){}
	
	@Deprecated
	private TestVo3(String name, int age, String email) {
		super();
		this.name = name;
		this.age = age;
		this.email = email;
	}
	
	@Deprecated
	public int getAge() {
		return age;
	}

	@Deprecated
	public void setAge(int age) {
		this.age = age;
	}

	@Deprecated
	public String getName() {
		return name;
	}

	@Deprecated
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TestVo3 [name=" + name + ", age=" + age + ", email=" + email + "]";
	}
	
}
