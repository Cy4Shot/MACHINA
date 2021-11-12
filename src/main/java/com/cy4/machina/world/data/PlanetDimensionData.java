package com.cy4.machina.world.data;

import java.util.HashSet;

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

	private static final String ID = "planet_dimensions";

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
		return nbt;
	}

	public void addId(String id) {
		this.dimensionIds.add(id);
		setDirty();
	}
	
	public void removeId(String id) {
		this.dimensionIds.remove(id);
		setDirty();
	}

}
