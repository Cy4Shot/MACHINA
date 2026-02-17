package com.machina.api.network.s2c;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.network.S2CMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record S2CFluidSync(BlockPos pos, FluidStack stack, int i) implements S2CMessage<S2CFluidSync> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, S2CFluidSync> streamCodec() {
        return StreamCodec.composite(BlockPos.STREAM_CODEC, S2CFluidSync::pos, FluidStack.STREAM_CODEC,
                S2CFluidSync::stack, ByteBufCodecs.INT, S2CFluidSync::i, S2CFluidSync::new);
    }

    @Override
    public void handle() {
        BlockPos pos = pos();
        FluidStack stack = stack();
        int i = i();

        mc.execute(() -> {
            if (mc.level != null) {
                IFluidHandler handler = mc.level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
                if (handler != null) {
                   BlockEntity be = mc.level.getBlockEntity(pos);
                    if (be instanceof MachinaBlockEntity) {
                        ((MachinaBlockEntity) be).setFluid(i, stack);
                    }
                }
            }
        });
    }

}
