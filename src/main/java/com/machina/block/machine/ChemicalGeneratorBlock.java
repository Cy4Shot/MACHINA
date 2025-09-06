package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.ChemicalGeneratorBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ChemicalGeneratorBlock extends LitMachineBlock {

    public ChemicalGeneratorBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.CHEMICAL_GENERATOR.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return ChemicalGeneratorBlockEntity.class;
    }
}