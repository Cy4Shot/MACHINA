package com.machina.world.structure.vault;

import java.util.List;

import com.machina.registration.init.StructureInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class VaultCorridorPiece extends StructurePiece {

	public static final int CORRIDOR_WIDTH = 5;
	public static final int CORRIDOR_HEIGHT = 5;

	private final BlockPos start;
	private final Direction direction;
	private final int length;
	private final int floorY;
	private final List<BlockState> blocks;

	public VaultCorridorPiece(BlockPos start, Direction direction, int length, int floorY, List<BlockState> blocks) {
		super(StructureInit.VAULT_CORRIDOR.get(), 0, createBoundingBox(start, direction, length, floorY));
		this.start = start;
		this.direction = direction;
		this.length = length;
		this.floorY = floorY;
		this.blocks = blocks;
	}

	public VaultCorridorPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_CORRIDOR.get(), tag);
		this.start = NbtUtils.readBlockPos(tag, "Start").orElse(BlockPos.ZERO);
		this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
		this.length = tag.getInt("Length");
		this.floorY = tag.getInt("FloorY");
		ListTag blockList = tag.getList("Blocks", Tag.TAG_COMPOUND);
		this.blocks = blockList.stream()
			.map(t -> NbtUtils.readBlockState(ctx.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), (CompoundTag) t))
			.toList();
	}

	private static BoundingBox createBoundingBox(BlockPos start, Direction dir, int length, int floorY) {
		int perpX = dir == Direction.NORTH || dir == Direction.SOUTH ? 1 : 0;
		int perpZ = dir == Direction.EAST || dir == Direction.WEST ? 1 : 0;
		int halfWidth = CORRIDOR_WIDTH / 2 + 1;

		int endX = start.getX() + dir.getStepX() * length;
		int endZ = start.getZ() + dir.getStepZ() * length;

		int minX = Math.min(start.getX(), endX) - perpX * halfWidth;
		int maxX = Math.max(start.getX(), endX) + perpX * halfWidth;
		int minZ = Math.min(start.getZ(), endZ) - perpZ * halfWidth;
		int maxZ = Math.max(start.getZ(), endZ) + perpZ * halfWidth;

		return new BoundingBox(minX, floorY - 1, minZ, maxX, floorY + CORRIDOR_HEIGHT + 2, maxZ);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Start", NbtUtils.writeBlockPos(start));
		tag.putInt("Direction", direction.get2DDataValue());
		tag.putInt("Length", length);
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

		int perpX = direction == Direction.NORTH || direction == Direction.SOUTH ? 1 : 0;
		int perpZ = direction == Direction.EAST || direction == Direction.WEST ? 1 : 0;
		int halfWidth = CORRIDOR_WIDTH / 2;

		for (int i = 0; i <= length; i++) {
			int corridorX = start.getX() + direction.getStepX() * i;
			int corridorZ = start.getZ() + direction.getStepZ() * i;

			for (int w = -halfWidth - 1; w <= halfWidth + 1; w++) {
				int wx = corridorX + perpX * w;
				int wz = corridorZ + perpZ * w;

				if (!box.isInside(wx, floorY, wz)) continue;

				boolean isWall = Math.abs(w) == halfWidth + 1;

				// Floor
				level.setBlock(new BlockPos(wx, floorY, wz), getPatchyBlock(wx, floorY, wz), 2);

				// Walls and interior
				for (int y = floorY + 1; y <= floorY + CORRIDOR_HEIGHT; y++) {
					BlockPos pos = new BlockPos(wx, y, wz);
					if (isWall) {
						level.setBlock(pos, getPatchyBlock(wx, y, wz), 2);
					} else {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling
				level.setBlock(new BlockPos(wx, floorY + CORRIDOR_HEIGHT + 1, wz), getPatchyBlock(wx, floorY + CORRIDOR_HEIGHT + 1, wz), 2);
			}
		}
	}

	public BlockPos getEndPosition() {
		return new BlockPos(
			start.getX() + direction.getStepX() * length,
			floorY,
			start.getZ() + direction.getStepZ() * length
		);
	}

	public Direction getDirection() {
		return direction;
	}
}
