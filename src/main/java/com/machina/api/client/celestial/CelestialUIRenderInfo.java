package com.machina.api.client.celestial;

import org.joml.Vector2d;

import com.machina.api.starchart.obj.Celestial;

import net.minecraft.world.phys.Vec3;

public record CelestialUIRenderInfo(Celestial celestial, Vector2d screenPos, Vec3 worldPos) {

    public static final CelestialUIRenderInfo from(CelestialRenderInfo info, Vector2d screenPos, Vec3 worldPos) {
        return new CelestialUIRenderInfo(info.celestial(), screenPos, worldPos);
    }
}