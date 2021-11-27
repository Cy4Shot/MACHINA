package com.machina.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

@Mixin(value = SugarCaneBlock.class, priority = 1020)
public abstract class SugarCaneBlockMixin extends Block {

	private SugarCaneBlockMixin(Properties p_i48440_1_) {
		super(p_i48440_1_);
	}

	private static final ITag.INamedTag<Block> SUGARCANE_CAN_LIVE_TAG = BlockTags
			.bind(new ResourceLocation("sugarcane_can_live").toString());

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;is(Lnet/minecraft/block/Block;)Z", ordinal = 0), method = "Lnet/minecraft/block/SugarCaneBlock;canSurvive(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;)Z")
	private boolean machina$canSurvive(BlockState blockstate, Block block, BlockState methodState, IWorldReader level,
			BlockPos pos) {
		return blockstate.is(SUGARCANE_CAN_LIVE_TAG) || blockstate.is(Blocks.GRASS_BLOCK);
	}

}
