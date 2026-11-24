package com.machina.world.structure.vault;

import java.util.List;

import com.machina.registration.init.StructureInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VaultEntrancePiece extends StructurePiece {

	private static final int BUILDING_SIZE = 6;
	private static final int BUILDING_HEIGHT = 5;
	private static final int CLEAR_HEIGHT = 20;

	private final int centerX;
	private final int centerZ;
	private final int surfaceY;
	private final List<BlockState> blocks;

	public VaultEntrancePiece(int centerX, int centerZ, int surfaceY, List<BlockState> blocks) {
		super(StructureInit.VAULT_ENTRANCE.get(), 0, createBoundingBox(centerX, centerZ, surfaceY));
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.surfaceY = surfaceY;
		this.blocks = blocks;
	}

	public VaultEntrancePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_ENTRANCE.get(), tag);
		this.centerX = tag.getInt("CenterX");
		this.centerZ = tag.getInt("CenterZ");
		this.surfaceY = tag.getInt("SurfaceY");
		ListTag blockList = tag.getList("Blocks", Tag.TAG_COMPOUND);
		this.blocks = blockList.stream()
			.map(t -> NbtUtils.readBlockState(ctx.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), (CompoundTag) t))
			.toList();
	}

	private static BoundingBox createBoundingBox(int cx, int cz, int surfaceY) {
		return new BoundingBox(
			cx - BUILDING_SIZE - 2, surfaceY - 1, cz - BUILDING_SIZE - 2,
			cx + BUILDING_SIZE + 2, surfaceY + CLEAR_HEIGHT, cz + BUILDING_SIZE + 2
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.putInt("CenterX", centerX);
		tag.putInt("CenterZ", centerZ);
		tag.putInt("SurfaceY", surfaceY);
		ListTag blockList = new ListTag();
		for (BlockState state : blocks) {
			blockList.add(NbtUtils.writeBlockState(state));
		}
		tag.put("Blocks", blockList);
	}

	private BlockState getPatchyBlock(int x, int y, int z) {
		if (blocks.size() == 1) return blocks.get(0);
		int hash = ((x * 73856093) ^ (y * 19349663) ^ (z * 83492791)) / 7;
		int index = Math.abs(hash) % blocks.size();
		return blocks.get(index);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		if (blocks.isEmpty()) return;

		// First: clear entire building area including everything above
		for (int x = centerX - BUILDING_SIZE - 1; x <= centerX + BUILDING_SIZE + 1; x++) {
			for (int z = centerZ - BUILDING_SIZE - 1; z <= centerZ + BUILDING_SIZE + 1; z++) {
				for (int y = surfaceY; y <= surfaceY + CLEAR_HEIGHT; y++) {
					if (!box.isInside(x, y, z)) continue;
					level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}

		// Build entrance structure
		for (int x = centerX - BUILDING_SIZE; x <= centerX + BUILDING_SIZE; x++) {
			for (int z = centerZ - BUILDING_SIZE; z <= centerZ + BUILDING_SIZE; z++) {
				if (!box.isInside(x, surfaceY, z)) continue;

				boolean isWall = x == centerX - BUILDING_SIZE || x == centerX + BUILDING_SIZE ||
								 z == centerZ - BUILDING_SIZE || z == centerZ + BUILDING_SIZE;
				boolean isCorner = (x == centerX - BUILDING_SIZE || x == centerX + BUILDING_SIZE) &&
								   (z == centerZ - BUILDING_SIZE || z == centerZ + BUILDING_SIZE);

				// Foundation/floor
				level.setBlock(new BlockPos(x, surfaceY - 1, z), getPatchyBlock(x, surfaceY - 1, z), 2);
				level.setBlock(new BlockPos(x, surfaceY, z), getPatchyBlock(x, surfaceY, z), 2);

				// Walls
				if (isWall) {
					for (int y = surfaceY + 1; y <= surfaceY + BUILDING_HEIGHT; y++) {
						// Door openings on each wall (3 blocks wide, 3 high)
						boolean isDoorX = Math.abs(x - centerX) <= 1 && (z == centerZ - BUILDING_SIZE || z == centerZ + BUILDING_SIZE);
						boolean isDoorZ = Math.abs(z - centerZ) <= 1 && (x == centerX - BUILDING_SIZE || x == centerX + BUILDING_SIZE);
						boolean isDoor = (isDoorX || isDoorZ) && !isCorner && y <= surfaceY + 3;

						if (isDoor) {
							level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
						} else {
							level.setBlock(new BlockPos(x, y, z), getPatchyBlock(x, y, z), 2);
						}
					}
				}

				// Roof
				level.setBlock(new BlockPos(x, surfaceY + BUILDING_HEIGHT + 1, z), getPatchyBlock(x, surfaceY + BUILDING_HEIGHT + 1, z), 2);
			}
		}

		// Staircase opening in center of floor (connects to spiral staircase below)
		int holeRadius = 3;
		for (int dx = -holeRadius; dx <= holeRadius; dx++) {
			for (int dz = -holeRadius; dz <= holeRadius; dz++) {
				if (dx * dx + dz * dz <= holeRadius * holeRadius) {
					int hx = centerX + dx;
					int hz = centerZ + dz;
					if (box.isInside(hx, surfaceY, hz)) {
						level.setBlock(new BlockPos(hx, surfaceY, hz), Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}
	}
}
