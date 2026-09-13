package com.machina.world.feature.city;

import java.util.List;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * All city generation logic: roads and buildings.
 * Deterministic road grid based on chunk coordinates.
 * Buildings procedurally generated with weathering.
 */
public class CityGen {

	// ================================================================
	// CHUNK TYPE DETERMINATION (pure math, no state)
	// ================================================================

	/**
	 * Is this chunk on a road line along the Z axis?
	 * (Road runs north-south at this X column of chunks)
	 */
	public static boolean isZRoadChunk(int chunkX, int roadSpacing) {
		return Math.floorMod(chunkX, roadSpacing) == 0;
	}

	/**
	 * Is this chunk on a road line along the X axis?
	 * (Road runs east-west at this Z row of chunks)
	 */
	public static boolean isXRoadChunk(int chunkZ, int roadSpacing) {
		return Math.floorMod(chunkZ, roadSpacing) == 0;
	}

	// ================================================================
	// ROAD GENERATION
	// ================================================================

	// [0]=smooth, [1]=slopeCheck, [2]=headroom, [3]=bridgeThreshold, [4]=tunnelThreshold, [5]=maxHeightDiff
	private static final ThreadLocal<int[]> ROAD_CFG =
			ThreadLocal.withInitial(() -> new int[]{8, 4, 4, 2, 2, 8});

