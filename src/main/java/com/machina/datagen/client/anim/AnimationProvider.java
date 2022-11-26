package com.machina.datagen.client.anim;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;

import net.minecraft.data.DataGenerator;

public class AnimationProvider extends BaseAnimationProvider {

	public AnimationProvider(DataGenerator gen) {
		super(gen, Machina.MOD_ID);
	}

	@Override
	public void createAnimations() {
		noAnim(BlockInit.COMPONENT_ANALYZER.get());
		noAnim(BlockInit.IRON_SCAFFOLDING.get());
		noAnim(BlockInit.STEEL_SCAFFOLDING.get());
		noAnim(BlockInit.ALUMINUM_SCAFFOLDING.get());
		noAnim(BlockInit.COPPER_SCAFFOLDING.get());
	}
}