package com.github.sirblobman.bossbar.legacy.reflection;

public abstract class MathUtil {
    public static int floor(final double d1) {
        final int i = (int)d1;
        return (d1 >= i) ? i : (i - 1);
    }
    
    public static int d(final float f1) {
        final int i = (int)f1;
        return (f1 >= i) ? i : (i - 1);
    }
}
