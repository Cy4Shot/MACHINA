package com.machina.api.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.machina.Machina;

import net.minecraft.network.chat.Component;

public class StringUtils {

	public static final String TREE_V = "\u2502";
	public static final String TREE_H = "\u2500";
	public static final String TREE_F = "\u251c";
	public static final String TREE_L = "\u2514";

	public static final Charset utf8Charset = Charset.forName("UTF-8");
	public static final Charset defaultCharset = Charset.defaultCharset();

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

	public static Component translateMultiblockComp(String key) {
		return Component.translatable(Machina.MOD_ID + ".multiblock." + key);
	}
}
