package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.client.screen.menu.entity.RocketScreen;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;

public record S2CRocketScreenOpen(int window, int size, int entity) implements S2CMessage<S2CRocketScreenOpen> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CRocketScreenOpen> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.INT, S2CRocketScreenOpen::window, ByteBufCodecs.INT,
				S2CRocketScreenOpen::size, ByteBufCodecs.INT, S2CRocketScreenOpen::entity, S2CRocketScreenOpen::new);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		if (player.level() == null) {
			return;
		}
		Entity entity = player.level().getEntity(entity());
		if (entity instanceof RocketEntity) {
			LocalPlayer localplayer = (LocalPlayer) player;
			RocketEntity rocket = (RocketEntity) entity;
			ItemStackHandler simplecontainer = new ItemStackHandler(size);
			RocketMenu rocketmenu = new RocketMenu(window, localplayer.getInventory(), simplecontainer, rocket);
			localplayer.containerMenu = rocketmenu;
			Minecraft.getInstance().setScreen(new RocketScreen(rocketmenu, localplayer.getInventory(), rocketmenu.getName()));
		}
	}

}
