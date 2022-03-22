package com.machina.world.data;

import java.util.HashSet;

import com.machina.Machina;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class PlanetDimensionData extends WorldSavedData {

	public final HashSet<String> dimensionIds = new HashSet<>();

	public PlanetDimensionData(String n) {
		super(n);
	}

	private static final String ID = Machina.MOD_ID + "_planet_dimensions";

	public static PlanetDimensionData getDefaultInstance(MinecraftServer server) {
		return server.getLevel(World.OVERWORLD).getDataStorage().computeIfAbsent(() -> new PlanetDimensionData(ID), ID);
	}

	@Override
	public void load(CompoundNBT nbt) {
		dimensionIds.clear();
		ListNBT listNBT = nbt.getList("dimensionIds", Constants.NBT.TAG_STRING);
		for (INBT inbt : listNBT) {
			StringNBT stringNBT = (StringNBT) inbt;
			dimensionIds.add(stringNBT.getAsString());
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ListNBT listNBT = new ListNBT();
		for (String str : dimensionIds) {
			listNBT.add(StringNBT.valueOf(str));
		}
		nbt.put("dimensionIds", listNBT);
		nbt.putString("dataOwnerMod", Machina.MOD_ID);
		return nbt;
	}

	public void addId(String id) {
		dimensionIds.add(id);
		setDirty();
	}

	public void removeId(String id) {
		dimensionIds.remove(id);
		setDirty();
	}

}
