package com.machina.world.structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityVault;
import com.machina.registration.init.StructureInit;
import com.machina.world.biome.PlanetBiome;
import com.machina.world.structure.vault.VaultCorridorPiece;
import com.machina.world.structure.vault.VaultEntrancePiece;
import com.machina.world.structure.vault.VaultLevelStaircasePiece;
import com.machina.world.structure.vault.VaultMainChamberPiece;
import com.machina.world.structure.vault.VaultSideRoomPiece;
import com.machina.world.structure.vault.VaultStaircasePiece;
import com.machina.world.structure.vault.helper.VaultOvergrowthHelper;
import com.machina.world.structure.vault.helper.VaultPalette;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class CityVaultStructure extends Structure {

	private static final int NUM_LEVELS = 4;
	private static final int LEVEL_HEIGHT = 25;
	private static final int MIN_FIRST_LEVEL_DEPTH = 30;
	private static final int MAX_FIRST_LEVEL_DEPTH = 40;
	private static final int GRID_SIZE = 3;
	private static final int ROOM_SPACING = 35;

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
		if (vault == null || !vault.enabled()) {
			return Optional.empty();
		}

		int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(
			centerX, centerZ, Heightmap.Types.WORLD_SURFACE_WG,
			context.heightAccessor(), context.randomState()
		);

		BlockPos structurePos = new BlockPos(centerX, surfaceY, centerZ);
		int blockSize = planetBiome.settings.city_grid().block_size();

		// Use vault-specific overgrowth config
		VaultOvergrowthHelper.OvergrowthData overgrowthData = VaultOvergrowthHelper.OvergrowthData.createVaultDefault();

		return Optional.of(new GenerationStub(structurePos, builder -> {
			RandomSource random = RandomSource.create(seed ^ ((long) chunkX * 198491317L + (long) chunkZ * 6542989L));

			// Track where each level's main chamber is (they're offset due to staircases)
			int[] levelCenterX = new int[NUM_LEVELS];
			int[] levelCenterZ = new int[NUM_LEVELS];
			levelCenterX[0] = centerX;
			levelCenterZ[0] = centerZ;

			// Pre-calculate staircase positions to know where next level's main room will be
			for (int level = 0; level < NUM_LEVELS - 1; level++) {
				// Pick a side room position for the staircase down
				int stairOffsetX = (random.nextBoolean() ? 1 : -1) * ROOM_SPACING;
				int stairOffsetZ = (random.nextBoolean() ? 1 : -1) * ROOM_SPACING;

				// Next level's main chamber is directly below this staircase
				levelCenterX[level + 1] = levelCenterX[level] + stairOffsetX;
				levelCenterZ[level + 1] = levelCenterZ[level] + stairOffsetZ;
			}

			// Calculate first level depth (at least 30 blocks below surface, but no higher than Y=40)
			int firstLevelY = Math.min(MAX_FIRST_LEVEL_DEPTH, surfaceY - MIN_FIRST_LEVEL_DEPTH);

			// Generate each level
			for (int level = 0; level < NUM_LEVELS; level++) {
				int levelY = firstLevelY - (level * LEVEL_HEIGHT);
				VaultPalette palette = VaultPalette.forLevel(level);

				// Position for staircase to next level (null for bottom level)
				BlockPos nextLevelStairPos = null;
				if (level < NUM_LEVELS - 1) {
					nextLevelStairPos = new BlockPos(levelCenterX[level + 1], levelY, levelCenterZ[level + 1]);
				}

				generateLevel(builder, random, levelCenterX[level], levelCenterZ[level], levelY, surfaceY,
					level, palette, blockSize, nextLevelStairPos, overgrowthData);
			}
		}));
	}

	private void generateLevel(StructurePiecesBuilder builder, RandomSource random, int centerX, int centerZ,
			int levelY, int surfaceY, int levelIndex, VaultPalette palette, int blockSize,
			BlockPos nextLevelStairPos, VaultOvergrowthHelper.OvergrowthData overgrowthData) {

		// For level 0, add surface entrance and main staircase
		if (levelIndex == 0) {
			int chamberCeilingY = levelY + VaultMainChamberPiece.CHAMBER_HEIGHT;
			builder.addPiece(new VaultStaircasePiece(centerX, centerZ, surfaceY, levelY, chamberCeilingY, palette));
			builder.addPiece(new VaultEntrancePiece(centerX, centerZ, surfaceY, palette, blockSize));
		}

		// Generate room grid with maze-like connections
		RoomGrid grid = new RoomGrid(GRID_SIZE, random);

		// Track which grid position has the staircase to next level
		int stairGridX = -1, stairGridZ = -1;
		if (nextLevelStairPos != null) {
			stairGridX = Math.round((float)(nextLevelStairPos.getX() - centerX) / ROOM_SPACING) + GRID_SIZE / 2;
			stairGridZ = Math.round((float)(nextLevelStairPos.getZ() - centerZ) / ROOM_SPACING) + GRID_SIZE / 2;
		}

		// Create all rooms in the grid
		for (int gx = 0; gx < GRID_SIZE; gx++) {
			for (int gz = 0; gz < GRID_SIZE; gz++) {
				int roomX = centerX + (gx - GRID_SIZE / 2) * ROOM_SPACING;
				int roomZ = centerZ + (gz - GRID_SIZE / 2) * ROOM_SPACING;

				boolean isCenter = gx == GRID_SIZE / 2 && gz == GRID_SIZE / 2;
				boolean hasStaircaseDown = (gx == stairGridX && gz == stairGridZ);

				if (isCenter) {
					// Main chamber at center
					boolean[] corridors = new boolean[4];
					if (gx > 0 && grid.hasConnection(gx, gz, Direction.WEST)) corridors[Direction.WEST.get2DDataValue()] = true;
					if (gx < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.EAST)) corridors[Direction.EAST.get2DDataValue()] = true;
					if (gz > 0 && grid.hasConnection(gx, gz, Direction.NORTH)) corridors[Direction.NORTH.get2DDataValue()] = true;
					if (gz < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.SOUTH)) corridors[Direction.SOUTH.get2DDataValue()] = true;

					builder.addPiece(new VaultMainChamberPiece(new BlockPos(roomX, levelY, roomZ), palette, corridors, levelIndex, NUM_LEVELS, overgrowthData));
				} else if (grid.isRoomConnected(gx, gz)) {
					// Side room
					int roomSize = 10 + random.nextInt(4);

					// Build corridor directions for all connections
					boolean[] corridors = new boolean[4];
					if (gx > 0 && grid.hasConnection(gx, gz, Direction.WEST)) corridors[Direction.WEST.get2DDataValue()] = true;
					if (gx < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.EAST)) corridors[Direction.EAST.get2DDataValue()] = true;
					if (gz > 0 && grid.hasConnection(gx, gz, Direction.NORTH)) corridors[Direction.NORTH.get2DDataValue()] = true;
					if (gz < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.SOUTH)) corridors[Direction.SOUTH.get2DDataValue()] = true;

					// Pass staircase center if this room has one
					BlockPos staircaseCenter = hasStaircaseDown ? new BlockPos(roomX, levelY, roomZ) : null;
					builder.addPiece(new VaultSideRoomPiece(new BlockPos(roomX, levelY, roomZ), roomSize, levelY, palette, corridors, levelIndex, NUM_LEVELS, overgrowthData, staircaseCenter));

					// Add staircase down from this room if it's the designated stair room
					if (hasStaircaseDown && levelIndex < NUM_LEVELS - 1) {
						int nextLevelY = levelY - LEVEL_HEIGHT;
						int nextChamberCeilingY = nextLevelY + VaultMainChamberPiece.CHAMBER_HEIGHT;
						builder.addPiece(new VaultLevelStaircasePiece(roomX, roomZ, levelY, nextLevelY, nextChamberCeilingY, palette));
					}
				}
			}
		}

		// Add corridors between connected rooms
		int mainChamberEdge = VaultMainChamberPiece.CHAMBER_RADIUS + 3;
		int sideRoomEdge = 7;

		for (int gx = 0; gx < GRID_SIZE; gx++) {
			for (int gz = 0; gz < GRID_SIZE; gz++) {
				int roomX = centerX + (gx - GRID_SIZE / 2) * ROOM_SPACING;
				int roomZ = centerZ + (gz - GRID_SIZE / 2) * ROOM_SPACING;

				boolean isMainChamber = (gx == GRID_SIZE / 2 && gz == GRID_SIZE / 2);

				// Add corridor to east neighbor
				if (gx < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.EAST)) {
					boolean neighborIsMainChamber = (gx + 1 == GRID_SIZE / 2 && gz == GRID_SIZE / 2);

					int startOffset = isMainChamber ? mainChamberEdge : sideRoomEdge;
					int endOffset = neighborIsMainChamber ? mainChamberEdge : sideRoomEdge;
					int corridorLength = ROOM_SPACING - startOffset - endOffset;

					BlockPos corridorStart = new BlockPos(roomX + startOffset, levelY, roomZ);
					builder.addPiece(new VaultCorridorPiece(corridorStart, Direction.EAST, corridorLength, levelY, palette, levelIndex, NUM_LEVELS, overgrowthData));
				}

				// Add corridor to south neighbor
				if (gz < GRID_SIZE - 1 && grid.hasConnection(gx, gz, Direction.SOUTH)) {
					boolean neighborIsMainChamber = (gx == GRID_SIZE / 2 && gz + 1 == GRID_SIZE / 2);

					int startOffset = isMainChamber ? mainChamberEdge : sideRoomEdge;
					int endOffset = neighborIsMainChamber ? mainChamberEdge : sideRoomEdge;
					int corridorLength = ROOM_SPACING - startOffset - endOffset;

					BlockPos corridorStart = new BlockPos(roomX, levelY, roomZ + startOffset);
					builder.addPiece(new VaultCorridorPiece(corridorStart, Direction.SOUTH, corridorLength, levelY, palette, levelIndex, NUM_LEVELS, overgrowthData));
				}
			}
		}
	}

	@Override
	public StructureType<?> type() {
		return StructureInit.CITY_VAULT.get();
	}

	private static class RoomGrid {
		private final int size;
		private final Set<Long> connections = new HashSet<>();
		private final Set<Integer> connectedRooms = new HashSet<>();

		RoomGrid(int size, RandomSource random) {
			this.size = size;
			generateMaze(random);
		}

		private void generateMaze(RandomSource random) {
			int startX = size / 2;
			int startZ = size / 2;

			boolean[][] visited = new boolean[size][size];
			dfs(startX, startZ, visited, random);
		}

		private void dfs(int x, int z, boolean[][] visited, RandomSource random) {
			visited[x][z] = true;
			connectedRooms.add(x * size + z);

			Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
			for (int i = dirs.length - 1; i > 0; i--) {
				int j = random.nextInt(i + 1);
				Direction temp = dirs[i];
				dirs[i] = dirs[j];
				dirs[j] = temp;
			}

			for (Direction dir : dirs) {
				int nx = x + dir.getStepX();
				int nz = z + dir.getStepZ();

				if (nx >= 0 && nx < size && nz >= 0 && nz < size && !visited[nx][nz]) {
					addConnection(x, z, dir);
					dfs(nx, nz, visited, random);
				}
			}

			if (random.nextFloat() < 0.3f) {
				Direction dir = dirs[random.nextInt(dirs.length)];
				int nx = x + dir.getStepX();
				int nz = z + dir.getStepZ();
				if (nx >= 0 && nx < size && nz >= 0 && nz < size) {
					addConnection(x, z, dir);
				}
			}
		}

		private void addConnection(int x, int z, Direction dir) {
			connections.add(packConnection(x, z, dir));
			int nx = x + dir.getStepX();
			int nz = z + dir.getStepZ();
			connections.add(packConnection(nx, nz, dir.getOpposite()));
		}

		private long packConnection(int x, int z, Direction dir) {
			return ((long) x << 32) | ((long) z << 16) | dir.get2DDataValue();
		}

		boolean hasConnection(int x, int z, Direction dir) {
			return connections.contains(packConnection(x, z, dir));
		}

		boolean isRoomConnected(int x, int z) {
			return connectedRooms.contains(x * size + z);
		}
	}

	// ================================================================
	// STATIC VAULT POSITION CALCULATION (for exclusion zones)
	// ================================================================

	/**
	 * Calculate potential vault entrance positions near a given chunk without loading any chunks.
	 * Uses the same deterministic placement algorithm as Minecraft's RandomSpreadStructurePlacement.
	 *
	 * @param worldSeed The world seed
	 * @param chunkX Current chunk X
	 * @param chunkZ Current chunk Z
	 * @param spacing Structure spacing in chunks (from structure set config)
	 * @param separation Minimum separation in chunks
	 * @param salt Structure salt for randomization
	 * @param searchRadius How many regions around the current position to check
	 * @return List of block positions where vault entrances would be centered
	 */
	public static List<BlockPos> calculateNearbyVaultPositions(long worldSeed, int chunkX, int chunkZ,
			int spacing, int separation, int salt, int searchRadius) {
		List<BlockPos> positions = new ArrayList<>();

		// Find which region the current chunk is in
		int regionX = Math.floorDiv(chunkX, spacing);
		int regionZ = Math.floorDiv(chunkZ, spacing);

		// Check nearby regions
		for (int rx = regionX - searchRadius; rx <= regionX + searchRadius; rx++) {
			for (int rz = regionZ - searchRadius; rz <= regionZ + searchRadius; rz++) {
				// Calculate the vault position in this region using deterministic random
				// This replicates Minecraft's RandomSpreadStructurePlacement algorithm
				long regionSeed = worldSeed + (long) rx * 341873128712L + (long) rz * 132897987541L + (long) salt;
				RandomSource regionRandom = RandomSource.create(regionSeed);

				// Get offset within the region (between 0 and spacing-separation)
				int range = spacing - separation;
				int offsetX = range > 0 ? regionRandom.nextInt(range) : 0;
				int offsetZ = range > 0 ? regionRandom.nextInt(range) : 0;

				// Calculate the structure chunk position
				int structureChunkX = rx * spacing + offsetX;
				int structureChunkZ = rz * spacing + offsetZ;

				// Convert to block position (center of chunk)
				int centerX = (structureChunkX << 4) + 8;
				int centerZ = (structureChunkZ << 4) + 8;

				positions.add(new BlockPos(centerX, 0, centerZ));
			}
		}

		return positions;
	}

	/**
	 * Size of the vault entrance area that should be excluded from city generation.
	 * This is BUILDING_SIZE + pathLength from VaultEntrancePiece.
	 */
	public static final int ENTRANCE_EXCLUSION_RADIUS = 27; // 7 (building) + 20 (path)
}
