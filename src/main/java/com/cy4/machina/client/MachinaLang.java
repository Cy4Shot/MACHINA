package com.cy4.machina.client;

import com.cy4.machina.api.util.DiagonalDirection;

import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;

public class MachinaLang {

	public static String getDirectionName(Direction direction) {
		return getLangEntry("direction." + direction.getName());
	}

	public static String getDirectionName(DiagonalDirection direction) {
		return getLangEntry("direction." + direction.name);
	}

	private static String getLangEntry(String key) {
		return new TranslationTextComponent(key).getString();
	}
}
