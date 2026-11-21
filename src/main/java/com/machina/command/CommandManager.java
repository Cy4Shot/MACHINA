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
import com.machina.world.data.NuclearExplosionData;
import com.machina.world.data.RadiationData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
			RocketItem.setPart(stack, RocketPartType.CHASSIS, RocketPartInit.SIMPLE_CHASSIS.get());
			RocketItem.setPart(stack, RocketPartType.FUEL_TANK, RocketPartInit.SIMPLE_FUEL_TANK.get());
			RocketItem.setPart(stack, RocketPartType.LIFE_SUPPORT, RocketPartInit.SIMPLE_LIFE_SUPPORT.get());
			RocketItem.setPart(stack, RocketPartType.SHIELD, RocketPartInit.SIMPLE_SHIELD.get());
			RocketItem.setPart(stack, RocketPartType.THRUSTER, RocketPartInit.SIMPLE_THRUSTER.get());
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
			sys.pickWeather(weather.value(), true);
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

	public static final Function<CommandBuildContext, ArgumentBuilder<CommandSourceStack, ?>> TEMP = context -> Commands.literal("temp")
		.then(Commands.literal("get").executes(ctx -> {
			CommandSourceStack source = ctx.getSource();
			ServerWeatherSystem sys = ServerWeatherManager.getOrCreate(source.getLevel());
			if (sys == null) {
				source.sendFailure(Component.literal("Not currently on a planet!"));
				return Command.SINGLE_SUCCESS;
			}
			float temperature = sys.getTemperature();
			source.sendSuccess(() -> Component.literal("Current temperature is " + String.format("%.1f", temperature) + "K."), true);
			return Command.SINGLE_SUCCESS;
		}));

	public static final ArgumentBuilder<CommandSourceStack, ?> RADIATION = Commands.literal("radiation")
		.then(Commands.literal("set")
			.then(Commands.argument("duration", LongArgumentType.longArg(-1))
				.executes(ctx -> {
					CommandSourceStack source = ctx.getSource();
					ServerLevel level = source.getLevel();
					long duration = LongArgumentType.getLong(ctx, "duration");
					ChunkPos chunkPos = new ChunkPos(source.getPlayer().blockPosition());
					RadiationData data = RadiationData.get(level);
					if (duration == RadiationData.PERMANENT) {
						data.setChunkIrradiatedPermanent(chunkPos);
						source.sendSuccess(() -> Component.literal("Set permanent radiation at chunk [" + chunkPos.x + ", " + chunkPos.z + "]"), true);
					} else {
						data.setChunkIrradiated(chunkPos, duration, level.getGameTime());
						source.sendSuccess(() -> Component.literal("Set radiation at chunk [" + chunkPos.x + ", " + chunkPos.z + "] for " + duration + " ticks"), true);
					}
					return Command.SINGLE_SUCCESS;
				})
				.then(Commands.argument("chunkX", IntegerArgumentType.integer())
					.then(Commands.argument("chunkZ", IntegerArgumentType.integer())
						.executes(ctx -> {
							CommandSourceStack source = ctx.getSource();
							ServerLevel level = source.getLevel();
							long duration = LongArgumentType.getLong(ctx, "duration");
							int chunkX = IntegerArgumentType.getInteger(ctx, "chunkX");
							int chunkZ = IntegerArgumentType.getInteger(ctx, "chunkZ");
							ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
							RadiationData data = RadiationData.get(level);
							if (duration == RadiationData.PERMANENT) {
								data.setChunkIrradiatedPermanent(chunkPos);
								source.sendSuccess(() -> Component.literal("Set permanent radiation at chunk [" + chunkX + ", " + chunkZ + "]"), true);
							} else {
								data.setChunkIrradiated(chunkPos, duration, level.getGameTime());
								source.sendSuccess(() -> Component.literal("Set radiation at chunk [" + chunkX + ", " + chunkZ + "] for " + duration + " ticks"), true);
							}
							return Command.SINGLE_SUCCESS;
						})))))
		.then(Commands.literal("clear")
			.executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ServerLevel level = source.getLevel();
				ChunkPos chunkPos = new ChunkPos(source.getPlayer().blockPosition());
				RadiationData.get(level).clearChunkRadiation(chunkPos);
				source.sendSuccess(() -> Component.literal("Cleared radiation at chunk [" + chunkPos.x + ", " + chunkPos.z + "]"), true);
				return Command.SINGLE_SUCCESS;
			})
			.then(Commands.argument("chunkX", IntegerArgumentType.integer())
				.then(Commands.argument("chunkZ", IntegerArgumentType.integer())
					.executes(ctx -> {
						CommandSourceStack source = ctx.getSource();
						ServerLevel level = source.getLevel();
						int chunkX = IntegerArgumentType.getInteger(ctx, "chunkX");
						int chunkZ = IntegerArgumentType.getInteger(ctx, "chunkZ");
						RadiationData.get(level).clearChunkRadiation(chunkX, chunkZ);
						source.sendSuccess(() -> Component.literal("Cleared radiation at chunk [" + chunkX + ", " + chunkZ + "]"), true);
						return Command.SINGLE_SUCCESS;
					}))))
		.then(Commands.literal("check")
			.executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ServerLevel level = source.getLevel();
				ChunkPos chunkPos = new ChunkPos(source.getPlayer().blockPosition());
				RadiationData data = RadiationData.get(level);
				if (data.isChunkIrradiated(chunkPos)) {
					long remaining = data.getRemainingTicks(chunkPos, level.getGameTime());
					if (remaining == RadiationData.PERMANENT) {
						source.sendSuccess(() -> Component.literal("Chunk [" + chunkPos.x + ", " + chunkPos.z + "] has permanent radiation"), true);
					} else {
						source.sendSuccess(() -> Component.literal("Chunk [" + chunkPos.x + ", " + chunkPos.z + "] has radiation for " + remaining + " more ticks"), true);
					}
				} else {
					source.sendSuccess(() -> Component.literal("Chunk [" + chunkPos.x + ", " + chunkPos.z + "] is not irradiated"), true);
				}
				return Command.SINGLE_SUCCESS;
			})
			.then(Commands.argument("chunkX", IntegerArgumentType.integer())
				.then(Commands.argument("chunkZ", IntegerArgumentType.integer())
					.executes(ctx -> {
						CommandSourceStack source = ctx.getSource();
						ServerLevel level = source.getLevel();
						int chunkX = IntegerArgumentType.getInteger(ctx, "chunkX");
						int chunkZ = IntegerArgumentType.getInteger(ctx, "chunkZ");
						ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
						RadiationData data = RadiationData.get(level);
						if (data.isChunkIrradiated(chunkPos)) {
							long remaining = data.getRemainingTicks(chunkPos, level.getGameTime());
							if (remaining == RadiationData.PERMANENT) {
								source.sendSuccess(() -> Component.literal("Chunk [" + chunkX + ", " + chunkZ + "] has permanent radiation"), true);
							} else {
								source.sendSuccess(() -> Component.literal("Chunk [" + chunkX + ", " + chunkZ + "] has radiation for " + remaining + " more ticks"), true);
							}
						} else {
							source.sendSuccess(() -> Component.literal("Chunk [" + chunkX + ", " + chunkZ + "] is not irradiated"), true);
						}
						return Command.SINGLE_SUCCESS;
					}))))
		.then(Commands.literal("clearall")
			.executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ServerLevel level = source.getLevel();
				RadiationData data = RadiationData.get(level);
				int count = data.getIrradiatedChunks().size();
				data.clearAll();
				source.sendSuccess(() -> Component.literal("Cleared radiation from " + count + " chunks"), true);
				return Command.SINGLE_SUCCESS;
			}));

	public static final ArgumentBuilder<CommandSourceStack, ?> NUKE = Commands.literal("nuke")
		.then(Commands.literal("start")
			.executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ServerPlayer player = source.getPlayer();
				if (player == null) {
					source.sendFailure(Component.literal("Must be run by a player"));
					return Command.SINGLE_SUCCESS;
				}
				HitResult hit = player.pick(1000.0, 0.0f, false);
				if (hit.getType() != HitResult.Type.BLOCK) {
					source.sendFailure(Component.literal("Not looking at a block"));
					return Command.SINGLE_SUCCESS;
				}
				BlockHitResult blockHit = (BlockHitResult) hit;
				return spawnNuke(source, blockHit.getBlockPos().getX() + 0.5, blockHit.getBlockPos().getY() + 0.5, blockHit.getBlockPos().getZ() + 0.5, 8.0f);
			})
			.then(Commands.argument("power", FloatArgumentType.floatArg(1.0f, 15.0f))
				.executes(ctx -> {
					CommandSourceStack source = ctx.getSource();
					ServerPlayer player = source.getPlayer();
					float power = FloatArgumentType.getFloat(ctx, "power");
					if (player == null) {
						source.sendFailure(Component.literal("Must be run by a player"));
						return Command.SINGLE_SUCCESS;
					}
					HitResult hit = player.pick(1000.0, 0.0f, false);
					if (hit.getType() != HitResult.Type.BLOCK) {
						source.sendFailure(Component.literal("Not looking at a block"));
						return Command.SINGLE_SUCCESS;
					}
					BlockHitResult blockHit = (BlockHitResult) hit;
					return spawnNuke(source, blockHit.getBlockPos().getX() + 0.5, blockHit.getBlockPos().getY() + 0.5, blockHit.getBlockPos().getZ() + 0.5, power);
				})))
		.then(Commands.literal("clearAll")
			.executes(ctx -> {
				CommandSourceStack source = ctx.getSource();
				ServerLevel level = source.getLevel();
				NuclearExplosionData data = NuclearExplosionData.get(level);
				int count = data.clearAll();
				source.sendSuccess(() -> Component.literal("Cleared " + count + " active nuclear explosions"), true);
				return Command.SINGLE_SUCCESS;
			}));

	private static int spawnNuke(CommandSourceStack source, double x, double y, double z, float power) {
		ServerLevel level = source.getLevel();
		NuclearExplosionData.get(level).spawnExplosion(x, y, z, power);
		source.sendSuccess(() -> Component.literal("Spawned nuclear explosion at [" + (int)x + ", " + (int)y + ", " + (int)z + "] with power " + power), true);
		return Command.SINGLE_SUCCESS;
	}
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
				.then(TEMP.apply(event.getBuildContext()))
				.then(RADIATION)
				.then(NUKE)
		);
		//@formatter:on
	}

}
