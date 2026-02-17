package com.machina.api.network.s2c;

import java.util.function.Function;

import com.machina.api.network.S2CMessage;
import com.machina.client.screen.menu.entity.RocketScreen;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

public record S2CRocketScreenOpen(int window, int size, int entity) implements S2CMessage<S2CRocketScreenOpen> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, S2CRocketScreenOpen> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.INT, S2CRocketScreenOpen::window, ByteBufCodecs.INT,
                S2CRocketScreenOpen::size, ByteBufCodecs.INT, S2CRocketScreenOpen::entity, S2CRocketScreenOpen::new);
    }

    @Override
    public void handle() {
        int window = window();
        int size = size();
        int entityId = entity();

        mc.execute(() -> {
            if (mc.level == null) {
                return;
            }
            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof RocketEntity) {
                LocalPlayer localplayer = mc.player;
                RocketEntity rocket = (RocketEntity) entity;
                SimpleContainer simplecontainer = new SimpleContainer(size);
                RocketMenu rocketmenu = new RocketMenu(window, localplayer.getInventory(), simplecontainer, rocket);
                localplayer.containerMenu = rocketmenu;
                mc.setScreen(new RocketScreen(rocketmenu, localplayer.getInventory(), rocketmenu.getName()));
            }
        });
    }

}
