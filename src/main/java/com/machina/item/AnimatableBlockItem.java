package com.machina.item;

import com.machina.client.model.CustomBlockModel;
import com.machina.client.renderer.item.GeoBlockItemRenderer;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AnimatableBlockItem extends BlockItem implements IAnimatable {

	private final AnimationFactory manager = new AnimationFactory(this);

	public AnimatableBlockItem(Block pBlock, Properties pProperties, final CustomBlockModel<?> blockModel) {
		super(pBlock, pProperties.setISTER(() -> () -> new GeoBlockItemRenderer(blockModel)));
	}

	@Override
	public void registerControllers(AnimationData data) {
	}

	@Override
	public AnimationFactory getFactory() {
		return manager;
	}
}