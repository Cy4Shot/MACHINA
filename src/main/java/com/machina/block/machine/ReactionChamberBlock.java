package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.ReactionChamberBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ReactionChamberBlock extends LitMachineBlock {

    public ReactionChamberBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityInit.REACTION_CHAMBER.get();
    }

    @Override
    public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
        return ReactionChamberBlockEntity.class;
    }
}