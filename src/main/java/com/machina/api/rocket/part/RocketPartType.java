package com.machina.api.rocket.part;

import com.machina.Machina;
import com.machina.api.util.reflect.MachinaStreamCodecs.HasId;

import net.minecraft.network.chat.Component;

public enum RocketPartType implements HasId {
	THRUSTER(384), FUEL_TANK(400), CHASSIS(416), LIFE_SUPPORT(432), SHIELD(448);

	private final int x;

	RocketPartType(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public Component getName() {
		return Component.translatable(Machina.MOD_ID + ".rocket_part_type." + name().toLowerCase());
	}

	public String getNBTName() {
		return "rocket_type_" + name().toLowerCase();
	}

	@Override
	public int getId() {
		return this.ordinal();
	}
}