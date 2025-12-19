package com.machina.client.cinema;

import com.machina.api.client.cinema.CameraPath;
import com.machina.api.client.cinema.CameraPath.InterpolationMethod;
import com.machina.api.client.cinema.effect.ActionEffect;
import com.machina.api.client.cinema.effect.CameraEffect;
import com.machina.api.client.cinema.effect.FadeInEffect;
import com.machina.api.client.cinema.effect.FadeOutEffect;
import com.machina.api.client.cinema.effect.OverlayEffect;
import com.machina.api.client.cinema.effect.ParticleEffect;
import com.machina.api.client.cinema.effect.ShakeEffect;
import com.machina.api.client.cinema.effect.SoundEffect;
import com.machina.api.client.cinema.entity.CameraClientEntity;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SRocketLaunchComplete;
import com.machina.api.network.c2s.C2SRocketCinematicOffset;
import com.machina.api.network.c2s.C2SSpawnParticle;
import com.machina.api.util.math.DirUtil;
import com.machina.registration.init.SoundInit;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public class LaunchCinematic extends PathCinematic {

    private final int id;
    
    public LaunchCinematic(RocketEntity entity) {
        this(new CameraClientEntity(), entity);
    }

    public LaunchCinematic(CameraClientEntity p, RocketEntity entity) {
        super("launch", p);
        this.id = entity.getId();

        Direction d = entity.getDirection();
        int yaw = DirUtil.toYaw(d.getOpposite());

        Vec3 pos = entity.position();

        CameraEffect SOUND = new SoundEffect(SoundInit.ROCKET_LAUNCH);
        CameraEffect FADE_IN = new FadeInEffect(10);
        CameraEffect FADE_OUT = new FadeOutEffect(180, 70);
        CameraEffect SHAKE1 = new ShakeEffect(0.3f);
        CameraEffect SHAKE2 = new ShakeEffect(0.75f);
        CameraEffect SMOKE = new ParticleEffect(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos, 0.05D, 0f, 0.8f);
        CameraEffect EXPLOSIONS = new ParticleEffect(ParticleTypes.EXPLOSION, pos, 0.05D, 1f, 0.1f);
        CameraEffect LAUNCH = new ActionEffect(ting -> {
            double off = Math.pow(Math.E, (double) ting / 9D) - 1D;
            clientEntity.moveTo(pos.add(0, off, 0));
            entity.moveTo(pos.add(0, off, 0));
            PacketSender.sendToServer(new C2SSpawnParticle<>(ParticleTypes.FLAME, -0.1f, 20, pos.add(0, off - 2.1D, 0),
                    new Vec3(0d, 1d, 0d)));
            PacketSender.sendToServer(new C2SSpawnParticle<>(ParticleTypes.ANGRY_VILLAGER, -0.1f, 2,
                    pos.add(0, off - 2.1D, 0), new Vec3(1d, 1d, 1d)));
            PacketSender.sendToServer(new C2SSpawnParticle<>(ParticleTypes.CAMPFIRE_COSY_SMOKE, -0.1f, 5,
                    pos.add(0, off - 2.1D, 0), new Vec3(0.1d, 1d, 0.1d)));
            PacketSender.sendToServer(new C2SSpawnParticle<>(ParticleTypes.CAMPFIRE_COSY_SMOKE, -0.1f, 10,
                    pos.add(0, off - 2.1D, 0), new Vec3(0d, 1d, 0d)));
            PacketSender.sendToServer(new C2SSpawnParticle<>(ParticleTypes.EXPLOSION, -0.1f, 6,
                    pos.add(0, off - 2.1D, 0), new Vec3(0d, 1d, 0d)));
            PacketSender.sendToServer(new C2SRocketCinematicOffset(this.id, pos, off));
        });

        CameraEffect PLACE_PLAYER = new ActionEffect(ting -> {
            clientEntity.moveTo(pos);
        });

        CameraEffect DARK = new OverlayEffect(50);

        // @formatter:off
        setPath(CameraPath.builder(pos)
        .addPath(InterpolationMethod.LERP, 100, effects(FADE_IN, SOUND, SMOKE),
                node(d.getStepX() * 3, 0, d.getStepZ() * 3, p.xRot, yaw),
                node(d.getStepX() * 10, 0, d.getStepZ() * 10, p.xRot, yaw))
        .addPath(InterpolationMethod.LERP, 150, effects(SHAKE1, SMOKE, EXPLOSIONS, PLACE_PLAYER),
                node(0, 12, 0, 90, yaw - 45),
                node(0, 8, 0, 90, yaw + 45))
        .addPath(InterpolationMethod.LERP, 100, effects(SHAKE1, SMOKE, EXPLOSIONS),
                node(d.getStepX() * 10, 0, d.getStepZ() * 10, p.xRot, yaw),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, p.xRot, yaw))
        .addPath(InterpolationMethod.BEZIER, 180, effects(SHAKE2, SMOKE, EXPLOSIONS, LAUNCH, FADE_OUT),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, p.xRot, yaw),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, -80, yaw),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, -85, yaw),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, -90, yaw))
        .addPath(InterpolationMethod.LERP, 50, effects(DARK),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, -90, yaw),
                node(d.getStepX() * 10, 1, d.getStepZ() * 10, -90, yaw))
        .build());
        // @formatter:on
    }

    @Override
    public void finish() {
        super.finish();
        PacketSender.sendToServer(new C2SRocketLaunchComplete(this.id));
    }
    
    @Override
    protected boolean suppressFadeReset() {
        return true;
    }
}