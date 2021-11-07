package com.cy4.machina.command.impl.traits;

import com.cy4.machina.Machina;
import com.cy4.machina.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class PlanetTraitsCommand extends BaseCommand {

	protected PlanetTraitsCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
	}

	protected boolean checkDimension(CommandContext<CommandSource> context) {
		if (context.getSource().getLevel().dimension().location().getNamespace().equals(Machina.MOD_ID)) {
			return true;
		} else {
			context.getSource().sendFailure(new TranslationTextComponent("command.planet_traits.not_planet"));
			return false;
		}
	}

}
