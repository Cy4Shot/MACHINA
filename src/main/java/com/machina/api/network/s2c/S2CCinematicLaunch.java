package com.machina.api.network.s2c;

import com.machina.api.client.cinema.CinematicHandler;
import com.machina.api.client.cinema.entity.CameraClientEntity;
import com.machina.api.network.S2CMessage;
import com.machina.client.cinema.LaunchCinematic;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public record S2CCinematicLaunch(int entity) implements S2CMessage {

    public static S2CCinematicLaunch decode(FriendlyByteBuf buf) {
        return new S2CCinematicLaunch(buf.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
    }

    @Override
    public void handle() {
        int id = entity();
        mc.execute(() -> {
            Entity e = mc.level.getEntity(id);
            if (e instanceof RocketEntity rocket) {
                CinematicHandler.INSTANCE.setCinematic(new LaunchCinematic(new CameraClientEntity(), rocket));
            }
        });
    }
}