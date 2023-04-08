package com.machina.world.gen;

import java.util.HashMap;
import java.util.Map;

import com.machina.util.Color;

public class PlanetPaletteGenerator {

	public static final Color[] DEFAULT_PALETTE = new Color[] { new Color(0x55C93F), new Color(0x3B912B),
			new Color(0x3EC87B), new Color(0x40CAA6), new Color(0x87CEEB) };

	private static final Map<Integer, Color[]> PALETTES = new HashMap<>();

	// 1. Surface A
	// 2. Surface B
	// 3. Stone A
	// 4. Stone B
	// 5. Fog / Sky

	static {
		register(0x55C93F, 0x3B912B, 0x3EC87B, 0x40CAA6, 0x87CEEB);
	}

	private static void register(int c1, int c2, int c3, int c4, int c5) {
		PALETTES.put(PALETTES.size(),
				new Color[] { new Color(c1), new Color(c2), new Color(c3), new Color(c4), new Color(c5) });
	}

	public static Color[] getPalette(int colorid) {
		System.out.println(colorid);
		return PALETTES.getOrDefault(colorid, DEFAULT_PALETTE);
	}

	public static int count() {
		return PALETTES.size();
	}
}
