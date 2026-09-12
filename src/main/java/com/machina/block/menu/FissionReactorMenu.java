package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.api.client.screen.ProgressBar;
import com.machina.api.util.StringUtils;
import com.machina.block.entity.machine.fission_reactor.FissionReactorControllerBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.DataMapsInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FissionReactorMenu extends MachinaMachineMenu<FissionReactorControllerBlockEntity> {

	public FissionReactorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, client(buf), new ItemStackHandler(2));
	}

	public FissionReactorMenu(int id, Inventory inv, ContainerLevelAccess level, IItemHandler container) {
		super(MenuTypeInit.FISSION_REACTOR.get(), id, level);

		this.addSlot(new AcceptSlot(container, 0, 10, 10,
				stack -> stack.getItemHolder().getData(DataMapsInit.FISSION_FUEL) != null));
		this.addSlot(new ResultSlot(container, 1, 10, 30));

		invSlots(inv, 0);
	}

	@Override
	protected MachineBlock getBlock() {
		return BlockInit.FISSION_REACTOR_CONTROLLER.get();
	}

	public float getReactivity() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getReactivity() : 1.0f;
	}

	public float getMaxReactivity() {
		return 1.006f;
	}

	public float getMinReactivity() {
		return 0.994f;
	}

	public ProgressBar<Float> getReactivityProgress() {
		return new ProgressBar<>(this::getReactivity, this::getMaxReactivity, this::getMinReactivity, String::valueOf);
	}

	public float getBurnProgress() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getBurnProgress() : 0;
	}

	public ProgressBar<Float> getBurnBar() {
		return new ProgressBar<>(() -> getBurnProgress(), () -> 1.0f, p -> StringUtils.formatPercent(p));
	}

	public int getWaterAmount() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getFluidMB(0) : 0;
	}

	public int getWaterCapacity() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getTankCapacity(0) : 1;
	}

	public int getCurrentSteamRate() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getCurrentSteamRate() : 0;
	}

	public int getMaxSteamRate() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getMaxSteamRate() : 0;
	}

	public float getSteamRatePercent() {
		FissionReactorControllerBlockEntity entity = getBlockEntity();
		return entity != null ? entity.getSteamRatePercent() : 0;
	}

	public ProgressBar<Float> getSteamRateBar() {
		return new ProgressBar<>(() -> (float) getCurrentSteamRate(), () -> (float) getMaxSteamRate(),
				rate -> String.format("%.0f mB/t", rate));
	}
}
