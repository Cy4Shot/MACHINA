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

public class VaultStaircasePiece extends StructurePiece {

	private static final int SHAFT_RADIUS = 4;
	private static final int STAIR_INNER_RADIUS = 1;
	private static final int STAIR_OUTER_RADIUS = 3;

	private final int centerX;
	private final int centerZ;
	private final int surfaceY;
	private final int bottomY;
	private final List<BlockState> blocks;

	public VaultStaircasePiece(int centerX, int centerZ, int surfaceY, int bottomY, List<BlockState> blocks) {
		super(StructureInit.VAULT_STAIRCASE.get(), 0, createBoundingBox(centerX, centerZ, surfaceY, bottomY));
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.surfaceY = surfaceY;
		this.bottomY = bottomY;
		this.blocks = blocks;
	}

	public VaultStaircasePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(StructureInit.VAULT_STAIRCASE.get(), tag);
		this.centerX = tag.getInt("CenterX");
		this.centerZ = tag.getInt("CenterZ");
		this.surfaceY = tag.getInt("SurfaceY");
		this.bottomY = tag.getInt("BottomY");
		ListTag blockList = tag.getList("Blocks", Tag.TAG_COMPOUND);
		this.blocks = blockList.stream()
			.map(t -> NbtUtils.readBlockState(ctx.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), (CompoundTag) t))
			.toList();
	}

	private static BoundingBox createBoundingBox(int cx, int cz, int surfaceY, int bottomY) {
		return new BoundingBox(
			cx - SHAFT_RADIUS - 1, bottomY - 1, cz - SHAFT_RADIUS - 1,
			cx + SHAFT_RADIUS + 1, surfaceY + 1, cz + SHAFT_RADIUS + 1
		);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
		tag.putInt("CenterX", centerX);
		tag.putInt("CenterZ", centerZ);
		tag.putInt("SurfaceY", surfaceY);
		tag.putInt("BottomY", bottomY);
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

		// First pass: clear the shaft and build walls
		for (int y = bottomY; y <= surfaceY; y++) {
			for (int dx = -SHAFT_RADIUS; dx <= SHAFT_RADIUS; dx++) {
				for (int dz = -SHAFT_RADIUS; dz <= SHAFT_RADIUS; dz++) {
					int wx = centerX + dx;
					int wz = centerZ + dz;
					if (!box.isInside(wx, y, wz)) continue;

					double dist = Math.sqrt(dx * dx + dz * dz);
					BlockPos pos = new BlockPos(wx, y, wz);

					// Outer wall
					if (dist > SHAFT_RADIUS - 0.5 && dist <= SHAFT_RADIUS + 0.5) {
						level.setBlock(pos, getPatchyBlock(wx, y, wz), 2);
					}
					// Central column
					else if (dist <= STAIR_INNER_RADIUS) {
						level.setBlock(pos, getPatchyBlock(wx, y, wz), 2);
					}
					// Air in between (will be filled with stairs)
					else {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}

		// Second pass: generate spiral staircase
		double anglePerStep = 20.0;

		for (int y = bottomY; y <= surfaceY; y++) {
			double angle = Math.toRadians((y - bottomY) * anglePerStep);

			// Generate stair steps as arc segments
			for (int step = 0; step < 3; step++) {
				double stepAngle = angle + Math.toRadians(step * 30);

				for (double r = STAIR_INNER_RADIUS + 0.5; r <= STAIR_OUTER_RADIUS; r += 0.5) {
					int sx = centerX + (int) Math.round(Math.cos(stepAngle) * r);
					int sz = centerZ + (int) Math.round(Math.sin(stepAngle) * r);

					if (!box.isInside(sx, y, sz)) continue;

					BlockPos stairPos = new BlockPos(sx, y, sz);

					// Only place stair if it's currently air
					if (level.getBlockState(stairPos).isAir()) {
						level.setBlock(stairPos, getPatchyBlock(sx, y, sz), 2);
					}

					// Clear headroom above each stair step
					for (int h = 1; h <= 2; h++) {
						BlockPos above = stairPos.above(h);
						if (box.isInside(above) && !level.getBlockState(above).isAir()) {
							int adx = above.getX() - centerX;
							int adz = above.getZ() - centerZ;
							double aboveDist = Math.sqrt(adx * adx + adz * adz);
							if (aboveDist < SHAFT_RADIUS - 0.5) {
								level.setBlock(above, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}
				}
			}
		}

		// Floor at bottom
		for (int dx = -SHAFT_RADIUS; dx <= SHAFT_RADIUS; dx++) {
			for (int dz = -SHAFT_RADIUS; dz <= SHAFT_RADIUS; dz++) {
				int wx = centerX + dx;
				int wz = centerZ + dz;
				if (!box.isInside(wx, bottomY - 1, wz)) continue;

				double dist = Math.sqrt(dx * dx + dz * dz);
				if (dist <= SHAFT_RADIUS) {
					level.setBlock(new BlockPos(wx, bottomY - 1, wz), getPatchyBlock(wx, bottomY - 1, wz), 2);
				}
			}
		}
	}
}
