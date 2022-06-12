package com.machina.util.server;

import com.machina.capability.energy.IEnergyContainer;
import com.machina.util.math.DirectionUtil;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtils {
	public static void trySendToNeighbors(IBlockReader world, BlockPos pos, IEnergyContainer cont, int maxSend) {
		if (cont.getEnergyStored() == 0)
			return;

		DirectionUtil.DIRECTIONS.forEach(side -> {
			trySendTo(world, pos, cont, maxSend, side);
		});
	}

	public static void trySendTo(IBlockReader world, BlockPos pos, IEnergyContainer cont, int maxSend, Direction side) {
		TileEntity tileEntity = world.getBlockEntity(pos.relative(side));
		if (tileEntity != null) {
			IEnergyStorage energy = cont.getEnergy(side).orElse(new EnergyStorage(0));
			tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite())
					.ifPresent(other -> trySendEnergy(maxSend, energy, other));
		}
	}

	private static void trySendEnergy(int maxSend, IEnergyStorage energy, IEnergyStorage other) {
		if (other.canReceive()) {
			int toSend = energy.extractEnergy(maxSend, true);
			int sent = other.receiveEnergy(toSend, false);
			if (sent > 0)
				energy.extractEnergy(sent, false);
		}
	}

	public static boolean hasEnergy(IBlockReader world, BlockPos pos, Direction dir) {
		TileEntity te = world.getBlockEntity(pos);
		if (te != null)
			return hasEnergy(te, dir);
		return false;
	}

	public static boolean hasEnergy(TileEntity te, Direction dir) {
		return te.getCapability(CapabilityEnergy.ENERGY, dir).isPresent();
	}
}
