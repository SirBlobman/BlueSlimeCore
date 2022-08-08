package com.github.sirblobman.api.bossbar.legacy.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Location;

import org.jetbrains.annotations.Nullable;

public abstract class ClassBuilder {
    public static Object buildWitherSpawnPacket(int id, Location location, Object dataWatcher) throws Exception {
        Class<?> class_Packet = NMSClass.PacketPlayOutSpawnEntityLiving;
        Constructor<?> constructor_Packet = class_Packet.getConstructor();
        Object object_Packet = constructor_Packet.newInstance();

        Field field_a = AccessUtil.setAccessible(class_Packet.getDeclaredField("a"));
        Field field_b = AccessUtil.setAccessible(class_Packet.getDeclaredField("b"));
        Field field_c = AccessUtil.setAccessible(class_Packet.getDeclaredField("c"));
        Field field_d = AccessUtil.setAccessible(class_Packet.getDeclaredField("d"));
        Field field_e = AccessUtil.setAccessible(class_Packet.getDeclaredField("e"));
        Field field_i = AccessUtil.setAccessible(class_Packet.getDeclaredField("i"));
        Field field_j = AccessUtil.setAccessible(class_Packet.getDeclaredField("j"));
        Field field_k = AccessUtil.setAccessible(class_Packet.getDeclaredField("k"));
        Field field_l = AccessUtil.setAccessible(class_Packet.getDeclaredField("l"));

        field_a.set(object_Packet, id);
        field_b.set(object_Packet, 64);
        field_c.set(object_Packet, location.getBlockX());
        field_d.set(object_Packet, MathUtil.floorDouble(location.getY()));
        field_e.set(object_Packet, location.getBlockZ());

        field_i.set(object_Packet, (byte) MathUtil.floorFloat(((location.getYaw() * 256.0F) / 360.0F)));
        field_j.set(object_Packet, (byte) MathUtil.floorFloat(((location.getPitch() * 256.0F) / 360.0F)));
        field_k.set(object_Packet, (byte) MathUtil.floorFloat(((location.getPitch() * 256.0F) / 360.0F)));

        field_l.set(object_Packet, dataWatcher);
        return object_Packet;
    }

    public static Object buildNameMetadataPacket(int id, Object dataWatcher, int nameIndex, int visibilityIndex,
                                                 String name) throws Exception {
        String nameString = (name == null ? "" : name);
        setDataWatcherValue(dataWatcher, nameIndex, nameString);

        byte nameByte = (byte) ((name == null || name.isEmpty()) ? 0 : 1);
        setDataWatcherValue(dataWatcher, visibilityIndex, nameByte);

        Constructor<?> constructor = NMSClass.PacketPlayOutEntityMetadata.getConstructor(Integer.TYPE,
                NMSClass.DataWatcher, Boolean.TYPE);
        return constructor.newInstance(id, dataWatcher, true);
    }

    public static Object buildDataWatcher(@Nullable Object entity) throws Exception {
        return NMSClass.DataWatcher.getConstructor(NMSClass.Entity).newInstance(entity);
    }

    public static Object buildWatchableObject(int type, int index, Object value) throws Exception {
        return NMSClass.WatchableObject.getConstructor(Integer.TYPE, Integer.TYPE, Object.class)
                .newInstance(type, index, value);
    }

    public static void setDataWatcherValue(Object dataWatcher, int index, Object value) throws Exception {
        int type = getDataWatcherValueType(value);
        Object dataValuesMap = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues"))
                .get(dataWatcher);
        NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", Integer.TYPE, Object.class)
                .invoke(dataValuesMap, index, buildWatchableObject(type, index, value));
    }

    public static int getDataWatcherValueType(Object value) {
        if (value instanceof Number) {
            if (value instanceof Byte) return 0;
            if (value instanceof Short) return 1;
            if (value instanceof Integer) return 2;
            if (value instanceof Float) return 3;
        }

        if (value instanceof String) {
            return 4;
        }

        if (value != null) {
            Class<?> valueClass = value.getClass();
            if (valueClass.equals(NMSClass.ItemStack)) return 5;
            if (valueClass.equals(NMSClass.ChunkCoordinates) || valueClass.equals(NMSClass.BlockPosition)) return 6;
            if (valueClass.equals(NMSClass.Vector3f)) return 7;
        }

        return 0;
    }

    public static Object buildTeleportPacket(int id, Location location) throws Exception {
        Class<?> class_Packet = NMSClass.PacketPlayOutEntityTeleport;
        Constructor<?> constructor_Packet = class_Packet.getConstructor();
        Object object_Packet = constructor_Packet.newInstance();

        Field field_a = AccessUtil.setAccessible(class_Packet.getDeclaredField("a"));
        Field field_b = AccessUtil.setAccessible(class_Packet.getDeclaredField("b"));
        Field field_c = AccessUtil.setAccessible(class_Packet.getDeclaredField("c"));
        Field field_d = AccessUtil.setAccessible(class_Packet.getDeclaredField("d"));
        Field field_e = AccessUtil.setAccessible(class_Packet.getDeclaredField("e"));
        Field field_f = AccessUtil.setAccessible(class_Packet.getDeclaredField("f"));

        field_a.set(object_Packet, id);
        field_b.set(object_Packet, (int) (location.getX() * 32.0D));
        field_c.set(object_Packet, (int) (location.getY() * 32.0D));
        field_d.set(object_Packet, (int) (location.getZ() * 32.0D));
        field_e.set(object_Packet, (byte) ((location.getYaw() * 256.0F) / 360.0F));
        field_f.set(object_Packet, (byte) ((location.getPitch() * 256.0F) / 360.0F));

        return object_Packet;
    }
}
