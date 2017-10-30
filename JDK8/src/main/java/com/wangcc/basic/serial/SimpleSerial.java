package com.wangcc.basic.serial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class SimpleSerial {
	@Test
	public void test() throws Exception {
		User user = new User();
		user.setAge(39);
		user.setName("科比");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.out"));
		out.writeObject(user);
		out.close();
		System.out.println("我是分割线-------");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.out"));
		Object object = in.readObject();
		in.close();
		System.out.println(object.toString());

	}
}
