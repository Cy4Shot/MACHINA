package com.machina.block.menu;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.menu.MachinaContainerMenu;
import com.machina.api.block.menu.slot.AcceptSlot;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.api.util.ItemStackUtil;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.MenuTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class RocketAssemblyStationMenu extends MachinaContainerMenu<RocketPartBenchBlockEntity> {

    public RocketAssemblyStationMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, clientLevel(), buf.readBlockPos(), inv);
    }

    public RocketAssemblyStationMenu(int id, Level level, BlockPos pos, Inventory inv) {
        super(MenuTypeInit.ROCKET_ASSEMBLY_STATION.get(), level, pos, id);

        invSlots(inv, 0);

        this.addSlot(new AcceptSlot(be, 0, -2, 14, i -> ItemStackUtil.isRocketPart(i, RocketPartType.THRUSTER)));
        this.addSlot(new AcceptSlot(be, 1, -2, 34, i -> ItemStackUtil.isRocketPart(i, RocketPartType.FUEL_TANK)));
        this.addSlot(new AcceptSlot(be, 2, -2, 54, i -> ItemStackUtil.isRocketPart(i, RocketPartType.CHASSIS)));
        this.addSlot(new AcceptSlot(be, 3, -2, 74, i -> ItemStackUtil.isRocketPart(i, RocketPartType.LIFE_SUPPORT)));
        this.addSlot(new AcceptSlot(be, 4, -2, 94, i -> ItemStackUtil.isRocketPart(i, RocketPartType.SHIELD)));
    }

    @Override
    protected MachineBlock getBlock() {
        return BlockInit.ROCKET_ASSEMBLY_STATION.get();
    }
}
