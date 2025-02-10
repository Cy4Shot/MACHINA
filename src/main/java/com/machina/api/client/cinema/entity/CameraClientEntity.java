package com.machina.api.client.cinema.entity;

import java.util.Collections;
import java.util.Objects;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class CameraClientEntity extends AbstractClientPlayer {

	private static final Minecraft mc = Minecraft.getInstance();

	private Vector3d cameraFocus = null;

	public CameraClientEntity() {
		super(Objects.requireNonNull(mc.level), Objects.requireNonNull(mc.player).getGameProfile());

		this.getAbilities().mayfly = true;
		this.getAbilities().flying = true;
		this.getAbilities().invulnerable = true;
	}

	@Nullable
	public Vector3d getCameraFocus() {
		return cameraFocus;
	}

	public void setCameraFocus(@Nullable Vector3d cameraFocus) {
		this.cameraFocus = cameraFocus;
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public @NotNull Iterable<ItemStack> getArmorSlots() {
		return Collections.emptyList();
	}

	@Override
	public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot pSlot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
	}
}