package com.machina.command.argument;

import java.util.concurrent.CompletableFuture;

import com.machina.planet.trait.PlanetTrait;
import com.machina.registration.registry.PlanetTraitRegistry;
import com.machina.util.text.MachinaRL;
import com.machina.util.text.StringUtils;
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

public class PlanetTraitArgument implements ArgumentType<MachinaRL> {

	private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
			p_212594_0_ -> StringUtils.translate("argument.planet_trait.invalid", p_212594_0_));

	@Override
	public MachinaRL parse(StringReader reader) throws CommandSyntaxException {
		return MachinaRL.read(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_,
			SuggestionsBuilder p_listSuggestions_2_) {
		return p_listSuggestions_1_.getSource() instanceof ISuggestionProvider
				? ISuggestionProvider.suggestResource(PlanetTraitRegistry.REGISTRY.getValues().stream()
						.filter(PlanetTrait::exists).map(PlanetTrait::getRegistryName), p_listSuggestions_2_)
				: Suggestions.empty();
	}

	public static PlanetTraitArgument planetTrait() {
		return new PlanetTraitArgument();
	}

	public static PlanetTrait getTrait(CommandContext<CommandSource> pContext, String pName)
			throws CommandSyntaxException {
		ResourceLocation resourcelocation = pContext.getArgument(pName, ResourceLocation.class);
		return PlanetTraitRegistry.REGISTRY.containsKey(resourcelocation)
				&& PlanetTraitRegistry.REGISTRY.getValue(resourcelocation).exists()
						? PlanetTraitRegistry.REGISTRY.getValue(resourcelocation)
						: notValid(resourcelocation);
	}

	public static PlanetTrait notValid(ResourceLocation location) throws CommandSyntaxException {
		throw ERROR_INVALID_VALUE.create(location);
	}

}
