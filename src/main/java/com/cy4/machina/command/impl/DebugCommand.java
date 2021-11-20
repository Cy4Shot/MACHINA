package com.cy4.machina.command.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import com.cy4.machina.api.annotation.DevelopmentOnly;
import com.cy4.machina.api.capability.planet_data.CapabilityPlanetData;
import com.cy4.machina.api.planet.trait.PlanetTrait;
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
		context.getSource().getServer().getAllLevels().forEach(world -> {
			List<PlanetTrait> traits = new LinkedList<>();
			world.getCapability(CapabilityPlanetData.PLANET_DATA_CAPABILITY).ifPresent(cap -> {
				cap.getTraits().forEach(traits::add);
			});
			LogManager.getLogger().info("World {} with the name {} has the following traits: {}", world, world.dimension().location(), traits);
		});
		return 0;
	}

	@Override
	public String getName() { return "debug"; }

}
