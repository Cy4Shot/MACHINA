package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.block.entity.machine.geothermal_generator.GeothermalGeneratorControllerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GeothermalGeneratorMenu extends MachinaMachineMenu<GeothermalGeneratorControllerBlockEntity> {

	public GeothermalGeneratorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(0));
	}

	public GeothermalGeneratorMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.GEOTHERMAL_GENERATOR.get(), id, level);

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.GEOTHERMAL_GENERATOR_CONTROLLER.get();
	}
}
