package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.ElectricSmelterBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ElectricSmelterBlock extends LitMachineBlock {

    public ElectricSmelterBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.ELECTRIC_SMELTER.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return ElectricSmelterBlockEntity.class;
    }
}