	public static void generateRoads(WorldGenLevel level, int chunkX, int chunkZ,
			boolean hasZRoad, boolean hasXRoad,
			List<BlockState> roadBlocks, int roadWidth, RandomSource random,
			int roadSmooth, int slopeCheck, int headroom,
			int bridgeThreshold, int tunnelThreshold, int maxHeightDiff) {

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
					if (!inZ && !inX) continue;

					int wx = baseX + lx;
					int wz = baseZ + lz;
					boolean overlap = inZ && inX;

					int roadY;
					boolean isEdge;
					boolean isPillarPos;

					if (overlap) {
						int zh = heightAt(zH, lz), xh = heightAt(xH, lx);
						if (Math.abs(zh - xh) > 3) continue;
						roadY = Math.max(zh, xh);
						isEdge = false;
						isPillarPos = (lz % 4 == 0) || (lx % 4 == 0);
					} else if (inZ) {
						if (isTooSteep(zH, lz)) continue;
						roadY = heightAt(zH, lz);
						isEdge = (lx == roadStart || lx == roadEnd - 1);
						isPillarPos = (lz % 4 == 0);
						int edgeDist = Math.min(lx - roadStart, roadEnd - 1 - lx);
						if (isRoadEdgeEroded(wx, wz, edgeDist)) continue;
					} else {
						if (isTooSteep(xH, lx)) continue;
						roadY = heightAt(xH, lx);
						isEdge = (lz == roadStart || lz == roadEnd - 1);
						isPillarPos = (lx % 4 == 0);
						int edgeDist = Math.min(lz - roadStart, roadEnd - 1 - lz);
						if (isRoadEdgeEroded(wx, wz, edgeDist)) continue;
					}

					placeRoadColumn(level, wx, wz, roadY,
							isEdge, isPillarPos, roadBlocks, random);
				}
			}
		} else if (hasZRoad) {
			for (int lz = 0; lz < 16; lz++) {
				if (isTooSteep(zH, lz)) continue;
				int roadY = heightAt(zH, lz);
				for (int lx = roadStart; lx < roadEnd; lx++) {
					int wx = baseX + lx;
					int wz = baseZ + lz;
					int edgeDist = Math.min(lx - roadStart, roadEnd - 1 - lx);
					if (isRoadEdgeEroded(wx, wz, edgeDist)) continue;
					boolean isEdge = (lx == roadStart || lx == roadEnd - 1);
					placeRoadColumn(level, wx, wz, roadY,
							isEdge, (lz % 4 == 0), roadBlocks, random);
				}
			}
		} else {
			for (int lx = 0; lx < 16; lx++) {
				if (isTooSteep(xH, lx)) continue;
				int roadY = heightAt(xH, lx);
				for (int lz = roadStart; lz < roadEnd; lz++) {
					int wx = baseX + lx;
					int wz = baseZ + lz;
					int edgeDist = Math.min(lz - roadStart, roadEnd - 1 - lz);
					if (isRoadEdgeEroded(wx, wz, edgeDist)) continue;
					boolean isEdge = (lz == roadStart || lz == roadEnd - 1);
					placeRoadColumn(level, wx, wz, roadY,
							isEdge, (lx % 4 == 0), roadBlocks, random);
				}
			}
		}
	}

	private static int[] computeRoadHeights(WorldGenLevel level,
			int startX, int startZ, int dx, int dz) {
		int[] cfg = ROAD_CFG.get();
		int smooth = cfg[0];
		int slopeCheck = cfg[1];
		int len = 16 + 2 * slopeCheck;
		int[] h = new int[len];
		int diam = 2 * smooth + 1;
		int refH = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG,
				startX + 8 * dx, startZ + 8 * dz) - 1;

		for (int i = 0; i < len; i++) {
			int pos = i - slopeCheck;
			int x = startX + pos * dx;
			int z = startZ + pos * dz;
			float sum = 0;
			for (int j = -smooth; j <= smooth; j++) {
				int t = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG,
						x + j * dx, z + j * dz) - 1;
				if (t < refH - 50) t = refH;
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
			if (idx - d >= 0 && Math.abs(here - h[idx - d]) > 1) return true;
			if (idx + d < h.length && Math.abs(here - h[idx + d]) > 1) return true;
		}
		return false;
	}

	private static void placeRoadColumn(WorldGenLevel level, int x, int z, int roadY,
			boolean isEdge, boolean isPillarPos,
			List<BlockState> roadBlocks, RandomSource random) {

		int[] cfg = ROAD_CFG.get();
		int headroom = cfg[2];
		int bridgeThreshold = cfg[3];
		int tunnelThreshold = cfg[4];
		int maxHeightDiff = cfg[5];

		int terrainY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
		if (roadY < level.getMinBuildHeight() + 1) return;

		int diff = roadY - terrainY;
		if (Math.abs(diff) > maxHeightDiff) return;
		if (random.nextFloat() < 0.015f) return;

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
				level.setBlock(new BlockPos(x, roadY + dy, z),
						Blocks.AIR.defaultBlockState(), 2);
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
	private static void fillDown(WorldGenLevel level, int x, int startY, int z,
			BlockState block) {
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
	private static void clearAbove(WorldGenLevel level, int x, int startY, int z,
			int count) {
		for (int dy = 0; dy < count; dy++) {
			BlockPos pos = new BlockPos(x, startY + dy, z);
			if (pos.getY() >= level.getMaxBuildHeight()) break;
			if (level.getBlockState(pos).isSolid())
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		}
	}

	private static boolean isRoadEdgeEroded(int wx, int wz, int edgeDist) {
		if (edgeDist >= 4) return false;
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

	private static final ThreadLocal<int[]> BUILDING_BOUNDS = ThreadLocal.withInitial(() -> new int[4]);

	public static void generateBuildings(WorldGenLevel level, int chunkX, int chunkZ,
			List<PlanetBiomeRuin> ruins, List<BlockState> gradientBlocks, List<BlockState> accentBlocks,
			float accentChance,
			long worldSeed, RandomSource random,
			int roadSpacing, int roadWidth) {

		if (ruins.isEmpty() || gradientBlocks.isEmpty()) return;
		GRADIENT_BLOCKS.set(gradientBlocks);
		ACCENT_BLOCKS.set(accentBlocks);
		ACCENT_CHANCE.set(accentChance);

		int baseX = chunkX << 4;
		int baseZ = chunkZ << 4;

		int[] bounds = BUILDING_BOUNDS.get();
		bounds[0] = baseX;      // minX
		bounds[1] = baseX + 15; // maxX
		bounds[2] = baseZ;      // minZ
		bounds[3] = baseZ + 15; // maxZ

		if (isZRoadChunk(chunkX - 1, roadSpacing)) bounds[0] = baseX + 2;
		if (isZRoadChunk(chunkX + 1, roadSpacing)) bounds[1] = baseX + 13;
		if (isXRoadChunk(chunkZ - 1, roadSpacing)) bounds[2] = baseZ + 2;
		if (isXRoadChunk(chunkZ + 1, roadSpacing)) bounds[3] = baseZ + 13;

		int count = 2 + random.nextInt(3);

		for (int i = 0; i < count; i++) {
			int rangeX = bounds[1] - bounds[0] - 1;
			int rangeZ = bounds[3] - bounds[2] - 1;
			if (rangeX < 3 || rangeZ < 3) continue;

			int wx = bounds[0] + 1 + random.nextInt(rangeX);
			int wz = bounds[2] + 1 + random.nextInt(rangeZ);
			int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);

			BlockPos origin = new BlockPos(wx, surfaceY, wz);

			// Pick ruin type
			PlanetBiomeRuin ruin = ruins.get(random.nextInt(ruins.size()));
			if (random.nextFloat() > ruin.chance()) continue;

			// Generate sub-variant based on shape
			switch (ruin.shape()) {
				case TUBE -> generateTubeVariant(level, origin, ruin, random);
				case PILLAR -> generatePillarVariant(level, origin, ruin, random);
				case WALL -> generateWallVariant(level, origin, ruin, random);
				case FOUNDATION -> generateFoundationVariant(level, origin, ruin, random);
				case RUBBLE -> generateRubbleVariant(level, origin, ruin, random);
			}
		}
	}

	// --- TUBE variants: tall hollow buildings ---

	private static void generateTubeVariant(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.2f) generateSkyscraper(level, origin, c, r);
		else if (v < 0.35f) generateCylinder(level, origin, c, r);
		else if (v < 0.55f) generateOfficeBlock(level, origin, c, r);
		else if (v < 0.7f) generateSteppedTower(level, origin, c, r);
		else if (v < 0.85f) generateUBuilding(level, origin, c, r);
		else generatePlusBuilding(level, origin, c, r);
	}

	private static void generateSkyscraper(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				boolean corner = (x == 0 || x == w - 1) && (z == 0 || z == d - 1);

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					if (!wall) {
						if (y % floorH == 0) placeBlock(level, origin, x, y, z, c, r);
						continue;
					}
					boolean isWindowLevel = y % floorH > 0 && y % floorH < floorH - 1;
					if (isWindowLevel && !corner && (float) y / colH > 0.5f && r.nextFloat() < 0.2f) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 4, c, r);
	}

	private static void generateCylinder(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int radius = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (radius < 3) radius = 3;
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				double dist = Math.sqrt(x * x + z * z);
				boolean shell = dist <= radius && dist > radius - 1.5;
				if (!shell) continue;

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, radius + 3, c, r);
	}

	private static void generateOfficeBlock(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange((int)(c.min_size() * 0.6f), c.max_size(), r);
		int h = randRange(c.min_height(), (c.min_height() + c.max_height()) / 2, r);
		setBuildingHeight(h);
		int floorH = 3 + r.nextInt(2);

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1;

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					if (!wall) {
						if (y % floorH == 0) placeBlock(level, origin, x, y, z, c, r);
						continue;
					}
					if (y % floorH > 0 && y % floorH < floorH - 1 && (float) y / colH > 0.5f && r.nextFloat() < 0.2f) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 3, c, r);
	}

	private static void generateSteppedTower(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int baseW = randRange(c.min_size(), c.max_size(), r);
		int baseD = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int tiers = 2 + r.nextInt(3);
		int tierH = h / tiers;
		int floorH = 4 + r.nextInt(2);

		for (int tier = 0; tier < tiers; tier++) {
			int inset = tier * 2;
			int tw = baseW - inset * 2;
			int td = baseD - inset * 2;
			if (tw < 3 || td < 3) break;

			int yStart = tier * tierH;
			int yEnd = Math.min(yStart + tierH, h);

			for (int x = 0; x < tw; x++) {
				for (int z = 0; z < td; z++) {
					boolean wall = x == 0 || x == tw - 1 || z == 0 || z == td - 1;

					int colH = columnHeight(yEnd - yStart, c, r);

					for (int y = 0; y < colH; y++) {
						int globalY = yStart + y;
						if (isDamaged(globalY, h, c, r)) continue;
						if (!wall) {
							if (globalY % floorH == 0) placeBlock(level, origin, x + inset, globalY, z + inset, c, r);
							continue;
						}
						placeBlock(level, origin, x + inset, globalY, z + inset, c, r);
					}

					if (tier == 0)
						extendToGround(level, origin.offset(x + inset, 0, z + inset), c, r);
				}
			}
		}

		scatterRubble(level, origin, Math.max(baseW, baseD) + 3, c, r);
	}

	private static void generateUBuilding(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
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
				if (inGap) continue;

				boolean wall = x == 0 || x == w - 1 || z == 0 || z == d - 1
						|| (openOnZ ? (x == gapStart || x == gapEnd) : (z == gapStart || z == gapEnd));

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					if (!wall) {
						if (y % floorH == 0) placeBlock(level, origin, x, y, z, c, r);
						continue;
					}
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 3, c, r);
	}

	private static void generatePlusBuilding(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int armW = randRange(c.min_size() / 3 + 1, c.max_size() / 3 + 1, r);
		int armLen = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (armW < 2) armW = 2;
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int floorH = 4 + r.nextInt(2);
		int fullSize = armLen * 2 + armW;

		for (int x = 0; x < fullSize; x++) {
			for (int z = 0; z < fullSize; z++) {
				boolean inHArm = x >= 0 && x < fullSize
						&& z >= armLen && z < armLen + armW;
				boolean inVArm = z >= 0 && z < fullSize
						&& x >= armLen && x < armLen + armW;
				if (!inHArm && !inVArm) continue;

				boolean wall = false;
				if (inHArm && !inVArm) {
					wall = z == armLen || z == armLen + armW - 1 || x == 0 || x == fullSize - 1;
				} else if (inVArm && !inHArm) {
					wall = x == armLen || x == armLen + armW - 1 || z == 0 || z == fullSize - 1;
				}

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					if (!wall) {
						if (y % floorH == 0) placeBlock(level, origin, x, y, z, c, r);
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

	private static void generatePillarVariant(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.25f) generateSpire(level, origin, c, r);
		else if (v < 0.5f) generateNarrowTower(level, origin, c, r);
		else if (v < 0.7f) generateAntenna(level, origin, c, r);
		else generateObelisk(level, origin, c, r);
	}

	private static void generateSpire(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int y = 0; y < h; y++) {
			if (isDamaged(y, h, c, r)) continue;
			float taper = 1.0f - (float) y / h * 0.6f;
			int lx = Math.max(1, (int) (sizeX * taper));
			int lz = Math.max(1, (int) (sizeZ * taper));

			for (int x = 0; x < lx; x++) {
				for (int z = 0; z < lz; z++) {
					placeBlock(level, origin, x, y, z, c, r);
					if (y == 0) extendToGround(level, origin.offset(x, 0, z), c, r);
				}
			}
		}
	}

	private static void generateNarrowTower(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}
	}

	private static void generateAntenna(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		int baseSize = randRange(c.min_size(), Math.min(c.max_size(), c.min_size() + 3), r);
		int colH = columnHeight(h, c, r);

		for (int y = 0; y < colH; y++) {
			if (isDamaged(y, colH, c, r)) continue;
			placeBlock(level, origin, 0, y, 0, c, r);

			if (y % 8 == 0 && y > 0) {
				int braceLen = 1 + r.nextInt(2);
				for (int d = -braceLen; d <= braceLen; d++) {
					if (d == 0) continue;
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

	private static void generateObelisk(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int baseW = randRange(c.min_size(), c.max_size(), r);
		int baseD = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int y = 0; y < h; y++) {
			if (isDamaged(y, h, c, r)) continue;
			float t = (float) y / h;
			int wx = Math.max(1, (int) (baseW * (1.0f - t * 0.85f)));
			int wz = Math.max(1, (int) (baseD * (1.0f - t * 0.85f)));
			int offX = (baseW - wx) / 2;
			int offZ = (baseD - wz) / 2;

			for (int x = 0; x < wx; x++) {
				for (int z = 0; z < wz; z++) {
					boolean shell = x == 0 || x == wx - 1 || z == 0 || z == wz - 1;
					if (!shell && wx > 2 && wz > 2) continue;
					placeBlock(level, origin, x + offX, y, z + offZ, c, r);
					if (y == 0) extendToGround(level, origin.offset(x + offX, 0, z + offZ), c, r);
				}
			}
		}
	}

	// --- WALL variants ---

	private static void generateWallVariant(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.2f) generateWall(level, origin, c, r);
		else if (v < 0.4f) generateLShape(level, origin, c, r);
		else if (v < 0.55f) generateCollapsedBuilding(level, origin, c, r);
		else if (v < 0.7f) generateTShape(level, origin, c, r);
		else if (v < 0.85f) generateBunker(level, origin, c, r);
		else generatePerimeterWall(level, origin, c, r);
	}

	private static void generateWall(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int length = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);
		boolean alongX = r.nextBoolean();

		for (int i = 0; i < length; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					int ox = alongX ? i : t;
					int oz = alongX ? t : i;
					placeBlock(level, origin, ox, y, oz, c, r);
					if (y == 0) extendToGround(level, origin.offset(ox, 0, oz), c, r);
				}
			}
		}
	}

	private static void generateLShape(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int len1 = randRange(c.min_size(), c.max_size(), r);
		int len2 = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int i = 0; i < len1; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, i, y, t, c, r);
					if (y == 0) extendToGround(level, origin.offset(i, 0, t), c, r);
				}
			}
		}

		for (int i = 1; i < len2; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, t, y, i, c, r);
					if (y == 0) extendToGround(level, origin.offset(t, 0, i), c, r);
				}
			}
		}
	}

	private static void generateCollapsedBuilding(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		int survivingWall = r.nextInt(4);
		boolean hasTwoWalls = r.nextFloat() < 0.4f;
		int secondWall = (survivingWall + 1 + r.nextInt(3)) % 4;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean onSurviving = (survivingWall == 0 && z == 0) ||
					(survivingWall == 1 && x == w - 1) ||
					(survivingWall == 2 && z == d - 1) ||
					(survivingWall == 3 && x == 0);
				if (hasTwoWalls) {
					onSurviving |= (secondWall == 0 && z == 0) ||
						(secondWall == 1 && x == w - 1) ||
						(secondWall == 2 && z == d - 1) ||
						(secondWall == 3 && x == 0);
				}

				if (onSurviving) {
					int colH = columnHeight(h, c, r);
					for (int y = 0; y < colH; y++) {
						if (isDamaged(y, colH, c, r)) continue;
						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else {
					int rubbleH = 1 + r.nextInt(4);
					for (int y = 0; y < rubbleH; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}
			}
		}
	}

	private static void generateTShape(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int stemLen = randRange(c.min_size(), c.max_size(), r);
		int crossLen = randRange(c.min_size(), c.max_size(), r);
		int thickness = 1 + r.nextInt(3);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		for (int i = 0; i < stemLen; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, crossLen / 2 + t, y, i, c, r);
					if (y == 0) extendToGround(level, origin.offset(crossLen / 2 + t, 0, i), c, r);
				}
			}
		}

		for (int i = 0; i < crossLen; i++) {
			for (int t = 0; t < thickness; t++) {
				int colH = columnHeight(h, c, r);
				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, i, y, t, c, r);
					if (y == 0) extendToGround(level, origin.offset(i, 0, t), c, r);
				}
			}
		}
	}

	private static void generateBunker(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(Math.min(c.min_height(), 8), Math.min(c.max_height(), 16), r);
		setBuildingHeight(h);
		int wallThick = 2;

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x < wallThick || x >= w - wallThick || z < wallThick || z >= d - wallThick;

				for (int y = 0; y < h; y++) {
					if (isWall) {
						placeBlock(level, origin, x, y, z, c, r);
					} else if (y == h - 1) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(w, d) + 2, c, r);
	}

	private static void generatePerimeterWall(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height() / 2, c.max_height() / 2, r);
		if (h < 4) h = 4;
		setBuildingHeight(h);

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isPerimeter = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				if (!isPerimeter) continue;

				int colH = columnHeight(h, c, r);

				for (int y = 0; y < colH; y++) {
					if (isDamaged(y, colH, c, r)) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}
	}

	// --- FOUNDATION variants ---

	private static void generateFoundationVariant(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.3f) generateFoundation(level, origin, c, r);
		else if (v < 0.55f) generateCrater(level, origin, c, r);
		else if (v < 0.8f) generateParkingLot(level, origin, c, r);
		else generatePlatform(level, origin, c, r);
	}

	private static void generateFoundation(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
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

	private static void generateParkingLot(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
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
						if (floor == 0) extendToGround(level, origin.offset(x, 0, z), c, r);
					}

					placeBlock(level, origin, x, yBase + levelH - 1, z, c, r);

					if (isEdge) {
						int wallH = (floor == levels - 1) ? 1 : levelH;
						for (int y = 0; y < wallH; y++) {
							placeBlock(level, origin, x, yBase + y, z, c, r);
						}
					}
				}
			}
		}
	}

	private static void generatePlatform(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int platformH = 2 + r.nextInt(4);
		setBuildingHeight(platformH);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < platformH; y++) {
					if (r.nextFloat() < 0.08f) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}
				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin, Math.max(sizeX, sizeZ) + 2, c, r);
	}

	private static void generateCrater(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int radius = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (radius < 2) radius = 2;
		setBuildingHeight(4);

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				double dist = Math.sqrt(x * x + z * z);
				if (dist > radius) continue;

				// Crater rim: blocks at edge, depression in middle
				if (dist > radius - 2) {
					// Rim — raised blocks
					int rimH = 1 + r.nextInt(2);
					for (int y = 0; y < rimH; y++) {
						if (r.nextFloat() < 0.2f) continue;
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

	private static void generateRubbleVariant(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		float v = r.nextFloat();
		if (v < 0.2f) generateRubblePile(level, origin, c, r);
		else if (v < 0.35f) generateScatteredDebris(level, origin, c, r);
		else if (v < 0.55f) generateHalfBuilding(level, origin, c, r);
		else if (v < 0.7f) generateCollapsedWarehouse(level, origin, c, r);
		else if (v < 0.85f) generateRubbleField(level, origin, c, r);
		else generateToppled(level, origin, c, r);
	}

	private static void generateRubblePile(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		int maxH = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(maxH);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				if (r.nextFloat() < 0.2f) continue;

				double distFromCenter = Math.sqrt(
					Math.pow(x - sizeX / 2.0, 2) + Math.pow(z - sizeZ / 2.0, 2));
				double maxDist = Math.max(sizeX, sizeZ) / 2.0;
				int h = (int) (maxH * Math.max(0, 1.0 - distFromCenter / maxDist));
				h = Math.max(1, h + r.nextInt(3) - 1);

				for (int y = 0; y < h; y++) {
					if (r.nextFloat() < 0.1f) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}
			}
		}
	}

	private static void generateScatteredDebris(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(4);

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				if (r.nextFloat() < 0.55f) continue;
				int h = 1 + r.nextInt(3);
				for (int y = 0; y < h; y++) {
					placeBlock(level, origin, x, y, z, c, r);
				}
			}
		}
	}

	private static void generateHalfBuilding(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange(c.min_size(), c.max_size(), r);
		int h = randRange(c.min_height(), c.max_height(), r);
		setBuildingHeight(h);

		boolean shearX = r.nextBoolean();

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				if (!isWall) continue;

				float shearFactor = shearX ? (float) x / w : (float) z / d;
				int colH = (int) (h * (1.0f - shearFactor * 0.8f));
				colH = columnHeight(colH, c, r);

				for (int y = 0; y < colH; y++) {
					if (r.nextFloat() < 0.06f) continue;
					placeBlock(level, origin, x, y, z, c, r);
				}

				extendToGround(level, origin.offset(x, 0, z), c, r);
			}
		}

		scatterRubble(level, origin.offset(shearX ? w : 0, 0, shearX ? 0 : d),
				Math.max(w, d) / 2 + 3, c, r);
	}

	private static void generateCollapsedWarehouse(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size(), c.max_size(), r);
		int d = randRange((int)(c.min_size() * 1.5f), (int)(c.max_size() * 1.5f), r);
		int h = 6 + r.nextInt(8);
		setBuildingHeight(h);
		int colSpacing = 4 + r.nextInt(3);

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == d - 1;
				boolean isColumn = (x % colSpacing == 0) && (z % colSpacing == 0);

				if (isColumn) {
					int colH = columnHeight(h, c, r);
					for (int y = 0; y < colH; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else if (isWall) {
					int wallH = columnHeight(h, c, r);
					for (int y = 0; y < wallH; y++) {
						if (isDamaged(y, wallH, c, r)) continue;
						placeBlock(level, origin, x, y, z, c, r);
					}
					extendToGround(level, origin.offset(x, 0, z), c, r);
				} else if (r.nextFloat() < 0.5f) {
					placeBlock(level, origin, x, h - 1, z, c, r);
				}
			}
		}
	}

	private static void generateRubbleField(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int sizeX = randRange(c.min_size(), c.max_size(), r);
		int sizeZ = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(randRange(c.min_height(), c.max_height(), r));
		int clumps = 3 + r.nextInt(5);

		for (int cl = 0; cl < clumps; cl++) {
			int cx = r.nextInt(sizeX);
			int cz = r.nextInt(sizeZ);
			int clumpR = 2 + r.nextInt(4);
			int maxH = randRange(c.min_height(), c.max_height(), r);

			for (int x = cx - clumpR; x <= cx + clumpR; x++) {
				for (int z = cz - clumpR; z <= cz + clumpR; z++) {
					if (x < 0 || x >= sizeX || z < 0 || z >= sizeZ) continue;
					double dist = Math.sqrt((x - cx) * (x - cx) + (z - cz) * (z - cz));
					if (dist > clumpR) continue;

					int h = (int)(maxH * (1.0 - dist / clumpR));
					h = Math.max(1, h);

					for (int y = 0; y < h; y++) {
						placeBlock(level, origin, x, y, z, c, r);
					}
				}
			}
		}
	}

	private static void generateToppled(WorldGenLevel level, BlockPos origin,
			PlanetBiomeRuin c, RandomSource r) {
		int w = randRange(c.min_size() / 2, c.max_size() / 2, r);
		if (w < 3) w = 3;
		int towerH = randRange(c.min_size(), c.max_size(), r);
		setBuildingHeight(towerH);

		int standingH = 2 + r.nextInt(5);
		for (int x = 0; x < w; x++) {
			for (int z = 0; z < w; z++) {
				boolean isWall = x == 0 || x == w - 1 || z == 0 || z == w - 1;
				if (!isWall) continue;
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
				if (r.nextFloat() < 0.15f) continue;
				placeBlock(level, origin, w / 2 + fx + (fallX ? 0 : t - decay / 2),
						standingH, w / 2 + fz + (fallX ? t - decay / 2 : 0), c, r);
			}
		}

		scatterRubble(level, origin.offset(
				fallX ? fallDir * towerH / 2 : 0, 0,
				fallX ? 0 : fallDir * towerH / 2),
				towerH / 3 + 3, c, r);
	}

	// ================================================================
	// UTILITIES
	// ================================================================

	private static boolean inBounds(int wx, int wz) {
		int[] b = BUILDING_BOUNDS.get();
		return wx >= b[0] && wx <= b[1] && wz >= b[2] && wz <= b[3];
	}

	private static final ThreadLocal<BlockPos.MutableBlockPos> MUTABLE_POS =
			ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

	private static final ThreadLocal<int[]> BUILDING_HEIGHT_CTX =
			ThreadLocal.withInitial(() -> new int[]{40});
	private static final ThreadLocal<List<BlockState>> GRADIENT_BLOCKS =
			ThreadLocal.withInitial(List::of);
	private static final ThreadLocal<List<BlockState>> ACCENT_BLOCKS =
			ThreadLocal.withInitial(List::of);
	private static final ThreadLocal<Float> ACCENT_CHANCE =
			ThreadLocal.withInitial(() -> 0.001f);
	private static void setBuildingHeight(int h) {
		BUILDING_HEIGHT_CTX.get()[0] = Math.max(1, h);
	}

	private static BlockState getGradientBlock(int wx, int wy, int wz, int oy) {
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty()) return Blocks.AIR.defaultBlockState();

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
		int zone = (hash & 0xFF) < (int)(blend * 256) ? baseZone + 1 : baseZone;
		return gradient.get(Math.max(0, Math.min(gradient.size() - 1, zone)));
	}

	private static void placeBlock(WorldGenLevel level, BlockPos origin,
			int ox, int oy, int oz, PlanetBiomeRuin c, RandomSource r) {
		int wx = origin.getX() + ox;
		int wy = origin.getY() + oy;
		int wz = origin.getZ() + oz;
		if (!inBounds(wx, wz)) return;
		BlockPos.MutableBlockPos pos = MUTABLE_POS.get();
		pos.set(wx, wy, wz);
		level.setBlock(pos, getGradientBlock(wx, wy, wz, oy), 2);
	}

	@SuppressWarnings("deprecation")
	private static void extendToGround(WorldGenLevel level, BlockPos startPos,
			PlanetBiomeRuin c, RandomSource r) {
		if (!inBounds(startPos.getX(), startPos.getZ())) return;
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty()) return;

		BlockPos.MutableBlockPos pos = startPos.mutable().move(0, -1, 0);

		for (int i = 0; i < 32 && pos.getY() > level.getMinBuildHeight() && !level.getBlockState(pos).isSolid(); i++) {
			level.setBlock(pos, gradient.get(0), 2);
			pos.move(0, -1, 0);
		}
	}

	private static void scatterRubble(WorldGenLevel level, BlockPos origin, int radius,
			PlanetBiomeRuin c, RandomSource r) {
		radius = Math.min(radius, 8);
		int count = 4 + r.nextInt(8);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		List<BlockState> gradient = GRADIENT_BLOCKS.get();
		if (gradient.isEmpty()) return;

		for (int i = 0; i < count; i++) {
			int dx = r.nextInt(radius * 2 + 1) - radius;
			int dz = r.nextInt(radius * 2 + 1) - radius;
			if (dx * dx + dz * dz > radius * radius) continue;

			int wx = origin.getX() + dx;
			int wz = origin.getZ() + dz;
			if (!inBounds(wx, wz)) continue;

			int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, wx, wz);
			int h = 1 + r.nextInt(2);
			for (int y = 0; y < h; y++) {
				pos.set(wx, surfaceY + y, wz);
				level.setBlock(pos, gradient.get(r.nextInt(Math.min(2, gradient.size()))), 2);
			}
		}
	}

	private static int columnHeight(int maxHeight, PlanetBiomeRuin c, RandomSource r) {
		float weatherRange = c.max_weathering() - c.min_weathering();
		float breakPoint = 1.0f - (c.min_weathering() + r.nextFloat() * weatherRange);
		return Math.max(1, (int) (maxHeight * breakPoint));
	}

	private static boolean isDamaged(int y, int maxY, PlanetBiomeRuin c, RandomSource r) {
		float t = (float) y / maxY;
		if (t < 0.85f) return false;
		float damageChance = (t - 0.85f) / 0.15f * c.max_weathering();
		return r.nextFloat() < damageChance;
	}

	private static int randRange(int min, int max, RandomSource r) {
		if (max <= min) return min;
		return min + r.nextInt(max - min + 1);
	}
}
