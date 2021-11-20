package com.cy4.machina.command.impl.traits;

import com.cy4.machina.api.capability.planet_data.CapabilityPlanetData;
import com.cy4.machina.api.command.argument.PlanetTraitArgument;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.util.helper.StarchartHelper;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class RemoveTraitCommand extends PlanetTraitsCommand {

	public RemoveTraitCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(Commands.argument("trait", PlanetTraitArgument.planetTrait()).executes(
				commandSource -> execute(commandSource, PlanetTraitArgument.getTrait(commandSource, "trait"))));
	}

	public int execute(CommandContext<CommandSource> context, PlanetTrait trait) {
		if (checkDimension(context)) {
			context.getSource().getLevel().getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY)
			.ifPresent(cap -> {
				if (cap.getTraits().contains(trait)) {
					StarchartHelper.removeTrait(context.getSource().getLevel(), trait);
					context.getSource().sendSuccess(
							new TranslationTextComponent("command.planet_traits.remove_trait.success"), true);
				} else {
					context.getSource()
					.sendFailure(new TranslationTextComponent(
							"command.planet_traits.remove_trait.not_existing",
							trait.getRegistryName().toString()));
				}
			});
		}
		return 0;
	}

	@Override
	public String getName() { return "remove_trait"; }

}
