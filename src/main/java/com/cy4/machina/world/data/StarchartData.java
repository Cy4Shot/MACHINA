package com.cy4.machina.world.data;

import javax.annotation.WillNotClose;

import com.cy4.machina.starchart.Starchart;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class StarchartData extends WorldSavedData {

	public Starchart starchart = new Starchart();

	public StarchartData(String n, long seed) {
		super(n);
		starchart = new Starchart(seed);
	}

	private static final String ID = "starchart";

	public static StarchartData getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new StarchartData(ID, w.getSeed()), ID);
	}

	@Override
	public void load(CompoundNBT nbt) {
		starchart.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		return starchart.serializeNBT();
	}

	public void setStarchart(Starchart sc) {
		starchart = sc;
		this.setDirty();
	}

	public void setStarchartIfNull(Starchart sc) {
		if (starchart.planets.size() == 0) {
			setStarchart(sc);
		}
	}

}