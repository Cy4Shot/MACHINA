package com.machina.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public abstract class BaseCommand {

	protected LiteralArgumentBuilder<CommandSource> builder;
	int permissionLevel;

	protected BaseCommand(int permissionLevel) {
		builder = Commands.literal(getName()).requires(source -> source.hasPermission(permissionLevel));
		this.permissionLevel = permissionLevel;
	}

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public LiteralArgumentBuilder<CommandSource> getBuilder() {
		return builder;
	}

	public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

	public abstract String getName();

	protected int execute(CommandContext<CommandSource> context) {
		return 0;
	}

	public abstract LiteralArgumentBuilder<CommandSource> buildPath(LiteralArgumentBuilder<CommandSource> source,
			LiteralArgumentBuilder<CommandSource> command);

}
