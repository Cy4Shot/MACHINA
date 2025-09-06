package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;
import net.minecraft.util.Mth;

public class SDFCapsule extends SDFPrimitive {
    private final float radius;
    private final float height;

    public SDFCapsule(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        return MathUtil.length(x, y - Mth.clamp(y, 0, height), z) - radius;
    }
}
