package com.github.sirblobman.api.location;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityLocation {
    @NotNull
    public static EntityLocation from(World world, double x, double y, double z, double yaw, double pitch) {
        Validate.notNull(world, "world must not be null!");
        UUID worldId = world.getUID();
        return new EntityLocation(worldId, x, y, z, yaw, pitch);
    }

    @Nullable
    public static EntityLocation from(Location location) {
        Validate.notNull(location, "location must not be null!");

        World world = location.getWorld();
        if(world == null) {
            return null;
        }

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double yaw = location.getYaw();
        double pitch = location.getPitch();
        return from(world, x, y, z, yaw, pitch);
    }

    @Nullable
    public static EntityLocation from(Entity entity) {
        Validate.notNull(entity, "entity must not be null!");
        Location location = entity.getLocation();
        return from(location);
    }

    private final UUID worldId;
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;

    public EntityLocation(UUID worldId, double x, double y, double z, double yaw, double pitch) {
        this.worldId = Validate.notNull(worldId, "worldId must not be null!");
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @NotNull
    public UUID getWorldId() {
        return this.worldId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

    @Nullable
    public World getWorld() {
        UUID worldId = getWorldId();
        return Bukkit.getWorld(worldId);
    }

    @Nullable
    public Location asLocation() {
        World world = getWorld();
        if(world == null) {
            return null;
        }

        double x = getX();
        double y = getY();
        double z = getZ();
        double yaw = getYaw();
        double pitch = getPitch();
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof EntityLocation)) {
            return false;
        }

        UUID thisWorldId = getWorldId();
        double thisX = getX();
        double thisY = getY();
        double thisZ = getZ();
        double thisYaw = getYaw();
        double thisPitch = getPitch();

        EntityLocation other = (EntityLocation) object;
        UUID otherWorldId = other.getWorldId();
        double otherX = other.getX();
        double otherY = other.getY();
        double otherZ = other.getZ();
        double otherYaw = other.getYaw();
        double otherPitch = other.getPitch();
        
        boolean checkId = Objects.equals(thisWorldId, otherWorldId);
        boolean checkCoordinates = (thisX == otherX && thisY == otherY && thisZ == otherZ);
        boolean checkFacing = (thisYaw == otherYaw && thisPitch == otherPitch);
        return (checkId && checkCoordinates && checkFacing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.worldId, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    @Override
    public String toString() {
        UUID worldId = getWorldId();
        double x = getX();
        double y = getY();
        double z = getZ();
        double yaw = getYaw();
        double pitch = getPitch();
        return String.format(Locale.US, "EntityLocation{worldId=%s,x=%s,y=%s,z=%s,yaw=%s,pitch=%s}",
                worldId, x, y, z, yaw, pitch);
    }
}
