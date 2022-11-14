package com.machina.util.text;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Random;

import com.machina.Machina;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StringUtils {

	public static final String TREE_V = "\u2502";
	public static final String TREE_H = "\u2500";
	public static final String TREE_F = "\u251c";
	public static final String TREE_L = "\u2514";

	public static final Charset utf8Charset = Charset.forName("UTF-8");
	public static final Charset defaultCharset = Charset.defaultCharset();

	public static final ITextComponent EMPTY = new StringTextComponent("");

	public static void printlnUtf8(String msg) {
		try {
			new PrintStream(System.out, true, utf8Charset.name())
					.println(new String(msg.getBytes("UTF-8"), defaultCharset.name()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String capitalizeWord(String str) {
		String capitalizeWord = "";
		for (String w : str.split("\\s")) {
			capitalizeWord += w.substring(0, 1).toUpperCase() + w.substring(1) + " ";
		}
		return capitalizeWord.trim();
	}

	public static String prettyItemStack(ItemStack stack) {
		return String.valueOf(stack.getCount()) + "x " + stack.getItem().getName(stack).getString();
	}

	public static String translate(String key) {
		return translateComp(key).getString();
	}

	public static String translateScreen(String key) {
		return translateComp(Machina.MOD_ID + ".screen." + key).getString();
	}

	public static TranslationTextComponent translateComp(String key) {
		return new TranslationTextComponent(key);
	}

	public static TranslationTextComponent translateCompScreen(String key) {
		return new TranslationTextComponent(Machina.MOD_ID + ".screen." + key);
	}

	public static TranslationTextComponent translate(String key, Object... args) {
		return new TranslationTextComponent(key, args);
	}

	public static StringTextComponent toComp(String text) {
		return new StringTextComponent(cleanString(text));
	}

	public static String cleanString(String component) {
		return component.replace("\u00A0", " ");
	}

	public static String repeat(String s, int n) {
		return String.join("", Collections.nCopies(n, s));
	}

	public static String random() {
		return Character.toString((char) (new Random().nextInt(26) + 'a'));
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
}
