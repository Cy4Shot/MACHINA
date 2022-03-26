package com.machina.block.tile;

import com.machina.block.CargoCrateBlock;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CargoCrateTileEntity extends TileEntity implements IAnimatable {

	private final AnimationFactory manager = new AnimationFactory(this);

	public CargoCrateTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CargoCrateTileEntity() {
		this(TileEntityTypesInit.CARGO_CRATE.get());
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

}
