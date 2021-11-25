/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MachinaLangTextComponent {

	private String text = "";
	private String[] args = new String[] {};

	public MachinaLangTextComponent(String pKey) {
		this(pKey, new String[] {});
	}

	public MachinaLangTextComponent(String key, String... args) {
		text = new TranslationTextComponent(key).getString();
		this.args = args;
		format();
	}

	private void format() {
		for (int i = 0; i < args.length; i++) {
			text = text.replace("$s" + i, args[i]);
		}
	}

	@Override
	public String toString() {
		return "";
	}

	public ITextComponent toComponent() {
		return new StringTextComponent(text);
	}

}
