package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.TankBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class TankMenu extends MachinaContainerMenu<TankBlockEntity> {

    public TankMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public TankMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.TANK.get(), level, pos, id);

        invSlots(inv, 0);

        this.addSlot(new AcceptSlot(be, 0, 21, 31, ItemStackUtil::hasFluid));
        this.addSlot(new AcceptSlot(be, 1, 198, 31, ItemStackUtil::hasFluid));
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.TANK.get();
    }
}
