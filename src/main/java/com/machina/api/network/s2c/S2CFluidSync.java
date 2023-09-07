package com.machina.api.network.s2c;

import com.machina.api.block.tile.MachinaBlockEntity;
import com.machina.api.cap.fluid.MachinaFluidStorage;
import com.machina.api.network.S2CMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

public record S2CFluidSync(BlockPos pos, FluidStack stack, int i) implements S2CMessage {
	
	public static S2CFluidSync decode(FriendlyByteBuf buf) {
		return new S2CFluidSync(buf.readBlockPos(), buf.readFluidStack(), buf.readInt());
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeFluidStack(stack);
		buf.writeInt(i);
	}

	@Override
	public void handle() {
		BlockPos pos = pos();
		FluidStack stack = stack();
		int i = i();
		
		Minecraft mc = Minecraft.getInstance();
		mc.execute(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
				if (be instanceof MachinaBlockEntity) {
					((MachinaBlockEntity) be).getCapability(ForgeCapabilities.FLUID_HANDLER).cast().ifPresent(o -> {
						if (o instanceof MachinaFluidStorage) {
							((MachinaFluidStorage) o).setFluidInTank(i, stack);
						}
					});
				}
			}
		});
	}
	
}
