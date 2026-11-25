package com.machina.world.structure.vault;

import com.machina.registration.init.StructureInit;
import com.machina.world.structure.vault.helper.VaultPalette;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VaultEntrancePiece extends StructurePiece {

	private static final int BUILDING_SIZE = 7;
	private static final int BUILDING_HEIGHT = 6;
	private static final int CLEAR_HEIGHT = 30;
	private static final int WALL_THICKNESS = 2;
	private static final int STAIR_RADIUS = VaultStaircasePiece.SHAFT_RADIUS;

	private final int centerX;
	private final int centerZ;
	private final int surfaceY;
	private final VaultPalette palette;
	private final int blockSize;

	public VaultEntrancePiece(int centerX, int centerZ, int surfaceY, VaultPalette palette, int blockSize) {
		super(StructureInit.VAULT_ENTRANCE.get(), 0, createBoundingBox(centerX, centerZ, surfaceY));
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.surfaceY = surfaceY;
		this.palette = palette;
		this.blockSize = blockSize;
	}

	public VaultEntrancePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_ENTRANCE.get(), tag);
		this.centerX = tag.getInt("CenterX");
		this.centerZ = tag.getInt("CenterZ");
		this.surfaceY = tag.getInt("SurfaceY");
		this.blockSize = tag.getInt("BlockSize");
		if (tag.contains("Palette")) {
			this.palette = VaultPalette.readFromNbt(tag.getCompound("Palette"), ctx.registryAccess());
		} else {
			this.palette = VaultPalette.forLevel(0);
		}
	}

	private static BoundingBox createBoundingBox(int cx, int cz, int surfaceY) {
		int pathLength = 20;
		return new BoundingBox(
			cx - BUILDING_SIZE - pathLength, surfaceY - 5, cz - BUILDING_SIZE - pathLength,
			cx + BUILDING_SIZE + pathLength, surfaceY + CLEAR_HEIGHT, cz + BUILDING_SIZE + pathLength
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.putInt("CenterX", centerX);
		tag.putInt("CenterZ", centerZ);
		tag.putInt("SurfaceY", surfaceY);
		tag.putInt("BlockSize", blockSize);
		CompoundTag paletteTag = new CompoundTag();
		palette.writeToNbt(paletteTag);
		tag.put("Palette", paletteTag);
	}

	private boolean hasRoadInDirection(Direction dir) {
		if (blockSize <= 0) return true;

		int roadSpacing = Math.max(2, blockSize / 16);
		int chunkX = centerX >> 4;
		int chunkZ = centerZ >> 4;

		switch (dir) {
			case NORTH, SOUTH -> {
				for (int offset = -1; offset <= 1; offset++) {
					if (Math.floorMod(chunkX + offset, roadSpacing) == 0) return true;
				}
				return false;
			}
			case EAST, WEST -> {
				for (int offset = -1; offset <= 1; offset++) {
					if (Math.floorMod(chunkZ + offset, roadSpacing) == 0) return true;
				}
				return false;
			}
			default -> { return false; }
		}
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		// Clear everything above and around the entrance, BUT NOT the stair shaft area
		for (int x = centerX - BUILDING_SIZE - 2; x <= centerX + BUILDING_SIZE + 2; x++) {
			for (int z = centerZ - BUILDING_SIZE - 2; z <= centerZ + BUILDING_SIZE + 2; z++) {
				int dx = x - centerX;
				int dz = z - centerZ;
				// Skip the stair shaft area - don't clear what the staircase built
				if (dx * dx + dz * dz <= (STAIR_RADIUS + 2) * (STAIR_RADIUS + 2)) continue;

				for (int y = surfaceY; y <= surfaceY + CLEAR_HEIGHT; y++) {
					if (!box.isInside(x, y, z)) continue;
					level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}

		// Build solid foundation below the structure - use base blocks
		for (int x = centerX - BUILDING_SIZE; x <= centerX + BUILDING_SIZE; x++) {
			for (int z = centerZ - BUILDING_SIZE; z <= centerZ + BUILDING_SIZE; z++) {
				for (int y = surfaceY - 2; y < surfaceY; y++) {
					if (!box.isInside(x, y, z)) continue;
					int dx = x - centerX;
					int dz = z - centerZ;
					// Don't fill foundation where staircase goes
					if (dx * dx + dz * dz <= (STAIR_RADIUS + 1) * (STAIR_RADIUS + 1)) continue;
					level.setBlock(new BlockPos(x, y, z), palette.getRandomBase(x, y, z), 2);
				}
			}
		}

		// Pick ONE door direction (first available road direction)
		Direction doorDir = null;
		if (hasRoadInDirection(Direction.NORTH)) doorDir = Direction.NORTH;
		else if (hasRoadInDirection(Direction.SOUTH)) doorDir = Direction.SOUTH;
		else if (hasRoadInDirection(Direction.EAST)) doorDir = Direction.EAST;
		else if (hasRoadInDirection(Direction.WEST)) doorDir = Direction.WEST;
		else doorDir = Direction.NORTH;

		boolean hasNorthDoor = doorDir == Direction.NORTH;
		boolean hasSouthDoor = doorDir == Direction.SOUTH;
		boolean hasEastDoor = doorDir == Direction.EAST;
		boolean hasWestDoor = doorDir == Direction.WEST;

		// Build the entrance building
		for (int x = centerX - BUILDING_SIZE; x <= centerX + BUILDING_SIZE; x++) {
			for (int z = centerZ - BUILDING_SIZE; z <= centerZ + BUILDING_SIZE; z++) {
				int dx = x - centerX;
				int dz = z - centerZ;

				boolean isOuterWall = Math.abs(dx) >= BUILDING_SIZE - WALL_THICKNESS + 1 ||
									  Math.abs(dz) >= BUILDING_SIZE - WALL_THICKNESS + 1;
				boolean isStairOpening = dx * dx + dz * dz <= STAIR_RADIUS * STAIR_RADIUS;

				// Floor - use base blocks
				if (box.isInside(x, surfaceY, z)) {
					if (isStairOpening) {
						level.setBlock(new BlockPos(x, surfaceY, z), Blocks.AIR.defaultBlockState(), 2);
					} else {
						level.setBlock(new BlockPos(x, surfaceY, z), palette.getRandomBase(x, surfaceY, z), 2);
					}
				}

				// Walls and interior
				for (int y = surfaceY + 1; y <= surfaceY + BUILDING_HEIGHT; y++) {
					if (!box.isInside(x, y, z)) continue;
					BlockPos pos = new BlockPos(x, y, z);

					if (isOuterWall) {
						// Door openings (3 wide, 3 high, through entire wall thickness)
						boolean isDoorArea = false;
						if (y <= surfaceY + 3) {
							if (dz <= -BUILDING_SIZE + WALL_THICKNESS && hasNorthDoor && Math.abs(dx) <= 1) isDoorArea = true;
							if (dz >= BUILDING_SIZE - WALL_THICKNESS && hasSouthDoor && Math.abs(dx) <= 1) isDoorArea = true;
							if (dx >= BUILDING_SIZE - WALL_THICKNESS && hasEastDoor && Math.abs(dz) <= 1) isDoorArea = true;
							if (dx <= -BUILDING_SIZE + WALL_THICKNESS && hasWestDoor && Math.abs(dz) <= 1) isDoorArea = true;
						}

						if (isDoorArea) {
							level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
						} else {
							// Walls - use base blocks
							level.setBlock(pos, palette.getRandomBase(x, y, z), 2);
						}
					} else if (!isStairOpening) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Roof - use base blocks
				if (box.isInside(x, surfaceY + BUILDING_HEIGHT + 1, z)) {
					level.setBlock(new BlockPos(x, surfaceY + BUILDING_HEIGHT + 1, z), palette.getRandomBase(x, surfaceY + BUILDING_HEIGHT + 1, z), 2);
				}
			}
		}

		// Build paths from doors to roads
		buildPathToRoad(level, box, Direction.NORTH, hasNorthDoor);
		buildPathToRoad(level, box, Direction.SOUTH, hasSouthDoor);
		buildPathToRoad(level, box, Direction.EAST, hasEastDoor);
		buildPathToRoad(level, box, Direction.WEST, hasWestDoor);
	}

	private void buildPathToRoad(WorldGenLevel level, BoundingBox box, Direction dir, boolean hasDoor) {
		if (!hasDoor) return;

		int pathLength = 15;
		int startX = centerX + dir.getStepX() * (BUILDING_SIZE + 1);
		int startZ = centerZ + dir.getStepZ() * (BUILDING_SIZE + 1);

		for (int i = 0; i <= pathLength; i++) {
			int px = startX + dir.getStepX() * i;
			int pz = startZ + dir.getStepZ() * i;

			// 3-wide path
			for (int w = -1; w <= 1; w++) {
				int wx = px + (dir.getAxis() == Direction.Axis.Z ? w : 0);
				int wz = pz + (dir.getAxis() == Direction.Axis.X ? w : 0);

				if (!box.isInside(wx, surfaceY, wz)) continue;

				// Place path block - use base blocks
				level.setBlock(new BlockPos(wx, surfaceY, wz), palette.getRandomBase(wx, surfaceY, wz), 2);

				// Clear above the path
				for (int y = surfaceY + 1; y <= surfaceY + 4; y++) {
					if (box.isInside(wx, y, wz)) {
						level.setBlock(new BlockPos(wx, y, wz), Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}
	}
}
