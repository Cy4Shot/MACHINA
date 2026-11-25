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

public class VaultCorridorPiece extends StructurePiece {

	public static final int CORRIDOR_WIDTH = 5;
	public static final int CORRIDOR_HEIGHT = 5;
	private static final int WALL_THICKNESS = 2;
	private static final int FLOOR_THICKNESS = 2;
	private static final int BEDROCK_SHELL = 1;

	private final BlockPos start;
	private final Direction direction;
	private final int length;
	private final int floorY;
	private final VaultPalette palette;
	private final int levelIndex;
	private final int totalLevels;
	private final VaultOvergrowthHelper.OvergrowthData overgrowthData;

	public VaultCorridorPiece(BlockPos start, Direction direction, int length, int floorY, VaultPalette palette, int levelIndex, int totalLevels, VaultOvergrowthHelper.OvergrowthData overgrowthData) {
		super(StructureInit.VAULT_CORRIDOR.get(), 0, createBoundingBox(start, direction, length, floorY));
		this.start = start;
		this.direction = direction;
		this.length = length;
		this.floorY = floorY;
		this.palette = palette;
		this.levelIndex = levelIndex;
		this.totalLevels = totalLevels;
		this.overgrowthData = overgrowthData;
	}

	public VaultCorridorPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_CORRIDOR.get(), tag);
		this.start = NbtUtils.readBlockPos(tag, "Start").orElse(BlockPos.ZERO);
		this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
		this.length = tag.getInt("Length");
		this.floorY = tag.getInt("FloorY");
		this.levelIndex = tag.getInt("LevelIndex");
		this.totalLevels = tag.getInt("TotalLevels");
		if (tag.contains("Palette")) {
			this.palette = VaultPalette.readFromNbt(tag.getCompound("Palette"), ctx.registryAccess());
		} else {
			this.palette = VaultPalette.forLevel(0);
		}
		if (tag.contains("OvergrowthData")) {
			this.overgrowthData = VaultOvergrowthHelper.OvergrowthData.readFromNbt(tag.getCompound("OvergrowthData"), ctx.registryAccess());
		} else {
			this.overgrowthData = VaultOvergrowthHelper.OvergrowthData.EMPTY;
		}
	}

	private static BoundingBox createBoundingBox(BlockPos start, Direction dir, int length, int floorY) {
		int perpX = dir == Direction.NORTH || dir == Direction.SOUTH ? 1 : 0;
		int perpZ = dir == Direction.EAST || dir == Direction.WEST ? 1 : 0;
		int halfWidth = CORRIDOR_WIDTH / 2 + WALL_THICKNESS + BEDROCK_SHELL;

		int endX = start.getX() + dir.getStepX() * length;
		int endZ = start.getZ() + dir.getStepZ() * length;

		int minX = Math.min(start.getX(), endX) - perpX * halfWidth - Math.abs(dir.getStepX()) * BEDROCK_SHELL;
		int maxX = Math.max(start.getX(), endX) + perpX * halfWidth + Math.abs(dir.getStepX()) * BEDROCK_SHELL;
		int minZ = Math.min(start.getZ(), endZ) - perpZ * halfWidth - Math.abs(dir.getStepZ()) * BEDROCK_SHELL;
		int maxZ = Math.max(start.getZ(), endZ) + perpZ * halfWidth + Math.abs(dir.getStepZ()) * BEDROCK_SHELL;

		return new BoundingBox(minX, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, minZ, maxX, floorY + CORRIDOR_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, maxZ);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Start", NbtUtils.writeBlockPos(start));
		tag.putInt("Direction", direction.get2DDataValue());
		tag.putInt("Length", length);
		tag.putInt("FloorY", floorY);
		tag.putInt("LevelIndex", levelIndex);
		tag.putInt("TotalLevels", totalLevels);
		CompoundTag paletteTag = new CompoundTag();
		palette.writeToNbt(paletteTag);
		tag.put("Palette", paletteTag);
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			CompoundTag overgrowthTag = new CompoundTag();
			overgrowthData.writeToNbt(overgrowthTag);
			tag.put("OvergrowthData", overgrowthTag);
		}
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		int perpX = direction == Direction.NORTH || direction == Direction.SOUTH ? 1 : 0;
		int perpZ = direction == Direction.EAST || direction == Direction.WEST ? 1 : 0;
		int halfWidth = CORRIDOR_WIDTH / 2;
		int totalHalfWidth = halfWidth + WALL_THICKNESS;
		int bedrockHalfWidth = totalHalfWidth + BEDROCK_SHELL;

		// First pass: bedrock shell (skip ends where other pieces connect)
		for (int i = -BEDROCK_SHELL; i <= length + BEDROCK_SHELL; i++) {
			int corridorX = start.getX() + direction.getStepX() * i;
			int corridorZ = start.getZ() + direction.getStepZ() * i;

			boolean isConnectionEnd = i < 2 || i > length - 2;

			for (int w = -bedrockHalfWidth; w <= bedrockHalfWidth; w++) {
				int wx = corridorX + perpX * w;
				int wz = corridorZ + perpZ * w;

				boolean isBedrockShell = Math.abs(w) > totalHalfWidth || i < 0 || i > length;

				if (isBedrockShell && !isConnectionEnd) {
					for (int y = floorY - FLOOR_THICKNESS - BEDROCK_SHELL; y <= floorY + CORRIDOR_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL; y++) {
						if (!box.isInside(wx, y, wz)) continue;
						level.setBlock(new BlockPos(wx, y, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				} else if (!isBedrockShell && !isConnectionEnd) {
					if (box.isInside(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
					if (box.isInside(wx, floorY + CORRIDOR_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY + CORRIDOR_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				}
			}
		}

		// Second pass: corridor structure
		for (int i = 0; i <= length; i++) {
			int corridorX = start.getX() + direction.getStepX() * i;
			int corridorZ = start.getZ() + direction.getStepZ() * i;

			for (int w = -totalHalfWidth; w <= totalHalfWidth; w++) {
				int wx = corridorX + perpX * w;
				int wz = corridorZ + perpZ * w;

				boolean isWall = Math.abs(w) > halfWidth;
				boolean isInterior = Math.abs(w) <= halfWidth;

				// Floor layers (thick) - use base blocks
				for (int y = floorY - FLOOR_THICKNESS; y <= floorY; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}

				// Walls and interior
				for (int y = floorY + 1; y <= floorY + CORRIDOR_HEIGHT; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					BlockPos pos = new BlockPos(wx, y, wz);

					if (isWall) {
						// Walls - use base blocks
						level.setBlock(pos, palette.getRandomBase(wx, y, wz), 2);
					} else if (isInterior) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling (thick) - use base blocks
				for (int y = floorY + CORRIDOR_HEIGHT + 1; y <= floorY + CORRIDOR_HEIGHT + WALL_THICKNESS; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}
			}
		}

		// Apply overgrowth using helper (no stairwell in corridors, reduced vine density)
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			int corridorWidth = direction.getAxis() == Direction.Axis.X ? length : CORRIDOR_WIDTH;
			int corridorDepth = direction.getAxis() == Direction.Axis.Z ? length : CORRIDOR_WIDTH;
			int minX = direction == Direction.WEST ? start.getX() - length : start.getX() - halfWidth;
			int minZ = direction == Direction.NORTH ? start.getZ() - length : start.getZ() - halfWidth;
			BlockPos overgrowthOrigin = new BlockPos(minX, floorY, minZ);
			VaultOvergrowthHelper.applyOvergrowth(level, box, overgrowthOrigin,
				corridorWidth, corridorDepth, CORRIDOR_HEIGHT, levelIndex, totalLevels, null, 0,
				overgrowthData, random, 0.25f);
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
