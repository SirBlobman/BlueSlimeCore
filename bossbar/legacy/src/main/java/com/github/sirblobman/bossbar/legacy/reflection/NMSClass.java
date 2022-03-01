package com.github.sirblobman.bossbar.legacy.reflection;

import java.lang.reflect.Field;

public abstract class NMSClass {
    public static Class<?> Entity;
    public static Class<?> PacketPlayOutSpawnEntityLiving;
    public static Class<?> PacketPlayOutEntityDestroy;
    public static Class<?> PacketPlayOutEntityTeleport;
    public static Class<?> PacketPlayOutEntityMetadata;
    public static Class<?> DataWatcher;
    public static Class<?> WatchableObject;
    public static Class<?> ItemStack;
    public static Class<?> ChunkCoordinates;
    public static Class<?> BlockPosition;
    public static Class<?> Vector3f;
    
    static {
        for(Field field : NMSClass.class.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if(!fieldType.equals(Class.class)) continue;
            String fieldName = field.getName();
            
            try {
                field.set(null, Reflection.getNMSClassWithException(fieldName));
            } catch(Exception ex) {
                if(fieldName.equals("WatchableObject")) {
                    try {
                        field.set(null, Reflection.getNMSClassWithException("DataWatcher$WatchableObject"));
                    } catch(Exception ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
    }
}
