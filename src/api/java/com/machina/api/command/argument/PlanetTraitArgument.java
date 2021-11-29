/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.api.command.argument;

import java.util.concurrent.CompletableFuture;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.util.MachinaRL;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class PlanetTraitArgument implements ArgumentType<MachinaRL> {

	private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
			p_212594_0_ -> new TranslationTextComponent("argument.planet_trait.invalid", p_212594_0_));

	@Override
	public MachinaRL parse(StringReader reader) throws CommandSyntaxException {
		return MachinaRL.read(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_,
			SuggestionsBuilder p_listSuggestions_2_) {
		return p_listSuggestions_1_.getSource() instanceof ISuggestionProvider
				? ISuggestionProvider.suggestResource(PlanetTrait.REGISTRY.getValues().stream()
						.filter(PlanetTrait::exists).map(PlanetTrait::getRegistryName), p_listSuggestions_2_)
				: Suggestions.empty();
	}

	public static PlanetTraitArgument planetTrait() {
		return new PlanetTraitArgument();
	}

	public static PlanetTrait getTrait(CommandContext<CommandSource> pContext, String pName)
			throws CommandSyntaxException {
		ResourceLocation resourcelocation = pContext.getArgument(pName, ResourceLocation.class);
		return PlanetTrait.REGISTRY.containsKey(resourcelocation)
				&& PlanetTrait.REGISTRY.getValue(resourcelocation).exists()
						? PlanetTrait.REGISTRY.getValue(resourcelocation)
						: notValid(resourcelocation);
	}

	public static PlanetTrait notValid(ResourceLocation location) throws CommandSyntaxException {
		throw ERROR_INVALID_VALUE.create(location);
	}

}
