package com.machina.world.gen;

import java.util.Random;

import com.machina.api.util.Color;

public class PlanetPaletteGenerator {

	public static Color[] getAnalogousPalette(Random r, int similarity, int len) {
		Color[] cols = new Color[len];
		float[] c = Color.random(r).getHSB();
		float[] hues = new float[len];

		hues[0] = c[0] * 255.0f;
		for (int i = 1; i < len; i++) {
			hues[i] = (hues[i - 1] + r.nextInt(similarity)) % 360;
		}

		for (int i = 0; i < len; i++) {
			c[0] = hues[i] / 255.0f;
			cols[i] = Color.getHSBColor(c);
		}

		return cols;
	}

	public static void main(String[] args) {
		for (Color c : getAnalogousPalette(new Random(), 15, 5)) {
			System.out.println(c.toString());
		}
	}
}
