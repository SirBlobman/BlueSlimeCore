package com.github.sirblobman.api.location;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.github.sirblobman.api.utility.Validate;

public class BlockLocation {
    private final UUID worldId;
    private final int x, y, z;

    public BlockLocation(@NotNull UUID worldId, int x, int y, int z) {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static @NotNull BlockLocation from(@NotNull World world, int x, int y, int z) {
        UUID worldId = world.getUID();
        return new BlockLocation(worldId, x, y, z);
    }

    public static @NotNull BlockLocation from(@NotNull Block block) {
        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return from(world, x, y, z);
    }

    public static @NotNull BlockLocation from(@NotNull Location location) {
        World world = location.getWorld();
        Validate.notNull(world, "world must not be null!");

        Block block = location.getBlock();
        return from(block);
    }

    public static @NotNull BlockLocation from(Entity entity) {
        Location location = entity.getLocation();
        return from(location);
    }

    public @NotNull UUID getWorldId() {
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

    public @Nullable World getWorld() {
        UUID worldId = getWorldId();
        return Bukkit.getWorld(worldId);
    }

    public @Nullable Location asLocation() {
        World world = getWorld();
        if (world == null) {
            return null;
        }

        int x = getX();
        int y = getY();
        int z = getZ();
        return new Location(world, x, y, z, 0.0F, 0.0F);
    }

    public @Nullable Block asBlock() {
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
