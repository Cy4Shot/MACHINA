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

public abstract class Cinematic {

	protected static final Minecraft mc = Minecraft.getInstance();

	protected CameraClientEntity player;
	protected CinematicClientEntity clientEntity;
	protected boolean active = false;

	boolean bobView = false;
	
	private double viewScale;
	private boolean hideGui = false, flying = false;
	private CameraType cameraType;
	private Vec3 pos;
	private float yaw, pitch;
	private String id;

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

	public void begin(float partial) {
		this.bobView = mc.options.bobView.get();
		this.hideGui = mc.options.hideGui;
		this.cameraType = mc.options.getCameraType();
		this.flying = mc.player.abilities.flying;
		this.pos = mc.player.position();
		this.yaw = mc.player.xRot;
		this.pitch = mc.player.yRot;
		mc.player.setDeltaMovement(0, 0, 0);
		this.active = true;

		CinematicClientEntity repl = new CinematicClientEntity();
		repl.load(mc.player.saveWithoutId(new CompoundTag()));
		this.clientEntity = repl;
		mc.setCameraEntity(this.player);

		this.viewScale = Entity.getViewScale();
	}

	public void finish(float partial) {
		if (active) {
			mc.options.bobView.set(bobView);
			mc.options.hideGui = hideGui;
			mc.options.setCameraType(cameraType);
			mc.player.abilities.flying = flying;
			mc.player.moveTo(pos.x, pos.y, pos.z);
			mc.player.xRot = yaw;
			mc.player.yRot = pitch;
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

	public void transform(float partial) {
		if (active) {
			Entity.setViewScale(1000);
			mc.options.hideGui = false;
			mc.options.bobView.set(false);
			mc.options.setCameraType(CameraType.FIRST_PERSON);
			mc.player.abilities.flying = true;
			mc.player.setDeltaMovement(0, 0, 0);
		}
	}

	public void setEntityPos(float x, float y, float z, float xr, float yr) {
		if (this.clientEntity != null) {
			this.clientEntity.setPosRaw(x, y, z);
			this.clientEntity.setXRot(xr);
			this.clientEntity.setYRot(yr);
		}
	}
}