package com.machina.client.cinema.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CinematicClientEntity extends AbstractClientPlayerEntity {

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
	public ItemStack getItemBySlot(EquipmentSlotType pSlot) {
		if (pSlot.getType().equals(EquipmentSlotType.Group.ARMOR))
			return super.getItemBySlot(pSlot);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getItemInHand(Hand pHand) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getMainHandItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getOffhandItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlotType pSlot, ItemStack pStack) {
	}
}
