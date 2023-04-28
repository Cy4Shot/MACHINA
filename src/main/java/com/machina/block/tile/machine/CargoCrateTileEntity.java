package com.machina.block.tile.machine;

import com.machina.block.CargoCrateBlock;
import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.inventory.MachinaItemStorage;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CargoCrateTileEntity extends MachinaTileEntity implements IAnimatable {

	private final AnimationFactory manager = new AnimationFactory(this);
	public boolean open = false;

	public CargoCrateTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CargoCrateTileEntity() {
		this(TileEntityInit.CARGO_CRATE.get());
	}
	
	MachinaItemStorage items;

	@Override
	public void createStorages() {
		this.items = add(new MachinaItemStorage(1));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		AnimationController controller = event.getController();
		controller.transitionLengthTicks = 0;

		if (event.getAnimatable().getBlockState().getValue(CargoCrateBlock.OPEN).booleanValue()) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.cargo_crate.open", false)
					.addAnimation("animation.cargo_crate.stay_open", true));
		} else {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.cargo_crate.stay_closed", true));
		}

		return PlayState.CONTINUE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.manager;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound.putBoolean("open", this.open);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		this.open = compound.getBoolean("open");
		super.load(state, compound);
	}

	public void setOpen() {
		this.open = true;
		this.sync();
	}
}
