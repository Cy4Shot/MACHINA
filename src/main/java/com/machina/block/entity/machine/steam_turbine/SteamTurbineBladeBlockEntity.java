package com.machina.block.entity.machine.steam_turbine;

import org.jetbrains.annotations.NotNull;

import com.machina.api.block.entity.MultiblockPartBlockEntity;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class SteamTurbineBladeBlockEntity extends MultiblockPartBlockEntity {

	public static final ModelProperty<Float> ROTATION = new ModelProperty<>();

	private float rotation = 0;
	private float rotationSpeed = 0;
	private static final float MAX_SPEED = 15f;
	private static final float ACCELERATION = 0.5f;
	private static final float DECELERATION = 0.3f;

	public SteamTurbineBladeBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.STEAM_TURBINE_BLADE.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.STEAM_TURBINE;
	}

	@Override
	public boolean isPort(MachinaCap cap) {
		return false;
	}

	@Override
	public int getMaxEnergy() {
		return 0;
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public void tick() {
		if (level == null) return;

		boolean turbineRunning = isTurbineRunning();

		if (turbineRunning) {
			if (rotationSpeed < MAX_SPEED) {
				rotationSpeed = Math.min(MAX_SPEED, rotationSpeed + ACCELERATION);
			}
		} else {
			if (rotationSpeed > 0) {
				rotationSpeed = Math.max(0, rotationSpeed - DECELERATION);
			}
		}

		if (rotationSpeed > 0) {
			rotation += rotationSpeed;
			if (rotation >= 360f) {
				rotation -= 360f;
			}

			if (level.isClientSide()) {
				requestModelDataUpdate();
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			}
		}

		super.tick();
	}

	private boolean isTurbineRunning() {
		if (getMaster() == null || level == null) {
			return false;
		}
		return BlockHelper.getFromTe(level, getMaster(), SteamTurbineControllerBlockEntity.class,
				SteamTurbineControllerBlockEntity::isEffectivelyRunning);
	}

	public float getRotation() {
		return rotation;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	@Override
	public @NotNull ModelData getModelData() {
		return ModelData.builder()
				.with(ROTATION, rotation)
				.build();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
		tag.putFloat("rotation", rotation);
		tag.putFloat("rotationSpeed", rotationSpeed);
		super.saveAdditional(tag, registries);
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
		rotation = tag.getFloat("rotation");
		rotationSpeed = tag.getFloat("rotationSpeed");
		super.loadAdditional(tag, registries);
	}
}
