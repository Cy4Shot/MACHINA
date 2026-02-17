package com.machina.client.cinema;

import com.machina.api.client.cinema.CameraPath;
import com.machina.api.client.cinema.CameraPath.InterpolationMethod;
import com.machina.api.client.cinema.effect.ActionEffect;
import com.machina.api.client.cinema.effect.CameraEffect;
import com.machina.api.client.cinema.effect.FadeInEffect;
import com.machina.api.client.cinema.effect.FadeOutEffect;
import com.machina.api.client.cinema.effect.ParticleEffect;
import com.machina.api.client.cinema.effect.ShakeEffect;
import com.machina.api.client.cinema.effect.SoundEffect;
import com.machina.api.client.cinema.entity.CameraClientEntity;
import com.machina.api.network.c2s.C2SRocketLandComplete;
import com.machina.api.network.c2s.C2SRocketCinematicOffset;
import com.machina.api.network.c2s.C2SSpawnParticle;
import com.machina.api.util.math.DirUtil;
import com.machina.registration.init.SoundInit;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class LandCinematic extends PathCinematic {

    private final int id;

    public LandCinematic(RocketEntity entity) {
        this(new CameraClientEntity(), entity);
    }

    public LandCinematic(CameraClientEntity p, RocketEntity entity) {
        super("land", p);
        this.id = entity.getId();

        Direction d = entity.getDirection();
        int yaw = DirUtil.toYaw(d.getOpposite());

        Vec3 pos = entity.position();

        CameraEffect SOUND = new SoundEffect(SoundInit.ROCKET_LAND);
        CameraEffect FADE_IN = new FadeInEffect(10);
        CameraEffect FADE_OUT = new FadeOutEffect(150, 50);
        CameraEffect SHAKE1 = new ShakeEffect(0.3f);
        CameraEffect SHAKE2 = new ShakeEffect(0.75f);
        CameraEffect SMOKE = new ParticleEffect(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos, 0.05D, 0f, 0.3f);

        CameraEffect LAND = new ActionEffect(ting -> {
            double off = Math.pow(Math.E, (200D - (double) ting) / 60D) - 1D;
            clientEntity.moveTo(pos.add(0, off, 0));
            entity.moveTo(pos.add(0, off, 0));
            PacketDistributor.sendToServer(
                    new C2SSpawnParticle<>(ParticleTypes.FLAME, -0.1f, 3, pos.add(0, off - 2.1D, 0), Vec3.ZERO));
            PacketDistributor.sendToServer(new C2SSpawnParticle<>(ParticleTypes.CAMPFIRE_COSY_SMOKE, -0.1f, 5,
                    pos.add(0, off - 2.1D, 0), new Vec3(0.1d, 1d, 0.1d)));
            PacketDistributor.sendToServer(new C2SRocketCinematicOffset(this.id, pos, off));
        });

        // @formatter:off
        setPath(CameraPath.builder(pos)
        .addPath(InterpolationMethod.BEZIER, 200, effects(FADE_IN, SHAKE2, LAND, SOUND),
                node(d.getStepX() * 10, 0, d.getStepZ() * 10, -85, yaw),
                node(d.getStepX() * 8, 0, d.getStepZ() * 8, -80, yaw),
                node(d.getStepX() * 5, 0, d.getStepZ() * 5, -70, yaw),
                node(d.getStepX() * 3, 0, d.getStepZ() * 3, p.xRot, yaw))
        .addPath(InterpolationMethod.LERP, 150, effects(SHAKE1, SMOKE, FADE_OUT),
                node(0, 12, 0, 90, yaw - 45),
                node(0, 8, 0, 90, yaw + 45))
        .build());
        // @formatter:on
    }

    @Override
    public void finish() {
        super.finish();
        PacketDistributor.sendToServer(new C2SRocketLandComplete(this.id));
    }
}