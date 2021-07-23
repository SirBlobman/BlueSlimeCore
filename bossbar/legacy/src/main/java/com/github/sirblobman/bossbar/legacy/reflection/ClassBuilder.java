package com.github.sirblobman.bossbar.legacy.reflection;

import org.bukkit.Location;

import org.jetbrains.annotations.Nullable;

public abstract class ClassBuilder {
    public static Object buildWitherSpawnPacket(final int id, final Location loc, final Object dataWatcher) throws Exception {
        final Object packet = NMSClass.PacketPlayOutSpawnEntityLiving.getConstructor().newInstance();
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("a")).set(packet, id);
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("b")).set(packet, 64);
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("c")).set(packet, (int)loc.getX());
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("d")).set(packet, MathUtil.floor(loc.getY() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("e")).set(packet, (int)loc.getZ());
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("i")).set(packet, (byte) MathUtil.d(loc.getYaw() * 256.0f / 360.0f));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("j")).set(packet, (byte) MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("k")).set(packet, (byte) MathUtil.d(loc.getPitch() * 256.0f / 360.0f));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntityLiving.getDeclaredField("l")).set(packet, dataWatcher);
        return packet;
    }

    public static Object buildNameMetadataPacket(final int id, Object dataWatcher, final int nameIndex, final int visibilityIndex, final String name) throws Exception {
        setDataWatcherValue(dataWatcher, nameIndex, (name != null) ? name : "");
        dataWatcher = setDataWatcherValue(dataWatcher, visibilityIndex, (byte)((name != null && !name.isEmpty()) ? 1 : 0));
        return NMSClass.PacketPlayOutEntityMetadata.getConstructor(Integer.TYPE, NMSClass.DataWatcher, Boolean.TYPE).newInstance(id, dataWatcher, true);
    }

    public static Object buildDataWatcher(@Nullable final Object entity) throws Exception {
        return NMSClass.DataWatcher.getConstructor(NMSClass.Entity).newInstance(entity);
    }

    public static Object buildWatchableObject(final int type, final int index, final Object value) throws Exception {
        return NMSClass.WatchableObject.getConstructor(Integer.TYPE, Integer.TYPE, Object.class).newInstance(type, index, value);
    }
    
    public static Object setDataWatcherValue(final Object dataWatcher, final int index, final Object value) throws Exception {
        final int type = getDataWatcherValueType(value);
        final Object map = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues")).get(dataWatcher);
        NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", Integer.TYPE, Object.class).invoke(map, index, buildWatchableObject(type, index, value));
        return dataWatcher;
    }

    public static int getDataWatcherValueType(final Object value) {
        int type = 0;
        if (value instanceof Number) {
            if (value instanceof Short) {
                type = 1;
            }
            else if (value instanceof Integer) {
                type = 2;
            }
            else if (value instanceof Float) {
                type = 3;
            }
        }
        else if (value instanceof String) {
            type = 4;
        }
        else if (value != null && value.getClass().equals(NMSClass.ItemStack)) {
            type = 5;
        }
        else if (value != null && (value.getClass().equals(NMSClass.ChunkCoordinates) || value.getClass().equals(NMSClass.BlockPosition))) {
            type = 6;
        }
        else if (value != null && value.getClass().equals(NMSClass.Vector3f)) {
            type = 7;
        }
        return type;
    }

    public static Object buildTeleportPacket(final int id, final Location loc) throws Exception {
        final Object packet = NMSClass.PacketPlayOutEntityTeleport.getConstructor().newInstance();
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("a")).set(packet, id);
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("b")).set(packet, (int)(loc.getX() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(packet, (int)(loc.getY() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("d")).set(packet, (int)(loc.getZ() * 32.0));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("e")).set(packet, (byte)(loc.getYaw() * 256.0f / 360.0f));
        AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("f")).set(packet, (byte)(loc.getPitch() * 256.0f / 360.0f));
        return packet;
    }
}
