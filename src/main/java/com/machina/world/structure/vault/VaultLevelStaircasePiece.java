package com.machina.world.structure.vault;

import com.machina.registration.init.StructureInit;
import com.machina.world.structure.vault.helper.VaultPalette;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

/**
 * Spiral staircase connecting two vault levels (from a side room down to the next main chamber).
 */
public class VaultLevelStaircasePiece extends StructurePiece {

	private static final int SHAFT_RADIUS = VaultStaircasePiece.SHAFT_RADIUS;
	private static final int WALL_THICKNESS = 2;
	private static final int PILLAR_RADIUS = 1;

	private final int centerX;
	private final int centerZ;
	private final int topY;
	private final int bottomY;
	private final int chamberCeilingY;
	private final VaultPalette palette;

	public VaultLevelStaircasePiece(int centerX, int centerZ, int topY, int bottomY, int chamberCeilingY, VaultPalette palette) {
		super(StructureInit.VAULT_LEVEL_STAIRCASE.get(), 0, createBoundingBox(centerX, centerZ, topY, bottomY));
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.topY = topY;
		this.bottomY = bottomY;
		this.chamberCeilingY = chamberCeilingY;
		this.palette = palette;
	}

	public VaultLevelStaircasePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_LEVEL_STAIRCASE.get(), tag);
		this.centerX = tag.getInt("CenterX");
		this.centerZ = tag.getInt("CenterZ");
		this.topY = tag.getInt("TopY");
		this.bottomY = tag.getInt("BottomY");
		this.chamberCeilingY = tag.getInt("ChamberCeilingY");
		if (tag.contains("Palette")) {
			this.palette = VaultPalette.readFromNbt(tag.getCompound("Palette"), ctx.registryAccess());
		} else {
			this.palette = VaultPalette.forLevel(0);
		}
	}

	private static BoundingBox createBoundingBox(int cx, int cz, int topY, int bottomY) {
		int radius = VaultStaircasePiece.SHAFT_RADIUS + 3;
		return new BoundingBox(
			cx - radius, bottomY, cz - radius,
			cx + radius, topY + 5, cz + radius
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.putInt("CenterX", centerX);
		tag.putInt("CenterZ", centerZ);
		tag.putInt("TopY", topY);
		tag.putInt("BottomY", bottomY);
		tag.putInt("ChamberCeilingY", chamberCeilingY);
		CompoundTag paletteTag = new CompoundTag();
		palette.writeToNbt(paletteTag);
		tag.put("Palette", paletteTag);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
			RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pivot) {

		int outerRadius = SHAFT_RADIUS + WALL_THICKNESS;
		int clearRadius = VaultStaircasePiece.SHAFT_RADIUS + 2;

		int upperClearance = 4;
		for (int y = bottomY + 1; y <= topY + upperClearance; y++) {
			boolean buildOuterWall = y > chamberCeilingY && y <= topY;
			boolean isAboveTopY = y > topY;

			for (int dx = -clearRadius; dx <= clearRadius; dx++) {
				for (int dz = -clearRadius; dz <= clearRadius; dz++) {
					int wx = centerX + dx;
					int wz = centerZ + dz;
					if (!box.isInside(wx, y, wz)) continue;

					double dist = Math.sqrt(dx * dx + dz * dz);
					BlockPos pos = new BlockPos(wx, y, wz);

					if (buildOuterWall && dist <= outerRadius && dist > SHAFT_RADIUS) {
						// Wall - use base blocks
						level.setBlock(pos, palette.getRandomBase(wx, y, wz), 2);
					} else if (dist <= PILLAR_RADIUS) {
						// Central pillar - use pillar blocks
						if (!isAboveTopY) {
							level.setBlock(pos, palette.getRandomPillar(wx, y, wz), 2);
						}
					} else if (dist <= clearRadius) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}

		// Spiral staircase
		double degreesPerLevel = 40.0;

		for (int y = bottomY + 1; y <= topY; y++) {
			double angle = Math.toRadians((y - bottomY - 1) * degreesPerLevel);

			for (int arcDeg = -20; arcDeg <= 20; arcDeg += 10) {
				double stepAngle = angle + Math.toRadians(arcDeg);

				for (double r = PILLAR_RADIUS + 0.6; r <= SHAFT_RADIUS - 0.3; r += 0.5) {
					int sx = centerX + (int) Math.round(Math.cos(stepAngle) * r);
					int sz = centerZ + (int) Math.round(Math.sin(stepAngle) * r);

					if (!box.isInside(sx, y, sz)) continue;

					BlockPos stairPos = new BlockPos(sx, y, sz);
					if (level.getBlockState(stairPos).isAir()) {
						// Use slabs for spiral stair platforms
						level.setBlock(stairPos, palette.getRandomSlab(sx, y, sz, SlabType.BOTTOM), 2);
					}
				}
			}
		}
	}

	public int getCenterX() { return centerX; }
	public int getCenterZ() { return centerZ; }
	public int getBottomY() { return bottomY; }
}
