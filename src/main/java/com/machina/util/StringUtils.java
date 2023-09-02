package com.machina.util;

import net.minecraft.network.chat.Component;

public class StringUtils {	
	
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

}
