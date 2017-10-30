package com.wangcc.JDK8.learnenum;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class HerbTest {
	Herb[] garden = null;

	@Before
	public void setup() {
		garden = new Herb[] { new Herb("f1", Herb.Type.ANNUAL), new Herb("f2", Herb.Type.PERENNIAL),
				new Herb("f3", Herb.Type.BIENNTAL), new Herb("f4", Herb.Type.PERENNIAL),
				new Herb("f5", Herb.Type.ANNUAL), new Herb("f6", Herb.Type.BIENNTAL), new Herb("f7", Herb.Type.ANNUAL),
				new Herb("f8", Herb.Type.BIENNTAL), new Herb("f9", Herb.Type.PERENNIAL) };
	}

	@Test
	public void testEnumMap() {

		Map<Herb.Type, Set<Herb>> herbsByType = new EnumMap<>(Herb.Type.class);
		for (Herb.Type t : Herb.Type.values()) {
			herbsByType.put(t, new HashSet<Herb>());
		}
		for (Herb h : garden) {
			herbsByType.get(h.type).add(h);
		}
		System.out.println(herbsByType);
	}

	@Test
	public void testArray() {

		Set<Herb>[] herbsByType = (Set<Herb>[]) new Set[Herb.Type.values().length];
		for (int i = 0; i < herbsByType.length; i++) {
			herbsByType[i] = new HashSet<Herb>();
		}
		for (Herb h : garden) {
			herbsByType[h.type.ordinal()].add(h);
		}
		for (int i = 0; i < herbsByType.length; i++) {
			System.out.printf("%s:%s%n", Herb.Type.values()[i], herbsByType[i]);
		}
	}
}
