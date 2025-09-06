package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class SolidifierMenu extends MachinaContainerMenu<SolidifierBlockEntity> {

    public SolidifierMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public SolidifierMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.SOLIDIFIER.get(), level, pos, id);

        this.addSlot(new InvSlot(be, 0, 193, -14));

        invSlots(inv, 0);
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.SOLIDIFIER.get();
    }
}
