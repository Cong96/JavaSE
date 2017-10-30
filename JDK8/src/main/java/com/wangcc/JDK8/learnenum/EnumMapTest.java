package com.wangcc.JDK8.learnenum;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class EnumMapTest {
	@Test
	public void test() {
		Map<TypeEnum, String> map = new EnumMap<TypeEnum, String>(TypeEnum.class);
		map.put(TypeEnum.AUDIO, "AUDIO KOBE");
		map.put(TypeEnum.IMAGE, "IMAGE KOBE");
		map.put(TypeEnum.TEXT, "TEXT KOBE");
		map.put(TypeEnum.VIDEO, "VIDEO KOBE");
		Set<Map.Entry<TypeEnum, String>> entrySet = map.entrySet();
		for (Map.Entry<TypeEnum, String> entry : entrySet) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
		System.out.println(map);
	}
}
