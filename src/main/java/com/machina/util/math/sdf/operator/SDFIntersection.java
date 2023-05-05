package com.machina.util.math.sdf.operator;

import com.machina.util.math.MathUtil;

public class SDFIntersection extends SDFBinary {
    @Override
    public float getDistance(float x, float y, float z) {
        float a = this.sourceA.getDistance(x, y, z);
        float b = this.sourceB.getDistance(x, y, z);
        this.selectValue(a, b);
        return MathUtil.max(a, b);
    }
}
