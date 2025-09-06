package com.machina.api.util.math;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

public class DirUtil {

    private static final Direction[] HORIZONTAL_VALUES = new Direction[]{Direction.NORTH, Direction.SOUTH,
            Direction.EAST, Direction.WEST};

    public static Direction getRandomHorizontal(RandomSource r) {
        return Util.getRandom(HORIZONTAL_VALUES, r);
    }

}
