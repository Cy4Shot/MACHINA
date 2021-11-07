package com.cy4.machina.api.command.argument;

import java.util.concurrent.CompletableFuture;

import com.cy4.machina.api.planet.PlanetTrait;
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

public class PlanetTraitArgument implements ArgumentType<ResourceLocation> {

	private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
			p_212594_0_ -> new TranslationTextComponent("argument.planet_trait.invalid", p_212594_0_));

	@Override
	public ResourceLocation parse(StringReader p_parse_1_) throws CommandSyntaxException {
		return ResourceLocation.read(p_parse_1_);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_,
			SuggestionsBuilder p_listSuggestions_2_) {
		return p_listSuggestions_1_.getSource() instanceof ISuggestionProvider ? ISuggestionProvider.suggestResource(
				PlanetTrait.registry.getValues().stream().map(PlanetTrait::getRegistryName),
				p_listSuggestions_2_) : Suggestions.empty();
	}

	public static PlanetTraitArgument planetTrait() {
		return new PlanetTraitArgument();
	}

	public static PlanetTrait getTrait(CommandContext<CommandSource> pContext, String pName)
			throws CommandSyntaxException {
		ResourceLocation resourcelocation = pContext.getArgument(pName, ResourceLocation.class);
		return PlanetTrait.registry.containsKey(resourcelocation) ? PlanetTrait.registry.getValue(resourcelocation) : notValid(resourcelocation);
	}

	public static PlanetTrait notValid(ResourceLocation location) throws CommandSyntaxException {
		throw ERROR_INVALID_VALUE.create(location);
	}

}
