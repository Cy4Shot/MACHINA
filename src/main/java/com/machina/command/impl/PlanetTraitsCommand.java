package com.machina.command.impl;

import com.machina.command.BaseCommand;
import com.machina.util.PlanetUtils;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class PlanetTraitsCommand extends BaseCommand {

	protected PlanetTraitsCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

	protected boolean checkDimension(CommandContext<CommandSource> context) {
		if (PlanetUtils.isDimensionPlanet(context.getSource().getLevel().dimension())) {
			return true;
		} else {
			context.getSource().sendFailure(new TranslationTextComponent("command.planet_traits.not_planet"));
			return false;
		}
	}

}
