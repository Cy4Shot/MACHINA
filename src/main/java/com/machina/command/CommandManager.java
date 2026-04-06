package com.machina.command;

import java.util.function.Function;

import com.machina.Machina;
import com.machina.api.item.RocketItem;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.math.VecUtil;
import com.machina.registration.init.DataComponentsInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RocketPartInit;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ServerWeatherManager;
import com.machina.weather.system.ServerWeatherSystem;
import com.machina.world.PlanetRegistrationHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
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
			stack.set(DataComponentsInit.ROCKET_DEBUG, true);
			if (source.getPlayer().addItem(stack)) {
				return Command.SINGLE_SUCCESS;
			}
			source.sendFailure(Component.literal("Add item failed."));
			return Command.SINGLE_SUCCESS;
		})
	);
	
	public static final Function<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>> WEATHER = context -> Commands.literal("weather")
		.then(Commands.literal("get").executes(ctx -> {
			CommandSourceStack source = ctx.getSource();
			ServerWeatherSystem sys = ServerWeatherManager.getOrCreate(source.getLevel());
			if (sys == null) {
				source.sendFailure(Component.literal("Not currently on a planet!"));
				return Command.SINGLE_SUCCESS;
			}
			WeatherEvent event = sys.getCurrentEvent();
			int ticksRemaining = sys.getTicksRemaining();
			source.sendSuccess(() -> Component.literal("It will be " + event.getName() + " for " + ticksRemaining + " ticks."), true);
			return Command.SINGLE_SUCCESS;
		}))
		.then(Commands.literal("set").then(Commands.argument("weather", WeatherEventArgument.weather(context)).executes(ctx -> {
			CommandSourceStack source = ctx.getSource();
			Reference<WeatherEvent> weather = WeatherEventArgument.get(ctx, "weather");
			ServerWeatherSystem sys = ServerWeatherManager.getOrCreate(source.getLevel());
			if (sys == null) {
				source.sendFailure(Component.literal("Not currently on a planet!"));
				return Command.SINGLE_SUCCESS;
			}
			sys.pickWeather(weather.value());
			source.sendSuccess(() -> Component.literal("Set weather to " + weather.key().location().toString()), true);
			return Command.SINGLE_SUCCESS;
		})));
	
	public static final Function<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>> WIND = context -> Commands.literal("wind")
		.then(Commands.literal("get").executes(ctx -> {
			CommandSourceStack source = ctx.getSource();
			ServerWeatherSystem sys = ServerWeatherManager.getOrCreate(source.getLevel());
			if (sys == null) {
				source.sendFailure(Component.literal("Not currently on a planet!"));
				return Command.SINGLE_SUCCESS;
			}
			Vec2 wind = sys.getWindDirection();
			double intensity = sys.getWindIntensity();
			source.sendSuccess(() -> Component.literal("The wind is currently facing " + VecUtil.vec2ToCardinal(wind.x, wind.y) + " with intensity " + intensity + "."), true);
			return Command.SINGLE_SUCCESS;
		}));
	//@formatter:on

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		//@formatter:off
		event.getDispatcher().register(
				Commands.literal("machina")
				.requires(cs -> cs.hasPermission(2))
				.then(TP)
				.then(DEBUG)
				.then(WEATHER.apply(event.getBuildContext()))
				.then(WIND.apply(event.getBuildContext()))
		);
		//@formatter:on
	}

}
