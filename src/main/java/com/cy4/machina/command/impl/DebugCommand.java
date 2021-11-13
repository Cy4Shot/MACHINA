package com.cy4.machina.command.impl;

import java.util.Set;

import com.cy4.machina.Machina;
import com.cy4.machina.command.BaseCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

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
		Set<Class<?>> allFields = Machina.REFLECTIONS.getSubTypesOf(Object.class);
		context.getSource().sendSuccess(new StringTextComponent(String.valueOf(allFields.size())), false);
		return 0;
	}

	@Override
	public String getName() { return "debug"; }

}
