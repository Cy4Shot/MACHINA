package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.client.screen.menu.entity.RocketScreen;
import com.machina.rocket.RocketEntity;
import com.machina.rocket.RocketMenu;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

public record S2CRocketScreenOpen(int window, int size, int entity) implements S2CMessage {

    public static S2CRocketScreenOpen decode(FriendlyByteBuf buf) {
        return new S2CRocketScreenOpen(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(window);
        buf.writeInt(size);
        buf.writeInt(entity);
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
