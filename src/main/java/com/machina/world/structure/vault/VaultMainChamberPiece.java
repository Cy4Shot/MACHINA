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

public class VaultMainChamberPiece extends StructurePiece {

	public static final int CHAMBER_RADIUS = 20;
	public static final int CHAMBER_HEIGHT = 12;
	private static final int WALL_THICKNESS = 3;
	private static final int FLOOR_THICKNESS = 2;
	private static final int STAIR_WELL_RADIUS = VaultStaircasePiece.SHAFT_RADIUS + 2;
	private static final int BEDROCK_SHELL = 1;

	private final BlockPos center;
	private final VaultPalette palette;
	private final boolean[] corridorDirections;
	private final int levelIndex;
	private final int totalLevels;
	private final VaultOvergrowthHelper.OvergrowthData overgrowthData;

	public VaultMainChamberPiece(BlockPos center, VaultPalette palette, boolean[] corridorDirections, int levelIndex, int totalLevels, VaultOvergrowthHelper.OvergrowthData overgrowthData) {
		super(StructureInit.VAULT_MAIN_CHAMBER.get(), 0, createBoundingBox(center));
		this.center = center;
		this.palette = palette;
		this.corridorDirections = corridorDirections;
		this.levelIndex = levelIndex;
		this.totalLevels = totalLevels;
		this.overgrowthData = overgrowthData;
	}

