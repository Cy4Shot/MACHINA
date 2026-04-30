package com.machina.api.util.block;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.machina.Machina;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.fluid.ChemicalFluid;
import com.machina.api.util.math.MathUtil;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class BlockHelper {
	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> boolean doWithTe(BlockGetter world, BlockPos pos, Class<T> clazz,
			Consumer<T> todo) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e == null || !(clazz.isAssignableFrom(e.getClass()))) {
			Machina.LOGGER.error("BE at {} is null.", pos.toShortString());
			new Throwable().printStackTrace(System.err);
			return false;
		}

		todo.accept((T) e);
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity, R> R getFromTe(BlockGetter world, BlockPos pos, Class<T> clazz,
			Function<T, R> todo) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e == null || !(clazz.isAssignableFrom(e.getClass()))) {
			Machina.LOGGER.error("BE at {} is null.", pos.toShortString());
			new Throwable().printStackTrace(System.err);
			return null;
		}

		return todo.apply((T) e);
	}

	public static BlockState waterlog(BlockState state, BlockGetter world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		return state.setValue(BlockStateProperties.WATERLOGGED,
				fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
	}

	public static HolderLookup<Block> blockHolderLookup() {
		return RegistryLayer.createRegistryAccess().compositeAccess().lookup(Registries.BLOCK).get();
	}

	public static BlockState parseState(HolderLookup<Block> block, String state) {
		try {
			return BlockStateParser.parseForBlock(block, state, true).blockState();
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static ChemicalFluid parseChemicalFluid(String chem) {
		FluidObject fob = FluidInit.OBJS.stream().filter(obj -> obj.name().equals(chem)).findFirst().orElse(null);
		if (fob == null) {
			return null;
		}
		return new ChemicalFluid(fob.chem(), fob.fluid());
	}

	public static boolean hasEnergy(Level level, BlockPos pos, @Nullable Direction side) {
		return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, side) != null;
	}

	public static boolean hasFluid(Level level, BlockPos pos, @Nullable Direction side) {
		return level.getCapability(Capabilities.FluidHandler.BLOCK, pos, side) != null;
	}

	public static boolean hasItem(Level level, BlockPos pos, @Nullable Direction side) {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side) != null;
	}

	// McJty
	public static int receiveEnergy(Level level, BlockPos pos, Direction from, long maxReceive) {
		IEnergyStorage store = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, from);
		if (store != null) {
			return store.receiveEnergy(MathUtil.unsignedClampToInt(maxReceive), false);
		}
		return 0;
	}

	public static void sendEnergy(Level world, BlockPos pos, long storedPower, long sendPerTick,
			MachinaBlockEntity storage) {
		for (Direction facing : Direction.values()) {
			if (!storage.canConsumeEnergy(facing))
				continue;
			BlockPos p = pos.relative(facing);
			Direction opposite = facing.getOpposite();
			if (hasEnergy(world, p, opposite)) {
				long rfToGive = Math.min(sendPerTick, storedPower);
				int received = receiveEnergy(world, p, opposite, rfToGive);
				storage.consumeEnergy(received);
				storedPower -= received;
				if (storedPower <= 0) {
					break;
				}
			}
		}
	}

}
