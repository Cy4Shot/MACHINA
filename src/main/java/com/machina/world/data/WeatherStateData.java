package com.machina.world.data;

import com.machina.Machina;
import com.machina.weather.system.ServerWeatherSystem;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WeatherStateData extends SavedData {

	private static final Factory<WeatherStateData> FACTORY = new Factory<>(WeatherStateData::new,
			WeatherStateData::new);

	public static final String ID = Machina.MOD_ID + "_weather_state";

	private String weatherEventId = "machina:clear";
	private int weatherTimer = 0;
	private int weatherDuration = 0;
	private int weatherAgeTicks = 0;
	private float weatherIntensity = 1.0F;
	private float windX = 0.0F;
	private float windZ = 0.0F;
	private float windTargetX = 0.0F;
	private float windTargetZ = 0.0F;
	private boolean hasState = false;

	WeatherStateData() {
		super();
	}

	public WeatherStateData(CompoundTag tag, Provider registries) {
		load(tag);
	}

	public static WeatherStateData get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(FACTORY, ID);
	}

	private void load(CompoundTag tag) {
		hasState = tag.contains("weatherEventId");
		if (!hasState) {
			return;
		}
		weatherEventId = tag.getString("weatherEventId");
		if (weatherEventId.isBlank()) {
			weatherEventId = "machina:clear";
		}
		weatherTimer = tag.getInt("weatherTimer");
		weatherDuration = tag.getInt("weatherDuration");
		weatherAgeTicks = tag.getInt("weatherAgeTicks");
		weatherIntensity = tag.getFloat("weatherIntensity");
		windX = tag.getFloat("windX");
		windZ = tag.getFloat("windZ");
		windTargetX = tag.getFloat("windTargetX");
		windTargetZ = tag.getFloat("windTargetZ");
	}

	@Override
	public CompoundTag save(CompoundTag tag, Provider registries) {
		tag.putString("weatherEventId", weatherEventId);
		tag.putInt("weatherTimer", weatherTimer);
		tag.putInt("weatherDuration", weatherDuration);
		tag.putInt("weatherAgeTicks", weatherAgeTicks);
		tag.putFloat("weatherIntensity", weatherIntensity);
		tag.putFloat("windX", windX);
		tag.putFloat("windZ", windZ);
		tag.putFloat("windTargetX", windTargetX);
		tag.putFloat("windTargetZ", windTargetZ);
		tag.putString("dataOwnerMod", Machina.MOD_ID);
		return tag;
	}

	public ServerWeatherSystem.PersistenceState toState() {
		ResourceLocation event = ResourceLocation.tryParse(weatherEventId);
		if (event == null) {
			event = ResourceLocation.fromNamespaceAndPath(Machina.MOD_ID, "clear");
		}
		return new ServerWeatherSystem.PersistenceState(event, weatherTimer, weatherDuration, weatherAgeTicks,
				weatherIntensity, windX, windZ, windTargetX, windTargetZ);
	}

	public boolean hasState() {
		return hasState;
	}

	public void fromState(ServerWeatherSystem.PersistenceState state) {
		boolean changed = !weatherEventId.equals(state.weatherEvent().toString())
				|| weatherTimer != state.weatherTimer() || weatherDuration != state.weatherDuration()
				|| weatherAgeTicks != state.weatherAgeTicks()
				|| Float.floatToIntBits(weatherIntensity) != Float.floatToIntBits(state.weatherIntensity())
				|| Float.floatToIntBits(windX) != Float.floatToIntBits(state.windX())
				|| Float.floatToIntBits(windZ) != Float.floatToIntBits(state.windZ())
				|| Float.floatToIntBits(windTargetX) != Float.floatToIntBits(state.windTargetX())
				|| Float.floatToIntBits(windTargetZ) != Float.floatToIntBits(state.windTargetZ());

		weatherEventId = state.weatherEvent().toString();
		weatherTimer = state.weatherTimer();
		weatherDuration = state.weatherDuration();
		weatherAgeTicks = state.weatherAgeTicks();
		weatherIntensity = state.weatherIntensity();
		windX = state.windX();
		windZ = state.windZ();
		windTargetX = state.windTargetX();
		windTargetZ = state.windTargetZ();

		if (changed) {
			setDirty();
		}
		hasState = true;
	}
}
