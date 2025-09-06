package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.MachineCaseBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MachineCaseBlock extends MachineBlock {

    public MachineCaseBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.MACHINE_CASE.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return MachineCaseBlockEntity.class;
    }
}
