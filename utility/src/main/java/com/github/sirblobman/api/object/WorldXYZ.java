package com.github.sirblobman.api.object;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WorldXYZ {
    private final UUID worldId;
    private final int x, y, z;

    public WorldXYZ(World world, int x, int y, int z) {
        this.worldId = Validate.notNull(world, "world must not be null!").getUID();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static WorldXYZ from(World world, int x, int y, int z) {
        return new WorldXYZ(world, x, y, z);
    }

    public static WorldXYZ from(Block block) {
        Validate.notNull(block, "block must not be null!");

        World world = block.getWorld();
        int x = block.getX(), y = block.getY(), z = block.getZ();
        return new WorldXYZ(world, x, y, z);
    }

    public static WorldXYZ from(Location location) {
        Validate.notNull(location, "location must not be null!");

        Block block = location.getBlock();
        return from(block);
    }

    public static WorldXYZ from(Entity entity) {
        Validate.notNull(entity, "entity must not be null!");

        Location location = entity.getLocation();
        return from(location);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof WorldXYZ) {
            WorldXYZ otherXYZ = (WorldXYZ) other;
            return (this.worldId.equals(otherXYZ.worldId)
                    && this.x == otherXYZ.x && this.y == otherXYZ.y && this.z == otherXYZ.z);
        }

        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.worldId, this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        UUID worldId = getWorldId();
        int x = getX(), y = getY(), z = getZ();
        return String.format(Locale.US, "WorldXYZ{worldId=%s,x=%s,y=%s,z=%s}", worldId, x, y, z);
    }

    @NotNull
    public UUID getWorldId() {
        return this.worldId;
    }

    @Nullable
    public World getWorld() {
        UUID uuid = getWorldId();
        return Bukkit.getWorld(uuid);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    @Nullable
    public Location asLocation() {
        World world = getWorld();
        if(world == null) return null;

        int x = getX(), y = getY(), z = getZ();
        return new Location(world, x, y, z);
    }
}
