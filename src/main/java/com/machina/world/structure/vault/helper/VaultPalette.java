package com.machina.world.structure.vault.helper;

import java.util.List;

import com.machina.registration.init.BlockInit;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class VaultPalette {

	// Perlin noise permutation table
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
	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		double u = h < 8 ? x : y;
		double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	private static double perlin3D(double x, double y, double z) {
		int X = (int) Math.floor(x) & 255;
		int Y = (int) Math.floor(y) & 255;
		int Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x);
		y -= Math.floor(y);
		z -= Math.floor(z);
		double u = fade(x), v = fade(y), w = fade(z);
		int A = PERM[X] + Y, AA = PERM[A] + Z, AB = PERM[A + 1] + Z;
		int B = PERM[X + 1] + Y, BA = PERM[B] + Z, BB = PERM[B + 1] + Z;
		return lerp(w, lerp(v, lerp(u, grad(PERM[AA], x, y, z), grad(PERM[BA], x - 1, y, z)),
							   lerp(u, grad(PERM[AB], x, y - 1, z), grad(PERM[BB], x - 1, y - 1, z))),
					   lerp(v, lerp(u, grad(PERM[AA + 1], x, y, z - 1), grad(PERM[BA + 1], x - 1, y, z - 1)),
							   lerp(u, grad(PERM[AB + 1], x, y - 1, z - 1), grad(PERM[BB + 1], x - 1, y - 1, z - 1))));
	}

	private final List<BlockState> baseBlocks;
	private final List<BlockState> stairBlocks;
	private final List<BlockState> slabBlocks;
	private final List<BlockState> wallBlocks;
	private final List<BlockState> pillarBlocks;

	public VaultPalette(List<BlockState> baseBlocks, List<BlockState> stairBlocks,
			List<BlockState> slabBlocks, List<BlockState> wallBlocks, List<BlockState> pillarBlocks) {
		this.baseBlocks = baseBlocks;
		this.stairBlocks = stairBlocks;
		this.slabBlocks = slabBlocks;
		this.wallBlocks = wallBlocks;
		this.pillarBlocks = pillarBlocks;
	}

	public static VaultPalette forLevel(int level) {
		return switch (level) {
			case 0 -> createUpperPalette();
			case 1 -> createTransitionalPalette();
			case 2 -> createDarkPalette();
			case 3 -> createAncientPalette();
			default -> createUpperPalette();
		};
	}

	private static VaultPalette createUpperPalette() {
		return new VaultPalette(
			List.of(
				BlockInit.RUINED_VAULT_STONE.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_BRICKS.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_BRICKS_CRACKED.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_BRICKS_MOSSY.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_TILES.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_STAIRS.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_TILES_STAIRS.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_SLAB.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_TILES_SLAB.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_WALL.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_TILES_WALL.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_PILLAR.get().defaultBlockState()
			)
		);
	}

	private static VaultPalette createTransitionalPalette() {
		return new VaultPalette(
			List.of(
				BlockInit.RUINED_VAULT_BRICKS.get().defaultBlockState(),
				BlockInit.RUINED_VAULT_BRICKS_CRACKED.get().defaultBlockState(),
				BlockInit.VAULT_STONE.get().defaultBlockState(),
				BlockInit.VAULT_BRICKS.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_STAIRS.get().defaultBlockState(),
				BlockInit.VAULT_BRICKS_STAIRS.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_SLAB.get().defaultBlockState(),
				BlockInit.VAULT_BRICKS_SLAB.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_BRICKS_WALL.get().defaultBlockState(),
				BlockInit.VAULT_BRICKS_WALL.get().defaultBlockState()
			),
			List.of(
				BlockInit.RUINED_VAULT_PILLAR.get().defaultBlockState(),
				BlockInit.VAULT_PILLAR.get().defaultBlockState()
			)
		);
	}

	private static VaultPalette createDarkPalette() {
		return new VaultPalette(
			List.of(
				BlockInit.VAULT_STONE.get().defaultBlockState(),
				BlockInit.VAULT_BRICKS.get().defaultBlockState(),
				BlockInit.VAULT_TILES.get().defaultBlockState(),
				BlockInit.DEEP_VAULT_TILES.get().defaultBlockState()
			),
			List.of(
				BlockInit.VAULT_BRICKS_STAIRS.get().defaultBlockState(),
				BlockInit.VAULT_TILES_STAIRS.get().defaultBlockState()
			),
			List.of(
				BlockInit.VAULT_BRICKS_SLAB.get().defaultBlockState(),
				BlockInit.VAULT_TILES_SLAB.get().defaultBlockState()
			),
			List.of(
				BlockInit.VAULT_BRICKS_WALL.get().defaultBlockState(),
				BlockInit.VAULT_TILES_WALL.get().defaultBlockState()
			),
			List.of(
				BlockInit.VAULT_PILLAR.get().defaultBlockState()
			)
		);
	}

	private static VaultPalette createAncientPalette() {
		return new VaultPalette(
			List.of(
				BlockInit.DEEP_VAULT_TILES.get().defaultBlockState()
			),
			List.of(
				BlockInit.DEEP_VAULT_TILES_STAIRS.get().defaultBlockState()
			),
			List.of(
				BlockInit.DEEP_VAULT_TILES_SLAB.get().defaultBlockState()
			),
			List.of(
				BlockInit.DEEP_VAULT_TILES_WALL.get().defaultBlockState()
			),
			List.of(
				BlockInit.DEEP_VAULT_PILLAR.get().defaultBlockState()
			)
		);
	}
	
	public List<BlockState> getBaseBlocks() {
		return baseBlocks;
	}

	public BlockState getRandomBase(int x, int y, int z) {
		return getPatchy(baseBlocks, x, y, z);
	}

	public BlockState getRandomStair(int x, int y, int z, Direction facing, Half half) {
		BlockState state = getPatchy(stairBlocks, x, y, z);
		if (state.getBlock() instanceof StairBlock) {
			return state.setValue(StairBlock.FACING, facing)
					.setValue(StairBlock.HALF, half)
					.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT);
		}
		return state;
	}

	public BlockState getRandomSlab(int x, int y, int z, SlabType type) {
		BlockState state = getPatchy(slabBlocks, x, y, z);
		if (state.getBlock() instanceof SlabBlock) {
			return state.setValue(SlabBlock.TYPE, type);
		}
		return state;
	}

	public BlockState getRandomWall(int x, int y, int z) {
		return getPatchy(wallBlocks, x, y, z);
	}

	public BlockState getRandomPillar(int x, int y, int z) {
		return getPatchy(pillarBlocks, x, y, z);
	}

	private static BlockState getPatchy(List<BlockState> blocks, int x, int y, int z) {
		if (blocks.isEmpty()) return BlockInit.VAULT_BRICKS.get().defaultBlockState();
		if (blocks.size() == 1) return blocks.get(0);
		double noiseScale = 0.12;
		double fuzzScale = 0.4;
		double noise = perlin3D(x * noiseScale, y * noiseScale, z * noiseScale);
		double fuzz = perlin3D(x * fuzzScale + 100, y * fuzzScale + 100, z * fuzzScale + 100) * 0.25;
		double normalized = (noise + fuzz + 1.0) * 0.5;
		normalized = Math.max(0, Math.min(1, normalized));
		int index = (int) (normalized * blocks.size());
		if (index >= blocks.size()) index = blocks.size() - 1;
		if (index < 0) index = 0;
		return blocks.get(index);
	}

	public void writeToNbt(CompoundTag tag) {
		tag.put("BaseBlocks", writeBlockList(baseBlocks));
		tag.put("StairBlocks", writeBlockList(stairBlocks));
		tag.put("SlabBlocks", writeBlockList(slabBlocks));
		tag.put("WallBlocks", writeBlockList(wallBlocks));
		tag.put("PillarBlocks", writeBlockList(pillarBlocks));
	}

	public static VaultPalette readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		return new VaultPalette(
			readBlockList(tag.getList("BaseBlocks", Tag.TAG_COMPOUND), registries),
			readBlockList(tag.getList("StairBlocks", Tag.TAG_COMPOUND), registries),
			readBlockList(tag.getList("SlabBlocks", Tag.TAG_COMPOUND), registries),
			readBlockList(tag.getList("WallBlocks", Tag.TAG_COMPOUND), registries),
			readBlockList(tag.getList("PillarBlocks", Tag.TAG_COMPOUND), registries)
		);
	}

	private static ListTag writeBlockList(List<BlockState> blocks) {
		ListTag list = new ListTag();
		for (BlockState state : blocks) {
			list.add(NbtUtils.writeBlockState(state));
		}
		return list;
	}

	private static List<BlockState> readBlockList(ListTag list, HolderLookup.Provider registries) {
		return list.stream()
			.map(t -> NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), (CompoundTag) t))
			.toList();
	}
}
