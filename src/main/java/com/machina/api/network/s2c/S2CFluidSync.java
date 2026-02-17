package com.machina.api.network.s2c;

import java.util.function.Function;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.network.S2CMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.neoforged.neoforge.fluids.FluidStack;

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
            BlockEntity be = null;
            if (mc.level != null) {
                be = mc.level.getBlockEntity(pos);
            }
            if (be instanceof MachinaBlockEntity) {
                be.getCapability(ForgeCapabilities.FLUID_HANDLER).cast().ifPresent(o -> {
                    if (o instanceof MachinaBlockEntity) {
                        ((MachinaBlockEntity) o).setFluid(i, stack);
                    }
                });
            }
        });
    }

}
