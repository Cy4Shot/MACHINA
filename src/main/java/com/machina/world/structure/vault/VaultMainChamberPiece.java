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

public class VaultMainChamberPiece extends StructurePiece {

	public static final int CHAMBER_RADIUS = 20;
	public static final int CHAMBER_HEIGHT = 12;
	private static final int STAIR_OPENING_RADIUS = 5;
	private static final int CORRIDOR_HALF_WIDTH = VaultCorridorPiece.CORRIDOR_WIDTH / 2 + 1;

	private final BlockPos center;
	private final List<BlockState> blocks;
	private final boolean[] corridorDirections;

	public VaultMainChamberPiece(BlockPos center, List<BlockState> blocks) {
		this(center, blocks, new boolean[]{false, false, false, false});
	}

	public VaultMainChamberPiece(BlockPos center, List<BlockState> blocks, boolean[] corridorDirections) {
		super(StructureInit.VAULT_MAIN_CHAMBER.get(), 0, createBoundingBox(center));
		this.center = center;
		this.blocks = blocks;
		this.corridorDirections = corridorDirections;
	}

	public VaultMainChamberPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_MAIN_CHAMBER.get(), tag);
		this.center = NbtUtils.readBlockPos(tag, "Center").orElse(BlockPos.ZERO);
		ListTag blockList = tag.getList("Blocks", Tag.TAG_COMPOUND);
		this.blocks = blockList.stream()
			.map(t -> NbtUtils.readBlockState(ctx.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), (CompoundTag) t))
			.toList();
		byte[] dirs = tag.getByteArray("CorridorDirs");
		this.corridorDirections = new boolean[4];
		for (int i = 0; i < Math.min(dirs.length, 4); i++) {
			this.corridorDirections[i] = dirs[i] != 0;
		}
	}

	private static BoundingBox createBoundingBox(BlockPos center) {
		return new BoundingBox(
			center.getX() - CHAMBER_RADIUS - 2, center.getY() - 1, center.getZ() - CHAMBER_RADIUS - 2,
			center.getX() + CHAMBER_RADIUS + 2, center.getY() + CHAMBER_HEIGHT + 2, center.getZ() + CHAMBER_RADIUS + 2
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Center", NbtUtils.writeBlockPos(center));
		ListTag blockList = new ListTag();
		for (BlockState state : blocks) {
			blockList.add(NbtUtils.writeBlockState(state));
		}
		tag.put("Blocks", blockList);
		byte[] dirs = new byte[4];
		for (int i = 0; i < 4; i++) {
			dirs[i] = (byte) (corridorDirections[i] ? 1 : 0);
		}
		tag.putByteArray("CorridorDirs", dirs);
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

		int cx = center.getX();
		int cz = center.getZ();
		int floorY = center.getY();

		for (int x = cx - CHAMBER_RADIUS; x <= cx + CHAMBER_RADIUS; x++) {
			for (int z = cz - CHAMBER_RADIUS; z <= cz + CHAMBER_RADIUS; z++) {
				if (!box.isInside(x, floorY, z)) continue;

				int dx = x - cx;
				int dz = z - cz;
				float dist = (float) Math.sqrt(dx * dx + dz * dz);

				if (dist > CHAMBER_RADIUS) continue;

				boolean isWallZone = dist > CHAMBER_RADIUS - 2;
				boolean isStaircaseOpening = dist < STAIR_OPENING_RADIUS;

				// Floor
				level.setBlock(new BlockPos(x, floorY, z), getPatchyBlock(x, floorY, z), 2);

				// Clear interior
				for (int y = floorY + 1; y < floorY + CHAMBER_HEIGHT; y++) {
					level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
				}

				// Ceiling - leave opening for staircase
				BlockPos ceilingPos = new BlockPos(x, floorY + CHAMBER_HEIGHT, z);
				if (isStaircaseOpening) {
					level.setBlock(ceilingPos, Blocks.AIR.defaultBlockState(), 2);
				} else {
					level.setBlock(ceilingPos, getPatchyBlock(x, floorY + CHAMBER_HEIGHT, z), 2);
				}

				// Walls at edge
				if (isWallZone) {
					for (int y = floorY + 1; y < floorY + CHAMBER_HEIGHT; y++) {
						BlockPos wallPos = new BlockPos(x, y, z);

						// Check if this is a corridor opening
						boolean isCorridorOpening = false;
						for (Direction dir : Direction.Plane.HORIZONTAL) {
							if (corridorDirections[dir.get2DDataValue()]) {
								int perpDist;
								int parallelDist;
								if (dir.getAxis() == Direction.Axis.X) {
									perpDist = Math.abs(dz);
									parallelDist = dir == Direction.EAST ? dx : -dx;
								} else {
									perpDist = Math.abs(dx);
									parallelDist = dir == Direction.SOUTH ? dz : -dz;
								}

								if (perpDist <= CORRIDOR_HALF_WIDTH && parallelDist > CHAMBER_RADIUS - 3 && y <= floorY + VaultCorridorPiece.CORRIDOR_HEIGHT) {
									isCorridorOpening = true;
									break;
								}
							}
						}

						if (isCorridorOpening) {
							level.setBlock(wallPos, Blocks.AIR.defaultBlockState(), 2);
						} else if (level.getBlockState(wallPos).isAir()) {
							level.setBlock(wallPos, getPatchyBlock(x, y, z), 2);
						}
					}
				}

				// Pillars at regular intervals (but not in staircase area)
				if (!isStaircaseOpening && (dx + 100) % 8 == 0 && (dz + 100) % 8 == 0 && dist < CHAMBER_RADIUS - 4) {
					for (int y = floorY + 1; y < floorY + CHAMBER_HEIGHT; y++) {
						level.setBlock(new BlockPos(x, y, z), getPatchyBlock(x, y, z), 2);
					}
				}
			}
		}
	}
}
