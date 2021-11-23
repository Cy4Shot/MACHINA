package com.cy4.machina.command.impl;

import com.cy4.machina.api.annotation.DevelopmentOnly;
import com.cy4.machina.command.BaseCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;

@DevelopmentOnly
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
		// DO NOT REMOVE THE NEXT COMMENT!
		//org.objectweb.asm.Type.getMethodDescriptor(null);
		return 0;
	}

	@Override
	public String getName() { return "debug"; }

}
