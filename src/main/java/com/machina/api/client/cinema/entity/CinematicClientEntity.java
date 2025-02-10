package com.machina.api.client.cinema.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CinematicClientEntity extends AbstractClientPlayer {

	private static final Minecraft mc = Minecraft.getInstance();

	public CinematicClientEntity() {
		super(Objects.requireNonNull(mc.level), Objects.requireNonNull(mc.player).getGameProfile());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isModelPartShown(@NotNull PlayerModelPart part) {
		return mc.player != null && mc.player.isModelPartShown(part);
	}

	@Override
	public @NotNull ItemStack getItemBySlot(EquipmentSlot pSlot) {
		if (pSlot.isArmor() || mc.player == null)
			return super.getItemBySlot(pSlot);
		return mc.player.getItemBySlot(pSlot);
	}

	@Override
	public @NotNull ItemStack getItemInHand(@NotNull InteractionHand pHand) {
		if (mc.player == null)
			return super.getItemInHand(pHand);
		return mc.player.getItemInHand(pHand);
	}

	@Override
	public @NotNull ItemStack getMainHandItem() {
		if (mc.player == null)
			return super.getMainHandItem();
		return mc.player.getMainHandItem();
	}

	@Override
	public @NotNull ItemStack getOffhandItem() {
		if (mc.player == null)
			return super.getOffhandItem();
		return mc.player.getOffhandItem();
	}

	@Override
	public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
	}
}