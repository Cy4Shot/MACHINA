package com.machina.api.block.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.machina.api.multiblock.Multiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.math.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiblockMasterBlockEntity extends MachinaBlockEntity {

	public Multiblock mb;
	public boolean formed = false;
	public Set<BlockPos> parts = new HashSet<>();

	public MultiblockMasterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		this.mb = MultiblockLoader.INSTANCE.get(getMultiblock());
	}

	public abstract ResourceLocation getMultiblock();

	public void update() {
		deform();

		ValidateResult res = valid();
		this.formed = res.valid;
		if (res.valid) {
			this.parts = res.pos;
			for (BlockPos pos : parts) {
				BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> {
					te.form(worldPosition);
				});
				postValidate().accept(pos);
			}
		} else {
			this.parts = new HashSet<>();
		}

		this.setChanged();
	}

	public void deform() {
		for (BlockPos pos : parts) {
			BlockHelper.doWithTe(level, pos, MultiblockPartBlockEntity.class, te -> {
				te.deform();
			});
		}
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		ListTag poss = new ListTag();
		this.parts.forEach(pos -> {
			CompoundTag block = new CompoundTag();
			block.put("block", NbtUtils.writeBlockPos(pos));
			poss.add(block);
		});

		tag.put("parts", poss);
		tag.putBoolean("formed", formed);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.parts.clear();
		ListTag cons = tag.getList("parts", Tag.TAG_COMPOUND);
		for (int j = 0; j < cons.size(); j++) {
			parts.add(NbtUtils.readBlockPos(cons.getCompound(j), "block").orElse(BlockPos.ZERO));
		}
		this.formed = tag.getBoolean("formed");
		super.loadAdditional(tag, registries);
	}

	private ValidateResult valid() {
		int s = (int) Math.ceil((double) VecUtil.max(mb.size));
		BlockPos corner = findCorner(worldPosition, b -> mb.allowedBlock.contains(level.getBlockState(b).getBlock()),
				s * s * s);
		return validateAll(corner, mb.size);

	}

	// Loops aren't real they don't exist.
	private ValidateResult validateAll(BlockPos corner, Vec3i size) {
		ValidateResult res = validateDirection(corner, mb.size, Direction.SOUTH);
		if (!res.valid) {
			res = validateDirection(corner, mb.size, Direction.WEST);
			if (!res.valid) {
				res = validateDirection(corner, mb.size, Direction.NORTH);
				if (!res.valid) {
					res = validateDirection(corner, mb.size, Direction.EAST);
					if (!res.valid) {
						return ValidateResult.REJECT;
					}
				}
			}
		}
		return res;
	}

	private ValidateResult validateDirection(BlockPos corner, Vec3i size, Direction rotation) {
		Set<BlockPos> poss = new HashSet<>();
		int controllerCount = 0;
		BlockPos controllerPos = null;

		for (int x = 0; x < size.getX(); x++) {
			for (int y = 0; y < size.getY(); y++) {
				for (int z = 0; z < size.getZ(); z++) {
					BlockPos pos = corner.offset(x, y, z);
					BlockState state = level.getBlockState(pos);
					String key;
					switch (rotation.get2DDataValue()) {
					case 1: // West
						key = mb.structure[z][y][size.getX() - 1 - x];
						break;
					case 2: // North
						key = mb.structure[size.getX() - 1 - x][y][size.getZ() - 1 - z];
						break;
					case 3: // East
						key = mb.structure[size.getZ() - 1 - z][y][x];
						break;
					default: // South
						key = mb.structure[x][y][z];
					}

					// Check if this is a controller slot position (? or !)
					if (key.equals("?") || key.equals("!")) {
						if (state.getBlock().equals(mb.controller.getBlock())) {
							if (controllerCount > 0) {
								return ValidateResult.REJECT;
							}
							controllerCount++;
							controllerPos = pos;
							poss.add(pos);
						} else {
							// Check if it's the allowed block for controller slots
							BlockState expected = mb.map.get(key);
							if (expected == null || !expected.getBlock().equals(state.getBlock())) {
								return ValidateResult.REJECT;
							}
							poss.add(pos);
						}
						continue;
					}

					// Regular block validation
					BlockState expected = mb.map.get(key);
					if (!key.equals(" ") && (expected == null || !expected.getBlock().equals(state.getBlock()))) {
						return ValidateResult.REJECT;
					}
					if (!key.equals(" "))
						poss.add(pos);
				}
			}
		}

		// Must have exactly one controller
		if (controllerCount != 1) {
			return ValidateResult.REJECT;
		}

		// Controller must be at the master block entity position
		if (!controllerPos.equals(worldPosition)) {
			return ValidateResult.REJECT;
		}

		return ValidateResult.accept(poss);
	}

	public Consumer<BlockPos> postValidate() {
		return p -> {
		};
	}

	private static BlockPos findCorner(BlockPos start, Predicate<BlockPos> checker, int maxCount) {
		Queue<BlockPos> openSet = new LinkedList<>();
		Set<BlockPos> traversed = new ObjectOpenHashSet<>();
		openSet.add(start.immutable());
		traversed.add(start.immutable());
		while (!openSet.isEmpty()) {
			BlockPos ptr = openSet.poll();
			if (traversed.size() >= maxCount) {
				return new BlockPos(VecUtil.minAll(traversed));
			}
			for (Direction side : Direction.values()) {
				BlockPos offset = ptr.relative(side);
				if (!traversed.contains(offset) && checker.test(offset)) {
					openSet.add(offset);
					traversed.add(offset);
				}
			}
		}
		return new BlockPos(VecUtil.minAll(traversed));
	}

	public static class ValidateResult {

		public static final ValidateResult REJECT = new ValidateResult(false, null);

		public final boolean valid;
		public final Set<BlockPos> pos;

		private ValidateResult(boolean valid, Set<BlockPos> pos) {
			this.valid = valid;
			this.pos = pos;
		}

		public static ValidateResult accept(Set<BlockPos> pos) {
			return new ValidateResult(true, pos);
		}
	}

	@Override
	public boolean isCapabilitiesActive(MachinaCap cap) {
		return this.formed;
	}
}
