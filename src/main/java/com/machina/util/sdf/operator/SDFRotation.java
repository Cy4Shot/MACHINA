package com.machina.util.sdf.operator;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class SDFRotation extends SDFUnary {
    private final Vector3f pos = new Vector3f();
    private Quaternion rotation;

    public SDFRotation setRotation(Vector3f axis, float rotationAngle) {
        rotation = new Quaternion(axis, rotationAngle, false);
        return this;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        pos.set(x, y, z);
        pos.transform(rotation);
        return source.getDistance(pos.x(), pos.y(), pos.z());
    }
}
