package com.machina.api.network.s2c;

import java.util.function.Function;

import com.machina.api.cap.fluid.FluidHandlerEntity;
import com.machina.api.network.S2CMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.fluids.FluidStack;

public record S2CFluidEntitySync(int entity, FluidStack stack, int i) implements S2CMessage<S2CFluidEntitySync> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, S2CFluidEntitySync> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.INT, S2CFluidEntitySync::entity, FluidStack.STREAM_CODEC,
                S2CFluidEntitySync::stack, ByteBufCodecs.INT, S2CFluidEntitySync::i, S2CFluidEntitySync::new);
    }

    @Override
    public void handle() {
        int id = entity();
        FluidStack stack = stack();
        int i = i();

        mc.execute(() -> {
            Entity e = mc.level.getEntity(id);
            if (e instanceof FluidHandlerEntity) {
                ((FluidHandlerEntity) e).setFluid(i, stack);
            }
        });
    }

}
