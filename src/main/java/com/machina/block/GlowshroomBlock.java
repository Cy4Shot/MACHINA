package com.machina.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GlowshroomBlock extends FlowerBlock {

	public static final MapCodec<GlowshroomBlock> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects),
					propertiesCodec()
			).apply(instance, GlowshroomBlock::new)
	);

	public GlowshroomBlock(SuspiciousStewEffects effects, Properties props) {
		super(effects, props);
	}

	public GlowshroomBlock(Holder<MobEffect> effect, int duration, Properties props) {
		super(effect, duration, props);
	}

	@Override
	public MapCodec<? extends FlowerBlock> codec() {
		return CODEC;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolid();
	}
}
