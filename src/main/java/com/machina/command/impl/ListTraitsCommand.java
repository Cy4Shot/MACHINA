package com.machina.command.impl;

import com.machina.command.BaseCommand;
import com.machina.planet.PlanetData;
import com.machina.util.helper.PlanetHelper;
import com.machina.util.text.StringUtils;
import com.machina.world.data.StarchartData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ListTraitsCommand extends BaseCommand {

	public ListTraitsCommand(int permissionLevel) {
		super(permissionLevel);
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(this::execute);
	}

	@Override
	protected int execute(CommandContext<CommandSource> context) {
		if (checkDimension(context)) {
			PlanetData pd = StarchartData.getStarchartForServer(context.getSource().getServer())
					.get(context.getSource().getLevel().dimension().location());

			if (pd.getTraits().isEmpty()) {
				context.getSource()
						.sendSuccess(StringUtils.translateComp("command.planet_traits.list_traits.no_traits"), true);
			} else {
				context.getSource()
						.sendSuccess(StringUtils.translate("command.planet_traits.list_traits.success",
								pd.getTraits().toString().replace("[", "").replace("]", "")), true);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	@Override
	public String getName() {
		return "list_traits";
	}
	
	protected boolean checkDimension(CommandContext<CommandSource> context) {
		if (PlanetHelper.isDimensionPlanet(context.getSource().getLevel().dimension())) {
			return true;
		} else {
			context.getSource().sendFailure(StringUtils.translateComp("command.planet_traits.not_planet"));
			return false;
		}
	}
	
	@Override
	public LiteralArgumentBuilder<CommandSource> buildPath(LiteralArgumentBuilder<CommandSource> source,
			LiteralArgumentBuilder<CommandSource> command) {
		return source.then(Commands.literal("debug").then(command));
	}
}
