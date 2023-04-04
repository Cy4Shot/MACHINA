package com.machina.util.sdf.primitive;

import com.machina.util.math.MathUtil;

import net.minecraft.util.math.MathHelper;

public class SDFCapsule extends SDFPrimitive {
    private float radius;
    private float height;

    public SDFCapsule setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public SDFCapsule setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        return MathUtil.length(x, y - MathHelper.clamp(y, 0, height), z) - radius;
    }
}
