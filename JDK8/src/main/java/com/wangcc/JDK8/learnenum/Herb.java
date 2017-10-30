package com.wangcc.JDK8.learnenum;

public class Herb {
	public enum Type {
		ANNUAL, PERENNIAL, BIENNTAL
	}

	private final String name;
	final Type type;

	public Herb(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return name;
	}
}