package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.MelterBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MelterBlock extends LitMachineBlock {

    public MelterBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.MELTER.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return MelterBlockEntity.class;
    }
}