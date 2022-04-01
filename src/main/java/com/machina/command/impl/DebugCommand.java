package com.machina.command.impl;

import com.machina.command.BaseCommand;
import com.machina.planet.attribute.PlanetAttribute;
import com.machina.util.server.PlanetUtils;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

public class DebugCommand extends BaseCommand {

	public DebugCommand(int permissionLevel, boolean enabled) {
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

			System.out.println("Attributes for " + String.valueOf(PlanetUtils.getId(context.getSource().getLevel().dimension())));
			for (PlanetAttribute<?> attribute : pd.getAttributes()) {
				System.out.println(attribute.getAttributeType().getRegistryName().getPath() + "\t - \t"
						+ attribute.getValue().toString());
			}
		}
		return super.execute(context);
	}

	protected boolean checkDimension(CommandContext<CommandSource> context) {
		if (PlanetUtils.isDimensionPlanet(context.getSource().getLevel().dimension())) {
			return true;
		} else {
			context.getSource().sendFailure(new TranslationTextComponent("command.planet_traits.not_planet"));
			return false;
		}
	}

	@Override
	public String getName() {
		return "debug";
	}

}
