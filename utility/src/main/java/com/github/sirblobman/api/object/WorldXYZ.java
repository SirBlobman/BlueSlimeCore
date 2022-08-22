package com.github.sirblobman.api.object;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.github.sirblobman.api.location.BlockLocation;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Use {@link BlockLocation} instead.
 */
@Deprecated
public final class WorldXYZ {
    private final UUID worldId;
    private final int x, y, z;

    private WorldXYZ(UUID worldId, int x, int y, int z) {
        this.worldId = Validate.notNull(worldId, "worldId must not be null!");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static WorldXYZ from(World world, int x, int y, int z) {
        Validate.notNull(world, "world must not be null!");
        UUID worldId = world.getUID();
        return new WorldXYZ(worldId, x, y, z);
    }

    public static WorldXYZ from(Block block) {
        Validate.notNull(block, "block must not be null!");
        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return from(world, x, y, z);
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

    @NotNull
    public UUID getWorldId() {
        return this.worldId;
    }

    @Nullable
    public World getWorld() {
        UUID worldId = getWorldId();
        return Bukkit.getWorld(worldId);
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
        if (world == null) {
            return null;
        }

        int x = getX();
        int y = getY();
        int z = getZ();
        return new Location(world, x, y, z, 0.0F, 0.0F);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof WorldXYZ)) {
            return false;
        }

        WorldXYZ other = (WorldXYZ) object;
        boolean checkId = Objects.equals(this.worldId, other.worldId);
        return (checkId && this.x == other.x && this.y == other.y && this.z == other.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.worldId, this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        UUID worldId = getWorldId();
        int x = getX();
        int y = getY();
        int z = getZ();

        return String.format(Locale.US, "WorldXYZ{worldId=%s,x=%s,y=%s,z=%s}", worldId, x, y, z);
    }
}
