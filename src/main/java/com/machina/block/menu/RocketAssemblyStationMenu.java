package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RocketAssemblyStationMenu extends MachinaMachineMenu<RocketAssemblyStationBlockEntity> {

	public RocketAssemblyStationMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(5));
	}

	public RocketAssemblyStationMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.ROCKET_ASSEMBLY_STATION.get(), id, level);

		invSlots(inv, 0);

		this.addSlot(new AcceptSlot(container, 0, 80, 41, i -> ItemStackUtil.isRocketPart(i, RocketPartType.THRUSTER)));
		this.addSlot(
				new AcceptSlot(container, 1, 80, 16, i -> ItemStackUtil.isRocketPart(i, RocketPartType.FUEL_TANK)));
		this.addSlot(new AcceptSlot(container, 2, 80, -9, i -> ItemStackUtil.isRocketPart(i, RocketPartType.CHASSIS)));
		this.addSlot(
				new AcceptSlot(container, 3, 80, -34, i -> ItemStackUtil.isRocketPart(i, RocketPartType.LIFE_SUPPORT)));
		this.addSlot(new AcceptSlot(container, 4, 80, -59, i -> ItemStackUtil.isRocketPart(i, RocketPartType.SHIELD)));
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ROCKET_ASSEMBLY_STATION.get();
	}
}
