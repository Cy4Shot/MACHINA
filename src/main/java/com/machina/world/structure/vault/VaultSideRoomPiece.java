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

public class VaultSideRoomPiece extends StructurePiece {

	private static final int ROOM_HEIGHT = 6;

	private final BlockPos center;
	private final int size;
	private final int floorY;
	private final List<BlockState> blocks;

	public VaultSideRoomPiece(BlockPos center, int size, int floorY, List<BlockState> blocks) {
		super(StructureInit.VAULT_SIDE_ROOM.get(), 0, createBoundingBox(center, size, floorY));
		this.center = center;
		this.size = size;
		this.floorY = floorY;
		this.blocks = blocks;
	}

	public VaultSideRoomPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_SIDE_ROOM.get(), tag);
		this.center = NbtUtils.readBlockPos(tag, "Center").orElse(BlockPos.ZERO);
		this.size = tag.getInt("Size");
		this.floorY = tag.getInt("FloorY");
		ListTag blockList = tag.getList("Blocks", Tag.TAG_COMPOUND);
		this.blocks = blockList.stream()
			.map(t -> NbtUtils.readBlockState(ctx.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), (CompoundTag) t))
			.toList();
	}

	private static BoundingBox createBoundingBox(BlockPos center, int size, int floorY) {
		int halfSize = size / 2 + 1;
		return new BoundingBox(
			center.getX() - halfSize, floorY - 1, center.getZ() - halfSize,
			center.getX() + halfSize, floorY + ROOM_HEIGHT + 1, center.getZ() + halfSize
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Center", NbtUtils.writeBlockPos(center));
		tag.putInt("Size", size);
		tag.putInt("FloorY", floorY);
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

		int halfSize = size / 2;
		int cx = center.getX();
		int cz = center.getZ();

		for (int rx = -halfSize; rx <= halfSize; rx++) {
			for (int rz = -halfSize; rz <= halfSize; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;

				if (!box.isInside(wx, floorY, wz)) continue;

				boolean isWall = Math.abs(rx) == halfSize || Math.abs(rz) == halfSize;

				// Floor
				level.setBlock(new BlockPos(wx, floorY, wz), getPatchyBlock(wx, floorY, wz), 2);

				// Walls or interior
				for (int y = floorY + 1; y <= floorY + ROOM_HEIGHT; y++) {
					BlockPos pos = new BlockPos(wx, y, wz);
					if (isWall) {
						level.setBlock(pos, getPatchyBlock(wx, y, wz), 2);
					} else {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling
				level.setBlock(new BlockPos(wx, floorY + ROOM_HEIGHT + 1, wz), getPatchyBlock(wx, floorY + ROOM_HEIGHT + 1, wz), 2);
			}
		}
	}
}
