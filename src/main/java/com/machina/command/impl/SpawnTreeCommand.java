package com.machina.command.impl;

import java.util.Random;

import com.machina.command.BaseCommand;
import com.machina.world.data.StarchartData;
import com.machina.world.feature.planet.tree.PlanetTreeFeature;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class SpawnTreeCommand extends BaseCommand {

	public SpawnTreeCommand(int permissionLevel) {
		super(permissionLevel);
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(Commands.argument("tree_id", IntegerArgumentType.integer())
				.executes(ctx -> execute(ctx, IntegerArgumentType.getInteger(ctx, "tree_id"))));
	}

	protected int execute(CommandContext<CommandSource> context, int treeId) throws CommandSyntaxException {

		new PlanetTreeFeature(
				StarchartData.getDataForDimension(context.getSource().getLevel().dimension()).getAttributes()).place(
						treeId, context.getSource().getLevel(),
						context.getSource().getPlayerOrException().blockPosition(), new Random());

		return 0;
	}

	@Override
	public String getName() {
		return "spawntree";
	}

	@Override
	public LiteralArgumentBuilder<CommandSource> buildPath(LiteralArgumentBuilder<CommandSource> source,
			LiteralArgumentBuilder<CommandSource> command) {
		return source.then(Commands.literal("debug").then(command));
	}

}
