package com.machina.util.text;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.machina.Machina;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

public class StringUtils {

	private static final Minecraft mc = Minecraft.getInstance();

	public static final String TREE_V = "\u2502";
	public static final String TREE_H = "\u2500";
	public static final String TREE_F = "\u251c";
	public static final String TREE_L = "\u2514";

	public static final Charset utf8Charset = Charset.forName("UTF-8");
	public static final Charset defaultCharset = Charset.defaultCharset();

	public static final ITextComponent EMPTY = new StringTextComponent("");

	private static final int[] TEST_SPLIT_OFFSETS = new int[] { 0, 10, -10, 25, -25 };

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

	public static String toEllipsis(String input, int maxCharacters, int charactersAfterEllipse) {
		if (input == null || input.length() < maxCharacters) {
			return input;
		}
		return input.substring(0, maxCharacters - charactersAfterEllipse) + "..."
				+ input.substring(input.length() - charactersAfterEllipse);
	}

	public static String translate(String key) {
		return translateComp(key).getString();
	}

	public static String translateScreen(String key) {
		return translateScreenComp(key).getString();
	}

	public static TranslationTextComponent translateScreenComp(String key) {
		return translateComp(Machina.MOD_ID + ".screen." + key);
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

	private static float getMaxWidth(CharacterManager pManager, List<ITextProperties> pText) {
		return (float) pText.stream().mapToDouble(pManager::stringWidth).max().orElse(0.0D);
	}

	public static List<ITextProperties> findOptimalLines(ITextComponent pComponent, int pMaxWidth) {
		CharacterManager charactermanager = mc.font.getSplitter();
		List<ITextProperties> list = null;
		float f = Float.MAX_VALUE;

		for (int i : TEST_SPLIT_OFFSETS) {
			List<ITextProperties> list1 = charactermanager.splitLines(pComponent, pMaxWidth - i, Style.EMPTY);
			float f1 = Math.abs(getMaxWidth(charactermanager, list1) - (float) pMaxWidth);
			if (f1 <= 10.0F) {
				return list1;
			}

			if (f1 < f) {
				f = f1;
				list = list1;
			}
		}

		return list;
	}
}
