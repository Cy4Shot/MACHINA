package com.machina.api.network.s2c;

import com.machina.api.cap.fluid.FluidHandlerEntity;
import com.machina.api.network.S2CMessage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;

public record S2CFluidEntitySync(int entity, FluidStack stack, int i) implements S2CMessage<S2CFluidEntitySync> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CFluidEntitySync> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.INT, S2CFluidEntitySync::entity, FluidStack.OPTIONAL_STREAM_CODEC,
				S2CFluidEntitySync::stack, ByteBufCodecs.INT, S2CFluidEntitySync::i, S2CFluidEntitySync::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		Entity e = player.level().getEntity(entity);
		if (e instanceof FluidHandlerEntity) {
			((FluidHandlerEntity) e).setFluid(i, stack);
		}
	}

}
