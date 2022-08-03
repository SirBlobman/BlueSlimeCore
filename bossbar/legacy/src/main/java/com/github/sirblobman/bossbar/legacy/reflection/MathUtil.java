package com.github.sirblobman.bossbar.legacy.reflection;

public abstract class MathUtil {
    public static int floorDouble(double value) {
        int castInt = (int) value;
        return (value >= castInt ? castInt : (castInt - 1));
    }

    public static int floorFloat(final float value) {
        int castInt = (int) value;
        return (value >= castInt ? castInt : (castInt - 1));
    }
}
