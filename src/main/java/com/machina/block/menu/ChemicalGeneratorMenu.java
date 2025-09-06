package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.block.entity.machine.ChemicalGeneratorBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ChemicalGeneratorMenu extends MachinaContainerMenu<ChemicalGeneratorBlockEntity> {

    public ChemicalGeneratorMenu(int id, Inventory inv, @NonNull FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public ChemicalGeneratorMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.CHEMICAL_GENERATOR.get(), level, pos, id);

        invSlots(inv, 0);
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.CHEMICAL_GENERATOR.get();
    }
}
