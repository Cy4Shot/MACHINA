package com.machina.util.sdf.operator;

import java.util.function.Function;

import net.minecraft.util.math.vector.Vector3f;

public class SDFDisplacement extends SDFUnary {
    private final Vector3f pos = new Vector3f();
    private Function<Vector3f, Float> displace;

    public SDFDisplacement setFunction(Function<Vector3f, Float> displace) {
        this.displace = displace;
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        pos.set(x, y, z);
        return this.source.getDistance(x, y, z) + displace.apply(pos);
    }
}
