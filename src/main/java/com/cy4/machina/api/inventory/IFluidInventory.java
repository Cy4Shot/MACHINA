package com.cy4.machina.api.inventory;

import static net.minecraft.item.ItemStack.EMPTY;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IFluidInventory extends IInventory, IFluidHandler {

	@Override
	default void clearContent() {
	}
	@Override
	default int getContainerSize() { return 0; }
	@Override
	default ItemStack getItem(int pIndex) {
		return ItemStack.EMPTY;
	}
	@Override
	default boolean isEmpty() { return true; }
	@Override
	default ItemStack removeItem(int pIndex, int pCount) {
		return EMPTY;
	}
	@Override
	default ItemStack removeItemNoUpdate(int pIndex) {
		return EMPTY;
	}
	@Override
	default void setChanged() {
	}
	@Override
	default void setItem(int pIndex, ItemStack pStack) {
	}
	@Override
	default boolean stillValid(PlayerEntity pPlayer) {
		return false;
	}
	@Override
	default boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return false;
	}
	
	/**
     * @return Current amount of fluid in the tank.
     */
    default int getFluidAmount(int tank) {
    	return getFluidInTank(tank).getAmount();
    }
    
    /**
     * @return Remaining capacity in tank
     */
    default int getRemainingCapacity(int tank) {
    	return getTankCapacity(tank) - getFluidInTank(tank).getAmount();
    }
	
}
