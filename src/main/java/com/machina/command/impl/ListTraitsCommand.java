package com.machina.command.impl;

import com.machina.util.text.StringUtils;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;

public class ListTraitsCommand extends PlanetTraitsCommand {

	public ListTraitsCommand(int permissionLevel, boolean enabled) {
		super(permissionLevel, enabled);
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
		return super.execute(context);
	}

	@Override
	public String getName() {
		return "list_traits";
	}

}
