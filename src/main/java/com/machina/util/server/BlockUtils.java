package com.machina.util.server;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockUtils {

	public static final Block getBlock(ResourceLocation loc) {
		return ForgeRegistries.BLOCKS.getValue(loc);
	}

	public static final Block getBlock(String loc) {
		return getBlock(new ResourceLocation(loc));
	}

	public static BlockState waterlog(BlockState state, IBlockReader world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		return state.setValue(BlockStateProperties.WATERLOGGED,
				fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
	}

	public static <C> LazyOptional<C> getCapability(IBlockReader world, BlockPos pos, Direction side,
			Capability<C> capability) {
		TileEntity tile = world.getBlockEntity(pos);
		if (tile != null) {
			return tile.getCapability(capability, side);
		}
		return LazyOptional.empty();
	}
}
