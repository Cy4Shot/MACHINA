package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;
import net.minecraft.util.Mth;

public class SDFPie extends SDFPrimitive {
    private final float sin;
    private final float cos;
    private final float radius;

    public SDFPie(float angle, float radius) {
        this.sin = (float) Math.sin(angle);
        this.cos = (float) Math.cos(angle);
        this.radius = radius;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float px = Math.abs(x);
        float l = MathUtil.length(px, y, z) - radius;
        float m = MathUtil.dot(px, z, sin, cos);
        m = Mth.clamp(m, 0, radius);
        m = MathUtil.length(px - sin * m, z - cos * m);
        return MathUtil.max(l, m * Math.signum(cos * px - sin * z));
    }
}
