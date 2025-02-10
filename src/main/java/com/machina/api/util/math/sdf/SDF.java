package com.machina.api.util.math.sdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// Modified SDF Library by quiquek. https://github.com/quiqueck/BCLib/

public abstract class SDF {

	public static final int UPDATE_FLAGS = 18;

	public interface SDFPostProcessor {
		default BlockState apply(PosInfo posInfo) {
			return posInfo.getState();
		}

		default Pair<BlockPos, BlockState> extra(PosInfo posInfo) {
			return null;
		}

		default void apply(List<PosInfo> infos) {
			infos.forEach((info) -> {
				info.setState(apply(info));
				info.setExtra(extra(info));
			});
		}
	}

	private final List<SDFPostProcessor> postProcesses = Lists.newArrayList();
	private Function<BlockState, Boolean> canReplace = BlockBehaviour.BlockStateBase::canBeReplaced;

	public abstract float getDistance(float x, float y, float z);

	public abstract BlockState getBlockState(BlockPos pos);

	public SDF addPostProcess(SDFPostProcessor postProcess) {
		this.postProcesses.add(0, postProcess);
		return this;
	}

	public SDF addPostProcesses(List<? extends SDFPostProcessor> postProcess) {
		postProcess.forEach(this::addPostProcess);
		return this;
	}

	public SDF setReplaceFunction(Function<BlockState, Boolean> canReplace) {
		this.canReplace = canReplace;
		return this;
	}

	public void fillRecursive(ServerLevelAccessor world, BlockPos start) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.offset(start);

					if (!blocks.contains(bPos) && canReplace.apply(world.getBlockState(wpos))) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, addInfo, wpos).setState(state);
							add.add(bPos.immutable());
						}
					}
				}
			}

			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();

			run = !ends.isEmpty();
		}

		List<PosInfo> infos = new ArrayList<>(mapWorld.values());
		if (!infos.isEmpty()) {
			Collections.sort(infos);
			postProcesses.forEach(x -> x.apply(infos));
			infos.forEach((info) -> world.setBlock(info.getPos(), info.getState(), UPDATE_FLAGS));
			infos.forEach((info) -> info.getExtra()
					.ifPresent((extra) -> world.setBlock(extra.getFirst(), extra.getSecond(), UPDATE_FLAGS)));

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach(x -> x.apply(infos));
			infos.forEach((info) -> {
				if (canReplace.apply(world.getBlockState(info.getPos()))) {
					world.setBlock(info.getPos(), info.getState(), UPDATE_FLAGS);
				}
			});
			infos.forEach((info) -> info.getExtra().ifPresent((extra) -> {
				if (canReplace.apply(world.getBlockState(info.getPos()))) {
					world.setBlock(extra.getFirst(), extra.getSecond(), UPDATE_FLAGS);
				}
			}));
		}
	}

	public Set<BlockPos> fillRecursiveShift(ServerLevelAccessor world, BlockPos start,
			Function<MutableBlockPos, Boolean> shifter) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.offset(start);

					if (!blocks.contains(bPos) && canReplace.apply(world.getBlockState(wpos))) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, addInfo, wpos).setState(state);
							add.add(bPos.immutable());
						}
					}
				}
			}

			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();

			run = !ends.isEmpty();
		}

		List<PosInfo> infos = new ArrayList<>(mapWorld.values());
		Set<BlockPos> positions = Sets.newHashSet();
		if (!infos.isEmpty()) {
			Collections.sort(infos);
			postProcesses.forEach(x -> x.apply(infos));
			infos.forEach((info) -> {
				MutableBlockPos mbp = info.getPos().mutable();
				if (shifter.apply(mbp)) {
					world.setBlock(mbp.immutable(), info.getState(), UPDATE_FLAGS);
					positions.add(mbp.immutable());
				}
			});
			infos.forEach((info) -> info.getExtra().ifPresent((extra) -> {
				MutableBlockPos mbp = extra.getFirst().mutable();
				if (shifter.apply(mbp)) {
					world.setBlock(mbp.immutable(), extra.getSecond(), UPDATE_FLAGS);
					positions.add(mbp.immutable());
				}
			}));

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach(x -> x.apply(infos));
			infos.forEach((info) -> {
				MutableBlockPos mbp = info.getPos().mutable();
				if (shifter.apply(mbp)) {
					if (canReplace.apply(world.getBlockState(mbp.immutable()))) {
						world.setBlock(mbp.immutable(), info.getState(), UPDATE_FLAGS);
						positions.add(mbp.immutable());
					}
				}
			});
			infos.forEach((info) -> info.getExtra().ifPresent((extra) -> {
				MutableBlockPos mbp = info.getPos().mutable();
				if (shifter.apply(mbp)) {
					if (canReplace.apply(world.getBlockState(extra.getFirst()))) {
						world.setBlock(mbp.immutable(), extra.getSecond(), UPDATE_FLAGS);
						positions.add(mbp.immutable());
					}
				}
			}));
		}

		return positions;
	}
}