package com.machina.block.entity;

import com.machina.api.block.entity.BaseBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
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
	private int chargeState = 3;
	private Player target = null;

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
		ticksSinceLastShot++;

		if (ticksSinceLastShot == 200) {
			ticksSinceLastShot = 0;
			triggerAnim("shoot_controller", "shoot");
		}

		if (level != null) {
			Player closestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(),
					worldPosition.getZ(), 10, false);
			if (closestPlayer != null) {
				target = closestPlayer;
			} else {
				target = null;
			}
		}
	}

	@Override
	public void registerControllers(ControllerRegistrar reg) {
		reg.add(new AnimationController<>(this, "shoot_controller", state -> PlayState.STOP).triggerableAnim("shoot",
				FIRE_TURRET));
	}

	public int getChargeState() {
		return chargeState;
	}

	public float getTargetRotX() {
		if (target == null) {
			return 0;
		}

		double turretX = worldPosition.getX() + 0.5;
		double turretY = worldPosition.getY() + 1.0;
		double turretZ = worldPosition.getZ() + 0.5;

		double deltaY = target.getEyeY() - turretY;
		double deltaXZ = Math.sqrt(
				Math.pow(target.getX() - turretX, 2) + Math.pow(target.getZ() - turretZ, 2));
		return (float) Math.atan2(deltaY, deltaXZ);
	}

	public float getTargetRotY() {
		if (target == null) {
			return 0;
		}

		double turretX = worldPosition.getX() + 0.5;
		double turretZ = worldPosition.getZ() + 0.5;

		double deltaX = target.getX() - turretX;
		double deltaZ = target.getZ() - turretZ;
		return (float) -Math.PI / 2 - (float) Math.atan2(deltaZ, deltaX);
	}
}
