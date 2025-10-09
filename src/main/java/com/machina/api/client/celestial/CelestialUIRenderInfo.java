package com.machina.api.client.celestial;

import com.machina.api.starchart.obj.Celestial;

import net.minecraft.world.phys.Vec2;


public record CelestialUIRenderInfo(Celestial celestial, Vec2 pos) {
    
    public static final CelestialUIRenderInfo from(CelestialRenderInfo info, Vec2 pos) {
        return new CelestialUIRenderInfo(info.celestial(), pos);
    }
}