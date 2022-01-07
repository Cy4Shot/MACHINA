package com.machina.api.planet;

import java.util.Random;

public class PlanetBlocksGenerator {
	public static final String[] BASE_BLOCKS = new String[] {"minecraft:stone"};
	
	public static String getBaseBlock(Random r) {
		return getRandom(BASE_BLOCKS, r);
	}
	
	
	private static String getRandom(String[] array, Random rand) {
	    return array[rand.nextInt(array.length)];
	}
}
