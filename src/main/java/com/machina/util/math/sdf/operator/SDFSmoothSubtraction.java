package com.machina.util.math.sdf.operator;

import net.minecraft.util.math.MathHelper;

public class SDFSmoothSubtraction extends SDFBinary {
    private float radius;

    public SDFSmoothSubtraction setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float a = this.sourceA.getDistance(x, y, z);
        float b = this.sourceB.getDistance(x, y, z);
        this.selectValue(a, b);
        float h = MathHelper.clamp(0.5F - 0.5F * (b + a) / radius, 0F, 1F);
        return MathHelper.lerp(h, b, -a) + radius * h * (1F - h);
    }
}
