package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.InvSlot;
import com.machina.api.block.menu.slot.ResultSlot;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class CompressorMenu extends MachinaContainerMenu<CompressorBlockEntity> {

    public CompressorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public CompressorMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.COMPRESSOR.get(), level, pos, id);

        this.addSlot(new InvSlot(be, 0, 62, -29));
        this.addSlot(new InvSlot(be, 1, 108, -5));
        this.addSlot(new ResultSlot(be, 2, 154, -29));

        invSlots(inv, 0);
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.COMPRESSOR.get();
    }
}
