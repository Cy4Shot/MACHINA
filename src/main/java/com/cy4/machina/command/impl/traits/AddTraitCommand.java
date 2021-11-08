package com.cy4.machina.command.impl.traits;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.command.argument.PlanetTraitArgument;
import com.cy4.machina.api.planet.PlanetTrait;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class AddTraitCommand extends PlanetTraitsCommand {

	public AddTraitCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(Commands.argument("trait", PlanetTraitArgument.planetTrait()).executes(
				commandSource -> execute(commandSource, PlanetTraitArgument.getTrait(commandSource, "trait"))));
	}

	public int execute(CommandContext<CommandSource> context, PlanetTrait trait) {
		if (checkDimension(context)) {
			ServerWorld world = context.getSource().getLevel();
			world.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
				if (!cap.getTraits().contains(trait)) {
					cap.addTrait(trait);
					context.getSource()
							.sendSuccess(new TranslationTextComponent("command.planet_traits.add_trait.success"), true);
				} else {
					context.getSource().sendFailure(new TranslationTextComponent(
							"command.planet_traits.add_trait.duplicate", trait.getRegistryName().toString()));
				}
			});
		}
		return 0;
	}

	@Override
	public String getName() { return "add_trait"; }

}