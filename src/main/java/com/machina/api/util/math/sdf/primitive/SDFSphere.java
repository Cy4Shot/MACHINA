package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;

public class SDFSphere extends SDFPrimitive {
    private final float radius;

    public SDFSphere(float radius) {
        this.radius = radius;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        return MathUtil.length(x, y, z) - radius;
    }
}
