package com.machina.api.block.menu;

import com.machina.api.network.s2c.S2COpenDirectionalContainer;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class DirectionalMenuFactory {

	@SuppressWarnings("resource")
	public static void create(ServerPlayer player, IDirectionalMenuProvider cont, BlockPos pos, Direction d) {
		if (player.level().isClientSide)
			return;
		player.doCloseContainer();
		player.nextContainerCounter();
		int openContainerId = player.containerCounter;
		FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
		extraData.writeBlockPos(pos);
		extraData.writeEnum(d);
		extraData.readerIndex(0);

		FriendlyByteBuf output = new FriendlyByteBuf(Unpooled.buffer());
		output.writeVarInt(extraData.readableBytes());
		output.writeBytes(extraData);

		if (output.readableBytes() > 32600 || output.readableBytes() < 1) {
			throw new IllegalArgumentException(
					"Invalid PacketBuffer for directional menu, found " + output.readableBytes() + " bytes");
		}
		var c = cont.createMenu(openContainerId, player.getInventory(), player, pos, d);
		if (c == null)
			return;
		MenuType<?> type = c.getType();
		PacketDistributor.sendToPlayer(player, new S2COpenDirectionalContainer(type, openContainerId, output));

		player.containerMenu = c;
		player.initMenu(player.containerMenu);
		NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, c));
	}
}
