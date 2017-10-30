package com.wangcc.JDK8.learnenum;

import org.junit.Test;

public class TypeEnumTest {
	@Test
	public void test() {
		TypeEnum type = TypeEnum.VIDEO;
		System.out.println(type.getName());
		System.out.println(type.getValue());
		System.out.println(type.ordinal());
		System.out.println(type.name());
	}

	@Test
	public void testCompare() {
		TypeEnum type = TypeEnum.VIDEO;
		TypeEnum type1 = TypeEnum.IMAGE;
		System.out.println(type.compareTo(type1));
	}

	@Test
	public void testClass() {
		TypeEnum type = TypeEnum.VIDEO;
		Class<?> clazz = type.getDeclaringClass();
		System.out.println(clazz.getName());
	}
}
