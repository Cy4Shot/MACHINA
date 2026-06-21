package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaMachineMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.api.client.screen.ProgressBar;
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
		return 1.0f;
	}
	
	public float getMaxReactivity() {
		return 1.006f;
	}
	
	public ProgressBar<Float> getReactivityProgress() {
		return new ProgressBar<>(this::getReactivity, this::getMaxReactivity, String::valueOf);
	}
}
