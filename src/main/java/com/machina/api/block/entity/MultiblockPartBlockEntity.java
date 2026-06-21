package com.machina.api.block.entity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.machina.api.cap.fluid.SidedFluidWrapper;
import com.machina.api.multiblock.Multiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.math.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public abstract class MultiblockPartBlockEntity extends MachinaBlockEntity {

	private Multiblock mb;
	private BlockPos master;
	public boolean formed = false;

	public MultiblockPartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		this.mb = MultiblockLoader.INSTANCE.get(getMultiblock());
	}

	@Override
	public void createStorages() {
	}

	public abstract ResourceLocation getMultiblock();

	public abstract boolean isPort(MachinaCap cap);

	@Override
	public boolean isCapabilitiesActive(MachinaCap cap) {
		return this.master != null && isPort(cap);
	}

	@Override
	public SidedInvWrapper getInvCap(Direction side) {
		if (!this.isCapabilitiesActive(MachinaCap.ITEM)) {
			return null;
		}
		return BlockHelper.getFromTe(level, master, MultiblockMasterBlockEntity.class, be -> be.getInvCap(side));
	}

	@Override
	public IEnergyStorage getEnergyCap(Direction side) {
		if (!this.isCapabilitiesActive(MachinaCap.ENERGY)) {
			return null;
		}
		return BlockHelper.getFromTe(level, master, MultiblockMasterBlockEntity.class, be -> be.getEnergyCap(side));
	}

	@Override
	public SidedFluidWrapper getFluidCap(Direction side) {
		if (!this.isCapabilitiesActive(MachinaCap.FLUID)) {
			return null;
		}
		return BlockHelper.getFromTe(level, master, MultiblockMasterBlockEntity.class, be -> be.getFluidCap(side));
	}

	public void form(BlockPos master) {
		this.formed = true;
		this.master = master;
		this.setChanged();
		if (!level.isClientSide()) {
			((ServerLevel) level).sendParticles(new DustParticleOptions(new Vector3f(0, 1, 1), 1),
					worldPosition.getX() + 0.5f, worldPosition.getY() + 0.5f, worldPosition.getZ() + 0.5f, 20, 0.5f,
					0.5f, 0.5f, 0);
		}
	}

	public void deform(boolean deleted) {
		this.formed = false;
		this.master = null;
		this.setChanged();
		if (!level.isClientSide()) {
			((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5f,
					worldPosition.getY() + 0.5f, worldPosition.getZ() + 0.5f, 20, 0.5f, 0.5f, 0.5f, 0);
		}
	}

	public void update(@Nullable BlockPos changed) {
		if (this.master != null) {
			BlockHelper.doWithTe(level, master, MultiblockMasterBlockEntity.class, te -> {
				te.update(changed);
			});
		}
	}

	public void attemptAssimilate(@Nullable BlockPos changed) {
		this.master = findMaster();
		update(changed);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		if (formed) {
			tag.put("master", NbtUtils.writeBlockPos(master));
		}
		tag.putBoolean("formed", formed);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.formed = tag.getBoolean("formed");
		if (formed) {
			this.master = NbtUtils.readBlockPos(tag, "master").orElse(BlockPos.ZERO);
		}
		super.loadAdditional(tag, registries);
	}

	private BlockPos findMaster() {

		int maxExplore = VecUtil.max(mb.size);
		int size = maxExplore * maxExplore * maxExplore;

		Queue<BlockPos> openSet = new LinkedList<>();
		Set<BlockPos> traversed = new ObjectOpenHashSet<>();
		openSet.add(worldPosition);
		traversed.add(worldPosition);
		while (!openSet.isEmpty()) {
			BlockPos ptr = openSet.poll();
			int traversedSize = traversed.size();
			if (traversedSize >= size) {
				return null;
			}
			for (Direction side : Direction.values()) {
				BlockPos offset = ptr.relative(side);
				if (!traversed.contains(offset)) {
					BlockState state = level.getBlockState(offset);
					if (state.getBlock().equals(mb.controller.getBlock())) {
						return offset;
					} else if (mb.allowedBlock.contains(state.getBlock())) {
						openSet.add(offset);
						traversed.add(offset);
					}
				}
			}
		}
		return null;
	}

	public BlockPos getMaster() {
		return this.master;
	}
}
