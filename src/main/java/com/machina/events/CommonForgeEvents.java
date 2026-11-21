package com.machina.events;

import com.machina.Machina;
import com.machina.api.block.IClickableBlock;
import com.machina.api.recipe.RecipeRefreshManager;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.registration.init.EffectInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.JsonLoaderInit;
import com.machina.world.biome.PlanetBiome;
import com.machina.world.data.NuclearExplosionData;
import com.machina.world.data.PlanetDimensionData;
import com.machina.world.data.RadiationData;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		JsonLoaderInit.registerAll(event);

		event.addListener((ResourceManagerReloadListener) manager -> RecipeRefreshManager.INSTANCE
				.setServerRecipeManager(event.getServerResources().getRecipeManager()));
	}

	@SubscribeEvent
	public static void tagsUpdated(final TagsUpdatedEvent event) {
		RecipeRefreshManager.INSTANCE.refreshServer(event.getRegistryAccess());
		RecipeRefreshManager.INSTANCE.refreshClient(event.getRegistryAccess());
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void recipesUpdated(final RecipesUpdatedEvent event) {
		RecipeRefreshManager.INSTANCE.setClientRecipeManager(event.getRecipeManager());
		RecipeRefreshManager.INSTANCE.refreshClient(Minecraft.getInstance().level.registryAccess());
	}

	@SubscribeEvent
	public static void getBurnTime(final FurnaceFuelBurnTimeEvent event) {
		if (event.getItemStack().getItem().equals(ItemInit.COAL_CHUNK.get())) {
			event.setBurnTime(new ItemStack(Items.COAL).getBurnTime(event.getRecipeType()) / 9);
		}
	}

	@SubscribeEvent
	public static void serverStart(final LevelEvent.Load event) {
		if (event.getLevel().isClientSide()) {
			return;
		}
		MinecraftServer server = event.getLevel().getServer();
		if (server != null && server.levelKeys().size() > 1) {
			return;
		}
		if (server != null) {
			PlanetDimensionData.getDefaultInstance(server).updateSeed(((ServerLevel) event.getLevel()).getSeed());
		}
	}

	@SubscribeEvent
	public static void serverAboutToStart(final ServerAboutToStartEvent event) {
		MinecraftServer server = event.getServer();
		PlanetBiomeLoader.INSTANCE.getEntrySet()
				.forEach(e -> registerBiome(server, e.getKey(), new PlanetBiome(e.getValue())));
	}

	@SuppressWarnings("deprecation")
	private static void registerBiome(MinecraftServer server, ResourceLocation loc, PlanetBiome biome) {
		ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, loc);
		Registry<Biome> dimRegFrozen = server.registryAccess().registryOrThrow(Registries.BIOME);
		if (dimRegFrozen.containsKey(key)) {
			return;
		}
		if (dimRegFrozen instanceof MappedRegistry<Biome> biomeReg) {
			biomeReg.unfreeze();
			biomeReg.register(key, biome, RegistrationInfo.BUILT_IN);
		} else {
			throw new IllegalStateException(
					String.format("Unable to register dimension %s -- dimension registry not writable", loc));
		}
	}

	@SubscribeEvent
	public static void blockToolModification(BlockEvent.BlockToolModificationEvent event) {
		ItemAbility action = event.getItemAbility();
		BlockState state = event.getState();
		if (!event.isSimulated()) {
			if (action == ItemAbilities.AXE_STRIP) {
				for (WoodFamily family : FamiliesInit.WOODS) {
					if (state.is(family.log())) {
						event.setFinalState(family.stripped_log().withPropertiesOf(state));
						return;
					}

					if (state.is(family.wood())) {
						event.setFinalState(family.stripped_wood().withPropertiesOf(state));
						return;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void itemUse(PlayerInteractEvent.RightClickBlock event) {
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		if (block instanceof IClickableBlock) {
			event.setUseBlock(TriState.TRUE);
		}
	}

	private static final long TICKS_PER_HOUR = 60L * 60 * 20;

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		ServerLevel level = (ServerLevel) player.level();
		RadiationData radiationData = RadiationData.get(level);
		ChunkPos chunkPos = new ChunkPos(player.blockPosition());

		if (radiationData.isChunkIrradiated(chunkPos)) {
			long remainingTicks = radiationData.getRemainingTicks(chunkPos, level.getGameTime());
			int amplifier;
			if (remainingTicks == RadiationData.PERMANENT) {
				amplifier = 4;
			} else {
				int hoursRemaining = (int) Math.ceil((double) remainingTicks / TICKS_PER_HOUR);
				amplifier = Math.max(0, hoursRemaining - 1);
			}

			MobEffectInstance current = player.getEffect(EffectInit.RADIATION);
			if (current == null || current.getDuration() < 30 || current.getAmplifier() != amplifier) {
				player.addEffect(new MobEffectInstance(EffectInit.RADIATION, 40, amplifier, false, true, true));
			}
		}
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}

		// Tick nuclear explosions every tick
		NuclearExplosionData.get(level).tick();

		// Tick radiation every 20 ticks
		if (level.getGameTime() % 20 != 0) {
			return;
		}
		RadiationData.get(level).tick(level.getGameTime());
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			ServerLevel level = (ServerLevel) player.level();
			RadiationData.get(level).syncToPlayer(player);
			NuclearExplosionData.get(level).syncToPlayer(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			ServerLevel level = (ServerLevel) player.level();
			RadiationData.get(level).syncToPlayer(player);
			NuclearExplosionData.get(level).syncToPlayer(player);
		}
	}
}
