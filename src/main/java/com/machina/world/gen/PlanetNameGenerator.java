package com.machina.world.gen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class PlanetNameGenerator {

	private final static TreeMap<Integer, String> map = new TreeMap<>();

	static {
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");

	}

	public final static String toRoman(int number) {
		int l = map.floorKey(number);
		if (number == l) { return map.get(number); }
		return map.get(l) + toRoman(number - l);
	}

	public static final List<String> syllables = Arrays.asList(new String[] {
			"ac", "ad", "af", "ag", "air", "al", "als", "am", "an", "ap", "ar", "as", "at", "au", "ba", "be", "bi",
			"ble", "by", "ca", "ci", "cle", "co", "cu", "cy", "da", "de", "di", "dle", "dy", "e", "ed", "ef", "el",
			"en", "end", "ent", "er", "ern", "ers", "es", "bel", "et", "ev", "eve", "ex", "fa", "fi", "gi", "gle", "go",
			"ho", "i", "ic", "ies", "il", "im", "in", "eer", "ion", "is", "ish", "it", "its", "jo", "la", "li", "lo",
			"lu", "ly", "ma", "me", "mi", "mo", "mu", "my", "na", "ni", "no", "nu", "ny", "ob", "oc", "of", "on", "one",
			"op", "or", "oth", "ous", "out", "pa", "pe", "peo", "pi", "ple", "ply", "po", "pre", "pro", "ra", "re",
			"ri", "ro", "ry", "sa", "se", "si", "so", "su", "ta", "te", "the", "ti", "tle", "to", "tra", "tri", "tro",
			"try", "tu", "ty", "uer", "um", "un", "up", "ure", "us", "va", "vi", "wa",
	});

	public static String getName(Random rand) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rand.nextInt(3) + 1; i++) {
			sb.append(syllables.get(rand.nextInt(syllables.size())));
		}

		if (rand.nextBoolean()) {
			sb.append(" " + toRoman(rand.nextInt(8) + 2));
		}

		return sb.toString();
	}
}
