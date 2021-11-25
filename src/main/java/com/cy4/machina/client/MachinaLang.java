/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
