package com.machina.events;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.machina.Machina;
import com.machina.api.block.IClickableBlock;
import com.machina.api.network.s2c.S2CVaultStateSync;
import com.machina.api.recipe.RecipeRefreshManager;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.api.starchart.planet_structure.PlanetStructure;
import com.machina.api.starchart.planet_structure.PlanetStructureLoader;
import com.machina.api.util.MachinaRL;
import com.machina.registration.init.EffectInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.JsonLoaderInit;
import com.machina.world.biome.PlanetBiome;
import com.machina.world.data.NuclearExplosionData;
import com.machina.world.data.PlanetDimensionData;
import com.machina.world.data.RadiationData;
import com.machina.world.structure.CityVaultStructure;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class CommonForgeEvents {

	private static final ResourceKey<Structure> CITY_VAULT_KEY = ResourceKey.create(Registries.STRUCTURE, MachinaRL.create("city_vault"));
	private static final java.util.WeakHashMap<ServerPlayer, Boolean> playerVaultState = new java.util.WeakHashMap<>();
	private static final java.util.WeakHashMap<ServerLevel, HolderSet<Structure>> vaultHolderSetCache = new java.util.WeakHashMap<>();

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

		// Register biomes first
		PlanetBiomeLoader.INSTANCE.getEntrySet()
				.forEach(e -> registerBiome(server, e.getKey(), new PlanetBiome(e.getValue())));

		// Register structures
		PlanetStructureLoader.INSTANCE.getEntrySet().forEach(e -> registerStructure(server, e.getKey(), e.getValue()));
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
					String.format("Unable to register biome %s -- biome registry not writable", loc));
		}
	}

	@SuppressWarnings("deprecation")
	private static void registerStructure(MinecraftServer server, ResourceLocation loc,
			PlanetStructure planetStructure) {
		// Get registries
		Registry<Structure> structureRegistry = server.registryAccess().registryOrThrow(Registries.STRUCTURE);
		Registry<StructureSet> structureSetRegistry = server.registryAccess().registryOrThrow(Registries.STRUCTURE_SET);
		Registry<Biome> biomeRegistry = server.registryAccess().registryOrThrow(Registries.BIOME);

		ResourceKey<Structure> structureKey = ResourceKey.create(Registries.STRUCTURE, loc);
		ResourceKey<StructureSet> structureSetKey = ResourceKey.create(Registries.STRUCTURE_SET, loc);

		if (structureRegistry.containsKey(structureKey)) {
			return;
		}

		// Create biome holder set (all registered planet biomes)
		List<Holder<Biome>> biomeHolders = PlanetBiomeLoader.INSTANCE.getAllLoc().stream()
				.map(biomeLoc -> biomeRegistry.getHolder(ResourceKey.create(Registries.BIOME, biomeLoc)))
				.filter(Optional::isPresent).map(Optional::get).map(h -> (Holder<Biome>) h).toList();
		HolderSet<Biome> biomeSet = HolderSet.direct(biomeHolders);

		// Create structure settings
		Structure.StructureSettings settings = new Structure.StructureSettings(biomeSet, Map.of(),
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE);

		// Create the structure based on type
		Structure structure = createStructure(planetStructure.type(), settings);
		if (structure == null) {
			Machina.LOGGER.warn("Unknown structure type: {}", planetStructure.type());
			return;
		}

		// Register structure
		if (structureRegistry instanceof MappedRegistry<Structure> reg) {
			reg.unfreeze();
			reg.register(structureKey, structure, RegistrationInfo.BUILT_IN);
		}

		// Create and register structure set
		Holder<Structure> structureHolder = structureRegistry.getHolder(structureKey).orElseThrow();
		RandomSpreadStructurePlacement placement = new RandomSpreadStructurePlacement(planetStructure.spacing(),
				planetStructure.separation(), RandomSpreadType.LINEAR, planetStructure.salt());
		StructureSet structureSet = new StructureSet(
				List.of(new StructureSet.StructureSelectionEntry(structureHolder, 1)), placement);

		if (structureSetRegistry instanceof MappedRegistry<StructureSet> reg) {
			reg.unfreeze();
			reg.register(structureSetKey, structureSet, RegistrationInfo.BUILT_IN);
		}

		Machina.LOGGER.info("Registered planet structure: {}", loc);
	}

	private static Structure createStructure(String type, Structure.StructureSettings settings) {
		return switch (type) {
		case "city_vault" -> new CityVaultStructure(settings, ConstantHeight.of(VerticalAnchor.absolute(64)),
				Optional.of(Heightmap.Types.WORLD_SURFACE_WG));
		default -> null;
		};
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

		// Check vault state every 10 ticks
		if (level.getGameTime() % 10 == 0) {
			boolean inVault = false;

			HolderSet<Structure> vaultHolderSet = vaultHolderSetCache.computeIfAbsent(level, lvl -> {
				var registry = lvl.registryAccess().lookupOrThrow(Registries.STRUCTURE);
				Optional<Holder.Reference<Structure>> holder = registry.get(CITY_VAULT_KEY);
				return holder.map(HolderSet::direct).orElse(null);
			});

			if (vaultHolderSet != null) {
				inVault = level.structureManager()
					.getStructureWithPieceAt(player.blockPosition(), vaultHolderSet)
					.isValid();
			}

			Boolean previousState = playerVaultState.get(player);
			if (previousState == null || previousState != inVault) {
				playerVaultState.put(player, inVault);
				PacketDistributor.sendToPlayer(player, new S2CVaultStateSync(inVault));
			}
		}

		RadiationData radiationData = RadiationData.get(level);
		ChunkPos chunkPos = new ChunkPos(player.blockPosition());

		// Check if player is in a radioactive fluid
		FluidState fluidState = level.getFluidState(player.blockPosition());
		if (!fluidState.isEmpty()) {
			FluidObject fluidObj = FluidObject.fromFluid(fluidState.getType());
			if (fluidObj != null && fluidObj.isRadioactive()) {
				MobEffectInstance current = player.getEffect(EffectInit.RADIATION);
				int amplifier = 2; // Strong radiation from direct contact
				if (current == null || current.getDuration() < 30 || current.getAmplifier() < amplifier) {
					player.addEffect(new MobEffectInstance(EffectInit.RADIATION, 60, amplifier, false, true, true));
				}
				return; // Skip chunk radiation check if in radioactive fluid
			}
		}

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
