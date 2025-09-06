package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.VecUtil;
import com.machina.api.util.math.sdf.SDF;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SDFRotation extends SDFUnary {
    private final Vector3f pos = new Vector3f();
    private final Quaternionf rotation;

    public SDFRotation(SDF source, Vector3f axis, float rotationAngle) {
        super(source);
        rotation = VecUtil.rotationDegrees(axis, rotationAngle);
    }

    @Override
    public float getDistance(float x, float y, float z) {
        pos.set(x, y, z);
        pos.rotate(rotation);
        return source.getDistance(pos.x(), pos.y(), pos.z());
    }
}
