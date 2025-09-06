package com.machina.block.menu;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.FurnaceGeneratorBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class FurnaceGeneratorMenu extends MachinaContainerMenu<FurnaceGeneratorBlockEntity> {

    public FurnaceGeneratorMenu(int id, Inventory inv, @NonNull FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public FurnaceGeneratorMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.FURNACE_GENERATOR.get(), level, pos, id);

        this.addSlot(new AcceptSlot(be, 0, 108, -25, ItemStackUtil::isBurnable));

        invSlots(inv, 0);
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.FURNACE_GENERATOR.get();
    }
}
