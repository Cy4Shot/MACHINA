package com.machina.api.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.machina.Machina;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StringUtils {

	private static final String[] units = { "", "K", "M", "B", "T", "P", "E", "Z", "Y" };
	private static final String[] smallUnits = { "", "m", "μ", "n", "p", "f", "a", "z", "y" };

	public static final String TREE_V = "\u2502";
	public static final String TREE_H = "\u2500";
	public static final String TREE_F = "\u251c";
	public static final String TREE_L = "\u2514";

	private static final Charset utf8Charset = Charset.forName("UTF-8");
	private static final Charset defaultCharset = Charset.defaultCharset();

	public static void printlnUtf8(String msg) {
		try {
			new PrintStream(System.out, true, utf8Charset.name())
					.println(new String(msg.getBytes("UTF-8"), defaultCharset.name()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String chemical(String data) {
		StringBuilder out = new StringBuilder();
		for (char c : data.toCharArray()) {
			out.append(Character.isDigit(c) ? getSubscript(Integer.toString(c - '0')) : c);
		}
		return out.toString();
	}

	public static String getSubscript(String pString) {
		final int subscriptZeroCodepoint = 0x2080;
		StringBuilder builder = new StringBuilder();
		for (char character : pString.toCharArray()) {
			builder.append(Character.toChars(subscriptZeroCodepoint + Character.getNumericValue(character)));
		}
		return builder.toString();
	}

	public static String translate(String key, Object... params) {
		return Component.translatable(key, params).getString();
	}

	public static MutableComponent translateMultiblockComp(String key) {
		return Component.translatable(Machina.MOD_ID + ".multiblock." + key);
	}

	private static String formatNumberWithUnit(double number) {

		if (number >= 1_000) {
			int magnitude = 0;
			while (number >= 1_000 && magnitude < units.length - 1) {
				number /= 1_000;
				magnitude++;
			}
			return String.format("%.1f%s", number, units[magnitude]);
		}

		else if (number > 0) {
			int magnitude = 0;
			while (number < 1 && magnitude < smallUnits.length - 1) {
				number *= 1_000;
				magnitude++;
			}
			return String.format("%.1f%s", number, smallUnits[magnitude]);
		}

		return String.format("%.1f", number);
	}

	public static String formatTemp(double temp) {
		return formatNumberWithUnit(temp) + "K";
	}

	public static String formatPower(long energy) {
		return formatNumberWithUnit(energy) + "RF";
	}

	public static String formatFluid(int mb) {
		return formatNumberWithUnit((double) mb / 1_000D) + "B";
	}

	public static String formatPressure(float pressure) {
		return formatNumberWithUnit(pressure) + "Pa";
	}

	public static String formatRadiation(float rad) {
		return formatNumberWithUnit(rad) + "rad";
	}

	public static String formatPercent(float percent) {
		return String.format("%.1f%%", percent * 100);
	}

	public static String formatTicks(float ticks) {
		return formatNumberWithUnit(ticks / 20f) + "s";
	}
}
