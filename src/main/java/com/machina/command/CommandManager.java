package com.machina.command;

import com.machina.Machina;
import com.machina.api.item.RocketItem;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.world.PlanetRegistrationHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class CommandManager {

	//@formatter:off
	public static final ArgumentBuilder<CommandSourceStack, ?> TP =
		Commands.literal("tp").then(Commands.argument("planet_id", IntegerArgumentType.integer()).executes(ctx -> {
			CommandSourceStack source = ctx.getSource();
			SolarSystem system = Starchart.system(source.getLevel());
			int id = IntegerArgumentType.getInteger(ctx, "planet_id");
			if (id < 0 || id > system.planets().size() - 1) {
				source.sendFailure(Component.literal("Planet index out of bounds: " + id));
				return Command.SINGLE_SUCCESS;
			}
			ServerLevel planet = PlanetRegistrationHandler.createPlanet(source.getServer(), id);
			ServerPlayer player = source.getPlayer();
			PlanetRegistrationHandler.sendPlayerToDimension(player, planet, player.blockPosition());
			source.sendSuccess(() -> Component.literal("Welcome to " + system.planets().get(id).name()), true);
			return Command.SINGLE_SUCCESS;
		}));
	
	public static final ArgumentBuilder<CommandSourceStack, ?> DEBUG = Commands.literal("debug")
			.then(Commands.literal("rocket").executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ItemStack stack = ItemInit.ROCKET.get().getDefaultInstance();
				RocketItem.setPart(stack, RocketPartType.CHASSIS, RocketPartInit.ADVANCED_CHASSIS.get());
				RocketItem.setPart(stack, RocketPartType.FUEL_TANK, RocketPartInit.PRESSURIZED_FUEL_TANK.get());
				RocketItem.setPart(stack, RocketPartType.LIFE_SUPPORT, RocketPartInit.REINFORCED_LIFE_SUPPORT.get());
				RocketItem.setPart(stack, RocketPartType.SHIELD, RocketPartInit.SIMPLE_SHIELD.get());
				RocketItem.setPart(stack, RocketPartType.THRUSTER, RocketPartInit.TRI_TALL_THRUSTER.get());
				RocketItem.initProperties(stack);
				if (source.getPlayer().addItem(stack)) {
					return Command.SINGLE_SUCCESS;
				}
				source.sendFailure(Component.literal("Add item failed."));
				return Command.SINGLE_SUCCESS;
			})
			);
	//@formatter:on

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		//@formatter:off
		event.getDispatcher().register(
				Commands.literal("machina")
				.requires(cs -> cs.hasPermission(2))
				.then(TP)
				.then(DEBUG)
		);
		//@formatter:on
	}

}
