package com.machina.world.gen;

import java.util.Random;

public class PlanetBlocksGenerator {
	public static final String[] BASE_BLOCKS = new String[] {"machina:alien_stone", "machina:twilight_dirt"};
	
	public static String getBaseBlock(Random rand) {
		return getRandom(BASE_BLOCKS, rand);
	}
	
	private static String getRandom(String[] array, Random rand) {
		return array[rand.nextInt(array.length)];
	}
}
