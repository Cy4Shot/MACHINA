/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.util;

import static com.cy4.machina.Machina.MOD_ID;

import com.cy4.machina.Machina;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.text.TranslationTextComponent;

public class MachinaRL extends ResourceLocation {

	private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(
			new TranslationTextComponent("argument.id.invalid"));

	public MachinaRL(int id) {
		super(MOD_ID, String.valueOf(id));
	}
	
	public MachinaRL(String name) {
		super(checkModId(name));
	}

	public MachinaRL(String modId, String path) {
		super(modId, path);
	}

	public static String checkModId(String input) {
		return input.contains(":") ? input : Machina.MOD_ID + ":" + input;
	}

	public static MachinaRL read(StringReader pReader) throws CommandSyntaxException {
		int i = pReader.getCursor();

		while (pReader.canRead() && isAllowedInResourceLocation(pReader.peek())) {
			pReader.skip();
		}

		String s = pReader.getString().substring(i, pReader.getCursor());

		try {
			return new MachinaRL(s);
		} catch (ResourceLocationException resourcelocationexception) {
			pReader.setCursor(i);
			throw ERROR_INVALID.createWithContext(pReader);
		}
	}

}
