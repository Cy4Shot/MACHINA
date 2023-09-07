package com.machina.api.client.cinema.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CinematicClientEntity extends AbstractClientPlayer {

	private static final Minecraft mc = Minecraft.getInstance();

	public CinematicClientEntity() {
		super(mc.level, mc.player.getGameProfile());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isModelPartShown(PlayerModelPart part) {
		return mc.player != null && mc.player.isModelPartShown(part);
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot pSlot) {
		if (pSlot.isArmor() || mc.player == null)
			return super.getItemBySlot(pSlot);
		return mc.player.getItemBySlot(pSlot);
	}

	@Override
	public ItemStack getItemInHand(InteractionHand pHand) {
		if (mc.player == null)
			return super.getItemInHand(pHand);
		return mc.player.getItemInHand(pHand);
	}

	@Override
	public ItemStack getMainHandItem() {
		if (mc.player == null)
			return super.getMainHandItem();
		return mc.player.getMainHandItem();
	}

	@Override
	public ItemStack getOffhandItem() {
		if (mc.player == null)
			return super.getOffhandItem();
		return mc.player.getOffhandItem();
	}

	@Override
	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
	}
}