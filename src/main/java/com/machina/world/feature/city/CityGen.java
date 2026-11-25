package com.machina.world.feature.city;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityGrid;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityOvergrown;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeOvergrownGround;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeOvergrownVine;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.registration.init.RegistryInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * All city generation logic: roads and buildings. Deterministic road grid based
 * on chunk coordinates. Buildings procedurally generated with weathering.
 */
public class CityGen {

	// ================================================================
	// CHUNK TYPE DETERMINATION (pure math, no state)
	// ================================================================

	/**
	 * Is this chunk on a road line along the Z axis? (Road runs north-south at this
	 * X column of chunks)
	 */
	public static boolean isZRoadChunk(int chunkX, int roadSpacing) {
		return Math.floorMod(chunkX, roadSpacing) == 0;
	}

	/**
	 * Is this chunk on a road line along the X axis? (Road runs east-west at this Z
	 * row of chunks)
	 */
	public static boolean isXRoadChunk(int chunkZ, int roadSpacing) {
		return Math.floorMod(chunkZ, roadSpacing) == 0;
	}

	// ================================================================
	// ROAD GENERATION
	// ================================================================

	// [0]=smooth, [1]=slopeCheck, [2]=headroom, [3]=bridgeThreshold,
	// [4]=tunnelThreshold, [5]=maxHeightDiff
	private static final ThreadLocal<int[]> ROAD_CFG = ThreadLocal.withInitial(() -> new int[] { 8, 4, 4, 2, 2, 8 });

	public static void generateRoads(WorldGenLevel level, int chunkX, int chunkZ, boolean hasZRoad, boolean hasXRoad,
			List<BlockState> roadBlocks, int roadWidth, RandomSource random, int roadSmooth, int slopeCheck,
			int headroom, int bridgeThreshold, int tunnelThreshold, int maxHeightDiff) {

		setChunkBounds(chunkX, chunkZ);
		ROAD_CFG.get()[0] = roadSmooth;
		ROAD_CFG.get()[1] = slopeCheck;
		ROAD_CFG.get()[2] = headroom;
		ROAD_CFG.get()[3] = bridgeThreshold;
		ROAD_CFG.get()[4] = tunnelThreshold;
		ROAD_CFG.get()[5] = maxHeightDiff;

		int baseX = chunkX << 4;
		int baseZ = chunkZ << 4;
		int roadStart = Math.max(0, (16 - roadWidth) / 2);
		int roadEnd = Math.min(16, roadStart + roadWidth);

		int[] zH = null, xH = null;

		if (hasZRoad) {
			int cx = baseX + (roadStart + roadEnd) / 2;
			zH = computeRoadHeights(level, cx, baseZ, 0, 1);
		}
		if (hasXRoad) {
			int cz = baseZ + (roadStart + roadEnd) / 2;
			xH = computeRoadHeights(level, baseX, cz, 1, 0);
		}

		if (hasZRoad && hasXRoad) {
			for (int lz = 0; lz < 16; lz++) {
				for (int lx = 0; lx < 16; lx++) {
					boolean inZ = lx >= roadStart && lx < roadEnd;
					boolean inX = lz >= roadStart && lz < roadEnd;
					if (!inZ && !inX)
						continue;

					int wx = baseX + lx;
					int wz = baseZ + lz;
					boolean overlap = inZ && inX;

					int roadY;
					boolean isEdge;
					boolean isPillarPos;

					if (overlap) {
						int zh = heightAt(zH, lz), xh = heightAt(xH, lx);
						if (Math.abs(zh - xh) > 3)
							continue;
						roadY = Math.max(zh, xh);
						isEdge = false;
						isPillarPos = (lz % 4 == 0) || (lx % 4 == 0);
					} else if (inZ) {
						if (isTooSteep(zH, lz))
							continue;
						roadY = heightAt(zH, lz);
						isEdge = (lx == roadStart || lx == roadEnd - 1);
						isPillarPos = (lz % 4 == 0);
						int edgeDist = Math.min(lx - roadStart, roadEnd - 1 - lx);
						if (isRoadEdgeEroded(wx, wz, edgeDist))
							continue;
					} else {
						if (isTooSteep(xH, lx))
							continue;
						roadY = heightAt(xH, lx);
						isEdge = (lz == roadStart || lz == roadEnd - 1);
						isPillarPos = (lx % 4 == 0);
						int edgeDist = Math.min(lz - roadStart, roadEnd - 1 - lz);
						if (isRoadEdgeEroded(wx, wz, edgeDist))
							continue;
					}

					placeRoadColumn(level, wx, wz, roadY, isEdge, isPillarPos, roadBlocks, random);

					// Street furniture at edges
					if (isEdge && isPillarPos) {
						placeStreetFurniture(level, wx, wz, roadY, random, inZ ? Direction.NORTH : Direction.EAST);
					}
				}
			}
		} else if (hasZRoad) {
			for (int lz = 0; lz < 16; lz++) {
				if (isTooSteep(zH, lz))
					continue;
				int roadY = heightAt(zH, lz);
				for (int lx = roadStart; lx < roadEnd; lx++) {
					int wx = baseX + lx;
					int wz = baseZ + lz;
					int edgeDist = Math.min(lx - roadStart, roadEnd - 1 - lx);
					if (isRoadEdgeEroded(wx, wz, edgeDist))
						continue;
					boolean isEdge = (lx == roadStart || lx == roadEnd - 1);
					placeRoadColumn(level, wx, wz, roadY, isEdge, (lz % 4 == 0), roadBlocks, random);

					// Street furniture at edges
					if (isEdge && (lz % 8 == 0)) {
						placeStreetFurniture(level, wx, wz, roadY, random, Direction.NORTH);
					}
				}
			}
		} else {
			for (int lx = 0; lx < 16; lx++) {
				if (isTooSteep(xH, lx))
					continue;
				int roadY = heightAt(xH, lx);
				for (int lz = roadStart; lz < roadEnd; lz++) {
					int wx = baseX + lx;
					int wz = baseZ + lz;
					int edgeDist = Math.min(lz - roadStart, roadEnd - 1 - lz);
					if (isRoadEdgeEroded(wx, wz, edgeDist))
						continue;
					boolean isEdge = (lz == roadStart || lz == roadEnd - 1);
					placeRoadColumn(level, wx, wz, roadY, isEdge, (lx % 4 == 0), roadBlocks, random);

					// Street furniture at edges
					if (isEdge && (lx % 8 == 0)) {
						placeStreetFurniture(level, wx, wz, roadY, random, Direction.EAST);
					}
				}
			}
		}
	}

	private static void placeStreetFurniture(WorldGenLevel level, int x, int z, int roadY, RandomSource random,
			Direction roadDir) {
		float choice = random.nextFloat();

		// Offset perpendicular to road direction
		int offX = roadDir == Direction.NORTH ? (random.nextBoolean() ? -1 : 1) : 0;
		int offZ = roadDir == Direction.EAST ? (random.nextBoolean() ? -1 : 1) : 0;
		int fx = x + offX;
		int fz = z + offZ;

		if (choice < 0.35f) {
			// Street light (lamp post)
			placeStreetLight(level, fx, roadY + 1, fz, random);
		} else if (choice < 0.55f) {
			// Debris pile
			placeDebris(level, fx, roadY + 1, fz, random);
		} else if (choice < 0.70f) {
			// Barrier
			placeBarrier(level, fx, roadY + 1, fz, roadDir, random);
		}
	}

	private static final BlockState ANTHRACITE_WALL = net.minecraft.core.registries.BuiltInRegistries.BLOCK
			.get(net.minecraft.resources.ResourceLocation.parse("machina:anthracite_wall")).defaultBlockState();
	private static final BlockState ANTHRACITE = net.minecraft.core.registries.BuiltInRegistries.BLOCK
			.get(net.minecraft.resources.ResourceLocation.parse("machina:anthracite")).defaultBlockState();
	private static final BlockState ANTHRACITE_SLAB = net.minecraft.core.registries.BuiltInRegistries.BLOCK
			.get(net.minecraft.resources.ResourceLocation.parse("machina:anthracite_slab")).defaultBlockState();
	private static final BlockState BASALT = net.minecraft.core.registries.BuiltInRegistries.BLOCK
			.get(net.minecraft.resources.ResourceLocation.parse("machina:basalt")).defaultBlockState();
	private static final BlockState BASALT_SLAB = net.minecraft.core.registries.BuiltInRegistries.BLOCK
			.get(net.minecraft.resources.ResourceLocation.parse("machina:basalt_slab")).defaultBlockState();

	private static void placeStreetLight(WorldGenLevel level, int x, int y, int z, RandomSource random) {
		if (!inBounds(x, z))
			return;

		int height = 3 + random.nextInt(4);
		boolean damaged = random.nextFloat() < 0.4f;
		int actualHeight = damaged ? 1 + random.nextInt(height) : height;

		// Anthracite wall base
		BlockPos basePos = new BlockPos(x, y, z);
		level.setBlock(basePos, ANTHRACITE_WALL, 2);

		// Iron bar post
		for (int dy = 1; dy < actualHeight; dy++) {
			BlockPos pos = new BlockPos(x, y + dy, z);
			level.setBlock(pos, Blocks.IRON_BARS.defaultBlockState(), 2);
		}
	}

	private static void placeDebris(WorldGenLevel level, int x, int y, int z, RandomSource random) {
		int count = 1 + random.nextInt(4);
		for (int i = 0; i < count; i++) {
			int dx = random.nextInt(3) - 1;
			int dz = random.nextInt(3) - 1;
			int wx = x + dx;
			int wz = z + dz;
			if (!inBounds(wx, wz))
				continue;

			BlockPos pos = new BlockPos(wx, y, wz);

			float choice = random.nextFloat();
			BlockState block;
			if (choice < 0.3f) {
				block = ANTHRACITE;
			} else if (choice < 0.5f) {
				block = BASALT;
			} else if (choice < 0.7f) {
				block = ANTHRACITE_SLAB;
			} else {
				block = BASALT_SLAB;
			}
			level.setBlock(pos, block, 2);
		}
	}

	private static void placeBarrier(WorldGenLevel level, int x, int y, int z, Direction facing, RandomSource random) {
		int length = 2 + random.nextInt(3);
		int dx = facing == Direction.NORTH || facing == Direction.SOUTH ? 0 : 1;
		int dz = facing == Direction.EAST || facing == Direction.WEST ? 0 : 1;

		for (int i = 0; i < length; i++) {
			if (random.nextFloat() < 0.2f)
				continue;
			int wx = x + dx * i;
			int wz = z + dz * i;
			if (!inBounds(wx, wz))
				continue;

			BlockPos pos = new BlockPos(wx, y, wz);

			// Use anthracite wall blocks for barriers
			BlockState wall = ANTHRACITE_WALL;
			// Connect walls
			if (i > 0)
				wall = wall.setValue(WallBlock.WEST_WALL, dx != 0 ? WallSide.LOW : WallSide.NONE)
						.setValue(WallBlock.NORTH_WALL, dz != 0 ? WallSide.LOW : WallSide.NONE);
			if (i < length - 1)
				wall = wall.setValue(WallBlock.EAST_WALL, dx != 0 ? WallSide.LOW : WallSide.NONE)
						.setValue(WallBlock.SOUTH_WALL, dz != 0 ? WallSide.LOW : WallSide.NONE);
			level.setBlock(pos, wall, 2);
		}
	}

