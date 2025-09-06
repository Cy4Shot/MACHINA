package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;

public class SDFHexPrism extends SDFPrimitive {
    private final float radius;
    private final float height;

    public SDFHexPrism(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float px = Math.abs(x);
        float py = Math.abs(y);
        float pz = Math.abs(z);
        return MathUtil.max(py - height, MathUtil.max((px * 0.866025F + pz * 0.5F), pz) - radius);
    }
}
