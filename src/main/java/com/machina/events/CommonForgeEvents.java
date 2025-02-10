package com.machina.events;

import com.machina.Machina;
import com.machina.api.recipe.RecipeRefreshManager;
import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.JsonLoaderInit;
import com.machina.world.biome.PlanetBiome;
import com.machina.world.data.PlanetDimensionData;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Bus.FORGE)
public class CommonForgeEvents {

	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		JsonLoaderInit.registerAll(event);

		event.addListener((ResourceManagerReloadListener) manager -> RecipeRefreshManager.INSTANCE
				.setServerRecipeManager(event.getServerResources().getRecipeManager()));
	}

	@SubscribeEvent
	public static void tagsUpdated(final TagsUpdatedEvent event) {
		RecipeRefreshManager.INSTANCE.refreshServer();
		RecipeRefreshManager.INSTANCE.refreshClient();
	}

	@SubscribeEvent
	public static void recipesUpdated(final RecipesUpdatedEvent event) {
		RecipeRefreshManager.INSTANCE.setClientRecipeManager(event.getRecipeManager());
		RecipeRefreshManager.INSTANCE.refreshClient();
	}

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent e) {
		if (e.getEntity().level().isClientSide())
			return;

		Starchart.syncClient((ServerPlayer) e.getEntity());
	}

	@SubscribeEvent
	public static void onDebug(final ItemTossEvent event) {

//		if (event.getEntity().getItem().getItem().equals(Items.STICK)) {
//			System.out.println("Placing tree");
//			boolean val = new PlanetTreeFeature().place(new FeaturePlaceContext<>(Optional.empty(),
//					(WorldGenLevel) event.getPlayer().level(), null, event.getPlayer().getRandom(),
//					event.getPlayer().level().getHeightmapPos(Types.OCEAN_FLOOR, event.getPlayer().blockPosition())
//							.above(5),
//					new PlanetTreeFeature.PlanetTreeFeatureConfig(
//							new PlanetBiomeTree(PlanetTreeInit.BRANCH_FUNNEL_MUSHROOM.getId(),
//									List.of(Blocks.DIAMOND_BLOCK.defaultBlockState(),
//											Blocks.GREEN_STAINED_GLASS.defaultBlockState()),
//									1, List.of(), List.of(), 0, 0))));
//			System.out.println("Result: " + val);
//			return;
//		}
//
//		int id = 8;
//		if (!event.getPlayer().level().isClientSide()) {
//			ServerLevel planet = PlanetRegistrationHandler.createPlanet(event.getPlayer().getServer(), id);
//			event.getPlayer().sendSystemMessage(
//					Component.literal("Sending to: " + Starchart.system(planet).planets().get(id).name()));
//			PlanetRegistrationHandler.sendPlayerToDimension((ServerPlayer) event.getPlayer(), planet,
//					new BlockPos(0, 100, 0));
//		}
	}

	@SubscribeEvent
	public static void getBurnTime(final FurnaceFuelBurnTimeEvent event) {
		if (event.getItemStack().getItem().equals(ItemInit.COAL_CHUNK.get())) {
			event.setBurnTime(ForgeHooks.getBurnTime(new ItemStack(Items.COAL), event.getRecipeType()) / 9);
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

	private static void registerBiome(MinecraftServer server, ResourceLocation loc, PlanetBiome biome) {
		ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, loc);
		Registry<Biome> dimRegFrozen = server.registryAccess().registryOrThrow(Registries.BIOME);
		if (dimRegFrozen.containsKey(key)) {
			return;
		}
		if (dimRegFrozen instanceof MappedRegistry<Biome> biomeReg) {
			biomeReg.unfreeze();
			biomeReg.register(key, biome, Lifecycle.stable());
		} else {
			throw new IllegalStateException(
					String.format("Unable to register dimension %s -- dimension registry not writable", loc));
		}
	}

	@SubscribeEvent
	public static void blockToolModification(BlockEvent.BlockToolModificationEvent event) {
		ToolAction action = event.getToolAction();
		BlockState state = event.getState();
		if (!event.isSimulated()) {
			if (action == ToolActions.AXE_STRIP) {
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
}
