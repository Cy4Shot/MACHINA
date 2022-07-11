package com.machina.client.cinema.entity;

import java.util.Collections;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

public class CameraClientEntity extends ClientPlayerEntity {

	private static final Minecraft mc = Minecraft.getInstance();

	private Vector3d cameraFocus = null;

	public CameraClientEntity() {
		super(mc, mc.level, mc.player.connection, mc.player.getStats(), mc.player.getRecipeBook(), false, false);

		abilities.mayfly = true;
		abilities.flying = true;
		abilities.invulnerable = true;
	}

	@Nullable
	public Vector3d getCameraFocus() {
		return cameraFocus;
	}

	public void setCameraFocus(@Nullable Vector3d cameraFocus) {
		this.cameraFocus = cameraFocus;
	}

//    public void transformToFocusOnPoint(Vector3d toFocus, float pTicks, boolean propagate) {
//        Vector3 angles = Vector3.atEntityCorner(this).subtract(toFocus).copyToPolar();
//        Vector3 prevAngles = new Vector3(prevPosX, prevPosY, prevPosZ).subtract(toFocus).copyToPolar();
//        double pitch = 90 - angles.getY();
//        double pitchPrev = 90 - prevAngles.getY();
//        double yaw = -angles.getZ();
//        double yawPrev = -prevAngles.getZ();
//
//        if (propagate) {
//            ClientCameraUtil.positionCamera(this, pTicks, getPosX(), getPosY(), getPosZ(), prevPosX, prevPosY, prevPosZ, yaw, yawPrev, pitch, pitchPrev);
//        }
//    }

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlotType pSlot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlotType pSlot, ItemStack pStack) {
	}
}