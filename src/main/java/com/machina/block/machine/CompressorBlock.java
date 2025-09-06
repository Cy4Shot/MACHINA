package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.CompressorBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompressorBlock extends LitMachineBlock {

    public CompressorBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.COMPRESSOR.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return CompressorBlockEntity.class;
    }
}