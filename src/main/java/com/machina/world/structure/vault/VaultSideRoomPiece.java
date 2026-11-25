package com.machina.world.structure.vault;

import com.machina.registration.init.StructureInit;
import com.machina.world.structure.vault.helper.VaultOvergrowthHelper;
import com.machina.world.structure.vault.helper.VaultPalette;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VaultSideRoomPiece extends StructurePiece {

	private static final int ROOM_HEIGHT = 6;
	private static final int WALL_THICKNESS = 2;
	private static final int FLOOR_THICKNESS = 2;
	private static final int BEDROCK_SHELL = 1;

	private static final int STAIRWELL_EXCLUSION_RADIUS = VaultStaircasePiece.SHAFT_RADIUS + 4;

	private final BlockPos center;
	private final int size;
	private final int floorY;
	private final VaultPalette palette;
	private final boolean[] corridorDirections;
	private final int levelIndex;
	private final int totalLevels;
	private final VaultOvergrowthHelper.OvergrowthData overgrowthData;
	private final BlockPos staircaseCenter;

	public VaultSideRoomPiece(BlockPos center, int size, int floorY, VaultPalette palette, boolean[] corridorDirections, int levelIndex, int totalLevels, VaultOvergrowthHelper.OvergrowthData overgrowthData, BlockPos staircaseCenter) {
		super(StructureInit.VAULT_SIDE_ROOM.get(), 0, createBoundingBox(center, size, floorY));
		this.center = center;
		this.size = size;
		this.floorY = floorY;
		this.palette = palette;
		this.corridorDirections = corridorDirections;
		this.levelIndex = levelIndex;
		this.totalLevels = totalLevels;
		this.overgrowthData = overgrowthData;
		this.staircaseCenter = staircaseCenter;
	}

	public VaultSideRoomPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_SIDE_ROOM.get(), tag);
		this.center = NbtUtils.readBlockPos(tag, "Center").orElse(BlockPos.ZERO);
		this.size = tag.getInt("Size");
		this.floorY = tag.getInt("FloorY");
		this.levelIndex = tag.getInt("LevelIndex");
		this.totalLevels = tag.getInt("TotalLevels");
		if (tag.contains("Palette")) {
			this.palette = VaultPalette.readFromNbt(tag.getCompound("Palette"), ctx.registryAccess());
		} else {
			this.palette = VaultPalette.forLevel(0);
		}
		byte[] dirs = tag.getByteArray("CorridorDirs");
		this.corridorDirections = new boolean[4];
		for (int i = 0; i < Math.min(dirs.length, 4); i++) {
			this.corridorDirections[i] = dirs[i] != 0;
		}
		if (tag.contains("OvergrowthData")) {
			this.overgrowthData = VaultOvergrowthHelper.OvergrowthData.readFromNbt(tag.getCompound("OvergrowthData"), ctx.registryAccess());
		} else {
			this.overgrowthData = VaultOvergrowthHelper.OvergrowthData.EMPTY;
		}
		this.staircaseCenter = tag.contains("StaircaseCenter") ? NbtUtils.readBlockPos(tag, "StaircaseCenter").orElse(null) : null;
	}

	private static BoundingBox createBoundingBox(BlockPos center, int size, int floorY) {
		int halfSize = size / 2 + WALL_THICKNESS + BEDROCK_SHELL;
		return new BoundingBox(
			center.getX() - halfSize, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, center.getZ() - halfSize,
			center.getX() + halfSize, floorY + ROOM_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, center.getZ() + halfSize
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Center", NbtUtils.writeBlockPos(center));
		tag.putInt("Size", size);
		tag.putInt("FloorY", floorY);
		tag.putInt("LevelIndex", levelIndex);
		tag.putInt("TotalLevels", totalLevels);
		CompoundTag paletteTag = new CompoundTag();
		palette.writeToNbt(paletteTag);
		tag.put("Palette", paletteTag);
		byte[] dirs = new byte[4];
		for (int i = 0; i < 4; i++) {
			dirs[i] = (byte) (corridorDirections[i] ? 1 : 0);
		}
		tag.putByteArray("CorridorDirs", dirs);
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			CompoundTag overgrowthTag = new CompoundTag();
			overgrowthData.writeToNbt(overgrowthTag);
			tag.put("OvergrowthData", overgrowthTag);
		}
		if (staircaseCenter != null) {
			tag.put("StaircaseCenter", NbtUtils.writeBlockPos(staircaseCenter));
		}
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		int halfSize = size / 2;
		int totalHalfSize = halfSize + WALL_THICKNESS;
		int bedrockHalfSize = totalHalfSize + BEDROCK_SHELL;
		int cx = center.getX();
		int cz = center.getZ();

		// First pass: bedrock shell (skip entrance connections)
		int corridorHalfWidth = VaultCorridorPiece.CORRIDOR_WIDTH / 2 + WALL_THICKNESS + 1;
		for (int rx = -bedrockHalfSize; rx <= bedrockHalfSize; rx++) {
			for (int rz = -bedrockHalfSize; rz <= bedrockHalfSize; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;

				boolean isBedrockShell = Math.abs(rx) > totalHalfSize || Math.abs(rz) > totalHalfSize;

				boolean isEntranceConnection = false;
				for (Direction dir : Direction.Plane.HORIZONTAL) {
					if (corridorDirections[dir.get2DDataValue()]) {
						switch (dir) {
							case NORTH -> { if (rz <= -totalHalfSize + 2 && Math.abs(rx) <= corridorHalfWidth) isEntranceConnection = true; }
							case SOUTH -> { if (rz >= totalHalfSize - 2 && Math.abs(rx) <= corridorHalfWidth) isEntranceConnection = true; }
							case WEST -> { if (rx <= -totalHalfSize + 2 && Math.abs(rz) <= corridorHalfWidth) isEntranceConnection = true; }
							case EAST -> { if (rx >= totalHalfSize - 2 && Math.abs(rz) <= corridorHalfWidth) isEntranceConnection = true; }
							default -> {}
						}
					}
				}

				if (isEntranceConnection) continue;

				if (isBedrockShell) {
					for (int y = floorY - FLOOR_THICKNESS - BEDROCK_SHELL; y <= floorY + ROOM_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL; y++) {
						if (!box.isInside(wx, y, wz)) continue;
						level.setBlock(new BlockPos(wx, y, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				} else {
					if (box.isInside(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
					if (box.isInside(wx, floorY + ROOM_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY + ROOM_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				}
			}
		}

		// Second pass: room structure
		for (int rx = -totalHalfSize; rx <= totalHalfSize; rx++) {
			for (int rz = -totalHalfSize; rz <= totalHalfSize; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;

				boolean isWall = Math.abs(rx) > halfSize || Math.abs(rz) > halfSize;
				boolean isInterior = Math.abs(rx) <= halfSize && Math.abs(rz) <= halfSize;

				boolean isEntrance = false;
				if (isWall) {
					int doorHalfWidth = VaultCorridorPiece.CORRIDOR_WIDTH / 2;
					for (Direction dir : Direction.Plane.HORIZONTAL) {
						if (corridorDirections[dir.get2DDataValue()]) {
							switch (dir) {
								case NORTH -> { if (rz <= -halfSize && Math.abs(rx) <= doorHalfWidth) isEntrance = true; }
								case SOUTH -> { if (rz >= halfSize && Math.abs(rx) <= doorHalfWidth) isEntrance = true; }
								case WEST -> { if (rx <= -halfSize && Math.abs(rz) <= doorHalfWidth) isEntrance = true; }
								case EAST -> { if (rx >= halfSize && Math.abs(rz) <= doorHalfWidth) isEntrance = true; }
								default -> {}
							}
						}
					}
				}

				// Floor layers (thick) - use base blocks
				for (int y = floorY - FLOOR_THICKNESS; y <= floorY; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}

				// Walls and interior
				for (int y = floorY + 1; y <= floorY + ROOM_HEIGHT; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					BlockPos pos = new BlockPos(wx, y, wz);

					if (isWall) {
						if (isEntrance && y <= floorY + VaultCorridorPiece.CORRIDOR_HEIGHT) {
							level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
						} else {
							// Walls - use base blocks
							level.setBlock(pos, palette.getRandomBase(wx, y, wz), 2);
						}
					} else if (isInterior) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling (thick) - use base blocks
				for (int y = floorY + ROOM_HEIGHT + 1; y <= floorY + ROOM_HEIGHT + WALL_THICKNESS; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}
			}
		}

		// Apply overgrowth using helper (exclude staircase area if present)
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			BlockPos overgrowthOrigin = new BlockPos(cx - halfSize, floorY, cz - halfSize);
			VaultOvergrowthHelper.applyOvergrowth(level, box, overgrowthOrigin,
				size, size, ROOM_HEIGHT, levelIndex, totalLevels,
				staircaseCenter, staircaseCenter != null ? STAIRWELL_EXCLUSION_RADIUS : 0,
				overgrowthData, random);
		}
	}
}