	private static int[] computeRoadHeights(WorldGenLevel level, int startX, int startZ, int dx, int dz) {
		int[] cfg = ROAD_CFG.get();
		int smooth = cfg[0];
		int slopeCheck = cfg[1];
		int len = 16 + 2 * slopeCheck;
		int[] h = new int[len];
		int diam = 2 * smooth + 1;
		int refH = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, startX + 8 * dx, startZ + 8 * dz) - 1;

		for (int i = 0; i < len; i++) {
			int pos = i - slopeCheck;
			int x = startX + pos * dx;
			int z = startZ + pos * dz;
			float sum = 0;
			for (int j = -smooth; j <= smooth; j++) {
				int t = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x + j * dx, z + j * dz) - 1;
				if (t < refH - 50)
					t = refH;
				sum += t;
			}
			h[i] = Math.round(sum / diam);
		}
		return h;
	}

	private static int heightAt(int[] h, int pos) {
		return h[pos + ROAD_CFG.get()[1]];
	}

	private static boolean isTooSteep(int[] h, int pos) {
		int slopeCheck = ROAD_CFG.get()[1];
		int idx = pos + slopeCheck;
		int here = h[idx];
		for (int d = 1; d <= slopeCheck; d++) {
			if (idx - d >= 0 && Math.abs(here - h[idx - d]) > 1)
				return true;
			if (idx + d < h.length && Math.abs(here - h[idx + d]) > 1)
				return true;
		}
		return false;
	}

	private static void placeRoadColumn(WorldGenLevel level, int x, int z, int roadY, boolean isEdge,
			boolean isPillarPos, List<BlockState> roadBlocks, RandomSource random) {

		if (!inBounds(x, z))
			return;

		int[] cfg = ROAD_CFG.get();
		int headroom = cfg[2];
		int bridgeThreshold = cfg[3];
		int tunnelThreshold = cfg[4];
		int maxHeightDiff = cfg[5];

		int terrainY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
		if (roadY < level.getMinBuildHeight() + 1)
			return;

		// Skip if terrain is water
		if (!level.getFluidState(new BlockPos(x, terrainY, z)).isEmpty())
			return;

		int diff = roadY - terrainY;
		if (Math.abs(diff) > maxHeightDiff)
			return;
		if (random.nextFloat() < 0.015f)
			return;

		BlockState roadBlock = roadBlocks.get(random.nextInt(roadBlocks.size()));

		if (diff > bridgeThreshold) {
			level.setBlock(new BlockPos(x, roadY, z), roadBlock, 2);
			if (isEdge && isPillarPos) {
				for (int y = roadY - 1; y >= terrainY; y--)
					level.setBlock(new BlockPos(x, y, z), roadBlock, 2);
			}
			clearAbove(level, x, roadY + 1, z, headroom);

		} else if (diff < -tunnelThreshold) {
			level.setBlock(new BlockPos(x, roadY, z), roadBlock, 2);
			fillDown(level, x, roadY - 1, z, roadBlock);
			for (int dy = 1; dy <= headroom; dy++)
				level.setBlock(new BlockPos(x, roadY + dy, z), Blocks.AIR.defaultBlockState(), 2);
			if (roadY + headroom + 1 < terrainY)
				level.setBlock(new BlockPos(x, roadY + headroom + 1, z), roadBlock, 2);
			if (isEdge) {
				for (int dy = 1; dy <= headroom; dy++)
					level.setBlock(new BlockPos(x, roadY + dy, z), roadBlock, 2);
			}

		} else {
			level.setBlock(new BlockPos(x, roadY, z), roadBlock, 2);
			fillDown(level, x, roadY - 1, z, roadBlock);
			clearAbove(level, x, roadY + 1, z, headroom);
		}
	}

	@SuppressWarnings("deprecation")
	private static void fillDown(WorldGenLevel level, int x, int startY, int z, BlockState block) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, startY, z);
		for (int i = 0; i < 48 && pos.getY() > level.getMinBuildHeight(); i++) {
			if (!level.getBlockState(pos).isSolid()) {
				level.setBlock(pos, block, 2);
				pos.move(0, -1, 0);
			} else {
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void clearAbove(WorldGenLevel level, int x, int startY, int z, int count) {
		for (int dy = 0; dy < count; dy++) {
			BlockPos pos = new BlockPos(x, startY + dy, z);
			if (pos.getY() >= level.getMaxBuildHeight())
				break;
			if (level.getBlockState(pos).isSolid())
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		}
	}

	private static boolean isRoadEdgeEroded(int wx, int wz, int edgeDist) {
		if (edgeDist >= 4)
			return false;
		int noise = wx * 374761393 + wz * 1274126177;
		noise ^= noise >>> 16;
		int jx = wx + (noise & 3) - 1;
		int jz = wz + ((noise >> 3) & 3) - 1;
		int px = Math.floorDiv(jx, 4);
		int pz = Math.floorDiv(jz, 4);
		int hash = px * 73856093 ^ pz * 83492791;
		hash ^= hash >>> 16;
		float t = (4 - edgeDist) / 5.0f;
		return (hash & 0xFF) < (int) (t * t * 256);
	}

	// ================================================================
	// BUILDING GENERATION
	// ================================================================

	private static final int BUILDING_CELL_SIZE = 16;

	private static final ThreadLocal<PlanetBiomeCityGrid> CITY_CONFIG = new ThreadLocal<>();
	private static final ThreadLocal<Float> HEIGHT_MULTIPLIER = ThreadLocal.withInitial(() -> 1.0f);

	public static void generateBuildings(WorldGenLevel level, int chunkX, int chunkZ, List<PlanetBiomeRuin> ruins,
			List<BlockState> gradientBlocks, List<BlockState> accentBlocks, float accentChance, long worldSeed,
			RandomSource random, int roadSpacing, int roadWidth,
			PlanetBiomeCityOvergrown overgrown, PlanetBiomeCityGrid cityConfig) {

		if (ruins.isEmpty() || gradientBlocks.isEmpty())
			return;
		setChunkBounds(chunkX, chunkZ);
		GRADIENT_BLOCKS.set(gradientBlocks);
		ACCENT_BLOCKS.set(accentBlocks);
		ACCENT_CHANCE.set(accentChance);
		OVERGROWN_CONFIG.set(overgrown);
		CITY_CONFIG.set(cityConfig);

		int baseX = chunkX << 4;
		int baseZ = chunkZ << 4;

		// Find all building cells that could affect this chunk
		// Building origin within cell (16 blocks) + building extends up to 28 blocks
		// But limit to 48 blocks to avoid chunk access errors (3 chunks away is usually safe)
		int lookupRadius = 48;
		int cellMinX = Math.floorDiv(baseX - lookupRadius, BUILDING_CELL_SIZE);
		int cellMaxX = Math.floorDiv(baseX + 15 + lookupRadius, BUILDING_CELL_SIZE);
		int cellMinZ = Math.floorDiv(baseZ - lookupRadius, BUILDING_CELL_SIZE);
		int cellMaxZ = Math.floorDiv(baseZ + 15 + lookupRadius, BUILDING_CELL_SIZE);

		for (int cellX = cellMinX; cellX <= cellMaxX; cellX++) {
			for (int cellZ = cellMinZ; cellZ <= cellMaxZ; cellZ++) {
				generateBuildingsInCell(level, cellX, cellZ, worldSeed, ruins, roadSpacing);
			}
		}
	}

	private static float getUrbanIntensity(int worldX, int worldZ, long worldSeed, int noiseScale) {
		// Large-scale simplex-like noise for urban intensity (0 = suburbs, 1 = city center)
		double nx = (double) worldX / noiseScale;
		double nz = (double) worldZ / noiseScale;

		// Simple hash-based smooth noise (like value noise)
		int ix = (int) Math.floor(nx);
		int iz = (int) Math.floor(nz);
		float fx = (float) (nx - ix);
		float fz = (float) (nz - iz);

		// Smoothstep interpolation
		fx = fx * fx * (3 - 2 * fx);
		fz = fz * fz * (3 - 2 * fz);

		// Hash corners
		float c00 = hashToFloat(ix, iz, worldSeed);
		float c10 = hashToFloat(ix + 1, iz, worldSeed);
		float c01 = hashToFloat(ix, iz + 1, worldSeed);
		float c11 = hashToFloat(ix + 1, iz + 1, worldSeed);

		// Bilinear interpolation
		float v0 = c00 + fx * (c10 - c00);
		float v1 = c01 + fx * (c11 - c01);
		return v0 + fz * (v1 - v0);
	}

	private static float hashToFloat(int x, int z, long seed) {
		long h = seed ^ ((long) x * 374761393L + (long) z * 668265263L);
		h = (h ^ (h >>> 33)) * 0xff51afd7ed558ccdL;
		h = (h ^ (h >>> 33)) * 0xc4ceb9fe1a85ec53L;
		return (float) ((h & 0xFFFFFFFFL) / (double) 0xFFFFFFFFL);
	}

	private static void generateBuildingsInCell(WorldGenLevel level, int cellX, int cellZ, long worldSeed,
			List<PlanetBiomeRuin> ruins, int roadSpacing) {

		PlanetBiomeCityGrid cfg = CITY_CONFIG.get();
		var urban = cfg.urban();

		// Deterministic seed for this cell
		long cellSeed = worldSeed ^ ((long) cellX * 341873128712L + (long) cellZ * 132897987541L);
		RandomSource cellRandom = RandomSource.create(cellSeed);

		int cellBaseX = cellX * BUILDING_CELL_SIZE;
		int cellBaseZ = cellZ * BUILDING_CELL_SIZE;

		// Skip if this cell center is on a road
		int cellChunkX = Math.floorDiv(cellBaseX + BUILDING_CELL_SIZE / 2, 16);
		int cellChunkZ = Math.floorDiv(cellBaseZ + BUILDING_CELL_SIZE / 2, 16);
		if (isZRoadChunk(cellChunkX, roadSpacing) || isXRoadChunk(cellChunkZ, roadSpacing)) {
			return;
		}

		// Compute urban intensity for this cell (0 = suburbs, 1 = city center)
		float urbanIntensity = getUrbanIntensity(cellBaseX, cellBaseZ, worldSeed, urban.noise_scale());

		// Check if entire city block is a toxic garden (determined per-block, not per-cell)
		int blockSize = cfg.block_size();
		int cityBlockX = Math.floorDiv(cellBaseX, blockSize);
		int cityBlockZ = Math.floorDiv(cellBaseZ, blockSize);
		long blockSeed = worldSeed ^ ((long) cityBlockX * 198491317L + (long) cityBlockZ * 6542989L);
		float blockRandom = hashToFloat(cityBlockX, cityBlockZ, blockSeed);
		float toxicChance = urban.toxic_garden_chance() * (1.5f - urbanIntensity);

		// Check if cell center is in water - skip garden/buildings if so
		int cellCenterX = cellBaseX + BUILDING_CELL_SIZE / 2;
		int cellCenterZ = cellBaseZ + BUILDING_CELL_SIZE / 2;
		int cellCenterY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, cellCenterX, cellCenterZ);
		if (!level.getFluidState(new BlockPos(cellCenterX, cellCenterY, cellCenterZ)).isEmpty()) {
			return; // Skip entire cell if center is water
		}

		if (blockRandom < toxicChance) {
			// This entire city block is a toxic garden - generate patches but no buildings
			generateToxicGarden(level, cellBaseX, cellBaseZ, cellRandom);

			// Try to spawn a tree at the center of this city block (only from the cell containing the center)
			int blockCenterX = cityBlockX * blockSize + blockSize / 2;
			int blockCenterZ = cityBlockZ * blockSize + blockSize / 2;
			if (cellBaseX <= blockCenterX && blockCenterX < cellBaseX + BUILDING_CELL_SIZE &&
				cellBaseZ <= blockCenterZ && blockCenterZ < cellBaseZ + BUILDING_CELL_SIZE) {
				// This cell contains the block center - try to spawn a tree
				float treeRandom = hashToFloat(cityBlockX + 1000, cityBlockZ + 1000, blockSeed);
				if (treeRandom < urban.garden_tree_chance() && !urban.garden_trees().isEmpty()) {
					// Pick a random tree from the list
					PlanetBiomeTree treeCfg = urban.garden_trees().get(cellRandom.nextInt(urban.garden_trees().size()));
					spawnGardenTree(level, blockCenterX, blockCenterZ, treeCfg, cellRandom);
				}
			}
			return;
		}

		// Building count varies with urban intensity
		int minBuildings = Math.round(urban.suburb_min_buildings() + urbanIntensity * (urban.center_min_buildings() - urban.suburb_min_buildings()));
		int maxBuildings = Math.round(urban.suburb_max_buildings() + urbanIntensity * (urban.center_max_buildings() - urban.suburb_max_buildings()));
		int buildingCount = minBuildings + (maxBuildings > minBuildings ? cellRandom.nextInt(maxBuildings - minBuildings + 1) : 0);

		// Height multiplier varies with urban intensity
		float heightMult = urban.suburb_height_mult() + urbanIntensity * (urban.center_height_mult() - urban.suburb_height_mult());
		HEIGHT_MULTIPLIER.set(heightMult);

		int[] cb = CHUNK_BOUNDS.get();
		int chunkCenterX = (cb[0] + cb[1]) / 2;
		int chunkCenterZ = (cb[2] + cb[3]) / 2;
		int roadBuffer = urban.road_buffer();

		for (int i = 0; i < buildingCount; i++) {
			int localX = roadBuffer + cellRandom.nextInt(Math.max(1, BUILDING_CELL_SIZE - roadBuffer * 2 - 4));
			int localZ = roadBuffer + cellRandom.nextInt(Math.max(1, BUILDING_CELL_SIZE - roadBuffer * 2 - 4));
			int wx = cellBaseX + localX;
			int wz = cellBaseZ + localZ;

			// Pick ruin type deterministically
			PlanetBiomeRuin ruin = ruins.get(cellRandom.nextInt(ruins.size()));
			if (cellRandom.nextFloat() > ruin.chance())
				continue;

			// Check if building origin is within safe query range (2 chunks = 32 blocks)
			int distX = Math.abs(wx - chunkCenterX);
			int distZ = Math.abs(wz - chunkCenterZ);
			if (distX > 32 || distZ > 32) {
				// Too far for safe heightmap query - run generator with dummy origin to maintain random state
				BlockPos dummyOrigin = new BlockPos(wx, 64, wz);
				switch (ruin.shape()) {
				case TUBE -> generateTubeVariant(level, dummyOrigin, ruin, cellRandom);
				case PILLAR -> generatePillarVariant(level, dummyOrigin, ruin, cellRandom);
				case WALL -> generateWallVariant(level, dummyOrigin, ruin, cellRandom);
				case FOUNDATION -> generateFoundationVariant(level, dummyOrigin, ruin, cellRandom);
				case RUBBLE -> generateRubbleVariant(level, dummyOrigin, ruin, cellRandom);
				}
				continue;
			}

			int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);
			BlockPos origin = new BlockPos(wx, surfaceY, wz);

			// Skip if no sky access (prevents buildings inside underground structures)
			if (!level.canSeeSky(origin.above())) {
				BlockPos dummyOrigin = new BlockPos(wx, 64, wz);
				switch (ruin.shape()) {
				case TUBE -> generateTubeVariant(level, dummyOrigin, ruin, cellRandom);
				case PILLAR -> generatePillarVariant(level, dummyOrigin, ruin, cellRandom);
				case WALL -> generateWallVariant(level, dummyOrigin, ruin, cellRandom);
				case FOUNDATION -> generateFoundationVariant(level, dummyOrigin, ruin, cellRandom);
				case RUBBLE -> generateRubbleVariant(level, dummyOrigin, ruin, cellRandom);
				}
				continue;
			}

			// Skip if building origin is in water
			if (!level.getFluidState(origin).isEmpty() || !level.getFluidState(origin.below()).isEmpty()) {
				// Still run generator with dummy to maintain random state
				BlockPos dummyOrigin = new BlockPos(wx, 64, wz);
				switch (ruin.shape()) {
				case TUBE -> generateTubeVariant(level, dummyOrigin, ruin, cellRandom);
				case PILLAR -> generatePillarVariant(level, dummyOrigin, ruin, cellRandom);
				case WALL -> generateWallVariant(level, dummyOrigin, ruin, cellRandom);
				case FOUNDATION -> generateFoundationVariant(level, dummyOrigin, ruin, cellRandom);
				case RUBBLE -> generateRubbleVariant(level, dummyOrigin, ruin, cellRandom);
				}
				continue;
			}

			// Run the generator - random state stays consistent, inBounds() clips to current chunk
			switch (ruin.shape()) {
			case TUBE -> generateTubeVariant(level, origin, ruin, cellRandom);
			case PILLAR -> generatePillarVariant(level, origin, ruin, cellRandom);
			case WALL -> generateWallVariant(level, origin, ruin, cellRandom);
			case FOUNDATION -> generateFoundationVariant(level, origin, ruin, cellRandom);
			case RUBBLE -> generateRubbleVariant(level, origin, ruin, cellRandom);
			}
		}
	}

	private static void generateToxicGarden(WorldGenLevel level, int baseX, int baseZ, RandomSource r) {
		// Generate patches of ash and toxic dirt as a "toxic garden" with decorations
		BlockState ash = net.minecraft.core.registries.BuiltInRegistries.BLOCK
				.get(net.minecraft.resources.ResourceLocation.parse("machina:ash")).defaultBlockState();
		BlockState toxicDirt = net.minecraft.core.registries.BuiltInRegistries.BLOCK
				.get(net.minecraft.resources.ResourceLocation.parse("machina:toxic_sand")).defaultBlockState();
		BlockState toxShroom = net.minecraft.core.registries.BuiltInRegistries.BLOCK
				.get(net.minecraft.resources.ResourceLocation.parse("machina:tox_shroom")).defaultBlockState();
		BlockState toxicGrass = net.minecraft.core.registries.BuiltInRegistries.BLOCK
				.get(net.minecraft.resources.ResourceLocation.parse("machina:toxic_grass")).defaultBlockState();

		int numPatches = 4 + r.nextInt(4);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int p = 0; p < numPatches; p++) {
			int cx = baseX + 3 + r.nextInt(BUILDING_CELL_SIZE - 6);
			int cz = baseZ + 3 + r.nextInt(BUILDING_CELL_SIZE - 6);
			int radius = 4 + r.nextInt(4);
			BlockState patchBlock = r.nextBoolean() ? ash : toxicDirt;

			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					if (dx * dx + dz * dz > radius * radius) continue;
					if (r.nextFloat() < 0.15f) continue;

					int wx = cx + dx;
					int wz = cz + dz;
					if (!inBounds(wx, wz)) continue;

					int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);
					pos.set(wx, surfaceY, wz);

					// Skip if in water
					if (!level.getFluidState(pos).isEmpty()) continue;

					level.setBlock(pos, patchBlock, 2);

					// Add decorations on top
					if (r.nextFloat() < 0.15f) {
						pos.set(wx, surfaceY + 1, wz);
						level.setBlock(pos, r.nextFloat() < 0.3f ? toxShroom : toxicGrass, 2);
					}
				}
			}
		}
	}

	private static void spawnGardenTree(WorldGenLevel level, int centerX, int centerZ, PlanetBiomeTree treeCfg, RandomSource r) {
		int[] cb = CHUNK_BOUNDS.get();

		// Snap tree position to center of current chunk to avoid cross-chunk issues
		// Trees can extend ~10 blocks, so placing at chunk center (8,8) keeps them in bounds
		int chunkBaseX = cb[0];
		int chunkBaseZ = cb[2];
		int treeX = chunkBaseX + 8;
		int treeZ = chunkBaseZ + 8;

		// Only spawn if the original center was meant for this chunk
		// (prevents duplicate trees from adjacent chunks)
		int targetChunkX = centerX >> 4;
		int targetChunkZ = centerZ >> 4;
		int currentChunkX = chunkBaseX >> 4;
		int currentChunkZ = chunkBaseZ >> 4;
		if (targetChunkX != currentChunkX || targetChunkZ != currentChunkZ) {
			return;
		}

		TreeMaker maker = RegistryInit.TREE.get(treeCfg.tree());
		if (maker == null) return;

		int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, treeX, treeZ);
		BlockPos origin = new BlockPos(treeX, surfaceY, treeZ);

		if (!maker.allowsWaterPlacement() && !level.getFluidState(origin).isEmpty()) {
			return;
		}

		SDF tree = maker.build(treeCfg, r, level, origin);
		if (tree == null) return;

		if (r.nextFloat() < treeCfg.tree_fruit_chance()) {
			BlockState fruit = MathUtil.randomInList(treeCfg.fruit(), r);
			tree = tree.addPostProcesses(maker.fruit(r, treeCfg, fruit));
		}

		tree.fillRecursive(level, origin);
	}

	// --- TUBE variants: tall hollow buildings ---

	private static void generateTubeVariant(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		// Favor full 4-wall buildings (skyscraper, cylinder, office block, stepped
		// tower)
		if (v < 0.30f)
			generateSkyscraper(level, origin, c, r);
		else if (v < 0.45f)
			generateCylinder(level, origin, c, r);
		else if (v < 0.65f)
			generateOfficeBlock(level, origin, c, r);
		else if (v < 0.85f)
			generateSteppedTower(level, origin, c, r);
		else if (v < 0.93f)
			generateUBuilding(level, origin, c, r);
		else
			generatePlusBuilding(level, origin, c, r);
	}

	private static void generateSkyscraper(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);

		// Get overgrown config
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();

		// Pick a door position
		int doorSide = r.nextInt(4);
		int doorPos = 2 + r.nextInt(Math.max(1, Math.min(w, d) - 4));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				boolean corner = (x == 0 || x == w - 1) && (z == 0 || z == d - 1);

				int colH = columnHeight(h, c, r);

				// Check if this is a door position
				boolean isDoor = false;
				if (wall && !corner) {
					if ((doorSide == 0 && z == 0 && Math.abs(x - doorPos) <= 1)
							|| (doorSide == 1 && x == w - 1 && Math.abs(z - doorPos) <= 1)
							|| (doorSide == 2 && z == d - 1 && Math.abs(x - doorPos) <= 1)
							|| (doorSide == 3 && x == 0 && Math.abs(z - doorPos) <= 1)) {
						isDoor = true;
					}
				}

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;

					// Door opening (first 3 blocks high)
					if (isDoor && y > 0 && y < 4) {
						continue;
					}

					if (!wall) {
						// Floor block
						if (y % floorH == 0) {
							placeBlock(level, origin, x, y, z, c, r);
						}
						continue;
					}

					boolean isWindowLevel = y % floorH > 0 && y % floorH < floorH - 1;
					boolean isWindow = false;

					// Windows: regular pattern on walls, not at corners
					if (isWindowLevel && !corner && y > 4) {
						int windowSpacing = 3 + r.nextInt(2);
						boolean onWindowCol = (x % windowSpacing == 1) || (z % windowSpacing == 1);
						if (onWindowCol && r.nextFloat() < 0.7f) {
							isWindow = true;
						}
					}

					// Damaged windows higher up
					if (isWindowLevel && !corner && (float) y / colH > 0.6f && r.nextFloat() < 0.25f) {
						continue;
					}

					if (isWindow) {
						// Place glass or leave empty for broken windows
						if (r.nextFloat() < 0.6f) {
							placeWindowBlock(level, origin, x, y, z);
						}
					} else {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 4, c, r);

		// Apply overgrown vegetation after building structure is complete
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin, w, d, h, floorH, overgrown, r);
		}
	}

	private static void placeWindowBlock(WorldGenLevel level, BlockPos origin, int ox, int oy, int oz) {
		int wx = origin.getX() + ox;
		int wz = origin.getZ() + oz;
		if (!inBounds(wx, wz))
			return;
		int wy = origin.getY() + oy;
		BlockPos.MutableBlockPos pos = MUTABLE_POS.get();
		pos.set(wx, wy, wz);
		level.setBlock(pos, Blocks.TINTED_GLASS.defaultBlockState(), 2);
	}

	private static void generateCylinder(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int radius = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (radius < 5)
			radius = 5;
		int h = randRange((int) (c.min_height() * 1.1f), (int) (c.max_height() * 1.2f), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				double dist = Math.sqrt(x * x + z * z);
				boolean shell = dist <= radius && dist > radius - 1.5;
				if (!shell)
					continue;

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;

					// Windows in cylindrical pattern
					boolean isWindowLevel = y % floorH > 0 && y % floorH < floorH - 1;
					double angle = Math.atan2(z, x);
					int windowSlot = (int) ((angle + Math.PI) / (Math.PI / 6));
					if (isWindowLevel && y > 4 && windowSlot % 2 == 0 && r.nextFloat() < 0.4f) {
						if (r.nextFloat() < 0.5f) {
							int wx = origin.getX() + x;
							int wy = origin.getY() + y;
							int wz = origin.getZ() + z;
							if (inBounds(wx, wz)) {
								level.setBlock(new BlockPos(wx, wy, wz), Blocks.TINTED_GLASS.defaultBlockState(), 2);
							}
						}
						continue;
					}

					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, radius + 3, c, r);
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin.offset(-radius, 0, -radius), radius * 2, radius * 2, h, floorH, overgrown, r);
		}
	}

	private static void generateOfficeBlock(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange((int) (c.min_size() * 0.8f), c.max_size(), r);
		int h = randRange(c.min_height(), (c.min_height() + c.max_height()) / 2, r);
		setBuildingHeight(h);
		int floorH = 3 + r.nextInt(2);

		// Door on front wall
		int doorPos = w / 2;
		boolean hasDoor = r.nextFloat() < 0.8f;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				boolean corner = (x == 0 || x == w - 1) && (z == 0 || z == d - 1);

				int colH = columnHeight(h, c, r);

				// Door check
				boolean isDoor = hasDoor && z == 0 && Math.abs(x - doorPos) <= 1;

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;

					// Door opening
					if (isDoor && y > 0 && y < 3)
						continue;

					if (!wall) {
						if (y % floorH == 0)
							placeBlock(level, origin, x, y, z, c, r);
						continue;
					}

					// Windows
					boolean isWindowLevel = y % floorH > 0 && y % floorH < floorH - 1;
					if (isWindowLevel && !corner && y > 3) {
						if ((x % 3 == 1 || z % 3 == 1) && r.nextFloat() < 0.65f) {
							if (r.nextFloat() < 0.5f) {
								placeWindowBlock(level, origin, x, y, z);
							}
							continue;
						}
					}

					if (isWindowLevel && !corner && (float) y / colH > 0.5f && r.nextFloat() < 0.2f)
						continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 3, c, r);
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin, w, d, h, floorH, overgrown, r);
		}
	}

	private static void generateSteppedTower(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int baseW = randRange(c.min_size(), c.max_size(), r);
		int baseD = randRange(c.min_size(), c.max_size(), r);
		int h = randRange((int) (c.min_height() * 1.2f), (int) (c.max_height() * 1.3f), r);
		setBuildingHeight(h);
		int tiers = 2 + r.nextInt(3);
		int tierH = h / tiers;
		int floorH = 4 + r.nextInt(2);

		for (int tier = 0; tier < tiers; tier++) {
			int inset = tier * 2;
			int tw = baseW - inset * 2;
			int td = baseD - inset * 2;
			if (tw < 3 || td < 3)
				break;

			int yStart = tier * tierH;
			int yEnd = Math.min(yStart + tierH, h);

			for (int x = 0; x < tw; x++) {
				for (int z = 0; z < td; z++) {
					boolean wall = x == 0 || x == tw - 1 || z == 0 || z == td - 1;
					boolean corner = (x == 0 || x == tw - 1) && (z == 0 || z == td - 1);

					int colH = columnHeight(yEnd - yStart, c, r);

					for (int y = 0; y < colH; y++) {
						int globalY = yStart + y;
						if (isDamaged(globalY, h, c, r))
							continue;
						if (!wall) {
							if (globalY % floorH == 0)
								placeBlock(level, origin, x + inset, globalY, z + inset, c, r);
							continue;
						}

						// Windows on upper tiers
						boolean isWindowLevel = globalY % floorH > 0 && globalY % floorH < floorH - 1;
						if (isWindowLevel && !corner && (x % 3 == 1 || z % 3 == 1) && r.nextFloat() < 0.5f) {
							if (r.nextFloat() < 0.4f) {
								placeWindowBlock(level, origin, x + inset, globalY, z + inset);
							}
							continue;
						}

						placeBlock(level, origin, x + inset, globalY, z + inset, c, r);
					}

					if (tier == 0)
						extendToGround(level, origin.offset(x + inset, 0, z + inset), c, r);
				}
			}

			// Tier ledge - prevents floating appearance
			if (tier > 0) {
				for (int x = 0; x < tw; x++) {
					for (int z = 0; z < td; z++) {
						boolean edge = x == 0 || x == tw - 1 || z == 0 || z == td - 1;
						if (edge) {
							placeBlock(level, origin, x + inset, yStart - 1, z + inset, c, r);
						}
					}
				}
			}
		}

		scatterRubble(level, origin, Math.max(baseW, baseD) + 3, c, r);
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin, baseW, baseD, h, floorH, overgrown, r);
		}
	}

	private static void generateUBuilding(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);
		int gapStart = w / 3;
		int gapEnd = w - w / 3;
		boolean openOnZ = r.nextBoolean();

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean inGap;
				if (openOnZ) {
					inGap = x > gapStart && x < gapEnd && z > d / 3;
				} else {
					inGap = z > gapStart && z < gapEnd && x > w / 3;
				}
				if (inGap)
					continue;

				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1
						|| (openOnZ ? (x == gapStart || x == gapEnd) : (z == gapStart || z == gapEnd));
				boolean corner = (x == 0 || x == w - 1) && (z == 0 || z == d - 1);

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					if (!wall) {
						if (y % floorH == 0)
							placeBlock(level, origin, x, y, z, c, r);
						continue;
					}

					// Windows
					boolean isWindowLevel = y % floorH > 0 && y % floorH < floorH - 1;
					if (isWindowLevel && !corner && y > 3 && (x % 3 == 1 || z % 3 == 1) && r.nextFloat() < 0.55f) {
						if (r.nextFloat() < 0.45f) {
							placeWindowBlock(level, origin, x, y, z);
						}
						continue;
					}

					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 3, c, r);
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin, w, d, h, floorH, overgrown, r);
		}
	}

	private static void generatePlusBuilding(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int armW = randRange(c.min_size() / 3 + 1, c.max_size() / 3 + 1, r);
		int armLen = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (armW < 2)
			armW = 2;
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);
		int fullSize = armLen * 2 + armW;

		for (int x = 0; x < fullSize; x++) {
			for (int z = 0; z < fullSize; z++) {
				boolean inHArm = x >= 0 && x < fullSize && z >= armLen && z < armLen + armW;
				boolean inVArm = z >= 0 && z < fullSize && x >= armLen && x < armLen + armW;
				if (!inHArm && !inVArm)
					continue;

				boolean wall = false;
				if (inHArm && !inVArm) {
					wall = z == armLen || z == armLen + armW - 1 || x == 0 || x == fullSize - 1;
				} else if (inVArm && !inHArm) {
					wall = x == armLen || x == armLen + armW - 1 || z == 0 || z == fullSize - 1;
				}

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					if (!wall) {
						if (y % floorH == 0)
							placeBlock(level, origin, x, y, z, c, r);
						continue;
					}
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin.offset(fullSize / 2, 0, fullSize / 2), fullSize / 2 + 3, c, r);
	}

	// --- PILLAR variants: tall narrow structures ---

	private static void generatePillarVariant(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.25f)
			generateSpire(level, origin, c, r);
		else if (v < 0.5f)
			generateNarrowTower(level, origin, c, r);
		else if (v < 0.7f)
			generateAntenna(level, origin, c, r);
		else
			generateObelisk(level, origin, c, r);
	}

	private static void generateSpire(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int y = 0; y < h; y++) {
			if (isDamaged(y, h, c, r))
				continue;
			float taper = 1.0f - (float) y / h * 0.6f;
			int lx = Math.max(1, (int) (sizeX * taper));
			int lz = Math.max(1, (int) (sizeZ * taper));

			for (int x = 0; x < lx; x++) {
				for (int z = 0; z < lz; z++) {
					placeBlock(level, origin, x, y, z, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(x, 0, z), c, r);
				}
			}
		}
	}

	private static void generateNarrowTower(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 5 + r.nextInt(2);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				boolean wall = x == 0 || x == sizeX - 1 || z == 0 || z == sizeZ - 1;
				boolean corner = (x == 0 || x == sizeX - 1) && (z == 0 || z == sizeZ - 1);
				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;

					if (!wall) {
						// Interior floors
						if (y % floorH == 0)
							placeBlock(level, origin, x, y, z, c, r);
						continue;
					}

					// Narrow windows
					boolean isWindowLevel = y % floorH > 1 && y % floorH < floorH - 1;
					if (isWindowLevel && !corner && y > 4 && (x + z) % 4 == 1 && r.nextFloat() < 0.5f) {
						if (r.nextFloat() < 0.4f) {
							placeWindowBlock(level, origin, x, y, z);
						}
						continue;
					}

					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(sizeX, sizeZ) + 2, c, r);
	}

	private static void generateAntenna(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int baseSize = randRange(c.min_size(), Math.min(c.max_size(), c.min_size() + 3), r);
		int colH = columnHeight(h, c, r);

		for (int y = 0; y < colH; y++) {
			if (isDamaged(y, colH, c, r))
				continue;
			placeBlock(level, origin, 0, y, 0, c, r);

			if (y % 8 == 0 && y > 0) {
				int braceLen = 1 + r.nextInt(2);
				for (int d = -braceLen; d <= braceLen; d++) {
					if (d == 0)
						continue;
					placeBlock(level, origin, d, y, 0, c, r);
					placeBlock(level, origin, 0, y, d, c, r);
				}
			}
		}

		extendToGround(level, origin, c, r);

		for (int x = -baseSize; x <= baseSize; x++) {
			for (int z = -baseSize; z <= baseSize; z++) {
				placeBlock(level, origin, x, 0, z, c, r);
				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}
	}

	private static void generateObelisk(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int baseW = randRange(c.min_size(), c.max_size(), r);
		int baseD = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int y = 0; y < h; y++) {
			if (isDamaged(y, h, c, r))
				continue;
			float t = (float) y / h;
			int wx = Math.max(1, (int) (baseW * (1.0f - t * 0.85f)));
			int wz = Math.max(1, (int) (baseD * (1.0f - t * 0.85f)));
			int offX = (baseW - wx) / 2;
			int offZ = (baseD - wz) / 2;

			for (int x = 0; x < wx; x++) {
				for (int z = 0; z < wz; z++) {
					boolean shell = x == 0 || x == wx - 1 || z == 0 || z == wz - 1;
					if (!shell && wx > 2 && wz > 2)
						continue;
					placeBlock(level, origin, x + offX, y, z + offZ, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(x + offX, 0, z + offZ), c, r);
				}
			}
		}
	}

	// --- WALL variants ---

	private static void generateWallVariant(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		// Favor enclosed structures (bunker, perimeter wall, collapsed building) over
		// partial walls
		if (v < 0.10f)
			generateWall(level, origin, c, r);
		else if (v < 0.20f)
			generateLShape(level, origin, c, r);
		else if (v < 0.45f)
			generateCollapsedBuilding(level, origin, c, r);
		else if (v < 0.55f)
			generateTShape(level, origin, c, r);
		else if (v < 0.80f)
			generateBunker(level, origin, c, r);
		else
			generatePerimeterWall(level, origin, c, r);
	}

	private static void generateWall(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int length = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		boolean alongX = r.nextBoolean();

		for (int i = 0; i < length; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					int ox = alongX ? i : t;
					int oz = alongX ? t : i;
					placeBlock(level, origin, ox, y, oz, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(ox, 0, oz), c, r);
				}
			}
		}
	}

	private static void generateLShape(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int len1 = randRange(c.min_size(), c.max_size(), r);
		int len2 = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int i = 0; i < len1; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					placeBlock(level, origin, i, y, t, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(i, 0, t), c, r);
				}
			}
		}

		for (int i = 1; i < len2; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					placeBlock(level, origin, t, y, i, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(t, 0, i), c, r);
				}
			}
		}
	}

	private static void generateCollapsedBuilding(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c,
			RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange((int) (c.min_height() * 1.1f), (int) (c.max_height() * 1.2f), r);
		setBuildingHeight(h);

		int survivingWall = r.nextInt(4);
		// More likely to have multiple walls surviving
		boolean hasTwoWalls = r.nextFloat() < 0.7f;
		boolean hasThreeWalls = hasTwoWalls && r.nextFloat() < 0.4f;
		int secondWall = (survivingWall + 1 + r.nextInt(3)) % 4;
		int thirdWall = hasThreeWalls ? (secondWall + 1 + r.nextInt(2)) % 4 : -1;
		if (thirdWall == survivingWall)
			thirdWall = (thirdWall + 1) % 4;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean onSurviving = (survivingWall == 0 && z == 0) || (survivingWall == 1 && x == w - 1)
						|| (survivingWall == 2 && z == d - 1) || (survivingWall == 3 && x == 0);
				if (hasTwoWalls) {
					onSurviving |= (secondWall == 0 && z == 0) || (secondWall == 1 && x == w - 1)
							|| (secondWall == 2 && z == d - 1) || (secondWall == 3 && x == 0);
				}
				if (hasThreeWalls && thirdWall >= 0) {
					onSurviving |= (thirdWall == 0 && z == 0) || (thirdWall == 1 && x == w - 1)
							|| (thirdWall == 2 && z == d - 1) || (thirdWall == 3 && x == 0);
				}

				if (onSurviving) {
					int colH = columnHeight(h, c, r);
					// Make surviving walls decay more towards edges
					float distFromCorner = Math.min(x, Math.min(z, Math.min(w - 1 - x, d - 1 - z)));
					float heightMult = 0.5f + 0.5f * Math.min(1f, distFromCorner / 5f);
					colH = (int) (colH * heightMult);

					for (int y = 0; y < colH; y++) {
						if (isDamaged(y, colH, c, r))
							continue;

						// Windows in surviving walls
						boolean isWindowLevel = y % 4 > 0 && y % 4 < 3;
						if (isWindowLevel && y > 3 && (x % 3 == 1 || z % 3 == 1) && r.nextFloat() < 0.3f) {
							continue;
						}

						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else {
					// Rubble floor with gradient height - snap to actual surface
					float distFromCenter = (float) Math.sqrt(Math.pow(x - w / 2.0, 2) + Math.pow(z - d / 2.0, 2));
					float maxDist = (float) Math.sqrt(w * w + d * d) / 2f;
					int rubbleH = 1 + (int) (5 * (1 - distFromCenter / maxDist)) + r.nextInt(3);

					// Place rubble at actual surface level
					placeRubbleAtSurface(level, origin.getX() + x, origin.getZ() + z, rubbleH, c, r);
				}
			}
		}
	}

	private static void generateTShape(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int stemLen = randRange(c.min_size(), c.max_size(), r);
		int crossLen = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int i = 0; i < stemLen; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					placeBlock(level, origin, crossLen / 2 + t, y, i, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(crossLen / 2 + t, 0, i), c, r);
				}
			}
		}

		for (int i = 0; i < crossLen; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					placeBlock(level, origin, i, y, t, c, r);
					if (y == 0)
						extendToGround(level, origin.offset(i, 0, t), c, r);
				}
			}
		}
	}

	private static void generateBunker(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(Math.min(c.min_height(), 10), Math.min(c.max_height(), 20), r);
		setBuildingHeight(h);
		int wallThick = 2;

		// Door position
		int doorPos = w / 2;
		boolean hasDoor = r.nextFloat() < 0.7f;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x < wallThick || x >= w - wallThick || z < wallThick || z >= d - wallThick;

				// Check for door
				boolean isDoor = hasDoor && z < wallThick && Math.abs(x - doorPos) <= 1;

				for (int y = 0; y < h; y++) {
					// Door opening
					if (isDoor && y > 0 && y < 3)
						continue;

					if (isWall) {
						placeBlock(level, origin, x, y, z, c, r);
					} else if (y == h - 1) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		// Add some roof damage
		for (int i = 0; i < 3 + r.nextInt(5); i++) {
			int hx = wallThick + r.nextInt(Math.max(1, w - wallThick * 2));
			int hz = wallThick + r.nextInt(Math.max(1, d - wallThick * 2));
			int radius = 1 + r.nextInt(2);
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					if (dx * dx + dz * dz <= radius * radius) {
						BlockPos holePos = origin.offset(hx + dx, h - 1, hz + dz);
						if (inBounds(holePos.getX(), holePos.getZ())) {
							level.setBlock(holePos, Blocks.AIR.defaultBlockState(), 2);
						}
					}
				}
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 2, c, r);
		PlanetBiomeCityOvergrown overgrown = OVERGROWN_CONFIG.get();
		if (overgrown.enabled()) {
			applyOvergrownToBuilding(level, origin, w, d, h, 4, overgrown, r);
		}
	}

	private static void generatePerimeterWall(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height() / 2, c.max_height() / 2, r);
		if (h < 4)
			h = 4;
		setBuildingHeight(h);

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isPerimeter = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				if (!isPerimeter)
					continue;

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r))
						continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}
	}

	// --- FOUNDATION variants ---

	private static void generateFoundationVariant(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c,
			RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.3f)
			generateFoundation(level, origin, c, r);
		else if (v < 0.55f)
			generateCrater(level, origin, c, r);
		else if (v < 0.8f)
			generateParkingLot(level, origin, c, r);
		else
			generatePlatform(level, origin, c, r);
	}

	private static void generateFoundation(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(10);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				placeBlock(level, origin, x, 0, z, c, r);

				boolean isEdge = x == 0 || x == sizeX - 1 || z == 0 || z == sizeZ - 1;
				if (isEdge && r.nextFloat() < 0.35f) {
					int stubH = 1 + r.nextInt(5);
					for (int y = 1; y <= stubH; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}

				boolean isCorner = (x == 0 || x == sizeX - 1) && (z == 0 || z == sizeZ - 1);
				if (isCorner && r.nextFloat() < 0.6f) {
					int pillarH = 3 + r.nextInt(8);
					for (int y = 1; y <= pillarH; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}
			}
		}
	}

	private static void generateParkingLot(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int levels = 1 + r.nextInt(3);
		int levelH = 4;
		setBuildingHeight(levels * levelH);

		for (int floor = 0; floor < levels; floor++) {
			int yBase = floor * levelH;

			for (int x = 0; x < sizeX; x++) {
				for (int z = 0; z < sizeZ; z++) {
					boolean isEdge = x == 0 || x == sizeX - 1 || z == 0 || z == sizeZ - 1;
					boolean isColumn = (x % 5 == 0) && (z % 5 == 0);

					if (isColumn) {
						for (int y = 0; y < levelH; y++) {
							placeBlock(level, origin, x, yBase + y, z, c, r);
						}
						if (floor == 0)
							extendToGround(level, origin.offset(x, 0, z), c, r);
					}

					// Floor with some holes for damage
					if (r.nextFloat() > 0.05f) {
						placeBlock(level, origin, x, yBase + levelH - 1, z, c, r);
					}

					if (isEdge) {
						int wallH = (floor == levels - 1) ? 1 : levelH;
						for (int y = 0; y < wallH; y++) {
							// Gaps in walls (openings)
							if (y > 0 && y < wallH - 1 && r.nextFloat() < 0.15f)
								continue;
							placeBlock(level, origin, x, yBase + y, z, c, r);
						}
						if (floor == 0)
							extendToGround(level, origin.offset(x, 0, z), c, r);
					}
				}
			}
		}

		// Add some debris in parking structure
		if (levels > 0 && r.nextFloat() < 0.5f) {
			int debrisCount = 2 + r.nextInt(4);
			for (int v = 0; v < debrisCount; v++) {
				int vx = 2 + r.nextInt(Math.max(1, sizeX - 4));
				int vz = 2 + r.nextInt(Math.max(1, sizeZ - 4));
				int vFloor = r.nextInt(levels);
				placeDebris(level, origin.getX() + vx, origin.getY() + vFloor * levelH, origin.getZ() + vz, r);
			}
		}
	}

	private static void generatePlatform(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int platformH = 2 + r.nextInt(4);
		setBuildingHeight(platformH);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < platformH; y++) {
					if (r.nextFloat() < 0.08f)
						continue;
					placeBlock(level, origin, x, y, z, c, r);
				}
				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(sizeX, sizeZ) + 2, c, r);
	}

	private static void generateCrater(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int radius = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (radius < 2)
			radius = 2;
		setBuildingHeight(4);

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				double dist = Math.sqrt(x * x + z * z);
				if (dist > radius)
					continue;

				// Crater rim: blocks at edge, depression in middle
				if (dist > radius - 2) {
					// Rim — raised blocks
					int rimH = 1 + r.nextInt(2);
					for (int y = 0; y < rimH; y++) {
						if (r.nextFloat() < 0.2f)
							continue;
						placeBlock(level, origin, x, y, z, c, r);
					}
				} else {
					int depth = (int) ((1.0 - dist / radius) * 4);
					if (inBounds(origin.getX() + x, origin.getZ() + z)) {
						for (int y = 0; y < depth; y++) {
							BlockPos pos = origin.offset(x, -y, z);
							if (pos.getY() > level.getMinBuildHeight()) {
								level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}
					// Scattered debris at bottom
					if (r.nextFloat() < 0.3f) {
						placeBlock(level, origin, x, -depth, z, c, r);
					}
				}
			}
		}
	}

	// --- RUBBLE variants ---

	private static void generateRubbleVariant(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.2f)
			generateRubblePile(level, origin, c, r);
		else if (v < 0.35f)
			generateScatteredDebris(level, origin, c, r);
		else if (v < 0.55f)
			generateHalfBuilding(level, origin, c, r);
		else if (v < 0.7f)
			generateCollapsedWarehouse(level, origin, c, r);
		else if (v < 0.85f)
			generateRubbleField(level, origin, c, r);
		else
			generateToppled(level, origin, c, r);
	}

	private static void generateRubblePile(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int maxH = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(maxH);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				if (r.nextFloat() < 0.2f)
					continue;

				double distFromCenter = Math.sqrt(Math.pow(x - sizeX / 2.0, 2) + Math.pow(z - sizeZ / 2.0, 2));
				double maxDist = Math.max(sizeX, sizeZ) / 2.0;
				int h = (int) (maxH * Math.max(0, 1.0 - distFromCenter / maxDist));
				h = Math.max(1, h + r.nextInt(3) - 1);

				// Place rubble at actual surface level
				placeRubbleAtSurface(level, origin.getX() + x, origin.getZ() + z, h, c, r);
			}
		}
	}

	private static void generateScatteredDebris(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c,
			RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(4);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				if (r.nextFloat() < 0.55f)
					continue;
				int h = 1 + r.nextInt(3);
				// Place debris at actual surface level
				placeRubbleAtSurface(level, origin.getX() + x, origin.getZ() + z, h, c, r);
			}
		}
	}

	private static void generateHalfBuilding(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randHeightScaled(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		boolean shearX = r.nextBoolean();

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				if (!isWall)
					continue;

				float shearFactor = shearX ? (float) x / w : (float) z / d;
				int colH = (int) (h * (1.0f - shearFactor * 0.8f));
				colH = columnHeight(colH, c, r);

				for (int y = 0; y < colH; y++) {
					if (r.nextFloat() < 0.06f)
						continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin.offset(shearX ? w : 0, 0, shearX ? 0 : d), Math.max(w, d) / 2 + 3, c, r);
	}

	private static void generateCollapsedWarehouse(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c,
			RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange((int) (c.min_size() * 1.3f), (int) (c.max_size() * 1.3f), r);
		int h = 8 + r.nextInt(10);
		setBuildingHeight(h);
		int colSpacing = 4 + r.nextInt(3);

		// Large door on one side
		int doorPos = w / 2;
		boolean hasDoor = r.nextFloat() < 0.8f;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				boolean isColumn = (x % colSpacing == 0) && (z % colSpacing == 0);

				// Large loading door
				boolean isDoor = hasDoor && z == 0 && Math.abs(x - doorPos) <= 2;

				if (isColumn) {
					int colH = columnHeight(h, c, r);
					for (int y = 0; y < colH; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else if (isWall) {
					int wallH = columnHeight(h, c, r);
					for (int y = 0; y < wallH; y++) {
						if (isDamaged(y, wallH, c, r))
							continue;
						// Large door opening
						if (isDoor && y > 0 && y < 5)
							continue;
						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else {
					// Partial roof with collapse pattern
					float collapseChance = 0.3f + 0.4f * ((float) z / d);
					if (r.nextFloat() > collapseChance) {
						placeBlock(level, origin, x, h - 1, z, c, r);
					} else if (r.nextFloat() < 0.3f) {
						// Fallen roof debris - snap to ground
						int debrisH = 1 + r.nextInt(2);
						placeRubbleAtSurface(level, origin.getX() + x, origin.getZ() + z, debrisH, c, r);
					}
				}
			}
		}

		scatterRubble(level, origin.offset(w / 2, 0, d + 2), w / 2 + 3, c, r);
	}

	private static void generateRubbleField(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(randHeightScaled(c.min_height(), c.max_height(), r));
		int clumps = 3 + r.nextInt(5);

		for (int cl = 0; cl < clumps; cl++) {
			int cx = r.nextInt(sizeX);
			int cz = r.nextInt(sizeZ);
			int clumpR = 2 + r.nextInt(4);
			int maxH = randHeightScaled(c.min_height(), c.max_height(), r);

			for (int x = cx - clumpR; x <= cx + clumpR; x++) {
				for (int z = cz - clumpR; z <= cz + clumpR; z++) {
					if (x < 0 || x >= sizeX || z < 0 || z >= sizeZ)
						continue;
					double dist = Math.sqrt((x - cx) * (x - cx) + (z - cz) * (z - cz));
					if (dist > clumpR)
						continue;

					int h = (int) (maxH * (1.0 - dist / clumpR));
					h = Math.max(1, h);

					// Place rubble at actual surface level
					placeRubbleAtSurface(level, origin.getX() + x, origin.getZ() + z, h, c, r);
				}
			}
		}
	}

	private static void generateToppled(WorldGenLevel level, BlockPos origin, PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (w < 3)
			w = 3;
		int towerH = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(towerH);

		int standingH = 2 + r.nextInt(5);
		for (int x = 0; x < w; x++) {
			for (int z = 0; z < w; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == w - 1;
				if (!isWall)
					continue;
				for (int y = 0; y < standingH; y++) {
					placeBlock(level, origin, x, y, z, c, r);
				}
				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		boolean fallX = r.nextBoolean();
		int fallDir = r.nextBoolean() ? 1 : -1;
		for (int i = 0; i < towerH; i++) {
			int fx = fallX ? fallDir * i : 0;
			int fz = fallX ? 0 : fallDir * i;
			int decay = Math.max(1, w - i / 4);

			for (int t = 0; t < decay; t++) {
				if (r.nextFloat() < 0.15f)
					continue;
				int bx = origin.getX() + w / 2 + fx + (fallX ? 0 : t - decay / 2);
				int bz = origin.getZ() + w / 2 + fz + (fallX ? t - decay / 2 : 0);
				// Place fallen debris at ground level
				placeRubbleAtSurface(level, bx, bz, 1 + r.nextInt(2), c, r);
			}
		}

		scatterRubble(level, origin.offset(fallX ? fallDir * towerH / 2 : 0, 0, fallX ? 0 : fallDir * towerH / 2),
				towerH / 3 + 3, c, r);
	}

	// ================================================================
	// UTILITIES
	// ================================================================

	private static final ThreadLocal<int[]> CHUNK_BOUNDS = ThreadLocal.withInitial(() -> new int[4]);

	private static void setChunkBounds(int chunkX, int chunkZ) {
		int[] cb = CHUNK_BOUNDS.get();
		cb[0] = chunkX << 4;
		cb[1] = (chunkX << 4) + 15;
		cb[2] = chunkZ << 4;
		cb[3] = (chunkZ << 4) + 15;
	}

	private static boolean inBounds(int wx, int wz) {
		// Use strict chunk bounds to prevent placing blocks in other chunks
		int[] cb = CHUNK_BOUNDS.get();
		return wx >= cb[0] && wx <= cb[1] && wz >= cb[2] && wz <= cb[3];
	}

	private static final ThreadLocal<BlockPos.MutableBlockPos> MUTABLE_POS = ThreadLocal
			.withInitial(BlockPos.MutableBlockPos::new);

	private static final ThreadLocal<int[]> BUILDING_HEIGHT_CTX = ThreadLocal.withInitial(() -> new int[] { 40 });
	private static final ThreadLocal<List<BlockState>> GRADIENT_BLOCKS = ThreadLocal.withInitial(List::of);
	private static final ThreadLocal<List<BlockState>> ACCENT_BLOCKS = ThreadLocal.withInitial(List::of);
	private static final ThreadLocal<Float> ACCENT_CHANCE = ThreadLocal.withInitial(() -> 0.001f);
	private static final ThreadLocal<PlanetBiomeCityOvergrown> OVERGROWN_CONFIG = ThreadLocal
			.withInitial(() -> PlanetBiomeCityOvergrown.DISABLED);

	private static void setBuildingHeight(int h) {
		BUILDING_HEIGHT_CTX.get()[0] = Math.max(1, h);
	}

	private static BlockState getGradientBlock(int wx, int wy, int wz, int oy) {
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty())
			return Blocks.AIR.defaultBlockState();

		int noise = wx * 374761393 + wy * 668265263 + wz * 1274126177;
		noise ^= noise >>> 16;
		int jx = wx + (noise & 3) - 1;
		int jy = wy + ((noise >> 3) & 3) - 1;
		int jz = wz + ((noise >> 6) & 3) - 1;
		int px = Math.floorDiv(jx, 6);
		int py = Math.floorDiv(jy, 6);
		int pz = Math.floorDiv(jz, 6);

		List<BlockState> accent = ACCENT_BLOCKS.get();
		if (!accent.isEmpty()) {
			int ah = px * 48271 ^ py * 9301 ^ pz * 101111;
			ah ^= ah >>> 16;
			float threshold = ACCENT_CHANCE.get() * 0xFFFF;
			if ((ah & 0xFFFF) < (int) threshold) {
				return accent.get(Math.floorMod(ah >> 16, accent.size()));
			}
		}

		int maxH = BUILDING_HEIGHT_CTX.get()[0];
		float frac = maxH > 1 ? Math.max(0, Math.min(1, (float) oy / maxH)) : 0;
		float zoneF = frac * (gradient.size() - 1);
		int baseZone = Math.min((int) zoneF, gradient.size() - 2);

		int hash = px * 73856093 ^ py * 19349663 ^ pz * 83492791;
		hash ^= hash >>> 16;
		float blend = zoneF - baseZone;
		int zone = (hash & 0xFF) < (int) (blend * 256) ? baseZone + 1 : baseZone;
		return gradient.get(Math.max(0, Math.min(gradient.size() - 1, zone)));
	}

	private static void placeBlock(WorldGenLevel level, BlockPos origin, int ox, int oy, int oz, PlanetBiomeRuin c,
			RandomSource r) {
		int wx = origin.getX() + ox;
		int wy = origin.getY() + oy;
		int wz = origin.getZ() + oz;
		if (!inBounds(wx, wz))
			return;
		BlockPos.MutableBlockPos pos = MUTABLE_POS.get();
		pos.set(wx, wy, wz);
		level.setBlock(pos, getGradientBlock(wx, wy, wz, oy), 2);
	}

	@SuppressWarnings("deprecation")
	private static void extendToGround(WorldGenLevel level, BlockPos startPos, PlanetBiomeRuin c, RandomSource r) {
		if (!inBounds(startPos.getX(), startPos.getZ()))
			return;
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty())
			return;

		BlockPos.MutableBlockPos pos = startPos.mutable().move(0, -1, 0);

		// Extended range to handle more terrain variation
		for (int i = 0; i < 64 && pos.getY() > level.getMinBuildHeight(); i++) {
			BlockState current = level.getBlockState(pos);
			if (current.isSolid() && !current.isAir()) {
				break;
			}
			level.setBlock(pos, gradient.get(0), 2);
			pos.move(0, -1, 0);
		}
	}

	private static void scatterRubble(WorldGenLevel level, BlockPos origin, int radius, PlanetBiomeRuin c,
			RandomSource r) {
		radius = Math.min(radius, 6);
		int count = 4 + r.nextInt(8);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty())
			return;

		for (int i = 0; i < count; i++) {
			int dx = r.nextInt(radius * 2 + 1) - radius;
			int dz = r.nextInt(radius * 2 + 1) - radius;
			if (dx * dx + dz * dz > radius * radius)
				continue;

			int wx = origin.getX() + dx;
			int wz = origin.getZ() + dz;
			if (!inBounds(wx, wz))
				continue;

			int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);
			int h = 1 + r.nextInt(2);
			for (int y = 0; y < h; y++) {
				pos.set(wx, surfaceY + y, wz);
				level.setBlock(pos, gradient.get(r.nextInt(Math.min(2, gradient.size()))), 2);
			}
		}
	}

	private static void placeRubbleAtSurface(WorldGenLevel level, int wx, int wz, int height, PlanetBiomeRuin c,
			RandomSource r) {
		if (!inBounds(wx, wz))
			return;
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty())
			return;

		int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);
		BlockPos.MutableBlockPos pos = MUTABLE_POS.get();

		for (int y = 0; y < height; y++) {
			if (r.nextFloat() < 0.1f)
				continue;
			pos.set(wx, surfaceY + y, wz);
			level.setBlock(pos, getGradientBlock(wx, surfaceY + y, wz, y), 2);
		}
	}

	private static int columnHeight(int maxHeight, PlanetBiomeRuin c, RandomSource r) {
		float weatherRange = c.max_weathering() - c.min_weathering();
		float breakPoint = 1.0f - (c.min_weathering() + r.nextFloat() * weatherRange);
		return Math.max(1, (int) (maxHeight * breakPoint));
	}

	private static boolean isDamaged(int y, int maxY, PlanetBiomeRuin c, RandomSource r) {
		float t = (float) y / maxY;
		if (t < 0.85f)
			return false;
		float damageChance = (t - 0.85f) / 0.15f * c.max_weathering();
		return r.nextFloat() < damageChance;
	}

	private static int randRange(int min, int max, RandomSource r) {
		if (max <= min)
			return min;
		return min + r.nextInt(max - min + 1);
	}

	private static int randHeightScaled(int min, int max, RandomSource r) {
		int base = randRange(min, max, r);
		return Math.max(min, Math.round(base * HEIGHT_MULTIPLIER.get()));
	}

	// ================================================================
	// OVERGROWN VEGETATION
	// ================================================================

	@SuppressWarnings("deprecation")
	private static void applyOvergrownToBuilding(WorldGenLevel level, BlockPos origin, int width, int depth,
			int height, int floorHeight, PlanetBiomeCityOvergrown overgrown, RandomSource r) {

		PlanetBiomeOvergrownGround ground = overgrown.ground();
		PlanetBiomeOvergrownVine vines = overgrown.vines();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		// Replace some floors with dirt and place grass on them
		if (ground.dirt_floor_chance() > 0 && !ground.dirt_floor_blocks().isEmpty()) {
			int numFloors = height / floorHeight;
			for (int floor = 0; floor < numFloors; floor++) {
				// Check if this floor should be dirt
				if (r.nextFloat() >= ground.dirt_floor_chance()) continue;

				int floorY = origin.getY() + (floor * floorHeight);

				// Replace floor blocks with dirt
				for (int x = 1; x < width - 1; x++) {
					for (int z = 1; z < depth - 1; z++) {
						int wx = origin.getX() + x;
						int wz = origin.getZ() + z;
						if (!inBounds(wx, wz)) continue;

						pos.set(wx, floorY, wz);
						BlockState currentBlock = level.getBlockState(pos);

						// Only replace solid blocks (actual floor blocks)
						if (!currentBlock.isSolid()) continue;

						// Replace with dirt
						BlockState dirtBlock = ground.dirt_floor_blocks().get(r.nextInt(ground.dirt_floor_blocks().size()));
						level.setBlock(pos, dirtBlock, 2);

						// Place grass on top (if space available)
						if (!ground.dirt_floor_grass().isEmpty() && r.nextFloat() < 0.4f) {
							pos.set(wx, floorY + 1, wz);
							if (level.getBlockState(pos).isAir()) {
								BlockState grassBlock = ground.dirt_floor_grass().get(r.nextInt(ground.dirt_floor_grass().size()));
								level.setBlock(pos, grassBlock, 2);
							}
						}
					}
				}
			}
		}

		// Track placed base block positions (top of each stack) for lichen and shroom placement
		List<int[]> topBlockPositions = new ArrayList<>();

		// Place floor vegetation in small MOUNDS on some floors
		if (!ground.base_blocks().isEmpty()) {
			int numFloors = height / floorHeight;
			for (int floor = 0; floor < numFloors; floor++) {
				// Not every floor is overgrown
				if (r.nextFloat() >= ground.floor_chance()) continue;

				int floorY = origin.getY() + (floor * floorHeight) + 1; // +1 to be on top of floor block

				// Place clusters per floor (configurable count)
				int numClusters = ground.cluster_min_count() + r.nextInt(Math.max(1, ground.cluster_max_count() - ground.cluster_min_count() + 1));
				for (int cluster = 0; cluster < numClusters; cluster++) {
					// Random cluster center (inside building, not on walls)
					int cx = 1 + r.nextInt(Math.max(1, width - 2));
					int cz = 1 + r.nextInt(Math.max(1, depth - 2));
					int clusterSize = ground.cluster_min_size() + r.nextInt(Math.max(1, ground.cluster_max_size() - ground.cluster_min_size() + 1));

					// Fill cluster with base blocks - small mounds
					for (int dx = -clusterSize; dx <= clusterSize; dx++) {
						for (int dz = -clusterSize; dz <= clusterSize; dz++) {
							// Circular cluster shape
							if (dx * dx + dz * dz > clusterSize * clusterSize) continue;
							// Fade out at edges
							if (r.nextFloat() < 0.3f) continue;

							int x = cx + dx;
							int z = cz + dz;
							if (x < 1 || x >= width - 1 || z < 1 || z >= depth - 1) continue;

							int wx = origin.getX() + x;
							int wz = origin.getZ() + z;
							if (!inBounds(wx, wz)) continue;

							pos.set(wx, floorY, wz);
							if (!level.getBlockState(pos).isAir()) continue;

							// Check there's a floor below
							pos.set(wx, floorY - 1, wz);
							if (!level.getBlockState(pos).isSolid()) continue;

							// Place base block
							pos.set(wx, floorY, wz);
							BlockState baseBlock = ground.base_blocks().get(r.nextInt(ground.base_blocks().size()));
							level.setBlock(pos, baseBlock, 2);

							int topY = floorY;
							// 30% chance to stack 2 high (only near cluster center)
							if (r.nextFloat() < 0.3f && dx * dx + dz * dz <= 1) {
								pos.set(wx, floorY + 1, wz);
								if (level.getBlockState(pos).isAir()) {
									level.setBlock(pos, baseBlock, 2);
									topY = floorY + 1;
								}
							}
							topBlockPositions.add(new int[]{wx, topY, wz});
						}
					}
				}
			}
		}

		// Place lichens on and around base blocks
		if (!ground.lichen_blocks().isEmpty() && ground.lichen_chance() > 0) {
			for (int[] basePos : topBlockPositions) {
				int bx = basePos[0], by = basePos[1], bz = basePos[2];

				// Try to place lichen on top of base block (attaching DOWN)
				if (r.nextFloat() < ground.lichen_chance()) {
					pos.set(bx, by + 1, bz);
					if (level.getBlockState(pos).isAir()) {
						BlockState lichen = ground.lichen_blocks().get(r.nextInt(ground.lichen_blocks().size()));
						// Set the DOWN face property for proper attachment
						lichen = lichen.setValue(MultifaceBlock.getFaceProperty(Direction.DOWN), true);
						level.setBlock(pos, lichen, 2);
					}
				}

				// Try to place lichen on nearby solid blocks (walls, floor, etc)
				Direction[] allDirs = Direction.values();
				for (Direction dir : allDirs) {
					if (r.nextFloat() >= ground.lichen_chance() * 0.5f) continue; // Lower chance for spread

					int nx = bx + dir.getStepX();
					int ny = by + dir.getStepY();
					int nz = bz + dir.getStepZ();
					if (!inBounds(nx, nz)) continue;

					pos.set(nx, ny, nz);
					BlockState targetState = level.getBlockState(pos);

					// If target is solid, try to place lichen on its exposed faces
					if (targetState.isSolid()) {
						// Check each face of the solid block for air
						for (Direction face : Direction.values()) {
							if (r.nextFloat() < 0.6f) continue;
							int lx = nx + face.getStepX();
							int ly = ny + face.getStepY();
							int lz = nz + face.getStepZ();
							if (!inBounds(lx, lz)) continue;

							pos.set(lx, ly, lz);
							if (level.getBlockState(pos).isAir()) {
								BlockState lichen = ground.lichen_blocks().get(r.nextInt(ground.lichen_blocks().size()));
								// Set face property pointing back toward the solid block
								lichen = lichen.setValue(MultifaceBlock.getFaceProperty(face.getOpposite()), true);
								level.setBlock(pos, lichen, 2);
								}
							}
						}
					}
				}
			}

		// Place shrooms on top of base blocks
		if (!ground.shroom_blocks().isEmpty() && ground.shroom_chance() > 0) {
			for (int[] basePos : topBlockPositions) {
				if (r.nextFloat() >= ground.shroom_chance()) continue;

				int bx = basePos[0], by = basePos[1], bz = basePos[2];
				pos.set(bx, by + 1, bz);
				if (level.getBlockState(pos).isAir()) {
					// Check the base block is solid (shrooms need solid base)
					pos.set(bx, by, bz);
					if (level.getBlockState(pos).isSolid()) {
						pos.set(bx, by + 1, bz);
						BlockState shroom = ground.shroom_blocks().get(r.nextInt(ground.shroom_blocks().size()));
						level.setBlock(pos, shroom, 2);
					}
				}
			}
		}

		// Spread lichens throughout the building on walls, floors, and ceilings
		if (!ground.lichen_blocks().isEmpty() && ground.lichen_chance() > 0) {
			int numFloors = height / floorHeight;
			// Scan through building and place lichens on exposed surfaces
			for (int floor = 0; floor < numFloors; floor++) {
				if (r.nextFloat() >= ground.floor_chance()) continue;

				int floorY = origin.getY() + (floor * floorHeight);

				// Scatter lichens throughout this floor
				int lichenAttempts = (width * depth) / 4; // More attempts for larger buildings
				for (int attempt = 0; attempt < lichenAttempts; attempt++) {
					int x = r.nextInt(width);
					int z = r.nextInt(depth);
					int wx = origin.getX() + x;
					int wz = origin.getZ() + z;
					if (!inBounds(wx, wz)) continue;

					// Pick a random Y within this floor
					int wy = floorY + 1 + r.nextInt(Math.max(1, floorHeight - 1));

					pos.set(wx, wy, wz);
					if (!level.getBlockState(pos).isAir()) continue;

					// Check all 6 directions for a solid block to attach to
					for (Direction dir : Direction.values()) {
						if (r.nextFloat() >= ground.lichen_chance() * 0.3f) continue; // Lower per-face chance

						int nx = wx + dir.getStepX();
						int ny = wy + dir.getStepY();
						int nz = wz + dir.getStepZ();

						BlockPos neighborPos = new BlockPos(nx, ny, nz);
						if (level.getBlockState(neighborPos).isSolid()) {
							// Place lichen attached to this face
							BlockState lichen = ground.lichen_blocks().get(r.nextInt(ground.lichen_blocks().size()));
							lichen = lichen.setValue(MultifaceBlock.getFaceProperty(dir), true);
							level.setBlock(pos, lichen, 2);
							break; // Only attach to one face per position
						}
					}
				}
			}
		}

		// Place ONE branching vine per building (chance-based) on a random wall
		List<BlockState> wallBlocks = vines.wall_blocks();
		if (!wallBlocks.isEmpty() && vines.vine_chance() > 0 && r.nextFloat() < vines.vine_chance()) {
			BlockState vineBlock = wallBlocks.get(r.nextInt(wallBlocks.size()));

			int maxVy = Math.min(height, vines.max_wall_height());
			int vy = origin.getY() + vines.min_wall_height() + r.nextInt(Math.max(1, maxVy - vines.min_wall_height()));

			// Pick a random wall (0=north, 1=south, 2=west, 3=east)
			int wall = r.nextInt(4);
			int vx, vz;
			Direction wallDir;

			switch (wall) {
				case 0: // North face
					vx = origin.getX() + 2 + r.nextInt(Math.max(1, width - 4));
					vz = origin.getZ() - 1;
					wallDir = Direction.SOUTH;
					break;
				case 1: // South face
					vx = origin.getX() + 2 + r.nextInt(Math.max(1, width - 4));
					vz = origin.getZ() + depth;
					wallDir = Direction.NORTH;
					break;
				case 2: // West face
					vx = origin.getX() - 1;
					vz = origin.getZ() + 2 + r.nextInt(Math.max(1, depth - 4));
					wallDir = Direction.EAST;
					break;
				default: // East face
					vx = origin.getX() + width;
					vz = origin.getZ() + 2 + r.nextInt(Math.max(1, depth - 4));
					wallDir = Direction.WEST;
					break;
			}

			// Pass building bounds for geometric wall checking (avoids chunk boundary issues)
			placeBranchingVine(level, vx, vy, vz, wallDir, vineBlock, r, vines, origin, width, depth, height);
		}
	}

	private static void placeBranchingVine(WorldGenLevel level, int startX, int startY, int startZ,
			Direction wallDir, BlockState vineBlock, RandomSource r, PlanetBiomeOvergrownVine config,
			BlockPos buildingOrigin, int buildingW, int buildingD, int buildingH) {

		// Get configurable vine parameters
		int minLength = config.vine_min_length();
		int maxLength = config.vine_max_length();
		int minBranches = config.vine_min_branches();
		int maxBranches = config.vine_max_branches();

		// Create main vine stem going DOWN with dramatic curves along the wall
		int vineLength = minLength + r.nextInt(Math.max(1, maxLength - minLength + 1));
		List<Vector3f> mainSpline = new ArrayList<>();

		// wallDir points TOWARD the wall the vine hugs
		boolean wallOnZ = (wallDir == Direction.NORTH || wallDir == Direction.SOUTH);

		float x = startX;
		float y = startY;
		float z = startZ;

		// Momentum for smooth curves
		float momentumX = 0;
		float momentumZ = 0;

		mainSpline.add(new Vector3f(x, y, z));

		// Build main stem with smooth curved motion
		for (int i = 1; i <= vineLength; i++) {
			y -= 0.9f + r.nextFloat() * 0.2f; // Go down

			// Add random force, then apply momentum for smooth curves
			if (wallOnZ) {
				// Wall is north/south, vine moves along X axis
				momentumX += (r.nextFloat() - 0.5f) * 0.8f;
				momentumX *= 0.85f; // Dampen
				x += momentumX;
			} else {
				// Wall is east/west, vine moves along Z axis
				momentumZ += (r.nextFloat() - 0.5f) * 0.8f;
				momentumZ *= 0.85f; // Dampen
				z += momentumZ;
			}

			mainSpline.add(new Vector3f(x, y, z));
		}

		int thickness = config.vine_thickness();

		// Place main stem blocks (use geometric wall check to avoid chunk boundary issues)
		placeVineSpline(level, mainSpline, vineBlock, wallDir, r, thickness, buildingOrigin, buildingW, buildingD, buildingH);

		// Add branches
		int numBranches = minBranches + (maxBranches > minBranches ? r.nextInt(maxBranches - minBranches + 1) : 0);
		for (int b = 0; b < numBranches; b++) {
			// Branch from random point along main stem (not too close to ends)
			int branchPoint = 3 + r.nextInt(Math.max(1, mainSpline.size() - 6));
			if (branchPoint >= mainSpline.size()) continue;

			Vector3f branchStart = mainSpline.get(branchPoint);
			int branchLength = 4 + r.nextInt(Math.max(1, vineLength / 2));

			List<Vector3f> branchSpline = new ArrayList<>();
			branchSpline.add(new Vector3f(branchStart));

			float bx = branchStart.x();
			float by = branchStart.y();
			float bz = branchStart.z();
			float bMomentum = 0;

			// Branch curves away then back
			float curveDir = r.nextBoolean() ? 1f : -1f;

			for (int i = 1; i <= branchLength; i++) {
				by -= 0.7f + r.nextFloat() * 0.5f;

				// Curve outward then back
				float curveFactor = (float) Math.sin(Math.PI * i / branchLength) * 2f;
				bMomentum += (r.nextFloat() - 0.5f) * 0.5f + curveDir * 0.3f * curveFactor;
				bMomentum *= 0.8f;

				if (wallOnZ) {
					bx += bMomentum;
				} else {
					bz += bMomentum;
				}
				branchSpline.add(new Vector3f(bx, by, bz));
			}

			// Branches are slightly thinner
			placeVineSpline(level, branchSpline, vineBlock, wallDir, r, Math.max(1, thickness - 1), buildingOrigin, buildingW, buildingD, buildingH);
		}
	}

	private static void placeVineSpline(WorldGenLevel level, List<Vector3f> spline, BlockState vineBlock,
			Direction wallDir, RandomSource r, int thickness,
			BlockPos buildingOrigin, int buildingW, int buildingD, int buildingH) {
		if (spline.size() < 2) return;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		// Building bounds for geometric wall check (avoids chunk boundary issues)
		int bMinX = buildingOrigin.getX();
		int bMaxX = bMinX + buildingW - 1;
		int bMinZ = buildingOrigin.getZ();
		int bMaxZ = bMinZ + buildingD - 1;
		int bMinY = buildingOrigin.getY();
		int bMaxY = bMinY + buildingH;

		// Max radius based on thickness (1=single, 2=3x3, 3=5x5)
		int maxRadius = thickness - 1;
		int splineLen = spline.size();

		// Walk along spline and place blocks
		Vector3f prev = spline.get(0);
		for (int i = 1; i < splineLen; i++) {
			Vector3f curr = spline.get(i);

			// Taper: thin at start, thick in middle, thin at end
			float progress = (float) i / (splineLen - 1);
			float taperFactor = (float) Math.sin(progress * Math.PI); // 0 -> 1 -> 0
			int radius = Math.round(maxRadius * taperFactor);

			// Interpolate between points
			float dx = curr.x() - prev.x();
			float dy = curr.y() - prev.y();
			float dz = curr.z() - prev.z();
			float dist = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
			int steps = Math.max(1, (int) Math.ceil(dist * 2));

			for (int s = 0; s <= steps; s++) {
				float t = (float) s / steps;
				int px = Math.round(prev.x() + dx * t);
				int py = Math.round(prev.y() + dy * t);
				int pz = Math.round(prev.z() + dz * t);

				// SNAP to wall face - force the perpendicular coordinate to the correct wall position
				// This prevents any floating point drift from causing floating vines
				if (wallDir == Direction.SOUTH) {
					pz = bMinZ - 1; // North face of building
				} else if (wallDir == Direction.NORTH) {
					pz = bMaxZ + 1; // South face of building
				} else if (wallDir == Direction.EAST) {
					px = bMinX - 1; // West face of building
				} else if (wallDir == Direction.WEST) {
					px = bMaxX + 1; // East face of building
				}

				// Place cluster of blocks based on current radius
				for (int ox = -radius; ox <= radius; ox++) {
					for (int oz = -radius; oz <= radius; oz++) {
						for (int oy = 0; oy <= (radius > 0 ? 1 : 0); oy++) {
							// Skip corners for organic shape
							if (radius > 0 && Math.abs(ox) + Math.abs(oz) > radius && r.nextFloat() < 0.7f) continue;

							int bx = px + ox;
							int by = py + oy;
							int bz = pz + oz;

							if (!inBounds(bx, bz)) continue;
							pos.set(bx, by, bz);

							// Skip if below ground or above building
							if (by < bMinY || by > bMaxY) continue;

							// Ensure block is within building's wall extent (along the wall, not perpendicular)
							if (wallDir == Direction.SOUTH || wallDir == Direction.NORTH) {
								// N/S wall - check X bounds
								if (bx < bMinX || bx > bMaxX) continue;
							} else {
								// E/W wall - check Z bounds
								if (bz < bMinZ || bz > bMaxZ) continue;
							}

							// Only check if position is air
							if (!level.getBlockState(pos).isAir()) continue;

							level.setBlock(pos, vineBlock, 2);
						}
					}
				}
			}
			prev = curr;
		}
	}
}
