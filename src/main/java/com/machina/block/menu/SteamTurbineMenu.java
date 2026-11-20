package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.data.SteamTurbinePropData;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.steam_turbine.SteamTurbineControllerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SteamTurbineMenu extends MachinaMachineMenu<SteamTurbineControllerBlockEntity> {

	private int lastSteamRate = 0;
	private int lastEnergyProduced = 0;
	private int efficiencyScaled = 0;
	private int bufferEfficiencyScaled = 10000;
	private int energy = 0;
	private int maxEnergy = 1;

	public SteamTurbineMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(0));
	}

	public SteamTurbineMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.STEAM_TURBINE.get(), id, level);

		invSlots(inv, 0);

		this.addDataSlots(SteamTurbinePropData.of(
				this::getBlockEntity,
				x -> this.lastSteamRate = x,
				x -> this.lastEnergyProduced = x,
				x -> this.efficiencyScaled = x,
				x -> this.bufferEfficiencyScaled = x,
				x -> this.energy = x,
				x -> this.maxEnergy = x));
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.STEAM_TURBINE_CONTROLLER.get();
	}

	public int getLastSteamRate() {
		return lastSteamRate;
	}

	public int getLastEnergyProduced() {
		return lastEnergyProduced;
	}

	public float getEfficiency() {
		return efficiencyScaled / 10000.0f;
	}

	public float getBufferEfficiency() {
		return bufferEfficiencyScaled / 10000.0f;
	}

	public int getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public float getEnergyPercent() {
		return maxEnergy > 0 ? (float) energy / maxEnergy : 0;
	}

	public String getEnergyString() {
		return StringUtils.formatPower(energy);
	}

	public String getMaxEnergyString() {
		return StringUtils.formatPower(maxEnergy);
	}

	public boolean isBufferFull() {
		return bufferEfficiencyScaled < 100; // Less than 1% buffer efficiency means essentially full
	}
}
