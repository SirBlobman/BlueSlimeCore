package com.github.sirblobman.bossbar.legacy.reflection;

import java.lang.reflect.Field;

public abstract class NMUClass {
    public static Class<?> gnu_trove_map_hash_TIntObjectHashMap;

    static {
        for (Field field : NMUClass.class.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (!fieldType.equals(Class.class)) continue;
            String fieldName = field.getName();

            try {
                String realFieldName = fieldName.replace("_", ".");
                String reflectionVersion = Reflection.getVersion();
                if (reflectionVersion.contains("1_8")) {
                    field.set(null, Class.forName(realFieldName));
                } else {
                    field.set(null, Class.forName("net.minecraft.util." + realFieldName));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
