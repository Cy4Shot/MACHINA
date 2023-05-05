package com.machina.block.tile.basic;

import com.machina.block.tile.BaseTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NumberNBT;

public class TintedTileEntity extends BaseTileEntity {

	public int id = -1;

	public TintedTileEntity() {
		super(TileEntityInit.TINTED.get());
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putInt("PlanetID", id);
		return super.save(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		id = getInt(nbt, "PlanetID", -1);
		super.load(state, nbt);
	}

	public static int getInt(CompoundNBT nbt, String pKey, int def) {
		try {
			if (nbt.contains(pKey, 99)) {
				return ((NumberNBT) nbt.get(pKey)).getAsInt();
			}
		} catch (ClassCastException classcastexception) {
		}

		return def;
	}
}
