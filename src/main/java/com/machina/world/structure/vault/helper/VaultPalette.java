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
		int px = Math.floorDiv(x, 4);
		int py = Math.floorDiv(y, 4);
		int pz = Math.floorDiv(z, 4);
		int hash = ((px * 73856093) ^ (py * 19349663) ^ (pz * 83492791));
		int index = (hash & 0x7FFFFFFF) % blocks.size();
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
