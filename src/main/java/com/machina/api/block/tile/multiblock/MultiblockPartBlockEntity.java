package com.machina.api.block.tile.multiblock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.tile.MachinaBlockEntity;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.multiblock.Multiblock;
import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.util.BlockHelper;
import com.machina.api.util.VecUtil;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

/**
 * The base class for a multiblock part / port.
 * 
 * Make sure to use the {@code formed} to check whether the multiblock has been
 * formed.
 * 
 * @author Cy4Shot
 * @since Machina 0.1.0
 */
public abstract class MultiblockPartBlockEntity extends MachinaBlockEntity {

	private Multiblock mb;
	private BlockPos master;
	public boolean formed = false;

	public MultiblockPartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		this.mb = MultiblockLoader.INSTANCE.getMultiblock(getMultiblock());
	}

	@Override
	public void createStorages() {
	}

	/**
	 * Function that returns a resource location pointing towards the multiblock
	 * json. </br>
	 * </br>
	 * For example, {@code machina:test} points to
	 * {@code data/machina/multiblock/test.json}.
	 * 
	 * @return {@link ResourceLocation}: The RL pointing towards the multiblock
	 *         json.
	 */
	public abstract ResourceLocation getMultiblock();

	/**
	 * Returns a boolean which specifies if the multiblock part is a port. </br>
	 * </br>
	 * A <strong>port</strong> is a multiblock part through which you can pipe
	 * items, fluid or energy into the multiblock. All controllers are ports.
	 * 
	 * @return {@code boolean}: Whether this block is a port.
	 */
	public abstract boolean isPort();

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (master == null || !isPort()) {
			return super.getCapability(cap, side);
		}

		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		if (cap == ForgeCapabilities.ENERGY) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return BlockHelper.getCapability(level, master, side, cap);
		}
		return super.getCapability(cap, side);
	}

	public void form(BlockPos master) {
		this.formed = true;
		this.master = master;
		this.setChanged();
		if (!level.isClientSide()) {
			((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, worldPosition.getX() + 0.5f,
					worldPosition.getY() + 0.5f, worldPosition.getZ() + 0.5f, 20, 0.5f, 0.5f, 0.5f, 0);
		}
	}

	public void deform() {
		this.formed = false;
		this.master = null;
		this.setChanged();
		if (!level.isClientSide()) {
			((ServerLevel) level).sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5f,
					worldPosition.getY() + 0.5f, worldPosition.getZ() + 0.5f, 20, 0.5f, 0.5f, 0.5f, 0);
		}
	}

	public void update() {
		if (this.master != null) {
			BlockHelper.doWithTe(level, master, MultiblockMasterBlockEntity.class, te -> {
				te.update();
			});
		}
	}

	public void attemptAssimilate() {
		this.master = findMaster();
		update();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		if (formed) {
			tag.put("master", NbtUtils.writeBlockPos(master));
		}
		tag.putBoolean("formed", formed);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		this.formed = tag.getBoolean("formed");
		if (formed) {
			this.master = NbtUtils.readBlockPos(tag.getCompound("master"));
		}
		super.load(tag);
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

	@Override
	public int getContainerSize() {
		if (!this.formed)
			return 0;
		return super.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		if (!this.formed)
			return true;
		return super.isEmpty();
	}

	@Override
	public @NotNull ItemStack getItem(int pIndex) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.getItem(pIndex);
	}

	@Override
	public @NotNull ItemStack removeItem(int pIndex, int pCount) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.removeItem(pIndex, pCount);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
		if (!this.formed)
			return ItemStack.EMPTY;
		return super.removeItemNoUpdate(pIndex);
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		if (!this.formed)
			return;
		super.setItem(pIndex, pStack);
	}

	@Override
	public void clearContent() {
		if (!this.formed)
			return;
		super.clearContent();
	}

	@Override
	public int getEnergy() {
		if (!this.formed)
			return 0;
		return super.getEnergy();
	}

	@Override
	public int getMaxEnergy() {
		if (!this.formed)
			return 0;
		return super.getMaxEnergy();
	}

	@Override
	public float getEnergyProp() {
		if (!this.formed)
			return 0;
		return super.getEnergyProp();
	}

	@Override
	public MachinaTank getTank(int id) {
		if (!this.formed)
			return null;
		return super.getTank(id);
	}
}