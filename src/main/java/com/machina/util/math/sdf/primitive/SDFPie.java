package com.machina.util.math.sdf.primitive;

import com.machina.util.math.MathUtil;

import net.minecraft.util.math.MathHelper;

public class SDFPie extends SDFPrimitive {
    private float sin;
    private float cos;
    private float radius;

    public SDFPie setAngle(float angle) {
        this.sin = (float) Math.sin(angle);
        this.cos = (float) Math.cos(angle);
        return this;
    }

    public SDFPie setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float px = Math.abs(x);
        float l = MathUtil.length(px, y, z) - radius;
        float m = MathUtil.dot(px, z, sin, cos);
        m = MathHelper.clamp(m, 0, radius);
        m = MathUtil.length(px - sin * m, z - cos * m);
        return MathUtil.max(l, m * Math.signum(cos * px - sin * z));
    }
}
