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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VaultStaircasePiece extends StructurePiece {

	public static final int SHAFT_RADIUS = 4;
	private static final int WALL_THICKNESS = 2;
	private static final int PILLAR_RADIUS = 1;

	private final int centerX;
	private final int centerZ;
	private final int surfaceY;
	private final int bottomY;
	private final int chamberCeilingY;
	private final VaultPalette palette;

	public VaultStaircasePiece(int centerX, int centerZ, int surfaceY, int bottomY, int chamberCeilingY, VaultPalette palette) {
		super(StructureInit.VAULT_STAIRCASE.get(), 0, createBoundingBox(centerX, centerZ, surfaceY, bottomY));
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.surfaceY = surfaceY;
		this.bottomY = bottomY;
		this.chamberCeilingY = chamberCeilingY;
		this.palette = palette;
	}

	public VaultStaircasePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_STAIRCASE.get(), tag);
		this.centerX = tag.getInt("CenterX");
		this.centerZ = tag.getInt("CenterZ");
		this.surfaceY = tag.getInt("SurfaceY");
		this.bottomY = tag.getInt("BottomY");
		this.chamberCeilingY = tag.getInt("ChamberCeilingY");
		if (tag.contains("Palette")) {
			this.palette = VaultPalette.readFromNbt(tag.getCompound("Palette"), ctx.registryAccess());
		} else {
			this.palette = VaultPalette.forLevel(0);
		}
	}

	private static BoundingBox createBoundingBox(int cx, int cz, int surfaceY, int bottomY) {
		int radius = SHAFT_RADIUS + WALL_THICKNESS;
		return new BoundingBox(
			cx - radius, bottomY - 1, cz - radius,
			cx + radius, surfaceY + 1, cz + radius
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.putInt("CenterX", centerX);
		tag.putInt("CenterZ", centerZ);
		tag.putInt("SurfaceY", surfaceY);
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

		// First: clear entire shaft interior and build walls/pillar
		for (int y = bottomY + 1; y <= surfaceY; y++) {
			boolean buildOuterWall = y > chamberCeilingY;

			for (int dx = -outerRadius; dx <= outerRadius; dx++) {
				for (int dz = -outerRadius; dz <= outerRadius; dz++) {
					int wx = centerX + dx;
					int wz = centerZ + dz;
					if (!box.isInside(wx, y, wz)) continue;

					double dist = Math.sqrt(dx * dx + dz * dz);
					BlockPos pos = new BlockPos(wx, y, wz);

					if (buildOuterWall && dist <= outerRadius && dist > SHAFT_RADIUS) {
						// Build wall above chamber ceiling - use base blocks
						level.setBlock(pos, palette.getRandomBase(wx, y, wz), 2);
					} else if (dist <= PILLAR_RADIUS) {
						// Central pillar - use pillar blocks
						level.setBlock(pos, palette.getRandomPillar(wx, y, wz), 2);
					} else if (dist <= outerRadius) {
						// Clear interior
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}

		// Spiral staircase: 40 degrees rotation per Y level
		double degreesPerLevel = 40.0;

		for (int y = bottomY + 1; y <= surfaceY; y++) {
			double angle = Math.toRadians((y - bottomY - 1) * degreesPerLevel);

			// Place stair blocks in a small arc at this height
			for (int arcDeg = -20; arcDeg <= 20; arcDeg += 10) {
				double stepAngle = angle + Math.toRadians(arcDeg);

				// Fill from pillar edge to near the wall
				for (double r = PILLAR_RADIUS + 0.6; r <= SHAFT_RADIUS - 0.3; r += 0.5) {
					int sx = centerX + (int) Math.round(Math.cos(stepAngle) * r);
					int sz = centerZ + (int) Math.round(Math.sin(stepAngle) * r);

					if (!box.isInside(sx, y, sz)) continue;

					BlockPos stairPos = new BlockPos(sx, y, sz);
					// Only place if currently air (don't overwrite pillar/walls)
					if (level.getBlockState(stairPos).isAir()) {
						// Use slabs for the spiral stair platforms
						level.setBlock(stairPos, palette.getRandomSlab(sx, y, sz, SlabType.BOTTOM), 2);
					}
				}
			}

			// Clear headroom above this stair level (3 blocks)
			for (int h = 1; h <= 3; h++) {
				if (y + h > surfaceY) break;

				for (int arcDeg = -20; arcDeg <= 20; arcDeg += 10) {
					double stepAngle = angle + Math.toRadians(arcDeg);

					for (double r = PILLAR_RADIUS + 0.6; r <= SHAFT_RADIUS - 0.3; r += 0.5) {
						int sx = centerX + (int) Math.round(Math.cos(stepAngle) * r);
						int sz = centerZ + (int) Math.round(Math.sin(stepAngle) * r);

						BlockPos above = new BlockPos(sx, y + h, sz);
						if (!box.isInside(above)) continue;

						int adx = sx - centerX;
						int adz = sz - centerZ;
						double adist = Math.sqrt(adx * adx + adz * adz);

						// Only clear interior, not pillar or walls
						if (adist > PILLAR_RADIUS && adist < SHAFT_RADIUS) {
							BlockState state = level.getBlockState(above);
							if (!state.isAir()) {
								level.setBlock(above, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}
				}
			}
		}
	}
}
