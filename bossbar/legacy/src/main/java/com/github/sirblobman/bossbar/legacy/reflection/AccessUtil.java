package com.github.sirblobman.bossbar.legacy.reflection;

import java.lang.reflect.Field;

public abstract class AccessUtil {
    public static Field setAccessible(Field field)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);

        Field field_modifiers = Field.class.getDeclaredField("modifiers");
        field_modifiers.setAccessible(true);

        int modifiers = field.getModifiers();
        field_modifiers.setInt(field, modifiers & 0xFFFFFFEF);
        return field;
    }
}
