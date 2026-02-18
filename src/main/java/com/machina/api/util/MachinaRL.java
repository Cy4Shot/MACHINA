package com.machina.api.util;

import com.machina.Machina;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MachinaRL {

	public static final ResourceLocation ID = MachinaRL.create(Machina.MOD_ID);

	private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(
			Component.literal("argument.id.invalid"));

	public static ResourceLocation create(int id) {
		return ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID, String.valueOf(id));
	}

	public static ResourceLocation create(String name) {
		return ResourceLocation.parse(checkModId(name));
	}

	public static ResourceLocation create(String modId, String path) {
		return ResourceLocation.fromNamespaceAndPath(modId, path);
	}

	public static String checkModId(String input) {
		return input.contains(":") ? input : Machina.MOD_ID + ":" + input;
	}

	public static ResourceLocation read(StringReader pReader) throws CommandSyntaxException {
		int i = pReader.getCursor();

		while (pReader.canRead() && ResourceLocation.isAllowedInResourceLocation(pReader.peek())) {
			pReader.skip();
		}

		String s = pReader.getString().substring(i, pReader.getCursor());

		try {
			return create(s);
		} catch (ResourceLocationException resourcelocationexception) {
			pReader.setCursor(i);
			throw ERROR_INVALID.createWithContext(pReader);
		}
	}

}