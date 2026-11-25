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

		int innerRadius = CHAMBER_RADIUS;
		int wallRadius = innerRadius + WALL_THICKNESS;
		int bedrockRadius = wallRadius + BEDROCK_SHELL;

		int corridorHalfWidth = VaultCorridorPiece.CORRIDOR_WIDTH / 2 + WALL_THICKNESS;

		// First pass: bedrock shell (skip entrance connections)
		for (int rx = -bedrockRadius; rx <= bedrockRadius; rx++) {
			for (int rz = -bedrockRadius; rz <= bedrockRadius; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;
				double dist = Math.sqrt(rx * rx + rz * rz);

				boolean isBedrockShell = dist > wallRadius && dist <= bedrockRadius;

				boolean isEntranceConnection = false;
				for (Direction dir : Direction.Plane.HORIZONTAL) {
					if (corridorDirections[dir.get2DDataValue()]) {
						switch (dir) {
							case NORTH -> { if (rz <= -innerRadius && Math.abs(rx) <= corridorHalfWidth) isEntranceConnection = true; }
							case SOUTH -> { if (rz >= innerRadius && Math.abs(rx) <= corridorHalfWidth) isEntranceConnection = true; }
							case WEST -> { if (rx <= -innerRadius && Math.abs(rz) <= corridorHalfWidth) isEntranceConnection = true; }
							case EAST -> { if (rx >= innerRadius && Math.abs(rz) <= corridorHalfWidth) isEntranceConnection = true; }
							default -> {}
						}
					}
				}

				if (isEntranceConnection) continue;

				if (isBedrockShell) {
					for (int y = floorY - FLOOR_THICKNESS - BEDROCK_SHELL; y <= floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL; y++) {
						if (!box.isInside(wx, y, wz)) continue;
						level.setBlock(new BlockPos(wx, y, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				} else if (dist <= wallRadius) {
					boolean isStairwell = rx * rx + rz * rz <= STAIR_WELL_RADIUS * STAIR_WELL_RADIUS;

					if (box.isInside(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY - FLOOR_THICKNESS - BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
					// Skip stairwell area for top bedrock
					if (!isStairwell && box.isInside(wx, floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz)) {
						level.setBlock(new BlockPos(wx, floorY + CHAMBER_HEIGHT + WALL_THICKNESS + BEDROCK_SHELL, wz), Blocks.BEDROCK.defaultBlockState(), 2);
					}
				}
			}
		}

		// Second pass: room structure
		for (int rx = -wallRadius; rx <= wallRadius; rx++) {
			for (int rz = -wallRadius; rz <= wallRadius; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;
				double dist = Math.sqrt(rx * rx + rz * rz);

				if (dist > wallRadius) continue;

				boolean isWall = dist > innerRadius;
				boolean isInterior = dist <= innerRadius;
				boolean isStairwell = rx * rx + rz * rz <= STAIR_WELL_RADIUS * STAIR_WELL_RADIUS;

				boolean isEntrance = false;
				if (isWall) {
					for (Direction dir : Direction.Plane.HORIZONTAL) {
						if (corridorDirections[dir.get2DDataValue()]) {
							int doorHalfWidth = VaultCorridorPiece.CORRIDOR_WIDTH / 2;
							switch (dir) {
								case NORTH -> { if (rz <= -innerRadius && Math.abs(rx) <= doorHalfWidth) isEntrance = true; }
								case SOUTH -> { if (rz >= innerRadius && Math.abs(rx) <= doorHalfWidth) isEntrance = true; }
								case WEST -> { if (rx <= -innerRadius && Math.abs(rz) <= doorHalfWidth) isEntrance = true; }
								case EAST -> { if (rx >= innerRadius && Math.abs(rz) <= doorHalfWidth) isEntrance = true; }
								default -> {}
							}
						}
					}
				}

				// Floor layers (thick) - fill everything, staircase piece carves through
				for (int y = floorY - FLOOR_THICKNESS; y <= floorY; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}

				// Walls and interior
				for (int y = floorY + 1; y <= floorY + CHAMBER_HEIGHT; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					BlockPos pos = new BlockPos(wx, y, wz);

					if (isStairwell) continue;

					if (isWall) {
						if (isEntrance && y <= floorY + VaultCorridorPiece.CORRIDOR_HEIGHT) {
							level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
						} else {
							level.setBlock(pos, palette.getRandomBase(wx, y, wz), 2);
						}
					} else if (isInterior) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}

				// Ceiling (thick) - skip stairwell for hole
				for (int y = floorY + CHAMBER_HEIGHT + 1; y <= floorY + CHAMBER_HEIGHT + WALL_THICKNESS; y++) {
					if (!box.isInside(wx, y, wz)) continue;
					if (isStairwell) continue;
					level.setBlock(new BlockPos(wx, y, wz), palette.getRandomBase(wx, y, wz), 2);
				}
			}
		}

		// Add pillars (8 around the room)
		int pillarDist = CHAMBER_RADIUS - 4;
		for (int i = 0; i < 8; i++) {
			double angle = Math.PI * 2 * i / 8;
			int px = cx + (int) (Math.cos(angle) * pillarDist);
			int pz = cz + (int) (Math.sin(angle) * pillarDist);

			for (int y = floorY + 1; y <= floorY + CHAMBER_HEIGHT; y++) {
				if (!box.isInside(px, y, pz)) continue;
				level.setBlock(new BlockPos(px, y, pz), palette.getRandomPillar(px, y, pz), 2);
			}
		}

		// Add floor pattern - checkerboard in center area
		for (int rx = -innerRadius / 2; rx <= innerRadius / 2; rx++) {
			for (int rz = -innerRadius / 2; rz <= innerRadius / 2; rz++) {
				int wx = cx + rx;
				int wz = cz + rz;

				if (!box.isInside(wx, floorY, wz)) continue;

				boolean isStairwell = rx * rx + rz * rz <= STAIR_WELL_RADIUS * STAIR_WELL_RADIUS;
				if (isStairwell) continue;

				if ((rx + rz) % 4 == 0) {
					level.setBlock(new BlockPos(wx, floorY, wz), palette.getRandomBase(wx, floorY, wz), 2);
				}
			}
		}

		// Apply overgrowth using helper (skip stairwell area, this is main chamber)
		if (overgrowthData != null && !overgrowthData.isEmpty()) {
			BlockPos overgrowthOrigin = new BlockPos(cx - CHAMBER_RADIUS, floorY, cz - CHAMBER_RADIUS);
			BlockPos stairwellCenter = new BlockPos(cx, floorY, cz);
			VaultOvergrowthHelper.applyOvergrowth(level, box, overgrowthOrigin,
				CHAMBER_RADIUS * 2, CHAMBER_RADIUS * 2, CHAMBER_HEIGHT,
				levelIndex, totalLevels, stairwellCenter, STAIR_WELL_RADIUS,
				overgrowthData, random, 1.0f, true);
		}
	}
}
