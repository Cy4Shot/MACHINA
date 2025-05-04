package com.machina.api.rocket;

import com.machina.registration.init.EntityTypeInit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class RocketEntity extends Entity {

	public RocketEntity(EntityType<?> t, Level l) {
		super(t, l);
	}

	public RocketEntity(Level level) {
		this(EntityTypeInit.ROCKET.get(), level);
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		// TODO Auto-generated method stub

	}

}
