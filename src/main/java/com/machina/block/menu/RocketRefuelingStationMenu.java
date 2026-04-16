package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.RocketRefuelingStationBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class RocketRefuelingStationMenu extends MachinaMachineMenu<RocketRefuelingStationBlockEntity> {

	private int selectedTank;
	private boolean hasTrackedRocket;

	public RocketRefuelingStationMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(2));
	}

	public RocketRefuelingStationMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.ROCKET_REFUELING_STATION.get(), id, level);

		invSlots(inv, 0);

		this.addSlot(new AcceptSlot(container, 0, 21, 31, ItemStackUtil::hasFluid));
		this.addSlot(new AcceptSlot(container, 1, 198, 31, ItemStackUtil::hasFluid));

		this.addDataSlot(new DataSlot() {
			@Override
			public int get() {
				RocketRefuelingStationBlockEntity entity = getBlockEntity();
				return entity == null ? RocketRefuelingStationBlockEntity.FUEL_TANK : entity.getSelectedTank();
			}

			@Override
			public void set(int value) {
				selectedTank = value;
			}
		});

		this.addDataSlot(new DataSlot() {
			@Override
			public int get() {
				RocketRefuelingStationBlockEntity entity = getBlockEntity();
				return entity != null && entity.hasTrackedRocket() ? 1 : 0;
			}

			@Override
			public void set(int value) {
				hasTrackedRocket = value == 1;
			}
		});
	}

	public int getSelectedTank() {
		return this.selectedTank;
	}

	public boolean isFuelTankSelected() {
		return this.selectedTank != RocketRefuelingStationBlockEntity.COOLANT_TANK;
	}

	public boolean hasTrackedRocket() {
		return this.hasTrackedRocket;
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.ROCKET_REFUELING_STATION.get();
	}
}
