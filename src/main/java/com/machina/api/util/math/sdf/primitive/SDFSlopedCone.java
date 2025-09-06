package com.machina.api.util.math.sdf.primitive;

public class SDFSlopedCone extends SDFPrimitive {
    private final float radius1;
    private final float radius2;
    private final float height;

    public SDFSlopedCone(float r1, float r2, float height) {
        this.radius1 = r1;
        this.radius2 = r2;
        this.height = height;
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float normalizedZ = Math.max(0, Math.min(height, -z)); // Constrain z between 0 and height

        // Quadratic interpolation of the radius
        float radiusAtZ = radius2 + (radius1 - radius2) * (normalizedZ / height) * (normalizedZ / height);

        // Radial distance from the center
        float radialDistance = (float) Math.sqrt(x * x + y * y);

        // Distance to the cone's surface
        float surfaceDistance = radialDistance - radiusAtZ;

        // Clamp the height
        // Top plane
        float bottomCap = height - normalizedZ; // Bottom plane

        // Combine the distances with max to respect boundaries
        return Math.max(surfaceDistance, Math.max(normalizedZ, -bottomCap));
    }
}
