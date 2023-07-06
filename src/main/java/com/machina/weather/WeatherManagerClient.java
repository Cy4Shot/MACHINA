package com.machina.weather;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class WeatherManagerClient extends WeatherManager {
	public WeatherManagerClient(RegistryKey<World> dimension) {
		super(dimension);
	}

	@SuppressWarnings("resource")
	@Override
	public World getWorld() {
		return Minecraft.getInstance().level;
	}

	public void nbtSyncFromServer(CompoundNBT parNBT) {
		String command = parNBT.getString("command");

		if (command.equals("syncWindUpdate")) {
			CompoundNBT nbt = parNBT.getCompound("data");
			getWindManager().nbtSyncFromServer(nbt);
		}
	}
}
