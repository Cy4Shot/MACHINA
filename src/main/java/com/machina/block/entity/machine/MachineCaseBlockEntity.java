package com.machina.block.entity.machine;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.multiblock.Multiblock;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.MachineCaseMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MachineCaseBlockEntity extends MachinaBlockEntity {

	public final Multiblock mb;
	public boolean formed = false;

	public MachineCaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		this.mb = null;
	}

	public MachineCaseBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.MACHINE_CASE.get(), pos, state);
	}

	@Override
	public void createStorages() {
		itemStorage(Side.NONES);
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return MachineCaseMenu::new;
	}

	public void update() {
		this.formed = this.mb != null && valid();
		this.setChanged();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putBoolean("formed", formed);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.formed = tag.getBoolean("formed");
		super.loadAdditional(tag, registries);
	}

	private boolean valid() {
		Vec3i cp = this.getBlockPos().subtract(mb.controller_pos);
		Vec3i size = mb.size;

		for (int x = 0; x < size.getX(); x++) {
			for (int y = 0; y < size.getY(); y++) {
				for (int z = 0; z < size.getZ(); z++) {
					String v = mb.structure[x][y][z];
					if (!v.equals(" ")) {
						List<BlockState> allowed = List.of(mb.map.get(v));
						BlockPos pos = new BlockPos(cp.getX() + x, cp.getY() + y, cp.getZ() + z);
						BlockState state = null;
						if (this.level != null) {
							state = this.level.getBlockState(pos);
						}
						if (!allowed.contains(state))
							return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}
}
