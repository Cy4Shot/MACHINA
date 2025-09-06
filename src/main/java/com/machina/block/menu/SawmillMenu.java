package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.SawmillBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class SawmillMenu extends MachinaContainerMenu<SawmillBlockEntity> {

    public SawmillMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public SawmillMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.SAWMILL.get(), level, pos, id);

        this.addSlot(new InvSlot(be, 0, 62, -17));
        this.addSlot(new ResultSlot(be, 1, 154, -17));

        invSlots(inv, 0);
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.SAWMILL.get();
    }
}
