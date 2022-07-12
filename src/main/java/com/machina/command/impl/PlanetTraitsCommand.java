package com.machina.command.impl;

import com.machina.command.BaseCommand;
import com.machina.util.server.PlanetUtils;
import com.machina.util.text.StringUtils;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;

public abstract class PlanetTraitsCommand extends BaseCommand {

	protected PlanetTraitsCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

	protected boolean checkDimension(CommandContext<CommandSource> context) {
		if (PlanetUtils.isDimensionPlanet(context.getSource().getLevel().dimension())) {
			return true;
		} else {
			context.getSource().sendFailure(StringUtils.translateComp("command.planet_traits.not_planet"));
			return false;
		}
	}
}
