package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class SDFLine extends SDFPrimitive {
    private final float radius;
    private final float x1;
    private final float y1;
    private final float z1;
    private final float x2;
    private final float y2;
    private final float z2;

    public SDFLine(float x1, float y1, float z1, float x2, float y2, float z2, float radius) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.radius = radius;
    }

    public SDFLine(Vector3f start, Vector3f end, float radius) {
        this(start.x, start.y, start.z, end.x, end.y, end.z, radius);
    }

    @Override
    public float getDistance(float x, float y, float z) {
        float pax = x - x1;
        float pay = y - y1;
        float paz = z - z1;

        float bax = x2 - x1;
        float bay = y2 - y1;
        float baz = z2 - z1;

        float dpb = MathUtil.dot(pax, pay, paz, bax, bay, baz);
        float dbb = MathUtil.dot(bax, bay, baz, bax, bay, baz);
        float h = Mth.clamp(dpb / dbb, 0F, 1F);
        return MathUtil.length(pax - bax * h, pay - bay * h, paz - baz * h) - radius;
    }
}
