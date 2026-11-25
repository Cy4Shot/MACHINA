package com.machina.world.structure.vault.helper;

import java.util.ArrayList;
import java.util.List;

import com.machina.registration.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Applies dense overgrowth to vault rooms with Perlin noise floor coverage
 * and surface-following vines.
 */
public class VaultOvergrowthHelper {

	public record OvergrowthData(
		List<BlockState> baseBlocks,
		List<BlockState> lichenBlocks,
		List<BlockState> shroomBlocks,
		float floorChance,
		int clusterMinCount,
		int clusterMaxCount,
		int clusterMinSize,
		int clusterMaxSize,
		float lichenChance,
		float shroomChance,
		List<BlockState> vineBlocks,
		float vineChance,
		int vineMinLength,
		int vineMaxLength,
		int vineMinBranches,
		int vineMaxBranches,
		int vineThickness
	) {
		public static final OvergrowthData EMPTY = new OvergrowthData(
			List.of(), List.of(), List.of(), 0f, 0, 0, 0, 0, 0f, 0f,
			List.of(), 0f, 0, 0, 0, 0, 1
		);

		public static OvergrowthData createVaultDefault() {
			return new OvergrowthData(
				// Base blocks (floor/ground)
				List.of(
					BlockInit.TOXIC_DIRT.get().defaultBlockState(),
					BlockInit.TOXIC_GRASS_BLOCK.get().defaultBlockState()
				),
				// Lichen blocks
				List.of(
					BlockInit.TOX_LICHEN.get().defaultBlockState()
				),
				// Shroom blocks (includes tendrils)
				List.of(
					BlockInit.TOX_SHROOM.get().defaultBlockState(),
					BlockInit.CYAN_TOX_SHROOM.get().defaultBlockState(),
					BlockInit.TOXIC_TENDRIL.get().defaultBlockState(),
					BlockInit.TOXIC_GRASS.get().defaultBlockState()
				),
				// Floor chance
				0.8f,
				// Cluster counts
				3, 8,
				// Cluster sizes
				2, 5,
				// Lichen chance
				0.3f,
				// Shroom chance
				0.15f,
				// Vine blocks
				List.of(
					BlockInit.IMBUED_CYAN_MUSHROOM_HYPHAE.get().defaultBlockState()
				),
				// Vine params
				0.4f,  // vine chance
				3,     // min length
				8,     // max length
				1,     // min branches
				3,     // max branches
				2      // thickness
			);
		}

		public boolean isEmpty() {
			return baseBlocks.isEmpty() && lichenBlocks.isEmpty() && shroomBlocks.isEmpty() && vineBlocks.isEmpty();
		}

		public void writeToNbt(CompoundTag tag) {
			writeBlockList(tag, "BaseBlocks", baseBlocks);
			writeBlockList(tag, "LichenBlocks", lichenBlocks);
			writeBlockList(tag, "ShroomBlocks", shroomBlocks);
			writeBlockList(tag, "VineBlocks", vineBlocks);
			tag.putFloat("FloorChance", floorChance);
			tag.putInt("ClusterMinCount", clusterMinCount);
			tag.putInt("ClusterMaxCount", clusterMaxCount);
			tag.putInt("ClusterMinSize", clusterMinSize);
			tag.putInt("ClusterMaxSize", clusterMaxSize);
			tag.putFloat("LichenChance", lichenChance);
			tag.putFloat("ShroomChance", shroomChance);
			tag.putFloat("VineChance", vineChance);
			tag.putInt("VineMinLength", vineMinLength);
			tag.putInt("VineMaxLength", vineMaxLength);
			tag.putInt("VineMinBranches", vineMinBranches);
			tag.putInt("VineMaxBranches", vineMaxBranches);
			tag.putInt("VineThickness", vineThickness);
		}

		private static void writeBlockList(CompoundTag tag, String name, List<BlockState> blocks) {
			ListTag list = new ListTag();
			for (BlockState state : blocks) {
				list.add(NbtUtils.writeBlockState(state));
			}
			tag.put(name, list);
		}

		public static OvergrowthData readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
			var blockLookup = registries.lookupOrThrow(Registries.BLOCK);
			return new OvergrowthData(
				readBlockList(tag, "BaseBlocks", blockLookup),
				readBlockList(tag, "LichenBlocks", blockLookup),
				readBlockList(tag, "ShroomBlocks", blockLookup),
				tag.getFloat("FloorChance"),
				tag.getInt("ClusterMinCount"),
				tag.getInt("ClusterMaxCount"),
				tag.getInt("ClusterMinSize"),
				tag.getInt("ClusterMaxSize"),
				tag.getFloat("LichenChance"),
				tag.getFloat("ShroomChance"),
				readBlockList(tag, "VineBlocks", blockLookup),
				tag.getFloat("VineChance"),
				tag.getInt("VineMinLength"),
				tag.getInt("VineMaxLength"),
				tag.getInt("VineMinBranches"),
				tag.getInt("VineMaxBranches"),
				tag.contains("VineThickness") ? tag.getInt("VineThickness") : 1
			);
		}