	public VaultMainChamberPiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_MAIN_CHAMBER.get(), tag);
		this.center = NbtUtils.readBlockPos(tag, "Center").orElse(BlockPos.ZERO);
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
	}

	private static BoundingBox createBoundingBox(BlockPos center) {
		int totalRadius = CHAMBER_RADIUS + WALL_THICKNESS + BEDROCK_SHELL;
		return new BoundingBox(
			center.getX() - totalRadius, center.getY() - FLOOR_THICKNESS - BEDROCK_SHELL, center.getZ() - totalRadius,
			center.getX() + totalRadius, center.getY() + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, center.getZ() + totalRadius
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.put("Center", NbtUtils.writeBlockPos(center));
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
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		int cx = center.getX();
		int cz = center.getZ();
		int floorY = center.getY();
		int totalRadius = CHAMBER_RADIUS + WALL_THICKNESS;
		int bedrockRadius = totalRadius + BEDROCK_SHELL;

		// First pass: bedrock shell (skip corridor connection points)
		for (int x = cx - bedrockRadius; x <= cx + bedrockRadius; x++) {
			for (int z = cz - bedrockRadius; z <= cz + bedrockRadius; z++) {
				int dx = x - cx;
				int dz = z - cz;
				float dist = (float) Math.sqrt(dx * dx + dz * dz);

				if (dist > bedrockRadius || dist <= totalRadius) continue;

				boolean isCorridorConnection = false;
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
						if (perpDist <= VaultCorridorPiece.CORRIDOR_WIDTH / 2 + WALL_THICKNESS + 1 && parallelDist > CHAMBER_RADIUS - 1) {
							isCorridorConnection = true;
							break;
						}
					}
				}

				if (isCorridorConnection) continue;

				for (int y = floorY - FLOOR_THICKNESS - BEDROCK_SHELL; y <= floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL; y++) {
					if (!box.isInside(x, y, z)) continue;
					level.setBlock(new BlockPos(x, y, z), Blocks.BEDROCK.defaultBlockState(), 2);
				}
			}
		}

		// Bedrock floor and ceiling
		for (int x = cx - totalRadius; x <= cx + totalRadius; x++) {
			for (int z = cz - totalRadius; z <= cz + totalRadius; z++) {
				int dx = x - cx;
				int dz = z - cz;
				float dist = (float) Math.sqrt(dx * dx + dz * dz);
				if (dist > totalRadius) continue;

				boolean isStairWell = dist <= STAIR_WELL_RADIUS;

				boolean isCorridorConnection = false;
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
						if (perpDist <= VaultCorridorPiece.CORRIDOR_WIDTH / 2 + WALL_THICKNESS + 1 && parallelDist > CHAMBER_RADIUS - 1) {
							isCorridorConnection = true;
							break;
						}
					}
				}

				if (!isCorridorConnection && box.isInside(x, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, z)) {
					level.setBlock(new BlockPos(x, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, z), Blocks.BEDROCK.defaultBlockState(), 2);
				}
				if (!isStairWell && !isCorridorConnection && box.isInside(x, floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, z)) {
					level.setBlock(new BlockPos(x, floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, z), Blocks.BEDROCK.defaultBlockState(), 2);
				}
			}
		}

		// Second pass: chamber structure
		for (int x = cx - totalRadius; x <= cx + totalRadius; x++) {
			for (int z = cz - totalRadius; z <= cz + totalRadius; z++) {
				int dx = x - cx;
				int dz = z - cz;
				float dist = (float) Math.sqrt(dx * dx + dz * dz);

				if (dist > totalRadius) continue;

				boolean isWallZone = dist > CHAMBER_RADIUS;
				boolean isInterior = dist <= CHAMBER_RADIUS;
				boolean isStairWell = dist <= STAIR_WELL_RADIUS;

				boolean isCorridorPath = false;
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
						if (perpDist <= VaultCorridorPiece.CORRIDOR_WIDTH / 2 + 1 && parallelDist > CHAMBER_RADIUS - WALL_THICKNESS) {
							isCorridorPath = true;
							break;
						}
					}
				}

				// Floor layers (thick) - use base blocks
				for (int y = floorY - FLOOR_THICKNESS; y < floorY; y++) {
					if (!box.isInside(x, y, z)) continue;
					level.setBlock(new BlockPos(x, y, z), palette.getRandomBase(x, y, z), 2);
				}

				// Main floor - use base blocks
				if (box.isInside(x, floorY, z)) {
					level.setBlock(new BlockPos(x, floorY, z), palette.getRandomBase(x, floorY, z), 2);
				}

				// Interior and walls
				for (int y = floorY + 1; y <= floorY + CHAMBER_HEIGHT; y++) {
					if (!box.isInside(x, y, z)) continue;
					BlockPos pos = new BlockPos(x, y, z);

					if (isWallZone) {
						if (isCorridorPath && y <= floorY + VaultCorridorPiece.CORRIDOR_HEIGHT) {
							level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
						} else {
							// Walls - use base blocks
							level.setBlock(pos, palette.getRandomBase(x, y, z), 2);
						}
					} else if (isInterior && !isStairWell) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling (thick) - skip stairwell, use base blocks
				if (!isStairWell) {
					for (int y = floorY + CHAMBER_HEIGHT + 1; y <= floorY + CHAMBER_HEIGHT + WALL_THICKNESS; y++) {
						if (!box.isInside(x, y, z)) continue;
						level.setBlock(new BlockPos(x, y, z), palette.getRandomBase(x, y, z), 2);
					}
				}

				// Pillars (but not in stair well area) - use pillar blocks
				if (!isStairWell && !isWallZone && (dx + 100) % 10 == 0 && (dz + 100) % 10 == 0 && dist < CHAMBER_RADIUS - 3) {
					for (int y = floorY + 1; y <= floorY + CHAMBER_HEIGHT; y++) {
						if (box.isInside(x, y, z)) {
							level.setBlock(new BlockPos(x, y, z), palette.getRandomPillar(x, y, z), 2);
						}
					}
				}
			}
		}

		// Apply overgrowth using helper (skip stairwell area)
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			BlockPos overgrowthOrigin = new BlockPos(cx - CHAMBER_RADIUS, floorY, cz - CHAMBER_RADIUS);
			BlockPos stairwellCenter = new BlockPos(cx, floorY, cz);
			VaultOvergrowthHelper.applyOvergrowth(level, box, overgrowthOrigin,
				CHAMBER_RADIUS * 2, CHAMBER_RADIUS * 2, CHAMBER_HEIGHT,
				levelIndex, totalLevels, stairwellCenter, STAIR_WELL_RADIUS,
				overgrowthData, random);
		}
	}
}
