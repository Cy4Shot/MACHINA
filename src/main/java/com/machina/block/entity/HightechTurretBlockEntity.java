package com.machina.block.entity;

import com.machina.api.block.entity.BaseBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HightechTurretBlockEntity extends BaseBlockEntity implements GeoBlockEntity {

	private static final RawAnimation FIRE_TURRET = RawAnimation.begin().thenPlay("fire");

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	private int ticksSinceLastShot = 0;
	private int ticksSinceLastAlertLevelChange = 0;
	private int alertLevel = 0;
	private Player target = null;

	public float clientRotX = 0;
	public float clientRotY = 0;

	public HightechTurretBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.HIGHTECH_TURRET.get(), pos, state);
	}

	@Override
	public boolean activeModel() {
		return false;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	public void tick() {
		if (this.level == null) {
			return;
		}

		// Update Shot
		ticksSinceLastShot++;
		if (ticksSinceLastShot == 200) {
			ticksSinceLastShot = 0;
			triggerAnim("shoot_controller", "shoot");
		}

		// Update Target
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
				10, false);
		if (closestPlayer != null) {
			target = closestPlayer;
		} else {
			target = null;
		}

		// Update Alert Level
		if (!this.level.isClientSide()) {
			boolean increase = this.target != null && this.alertLevel < 3;
			boolean decrease = this.target == null && this.alertLevel > 0;
			if (increase || decrease) {
				if (this.ticksSinceLastAlertLevelChange >= 5) {
					this.alertLevel += increase ? 1 : -1;
					this.sync();
					this.ticksSinceLastAlertLevelChange = 0;
				} else {
					this.ticksSinceLastAlertLevelChange++;
				}
			}
		}
	}

	@Override
	public void registerControllers(ControllerRegistrar reg) {
		reg.add(new AnimationController<>(this, "shoot_controller", state -> PlayState.STOP).triggerableAnim("shoot",
				FIRE_TURRET));
	}

	public int getAlertLevel() {
		return alertLevel;
	}

	public float getTargetRotX() {
		if (target == null || this.alertLevel < 2) {
			return 0;
		}

		double turretX = worldPosition.getX() + 0.5;
		double turretY = worldPosition.getY() + 1.0;
		double turretZ = worldPosition.getZ() + 0.5;

		double deltaY = target.getEyeY() - turretY;
		double deltaXZ = Math.sqrt(Math.pow(target.getX() - turretX, 2) + Math.pow(target.getZ() - turretZ, 2));
		return (float) Math.atan2(deltaY, deltaXZ);
	}

	public float getTargetRotY() {
		if (target == null || this.alertLevel < 2) {
			return 0;
		}

		double turretX = worldPosition.getX() + 0.5;
		double turretZ = worldPosition.getZ() + 0.5;

		double deltaX = target.getX() - turretX;
		double deltaZ = target.getZ() - turretZ;
		return (float) -Math.PI / 2 - (float) Math.atan2(deltaZ, deltaX);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, Provider registries) {
		tag.putInt("alertLevel", alertLevel);
		super.saveAdditional(tag, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, Provider registries) {
		this.alertLevel = tag.getInt("alertLevel");
		super.loadAdditional(tag, registries);
	}
}
