package com.machina.util.text;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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
			byte[] sourceBytes = msg.getBytes("UTF-8");
			String data = new String(sourceBytes, defaultCharset.name());

			PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
			out.println(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static String capitalizeWord(String str) {
		String words[] = str.split("\\s");
		String capitalizeWord = "";
		for (String w : words) {
			String first = w.substring(0, 1);
			String afterfirst = w.substring(1);
			capitalizeWord += first.toUpperCase() + afterfirst + " ";
		}
		return capitalizeWord.trim();
	}
	
	public static String translate(String key) {
		return translateComp(key).getString();
	}
	
	public static String prettyItemStack(ItemStack stack) {
		return String.valueOf(stack.getCount()) + "x " + stack.getItem().getName(stack).getString();
	}
	
	public static TranslationTextComponent translateComp(String key) {
		return new TranslationTextComponent(key);
	}
	
	public static StringTextComponent toComp(String text) {
		return new StringTextComponent(text);
	}
}
