/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.command.impl.traits;

import com.cy4.machina.api.planet.PlanetUtils;
import com.cy4.machina.command.BaseCommand;
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
