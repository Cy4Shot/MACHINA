package com.machina.api.util;

import com.machina.api.MachinaConstants;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MachinaRL extends ResourceLocation {

	private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(
			Component.literal("argument.id.invalid"));

	public MachinaRL(int id) {
		super(MachinaConstants.MOD_ID, String.valueOf(id));
	}

	public MachinaRL(String name) {
		super(checkModId(name));
	}

	public MachinaRL(String modId, String path) {
		super(modId, path);
	}

	public static String checkModId(String input) {
		return input.contains(":") ? input : MachinaConstants.MOD_ID + ":" + input;
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