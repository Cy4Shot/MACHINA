package com.machina.command.impl;

import com.machina.command.BaseCommand;
import com.machina.registration.registry.PlanetAttributeRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;

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
		System.out.println(PlanetAttributeRegistry.REGISTRY.getValues());
		return 0;
	}

	@Override
	public String getName() { return "debug"; }

}
