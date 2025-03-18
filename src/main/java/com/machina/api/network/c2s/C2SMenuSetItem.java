package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public record C2SMenuSetItem(int slot, ItemStack stack, BlockPos pos) implements C2SMessage {
	public static C2SMenuSetItem decode(FriendlyByteBuf buf) {
		return new C2SMenuSetItem(buf.readInt(), buf.readItem(), buf.readBlockPos());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(slot);
		buf.writeItem(stack);
		buf.writeBlockPos(pos);
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			BlockEntity be = player.level().getBlockEntity(pos);
			if (be instanceof Container c) {
				c.setItem(slot, stack);
				c.setChanged();
			}
		});
	}
}