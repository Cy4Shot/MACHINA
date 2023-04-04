package com.machina.util.sdf.primitive;

import com.machina.util.math.MathUtil;

public class SDFTorus extends SDFPrimitive {
    private float radiusSmall;
    private float radiusBig;

    public SDFTorus setBigRadius(float radius) {
        this.radiusBig = radius;
        return this;
    }

    public SDFTorus setSmallRadius(float radius) {
        this.radiusSmall = radius;
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float nx = MathUtil.length(x, z) - radiusBig;
        return MathUtil.length(nx, y) - radiusSmall;
    }
}
