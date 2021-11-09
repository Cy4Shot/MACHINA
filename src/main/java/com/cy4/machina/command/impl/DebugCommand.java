package com.cy4.machina.command.impl;

import com.cy4.machina.command.BaseCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistries;

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
		try {
			context.getSource().getPlayerOrException().getAttribute(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("forge", "entity_gravity"))).removeModifier(new AttributeModifier("AA", 1, net.minecraft.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL));
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getName() { return "debug"; }

}
