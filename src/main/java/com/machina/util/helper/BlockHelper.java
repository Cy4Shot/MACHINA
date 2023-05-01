package com.machina.util.helper;

import java.util.function.Consumer;

import com.machina.Machina;
import com.machina.block.container.base.IMachinaContainerProvider;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockHelper {

	public static <T extends TileEntity & IMachinaContainerProvider> void openGui(World world, BlockPos pos,
			PlayerEntity player, Class<T> clazz) {
		doWithTe(world, pos, clazz, te -> {
			NetworkHooks.openGui((ServerPlayerEntity) player, clazz.cast(te), pos);
		});
	}

	public static <T extends TileEntity> void doWithTe(Context context, BlockPos pos, Class<T> clazz,
			Consumer<T> todo) {
		doWithTe(context.getSender().getLevel(), pos, clazz, todo);
	}

	@SuppressWarnings("unchecked")
	public static <T extends TileEntity> void doWithTe(World world, BlockPos pos, Class<T> clazz, Consumer<T> todo) {
		TileEntity e = world.getBlockEntity(pos);
		if (e == null || !(clazz.isAssignableFrom(e.getClass()))) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}

		todo.accept((T) e);
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
