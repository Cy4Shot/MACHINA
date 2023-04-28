package com.machina.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICustomStorage extends INBTSerializable<CompoundNBT> {
	public void setChanged(Runnable runnable);
	public String getTag();
}
