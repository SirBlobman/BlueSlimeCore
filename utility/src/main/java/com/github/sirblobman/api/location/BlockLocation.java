package com.github.sirblobman.api.location;

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

public class BlockLocation {
    private final UUID worldId;
    private final int x, y, z;

    public BlockLocation(UUID worldId, int x, int y, int z) {
        this.worldId = Validate.notNull(worldId, "worldId must not be null!");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NotNull
    public static BlockLocation from(World world, int x, int y, int z) {
        Validate.notNull(world, "world must not be null!");

        UUID worldId = world.getUID();
        return new BlockLocation(worldId, x, y, z);
    }

    @NotNull
    public static BlockLocation from(Block block) {
        Validate.notNull(block, "block must not be null!");

        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return from(world, x, y, z);
    }

    @Nullable
    public static BlockLocation from(Location location) {
        Validate.notNull(location, "location must not be null!");

        World world = location.getWorld();
        if (world == null) {
            return null;
        }

        Block block = location.getBlock();
        return from(block);
    }

    @Nullable
    public static BlockLocation from(Entity entity) {
        Validate.notNull(entity, "entity must not be null!");

        Location location = entity.getLocation();
        return from(location);
    }

    @NotNull
    public UUID getWorldId() {
        return this.worldId;
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
    public World getWorld() {
        UUID worldId = getWorldId();
        return Bukkit.getWorld(worldId);
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

    @Nullable
    public Block asBlock() {
        World world = getWorld();
        if (world == null) {
            return null;
        }

        int x = getX();
        int y = getY();
        int z = getZ();
        return world.getBlockAt(x, y, z);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof BlockLocation)) {
            return false;
        }

        BlockLocation other = (BlockLocation) object;
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

        return String.format(Locale.US, "BlockLocation{worldId=%s,x=%s,y=%s,z=%s}", worldId, x, y, z);
    }
}
