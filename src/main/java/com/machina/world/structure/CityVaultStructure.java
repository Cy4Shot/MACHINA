package com.machina.world.structure;

import java.util.List;
import java.util.Optional;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityVault;
import com.machina.registration.init.StructureInit;
import com.machina.world.biome.PlanetBiome;
import com.machina.world.structure.vault.VaultCorridorPiece;
import com.machina.world.structure.vault.VaultEntrancePiece;
import com.machina.world.structure.vault.VaultMainChamberPiece;
import com.machina.world.structure.vault.VaultSideRoomPiece;
import com.machina.world.structure.vault.VaultStaircasePiece;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class CityVaultStructure extends Structure {

	private static final int VAULT_DEPTH = 40;

	public static final MapCodec<CityVaultStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
		instance.group(
			settingsCodec(instance),
			HeightProvider.CODEC.fieldOf("start_height").forGetter(s -> s.startHeight),
			Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(s -> s.projectStartToHeightmap)
		).apply(instance, CityVaultStructure::new)
	);

	private final HeightProvider startHeight;
	private final Optional<Heightmap.Types> projectStartToHeightmap;

	public CityVaultStructure(Structure.StructureSettings settings, HeightProvider startHeight,
			Optional<Heightmap.Types> projectStartToHeightmap) {
		super(settings);
		this.startHeight = startHeight;
		this.projectStartToHeightmap = projectStartToHeightmap;
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		int chunkX = context.chunkPos().x;
		int chunkZ = context.chunkPos().z;
		int centerX = (chunkX << 4) + 8;
		int centerZ = (chunkZ << 4) + 8;
		long seed = context.seed();

		Biome biome = context.biomeSource().getNoiseBiome(
			centerX >> 2, 0, centerZ >> 2, context.randomState().sampler()
		).value();

		if (!(biome instanceof PlanetBiome planetBiome)) {
			return Optional.empty();
		}

		if (planetBiome.settings.city_grid() == null) {
			return Optional.empty();
		}

		PlanetBiomeCityVault vault = planetBiome.settings.city_grid().vault();
		if (vault == null || !vault.enabled() || vault.blocks().isEmpty()) {
			return Optional.empty();
		}

		int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(
			centerX, centerZ, Heightmap.Types.WORLD_SURFACE_WG,
			context.heightAccessor(), context.randomState()
		);

		BlockPos structurePos = new BlockPos(centerX, surfaceY, centerZ);
		List<BlockState> blocks = vault.blocks();
		int vaultCenterX = centerX;
		int vaultCenterZ = centerZ;

		return Optional.of(new GenerationStub(structurePos, builder -> {
			BlockPos chamberCenter = new BlockPos(vaultCenterX, VAULT_DEPTH, vaultCenterZ);

			// Determine which corridors to generate (deterministic based on seed)
			RandomSource random = RandomSource.create(seed ^ ((long) chunkX * 198491317L + (long) chunkZ * 6542989L));
			Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
			boolean[] hasCorridors = new boolean[4];

			for (Direction dir : dirs) {
				hasCorridors[dir.get2DDataValue()] = random.nextFloat() <= 0.7f;
			}

			// Main chamber (needs to know corridor directions for wall openings)
			VaultMainChamberPiece mainChamber = new VaultMainChamberPiece(chamberCenter, blocks, hasCorridors);
			builder.addPiece(mainChamber);

			// Staircase from surface to vault (bottom connects to chamber ceiling opening)
			builder.addPiece(new VaultStaircasePiece(vaultCenterX, vaultCenterZ, surfaceY, VAULT_DEPTH + VaultMainChamberPiece.CHAMBER_HEIGHT, blocks));

			// Surface entrance building
			builder.addPiece(new VaultEntrancePiece(vaultCenterX, vaultCenterZ, surfaceY, blocks));

			// Side corridors and rooms
			for (Direction dir : dirs) {
				if (!hasCorridors[dir.get2DDataValue()]) continue;

				int corridorLength = 15 + random.nextInt(15);
				int roomSize = 8 + random.nextInt(4);

				// Corridor starts at chamber wall
				int startOffset = VaultMainChamberPiece.CHAMBER_RADIUS - 1;

				BlockPos corridorStart = new BlockPos(
					vaultCenterX + dir.getStepX() * startOffset,
					VAULT_DEPTH,
					vaultCenterZ + dir.getStepZ() * startOffset
				);

				builder.addPiece(new VaultCorridorPiece(corridorStart, dir, corridorLength, VAULT_DEPTH, blocks));

				BlockPos roomCenter = new BlockPos(
					corridorStart.getX() + dir.getStepX() * (corridorLength + roomSize / 2 + 2),
					VAULT_DEPTH,
					corridorStart.getZ() + dir.getStepZ() * (corridorLength + roomSize / 2 + 2)
				);

				builder.addPiece(new VaultSideRoomPiece(roomCenter, roomSize, VAULT_DEPTH, blocks));
			}
		}));
	}

	@Override
	public StructureType<?> type() {
		return StructureInit.CITY_VAULT.get();
	}
}
