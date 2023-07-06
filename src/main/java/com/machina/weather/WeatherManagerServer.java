package com.machina.weather;

import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CSyncWeather;
import com.machina.weather.wind.WindManager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

public class WeatherManagerServer extends WeatherManager {

	private ServerWorld world;

	public WeatherManagerServer(ServerWorld world) {
		super(world.dimension());
		this.world = world;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void tick() {
		super.tick();

		if (world != null) {
			WindManager windMan = getWindManager();

			// sync wind
			if (world.getGameTime() % 60 == 0) {
				syncWindUpdate(windMan);
			}
		}
	}

	public void syncWindUpdate(WindManager wind) {
		// packets
		CompoundNBT data = new CompoundNBT();
		data.putString("command", "syncWindUpdate");
		data.put("data", wind.nbtSyncForClient());
		MachinaNetwork.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> getWorld().dimension()),
				new S2CSyncWeather(data));
	}
}
