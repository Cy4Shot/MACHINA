package com.machina.block.tile.multiblock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.machina.block.tile.MachinaTileEntity;
import com.machina.multiblock.Multiblock;
import com.machina.multiblock.MultiblockLoader;
import com.machina.util.helper.BlockHelper;
import com.machina.util.math.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class MultiblockPartTileEntity extends MachinaTileEntity {

	private Multiblock mb;
	private BlockPos master;
	public boolean formed = false;

	public MultiblockPartTileEntity(TileEntityType<?> type) {
		super(type);

		this.mb = MultiblockLoader.INSTANCE.getMultiblock(getMultiblock());
	}

	@Override
	public void createStorages() {
	}

	public abstract ResourceLocation getMultiblock();

	public abstract boolean isPort();

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (master == null || !isPort()) {
			return super.getCapability(cap, side);
		}

		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		return super.getCapability(cap, side);
	}

	public void form(BlockPos master) {
		this.formed = true;
		this.master = master;
		this.setChanged();
	}

	public void deform() {
		this.formed = false;
		this.master = null;
		this.setChanged();
	}

	public void update() {
		if (this.master != null) {
			BlockHelper.doWithTe(level, master, MultiblockMasterTileEntity.class, te -> {
				te.update();
			});
		}
	}

	public void attemptAssimilate() {
		this.master = findMaster();
		update();
	}

	@Override
	public CompoundNBT save(CompoundNBT tag) {
		if (formed) {
			tag.put("master", NBTUtil.writeBlockPos(master));
		}
		tag.putBoolean("formed", formed);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundNBT tag) {
		this.formed = tag.getBoolean("formed");
		if (formed) {
			this.master = NBTUtil.readBlockPos(tag.getCompound("master"));
		}
		super.load(state, tag);
	}

	private BlockPos findMaster() {

		int maxExplore = (int) Math.ceil((double) VecUtil.max(mb.size) / 2d);
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
					if (mb.allowedBlock.contains(state.getBlock())) {
						openSet.add(offset);
						traversed.add(offset);
					} else if (state.getBlock().equals(mb.controller.getBlock())) {
						return offset;
					}
				}
			}
		}
		return null;
	}
}