		private static List<BlockState> readBlockList(CompoundTag tag, String name,
				HolderLookup.RegistryLookup<net.minecraft.world.level.block.Block> lookup) {
			ListTag list = tag.getList(name, Tag.TAG_COMPOUND);
			List<BlockState> blocks = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				blocks.add(NbtUtils.readBlockState(lookup, list.getCompound(i)));
			}
			return blocks;
		}
	}

	// Perlin noise implementation
	private static final int[] PERM = new int[512];
	static {
		int[] p = {151,160,137,91,90,15,131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,
			8,99,37,240,21,10,23,190,6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,
			35,11,32,57,177,33,88,237,149,56,87,174,20,125,136,171,168,68,175,74,165,71,
			134,139,48,27,166,77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,
			55,46,245,40,244,102,143,54,65,25,63,161,1,216,80,73,209,76,132,187,208,89,
			18,169,200,196,135,130,116,188,159,86,164,100,109,198,173,186,3,64,52,217,226,
			250,124,123,5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,
			189,28,42,223,183,170,213,119,248,152,2,44,154,163,70,221,153,101,155,167,43,
			172,9,129,22,39,253,19,98,108,110,79,113,224,232,178,185,112,104,218,246,97,
			228,251,34,242,193,238,210,144,12,191,179,162,241,81,51,145,235,249,14,239,
			107,49,192,214,31,181,199,106,157,184,84,204,176,115,121,50,45,127,4,150,254,
			138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180};
		for (int i = 0; i < 256; i++) {
			PERM[i] = p[i];
			PERM[256 + i] = p[i];
		}
	}

	private static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
	private static double lerp(double t, double a, double b) { return a + t * (b - a); }
	private static double grad(int hash, double x, double y) {
		int h = hash & 15;
		double u = h < 8 ? x : y;
		double v = h < 4 ? y : (h == 12 || h == 14 ? x : 0);
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	private static double perlin(double x, double y, long seed) {
		x += (seed & 0xFFFF) * 0.1;
		y += ((seed >> 16) & 0xFFFF) * 0.1;
		int X = (int) Math.floor(x) & 255;
		int Y = (int) Math.floor(y) & 255;
		x -= Math.floor(x);
		y -= Math.floor(y);
		double u = fade(x), v = fade(y);
		int A = PERM[X] + Y, B = PERM[X + 1] + Y;
		return lerp(v, lerp(u, grad(PERM[A], x, y), grad(PERM[B], x - 1, y)),
					   lerp(u, grad(PERM[A + 1], x, y - 1), grad(PERM[B + 1], x - 1, y - 1)));
	}

	private static double octavePerlin(double x, double y, long seed, int octaves, double persistence) {
		double total = 0, frequency = 1, amplitude = 1, maxValue = 0;
		for (int i = 0; i < octaves; i++) {
			total += perlin(x * frequency, y * frequency, seed + i * 1000) * amplitude;
			maxValue += amplitude;
			amplitude *= persistence;
			frequency *= 2;
		}
		return total / maxValue;
	}

	public static void applyOvergrowth(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, int levelIndex, int totalLevels,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random) {
		applyOvergrowth(level, box, origin, width, depth, height, levelIndex, totalLevels,
			stairwellCenter, stairwellRadius, data, random, 1.0f, false);
	}

	public static void applyOvergrowth(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, int levelIndex, int totalLevels,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random, float vineMultiplier) {
		applyOvergrowth(level, box, origin, width, depth, height, levelIndex, totalLevels,
			stairwellCenter, stairwellRadius, data, random, vineMultiplier, false);
	}

	public static void applyOvergrowth(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, int levelIndex, int totalLevels,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random, float vineMultiplier, boolean isMainChamber) {

		if (data.isEmpty()) return;

		// All floors have equal dense overgrowth
		float levelMultiplier = 0.8f;

		long noiseSeed = random.nextLong();
		List<int[]> grassedPositions = new ArrayList<>();

		// === PERLIN NOISE FLOOR REPLACEMENT ===
		if (!data.baseBlocks().isEmpty() && data.floorChance() > 0) {
			applyPerlinFloor(level, box, origin, width, depth, height, levelMultiplier,
				stairwellCenter, stairwellRadius, data, random, noiseSeed, grassedPositions);
		}

		// === WALL AND CEILING LICHEN ===
		if (!data.lichenBlocks().isEmpty() && data.lichenChance() > 0) {
			float densityMultiplier = levelMultiplier * levelMultiplier * 100f;
			applyLichen(level, box, origin, width, depth, height, densityMultiplier,
				stairwellCenter, stairwellRadius, data, random, grassedPositions);
		}

		// === SURFACE-FOLLOWING VINES ===
		if (!data.vineBlocks().isEmpty() && data.vineChance() > 0) {
			float densityMultiplier = levelMultiplier * levelMultiplier * 100f * vineMultiplier;
			applySurfaceVines(level, box, origin, width, depth, height, densityMultiplier,
				stairwellCenter, stairwellRadius, data, random);
		}

		// === SPECIAL DECORATIONS ===
		if (isMainChamber) {
			// Main chamber gets ceiling hanging vines
			applyCeilingHangingVines(level, box, origin, width, depth, height,
				stairwellCenter, stairwellRadius, random, noiseSeed);
		} else {
			// Other rooms get small bushes/ground vines
			applySmallBushes(level, box, origin, width, depth, height,
				stairwellCenter, stairwellRadius, random, noiseSeed);
		}
	}

	private static void applyPerlinFloor(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, float levelMultiplier,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random, long noiseSeed, List<int[]> grassedPositions) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		double patchNoiseScale = 0.08; // Lower = bigger patches
		double moundNoiseScale = 0.2;
		int floorY = origin.getY(); // The actual floor block level

		// Patch threshold - higher = rarer patches
		float patchThreshold = 0.45f;

		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				int wx = origin.getX() + x;
				int wz = origin.getZ() + z;

				if (isInStairwell(wx, wz, stairwellCenter, stairwellRadius)) continue;

				// Patch noise determines where floor gets replaced
				// Use fixed seed (0) so noise is consistent across all structure pieces
				double patchNoise = octavePerlin(wx * patchNoiseScale, wz * patchNoiseScale, 0, 2, 0.6);
				patchNoise = (patchNoise + 1.0) * 0.5;

				// Skip if not in a patch
				if (patchNoise < patchThreshold) continue;

				// === LAYER 0: Replace floor block ===
				pos.set(wx, floorY, wz);
				if (box.isInside(pos)) {
					BlockState baseBlock = data.baseBlocks().get(random.nextInt(data.baseBlocks().size()));
					level.setBlock(pos, baseBlock, 2);
				}

				// Mound height noise - determines how tall this spot gets
				// Use fixed seed (12345) so mounds are consistent across pieces
				double moundNoise = octavePerlin(wx * moundNoiseScale, wz * moundNoiseScale, 12345, 2, 0.5);
				moundNoise = (moundNoise + 1.0) * 0.5;
				// Add randomness for organic edges
				moundNoise += (random.nextFloat() - 0.5) * 0.3;

				int topY = floorY;

				// === LAYER 1: First mound layer (common in center of patches) ===
				if (moundNoise > 0.4) {
					pos.set(wx, floorY + 1, wz);
					if (box.isInside(pos) && level.getBlockState(pos).isAir()) {
						BlockState baseBlock = data.baseBlocks().get(random.nextInt(data.baseBlocks().size()));
						level.setBlock(pos, baseBlock, 2);
						topY = floorY + 1;
					}
				}

				// === LAYER 2: Second mound layer (rarer, peaks of mounds) ===
				if (moundNoise > 0.7) {
					pos.set(wx, floorY + 2, wz);
					if (box.isInside(pos) && level.getBlockState(pos).isAir()) {
						BlockState baseBlock = data.baseBlocks().get(random.nextInt(data.baseBlocks().size()));
						level.setBlock(pos, baseBlock, 2);
						topY = floorY + 2;
					}
				}

				// Track top position for vegetation
				grassedPositions.add(new int[]{wx, topY, wz});

				// Add vegetation on top
				pos.set(wx, topY + 1, wz);
				if (box.isInside(pos) && level.getBlockState(pos).isAir()) {
					if (!data.shroomBlocks().isEmpty() && random.nextFloat() < data.shroomChance() * levelMultiplier) {
						BlockState shroom = data.shroomBlocks().get(random.nextInt(data.shroomBlocks().size()));
						level.setBlock(pos, shroom, 2);
					} else if (!data.lichenBlocks().isEmpty() && random.nextFloat() < data.lichenChance() * levelMultiplier * 0.3f) {
						BlockState lichen = data.lichenBlocks().get(random.nextInt(data.lichenBlocks().size()));
						if (lichen.getBlock() instanceof MultifaceBlock) {
							lichen = lichen.setValue(MultifaceBlock.getFaceProperty(Direction.DOWN), true);
						}
						level.setBlock(pos, lichen, 2);
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void applyLichen(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, float densityMultiplier,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random, List<int[]> grassedPositions) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		float scaledLichenChance = data.lichenChance() * densityMultiplier / 50f;
		int lichenAttempts = (int) ((width * depth * height) / 8.0f * densityMultiplier / 10f);

		for (int attempt = 0; attempt < lichenAttempts; attempt++) {
			int x = random.nextInt(width);
			int z = random.nextInt(depth);
			int y = 1 + random.nextInt(Math.max(1, height - 1));

			int wx = origin.getX() + x;
			int wy = origin.getY() + y;
			int wz = origin.getZ() + z;

			if (isInStairwell(wx, wz, stairwellCenter, stairwellRadius)) continue;
			if (!box.isInside(wx, wy, wz)) continue;

			pos.set(wx, wy, wz);
			if (!level.getBlockState(pos).isAir()) continue;

			for (Direction dir : Direction.values()) {
				if (random.nextFloat() >= scaledLichenChance) continue;

				int nx = wx + dir.getStepX();
				int ny = wy + dir.getStepY();
				int nz = wz + dir.getStepZ();

				if (!box.isInside(nx, ny, nz)) continue;

				BlockPos neighborPos = new BlockPos(nx, ny, nz);
				if (level.getBlockState(neighborPos).isSolid()) {
					BlockState lichen = data.lichenBlocks().get(random.nextInt(data.lichenBlocks().size()));
					if (lichen.getBlock() instanceof MultifaceBlock) {
						lichen = lichen.setValue(MultifaceBlock.getFaceProperty(dir), true);
					}
					level.setBlock(pos, lichen, 2);
					break;
				}
			}
		}

		// Extra lichen around grassed positions
		for (int[] basePos : grassedPositions) {
			placeLichenAroundBlock(level, box, basePos, data, random, scaledLichenChance * 5f);
		}
	}

	@SuppressWarnings("deprecation")
	private static void placeLichenAroundBlock(WorldGenLevel level, BoundingBox box,
			int[] basePos, OvergrowthData data, RandomSource random, float chance) {

		int bx = basePos[0], by = basePos[1], bz = basePos[2];
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (Direction dir : Direction.values()) {
			if (random.nextFloat() >= chance * 0.3f) continue;

			int nx = bx + dir.getStepX();
			int ny = by + dir.getStepY();
			int nz = bz + dir.getStepZ();

			if (!box.isInside(nx, ny, nz)) continue;

			pos.set(nx, ny, nz);
			if (level.getBlockState(pos).isSolid()) {
				for (Direction face : Direction.values()) {
					if (random.nextFloat() < 0.5f) continue;
					int lx = nx + face.getStepX();
					int ly = ny + face.getStepY();
					int lz = nz + face.getStepZ();

					if (!box.isInside(lx, ly, lz)) continue;

					pos.set(lx, ly, lz);
					if (level.getBlockState(pos).isAir()) {
						BlockState lichen = data.lichenBlocks().get(random.nextInt(data.lichenBlocks().size()));
						if (lichen.getBlock() instanceof MultifaceBlock) {
							lichen = lichen.setValue(MultifaceBlock.getFaceProperty(face.getOpposite()), true);
						}
						level.setBlock(pos, lichen, 2);
						break;
					}
				}
			}
		}
	}

	private static void applySurfaceVines(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, float densityMultiplier,
			BlockPos stairwellCenter, int stairwellRadius,
			OvergrowthData data, RandomSource random) {

		float scaledVineChance = data.vineChance() * densityMultiplier / 20f;
		int numVines = Math.max(0, (int) (scaledVineChance * 5));

		for (int v = 0; v < numVines; v++) {
			if (random.nextFloat() >= Math.min(1.0f, scaledVineChance)) continue;

			BlockState vineBlock = data.vineBlocks().get(random.nextInt(data.vineBlocks().size()));

			// Find a starting surface point
			int startX = origin.getX() + 2 + random.nextInt(Math.max(1, width - 4));
			int startZ = origin.getZ() + 2 + random.nextInt(Math.max(1, depth - 4));
			int startY = origin.getY() + height - 1; // Start near ceiling

			if (isInStairwell(startX, startZ, stairwellCenter, stairwellRadius)) continue;

			// Find actual surface to start from
			BlockPos startPos = findSurfaceStart(level, box, startX, startY, startZ, origin, width, depth, height, random);
			if (startPos == null) continue;

			// Generate surface-following vine
			int vineLength = data.vineMinLength() + random.nextInt(Math.max(1, data.vineMaxLength() - data.vineMinLength() + 1));
			placeSurfaceFollowingVine(level, box, startPos, vineLength, vineBlock, data, random,
				origin, width, depth, height, stairwellCenter, stairwellRadius);
		}
	}

	@SuppressWarnings("deprecation")
	private static BlockPos findSurfaceStart(WorldGenLevel level, BoundingBox box,
			int x, int y, int z, BlockPos origin, int width, int depth, int height, RandomSource random) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		// Try to find an air block adjacent to a solid surface
		for (int attempts = 0; attempts < 20; attempts++) {
			int tx = x + random.nextInt(5) - 2;
			int ty = y - random.nextInt(height / 2);
			int tz = z + random.nextInt(5) - 2;

			if (!box.isInside(tx, ty, tz)) continue;

			pos.set(tx, ty, tz);
			if (!level.getBlockState(pos).isAir()) continue;

			// Check for adjacent solid
			for (Direction dir : Direction.values()) {
				BlockPos neighbor = pos.relative(dir);
				if (box.isInside(neighbor) && level.getBlockState(neighbor).isSolid()) {
					return new BlockPos(tx, ty, tz);
				}
			}
		}
		return null;
	}

	private static void placeSurfaceFollowingVine(WorldGenLevel level, BoundingBox box, BlockPos start,
			int length, BlockState vineBlock, OvergrowthData data, RandomSource random,
			BlockPos origin, int width, int depth, int height,
			BlockPos stairwellCenter, int stairwellRadius) {

		BlockPos.MutableBlockPos current = new BlockPos.MutableBlockPos(start.getX(), start.getY(), start.getZ());
		Direction lastDir = null;
		Direction attachedFace = findAttachedFace(level, box, current);

		if (attachedFace == null) return;

		// Momentum for smooth curves
		float momentumX = 0, momentumY = 0, momentumZ = 0;

		for (int i = 0; i < length; i++) {
			int cx = current.getX();
			int cy = current.getY();
			int cz = current.getZ();

			if (!box.isInside(cx, cy, cz)) break;
			if (isInStairwell(cx, cz, stairwellCenter, stairwellRadius)) break;
			if (!level.getBlockState(current).isAir()) break;

			// Place vine block
			BlockState vine = vineBlock;
			if (vineBlock.getBlock() instanceof MultifaceBlock && attachedFace != null) {
				vine = vineBlock.setValue(MultifaceBlock.getFaceProperty(attachedFace), true);
			}
			level.setBlock(current.immutable(), vine, 2);

			// Add thickness perpendicular to movement
			if (data.vineThickness() > 1) {
				addVineThickness(level, box, current, attachedFace, vineBlock, data.vineThickness(), random);
			}

			// Choose next direction - prefer to follow surface with momentum
			Direction nextDir = chooseNextDirection(level, box, current, attachedFace, lastDir, random,
				momentumX, momentumY, momentumZ);

			if (nextDir == null) {
				// Try to transition to a new surface
				nextDir = tryTransitionSurface(level, box, current, attachedFace, random);
				if (nextDir == null) break;
			}

			// Update momentum for smooth curves
			momentumX = momentumX * 0.7f + nextDir.getStepX() * 0.3f;
			momentumY = momentumY * 0.7f + nextDir.getStepY() * 0.3f;
			momentumZ = momentumZ * 0.7f + nextDir.getStepZ() * 0.3f;

			current.move(nextDir);
			lastDir = nextDir;

			// Update attached face after moving
			Direction newAttached = findAttachedFace(level, box, current);
			if (newAttached != null) {
				attachedFace = newAttached;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static Direction findAttachedFace(WorldGenLevel level, BoundingBox box, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			BlockPos neighbor = pos.relative(dir);
			if (box.isInside(neighbor) && level.getBlockState(neighbor).isSolid()) {
				return dir;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private static Direction chooseNextDirection(WorldGenLevel level, BoundingBox box, BlockPos current,
			Direction attachedFace, Direction lastDir, RandomSource random,
			float momentumX, float momentumY, float momentumZ) {

		List<Direction> validDirs = new ArrayList<>();
		List<Float> weights = new ArrayList<>();

		for (Direction dir : Direction.values()) {
			if (dir == attachedFace) continue; // Don't go into the wall
			if (lastDir != null && dir == lastDir.getOpposite()) continue; // Don't backtrack

			BlockPos next = current.relative(dir);
			if (!box.isInside(next)) continue;
			if (!level.getBlockState(next).isAir()) continue;

			// Check if still adjacent to surface after moving
			boolean stillAttached = false;
			for (Direction check : Direction.values()) {
				BlockPos checkPos = next.relative(check);
				if (box.isInside(checkPos) && level.getBlockState(checkPos).isSolid()) {
					stillAttached = true;
					break;
				}
			}

			if (stillAttached) {
				validDirs.add(dir);

				// Weight based on momentum (prefer continuing in same general direction)
				float weight = 1.0f;
				weight += dir.getStepX() * momentumX * 2f;
				weight += dir.getStepY() * momentumY * 2f;
				weight += dir.getStepZ() * momentumZ * 2f;
				weight = Math.max(0.1f, weight);
				weights.add(weight);
			}
		}

		if (validDirs.isEmpty()) return null;

		// Weighted random selection
		float totalWeight = 0;
		for (float w : weights) totalWeight += w;

		float r = random.nextFloat() * totalWeight;
		float cumulative = 0;
		for (int i = 0; i < validDirs.size(); i++) {
			cumulative += weights.get(i);
			if (r <= cumulative) {
				return validDirs.get(i);
			}
		}
		return validDirs.get(validDirs.size() - 1);
	}

	@SuppressWarnings("deprecation")
	private static Direction tryTransitionSurface(WorldGenLevel level, BoundingBox box,
			BlockPos current, Direction currentFace, RandomSource random) {

		// Try to wrap around a corner
		List<Direction> options = new ArrayList<>();

		for (Direction dir : Direction.values()) {
			if (dir == currentFace || dir == currentFace.getOpposite()) continue;

			BlockPos next = current.relative(dir);
			if (!box.isInside(next)) continue;

			// Check if this direction leads to a new surface
			if (level.getBlockState(next).isAir()) {
				BlockPos beyond = next.relative(currentFace.getOpposite());
				if (box.isInside(beyond) && level.getBlockState(beyond).isSolid()) {
					options.add(dir);
				}
			}
		}

		if (options.isEmpty()) return null;
		return options.get(random.nextInt(options.size()));
	}

	@SuppressWarnings("deprecation")
	private static void addVineThickness(WorldGenLevel level, BoundingBox box, BlockPos center,
			Direction attachedFace, BlockState vineBlock, int thickness, RandomSource random) {

		for (Direction dir : Direction.values()) {
			if (dir == attachedFace || dir == attachedFace.getOpposite()) continue;
			if (random.nextInt(3) != 0) continue; // Sparse thickness

			for (int t = 1; t < thickness; t++) {
				BlockPos thickPos = center.relative(dir, t);
				if (!box.isInside(thickPos)) break;
				if (!level.getBlockState(thickPos).isAir()) break;

				// Check still attached to surface
				boolean attached = false;
				Direction newAttach = null;
				for (Direction check : Direction.values()) {
					BlockPos checkPos = thickPos.relative(check);
					if (box.isInside(checkPos) && level.getBlockState(checkPos).isSolid()) {
						attached = true;
						newAttach = check;
						break;
					}
				}

				if (attached) {
					BlockState vine = vineBlock;
					if (vineBlock.getBlock() instanceof MultifaceBlock && newAttach != null) {
						vine = vineBlock.setValue(MultifaceBlock.getFaceProperty(newAttach), true);
					}
					level.setBlock(thickPos, vine, 2);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void applyCeilingHangingVines(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, BlockPos stairwellCenter, int stairwellRadius,
			RandomSource random, long noiseSeed) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		BlockState hyphaeBlock = BlockInit.IMBUED_CYAN_MUSHROOM_HYPHAE.get().defaultBlockState();
		int ceilingY = origin.getY() + height;
		double noiseScale = 0.1;

		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				int wx = origin.getX() + x;
				int wz = origin.getZ() + z;

				if (isInStairwell(wx, wz, stairwellCenter, stairwellRadius)) continue;

				// Use noise to create patchy distribution
				double noise = octavePerlin(wx * noiseScale, wz * noiseScale, noiseSeed + 77777, 2, 0.5);
				if (noise < 0.3) continue;

				// Random chance for vine placement
				if (random.nextFloat() > 0.15f) continue;

				// Check ceiling is solid
				pos.set(wx, ceilingY, wz);
				if (!box.isInside(pos)) continue;
				if (!level.getBlockState(pos).isSolid()) continue;

				// Place hanging vine (long vines for main chamber)
				int vineLength = 4 + random.nextInt(6);
				for (int y = 1; y <= vineLength; y++) {
					int vy = ceilingY - y;
					if (vy <= origin.getY()) break;

					pos.set(wx, vy, wz);
					if (!box.isInside(pos)) break;
					if (!level.getBlockState(pos).isAir()) break;

					level.setBlock(pos, hyphaeBlock, 2);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void applySmallBushes(WorldGenLevel level, BoundingBox box, BlockPos origin,
			int width, int depth, int height, BlockPos stairwellCenter, int stairwellRadius,
			RandomSource random, long noiseSeed) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		BlockState hyphaeBlock = BlockInit.IMBUED_CYAN_MUSHROOM_HYPHAE.get().defaultBlockState();
		int floorY = origin.getY();
		double noiseScale = 0.12;

		for (int x = 0; x < width; x++) {
			for (int z = 0; z < depth; z++) {
				int wx = origin.getX() + x;
				int wz = origin.getZ() + z;

				if (isInStairwell(wx, wz, stairwellCenter, stairwellRadius)) continue;

				// Use noise to create patchy distribution
				double noise = octavePerlin(wx * noiseScale, wz * noiseScale, noiseSeed + 88888, 2, 0.5);
				if (noise < 0.5) continue;

				// Random chance for bush placement (sparse)
				if (random.nextFloat() > 0.06f) continue;

				// Find top of floor (may have grass mound)
				int startY = floorY + 1;
				for (int y = floorY + 4; y > floorY; y--) {
					pos.set(wx, y, wz);
					if (box.isInside(pos) && level.getBlockState(pos).isSolid()) {
						startY = y + 1;
						break;
					}
				}

				// Place small bush (1-2 blocks tall, mostly 1)
				int bushHeight = random.nextFloat() < 0.7f ? 1 : 2;
				for (int y = 0; y < bushHeight; y++) {
					int by = startY + y;
					pos.set(wx, by, wz);
					if (!box.isInside(pos)) break;
					if (!level.getBlockState(pos).isAir()) break;

					level.setBlock(pos, hyphaeBlock, 2);
				}
			}
		}
	}

	private static boolean isInStairwell(int x, int z, BlockPos center, int radius) {
		if (center == null || radius <= 0) return false;
		int dx = x - center.getX();
		int dz = z - center.getZ();
		return dx * dx + dz * dz <= radius * radius;
	}
}
