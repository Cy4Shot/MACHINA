package com.machina.api.network.s2c;

import com.machina.api.cap.fluid.FluidHandlerEntity;
import com.machina.api.network.S2CMessage;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidStack;

public record S2CFluidEntitySync(int entity, FluidStack stack, int i) implements S2CMessage {

    public static S2CFluidEntitySync decode(FriendlyByteBuf buf) {
        return new S2CFluidEntitySync(buf.readInt(), buf.readFluidStack(), buf.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
        buf.writeFluidStack(stack);
        buf.writeInt(i);
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
