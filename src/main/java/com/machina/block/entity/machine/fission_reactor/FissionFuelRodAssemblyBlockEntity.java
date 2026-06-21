package com.machina.block.entity.machine.fission_reactor;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.block.machine.fission_reactor.FissionFuelRodAssemblyBlock;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class FissionFuelRodAssemblyBlockEntity extends MultiblockPartBlockEntity {

	public final static int MAX_INSERT = 4;

	private static final String INSERT_KEY = "insertStage";

	private int insertStage = 0;

	public FissionFuelRodAssemblyBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.FISSION_FUEL_ROD_ASSEMBLY.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.FISSION_REACTOR;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return false;
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putInt(INSERT_KEY, this.insertStage);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		this.insertStage = tag.getInt(INSERT_KEY);
		super.loadAdditional(tag, registries);
	}

	public int getInsertStage() {
		return this.insertStage;
	}

	public boolean incrementStage() {
		this.insertStage++;
		if (this.insertStage > MAX_INSERT) {
			this.insertStage = MAX_INSERT;
			return false;
		}
		this.sync();
		return true;
	}

	public boolean decrementStage() {
		this.insertStage--;
		if (this.insertStage < 0) {
			this.insertStage = 0;
			return false;
		}
		this.sync();
		return true;
	}
	
	@Override
	public void form(BlockPos master) {
		setLit(true);
		super.form(master);
	}

	@Override
	public void deform(boolean deleted) {
		if (deleted) {
			setLit(false);
		}
		super.deform(deleted);
	}

	public void setLit(boolean lit) {
		this.level.setBlock(worldPosition, getBlockState().setValue(FissionFuelRodAssemblyBlock.LIT, lit), 3);
	}
}
