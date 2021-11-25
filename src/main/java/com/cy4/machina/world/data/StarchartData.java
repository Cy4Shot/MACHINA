/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.world.data;

import static com.cy4.machina.Machina.MOD_ID;

import javax.annotation.WillNotClose;

import com.cy4.machina.api.network.BaseNetwork;
import com.cy4.machina.network.MachinaNetwork;
import com.cy4.machina.network.message.S2CSyncStarchart;
import com.cy4.machina.starchart.Starchart;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class StarchartData extends WorldSavedData {

	private Starchart starchart = new Starchart();
	private boolean isGenerated;

	public StarchartData(String n) {
		super(n);
		starchart = new Starchart();
		isGenerated = false;
	}

	private static final String ID = MOD_ID + "_starchart";

	public static StarchartData getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new StarchartData(ID), ID);
	}

	@Override
	public void load(CompoundNBT nbt) {
		isGenerated = nbt.getBoolean("generated");
		starchart.deserializeNBT(nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("generated", isGenerated);
		return starchart.serializeNBT(nbt);
	}

	public void setStarchart(Starchart sc) {
		starchart = sc;
		this.setDirty();
	}

	public Starchart getStarchart() { return starchart; }

	public void setGenerated(boolean gen) {
		isGenerated = gen;
		this.setDirty();
	}

	public boolean getGenerated() { return isGenerated; }

	public void setStarchartIfNull(Starchart sc) {
		if (starchart.planets.size() == 0) {
			setStarchart(sc);
		}
	}

	public void syncClients() {
		BaseNetwork.sendToAll(MachinaNetwork.CHANNEL, new S2CSyncStarchart(starchart));
	}

	public void syncClient(ServerPlayerEntity e) {
		BaseNetwork.sendTo(MachinaNetwork.CHANNEL, new S2CSyncStarchart(starchart), e);
	}

	public void generateIf(long seed) {
		if (!isGenerated) {
			starchart.generateStarchart(seed);
			isGenerated = true;
			this.setDirty();
			syncClients();
		}
	}

	public static Starchart getStarchartForServer(MinecraftServer server) {
		return StarchartData.getDefaultInstance(server).getStarchart();
	}

}
