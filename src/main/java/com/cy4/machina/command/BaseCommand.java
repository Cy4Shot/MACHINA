/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public abstract class BaseCommand {

	protected LiteralArgumentBuilder<CommandSource> builder;
	boolean enabled;
	int permissionLevel;

	protected BaseCommand(int permissionLevel, boolean enabled) {
		builder = Commands.literal(getName()).requires(source -> source.hasPermission(permissionLevel));
		this.enabled = enabled;
		this.permissionLevel = permissionLevel;
	}

	public int getPermissionLevel() { return permissionLevel; }

	public LiteralArgumentBuilder<CommandSource> getBuilder() { return builder; }

	public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

	public boolean isEnabled() { return enabled; }

	public abstract String getName();

	protected int execute(CommandContext<CommandSource> context) {
		return 0;
	}

}
