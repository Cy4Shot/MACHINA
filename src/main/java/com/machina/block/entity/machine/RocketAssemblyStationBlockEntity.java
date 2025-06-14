package com.machina.block.entity.machine;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.sided.Side;
import com.machina.api.recipe.MachinaRecipe;
import com.machina.api.util.reflect.QuadFunction;
import com.machina.block.menu.RocketAssemblyStationMenu;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RocketAssemblyStationBlockEntity extends MachinaBlockEntity {

	private int progress = 0;

	public RocketAssemblyStationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public RocketAssemblyStationBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.ROCKET_ASSEMBLY_STATION.get(), pos, state);
	}

	@Override
	public void createStorages() {
		energyStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
		itemStorage(Side.INPUTS);
	}

	@Override
	public boolean hasItemIO() {
		return false;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	public boolean hasPower(MachinaRecipe<RocketAssemblyStationBlockEntity> r) {
		return this.getEnergy() >= r.getPowerRate();
	}

	public int getProgress() {
		return progress;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putInt("progress", progress);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		this.progress = tag.getInt("progress");
		super.load(tag);
	}

	@Override
	public int getMaxEnergy() {
		// TODO: Config
		return 100_000;
	}

	@Override
	protected QuadFunction<Integer, Level, BlockPos, Inventory, AbstractContainerMenu> createMenu() {
		return RocketAssemblyStationMenu::new;
	}

	@Override
	public void tick() {
		if (this.level.isClientSide())
			return;
		if (this.progress <= 0)
			return;

		this.progress--;
		if (this.progress == 0) {
		}
		this.setChanged();
	}
}