package com.machina.block.connector;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.fluid.PipeFluidStorage;
import com.machina.api.util.block.BlockHelper;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FluidPipeBlock extends ConnectorBlock {

    private static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

    public FluidPipeBlock() {
        super(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(1f).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public boolean canConnect(BlockEntity be, Direction dir) {
        return BlockHelper.hasFluid(be, dir);
    }

    @Override
    protected Map<BlockPos, Set<BlockPos>> getCache() {
        return CACHE;
    }

    @Override
    protected BlockEntityType<? extends ConnectorBlockEntity<FluidStack, PipeFluidStorage>> getBlockEntityType() {
        return BlockEntityInit.FLUID_PIPE.get();
    }

}
