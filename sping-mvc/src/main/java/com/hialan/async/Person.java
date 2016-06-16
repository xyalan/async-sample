package com.hialan.async;

import java.io.Serializable;

/**
 * User: Alan
 * Email: houlong.yang@shuyun.com
 * Date: 11:10 6/16/16
 */
public class Person implements Serializable {
	private String name;
	private String fullName;
	private Integer age;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
