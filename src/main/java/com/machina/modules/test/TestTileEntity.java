package com.machina.modules.test;

import com.machina.api.tile_entity.EnergyTileEntity;
import com.machina.api.tile_entity.MachinaEnergyStorage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TestTileEntity extends EnergyTileEntity implements INamedContainerProvider {

	protected TestTileEntity() {
		super(TestModule.TEST_TE_TYPE);
	}

	@Override
	protected MachinaEnergyStorage createEnergyStorage() {
		return new MachinaEnergyStorage(1200000, 20, 20);
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
		return new TestContainer(p_createMenu_1_, p_createMenu_2_, this);
	}

	@Override
	public ITextComponent getDisplayName() { return new StringTextComponent(""); }

}
