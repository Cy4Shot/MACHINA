package com.machina.client.cinema;

import com.machina.client.cinema.entity.CameraClientEntity;
import com.machina.client.cinema.entity.CinematicClientEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

public abstract class Cinematic {

	protected static final Minecraft mc = Minecraft.getInstance();

	protected CameraClientEntity player;
	protected CinematicClientEntity clientEntity;
	protected boolean active = false;

	private boolean bobView = false, hideGui = false, flying = false;
	private PointOfView cameraType;
	private Vector3d pos;
	private float yaw, pitch;

	public Cinematic(CameraClientEntity p) {
		this.player = p;
	}

	public abstract void onClientTick(int tick, float par);

	public abstract int getDuration();

	public void begin(float partial) {
		this.bobView = mc.options.bobView;
		this.hideGui = mc.options.hideGui;
		this.cameraType = mc.options.getCameraType();
		this.flying = mc.player.abilities.flying;
		this.pos = mc.player.position();
		this.yaw = mc.player.xRot;
		this.pitch = mc.player.yRot;
		mc.player.setDeltaMovement(0, 0, 0);
		this.active = true;

		CinematicClientEntity repl = new CinematicClientEntity();
		repl.load(mc.player.saveWithoutId(new CompoundNBT()));
		mc.level.addPlayer(repl.getId(), repl);
		this.clientEntity = repl;
		mc.setCameraEntity(this.player);
	}

	public void finish(float partial) {
		if (active) {
			mc.options.bobView = bobView;
			mc.options.hideGui = hideGui;
			mc.options.setCameraType(cameraType);
			mc.player.abilities.flying = flying;
			mc.player.setPos(pos.x, pos.y, pos.z);
			mc.player.xRot = yaw;
			mc.player.yRot = pitch;
			mc.player.setDeltaMovement(0, 0, 0);
			this.active = false;
		}

        CameraUtil.resetCamera();
	
        if (mc.player != null) {
        	mc.player.setPos(clientEntity.getX(), clientEntity.getY(), clientEntity.getZ());
        	mc.player.xRot = clientEntity.xRot;
        	mc.player.yRot = clientEntity.yRot;
            mc.player.setDeltaMovement(0, 0, 0);
        }
        
        if (mc.level != null) {
            mc.level.removeEntity(this.clientEntity.getId());
        }
	}

	public void transform(float partial) {
		if (active) {
			mc.options.hideGui = true;
			mc.options.bobView = false;
			mc.options.setCameraType(PointOfView.FIRST_PERSON);
			mc.player.abilities.flying = true;
			mc.player.setDeltaMovement(0, 0, 0);
		}
	}

}
