package com.machina.api.client.cinema;

import com.machina.api.client.cinema.entity.CameraClientEntity;
import com.machina.api.client.cinema.entity.CinematicClientEntity;
import com.machina.api.network.PacketSender;
import com.machina.api.network.c2s.C2SFinishCinematic;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public abstract class Cinematic {

    protected static final Minecraft mc = Minecraft.getInstance();

    protected final CameraClientEntity player;
    protected CinematicClientEntity clientEntity;
    protected boolean active = false;

    boolean bobView = false;

    private double viewScale;
    private boolean hideGui = false, flying = false;
    private CameraType cameraType;
    private Vec3 pos;
    private float yaw, pitch;
    private final String id;

    public Cinematic(String id, CameraClientEntity p) {
        this.id = id;
        this.player = p;
    }

    public abstract void onClientTick(int tick, float par);

    public abstract void onRenderTick(int tick, float par);

    public abstract int getDuration();

    public String getId() {
        return this.id;
    }

    public void begin() {
        this.bobView = mc.options.bobView().get();
        this.hideGui = mc.options.hideGui;
        this.cameraType = mc.options.getCameraType();
        if (mc.player != null) {
            this.flying = mc.player.getAbilities().flying;
        }
        if (mc.player != null) {
            this.pos = mc.player.position();
        }
        if (mc.player != null) {
            this.yaw = mc.player.getXRot();
        }
        this.pitch = Objects.requireNonNull(mc.player).getYRot();
        mc.player.setDeltaMovement(0, 0, 0);
        this.active = true;

        CinematicClientEntity repl = new CinematicClientEntity();
        repl.load(mc.player.saveWithoutId(new CompoundTag()));
        this.clientEntity = repl;
        mc.setCameraEntity(this.player);

        this.viewScale = Entity.getViewScale();
    }

    public void finish() {
        if (active) {
            mc.options.bobView().set(bobView);
            mc.options.hideGui = hideGui;
            mc.options.setCameraType(cameraType);
            if (mc.player != null) {
                mc.player.getAbilities().flying = flying;
            }
            if (mc.player != null) {
                mc.player.moveTo(pos.x, pos.y, pos.z);
            }
            if (mc.player != null) {
                mc.player.setXRot(yaw);
            }
            Objects.requireNonNull(mc.player).setYRot(pitch);
            mc.player.setDeltaMovement(0, 0, 0);
            this.active = false;
            PacketSender.sendToServer(new C2SFinishCinematic(this.id));
            Entity.setViewScale(viewScale);
        }

        CameraUtil.resetCamera();

        if (mc.level != null && this.clientEntity != null) {
            mc.level.removeEntity(this.clientEntity.getId(), Entity.RemovalReason.DISCARDED);
        }
    }

    public void transform() {
        if (active) {
            Entity.setViewScale(1000);
            mc.options.hideGui = false;
            mc.options.bobView().set(false);
            mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (mc.player != null) {
                mc.player.getAbilities().flying = true;
            }
            if (mc.player != null) {
                mc.player.setDeltaMovement(0, 0, 0);
            }
        }
    }
}