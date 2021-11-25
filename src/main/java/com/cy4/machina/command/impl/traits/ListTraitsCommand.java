package com.cy4.machina.command.impl.traits;

import com.cy4.machina.world.data.StarchartData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

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
			StarchartData.getStarchartForServer(context.getSource().getServer())
					.getDimensionDataOrCreate(context.getSource().getLevel().dimension().location()).ifPresent(cap -> {
						if (cap.getTraits().isEmpty()) {
							context.getSource().sendSuccess(
									new TranslationTextComponent("command.planet_traits.list_traits.no_traits"), true);
						} else {
							context.getSource()
									.sendSuccess(
											new TranslationTextComponent("command.planet_traits.list_traits.success",
													cap.getTraits().toString().replace("[", "").replace("]", "")),
											true);
						}
					});
		}
		return super.execute(context);
	}

	@Override
	public String getName() { return "list_traits"; }

}
