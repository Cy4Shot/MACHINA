package com.machina.block.tile;

import com.machina.block.tile.base.BaseTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CustomModelTileEntity extends BaseTileEntity implements IAnimatable {

	private final AnimationFactory manager = new AnimationFactory(this);
	
	public String name = "cargo_crate";

	public CustomModelTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CustomModelTileEntity() {
		this(TileEntityInit.CUSTOM_MODEL.get());
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString("model", name);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState p_230337_1_, CompoundNBT nbt) {
		name = nbt.getString("model");
		super.load(p_230337_1_, nbt);
	}

	@Override
	public void registerControllers(AnimationData data) {
	}

	@Override
	public AnimationFactory getFactory() {
		return this.manager;
	}
}