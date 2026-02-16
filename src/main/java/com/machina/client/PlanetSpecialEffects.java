package com.machina.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

public class PlanetSpecialEffects extends DimensionSpecialEffects {

    public PlanetSpecialEffects() {
        super(192, false, SkyType.NONE, false, false);
    }

    @Override
    public @NotNull Vec3 getBrightnessDependentFogColor(Vec3 col, float brightness) {
        return col.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

//	private Planet getPlanet(ClientLevel level) {
//		int id = PlanetHelper.getIdLevel(level.dimension());
//		return ClientStarchart.system.planets().get(id);
//	}


    @Override
    public boolean renderSnowAndRain(@NotNull ClientLevel level, int ticks, float partialTick,
                                     @NotNull LightTexture lightTexture, double camX, double camY, double camZ) {
        return true;
    }
    
    @Override
    public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX,
            double camY, double camZ, Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
        return true;
    }
    
    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera,
            Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        return true;
    }
}