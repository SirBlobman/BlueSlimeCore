package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import com.github.sirblobman.api.utility.Validate;

public final class EntityHandler_1_8_R3 extends EntityHandler {
    public EntityHandler_1_8_R3(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull String getName(@NotNull Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            return player.getName();
        }

        String customName = entity.getCustomName();
        return (customName != null ? customName : entity.getName());
    }

    @Override
    public void setCustomNameTextOnly(@NotNull Entity entity, String text, boolean visible) {
        entity.setCustomName(text);
        entity.setCustomNameVisible(visible);
    }

    @Override
    public double getMaxHealth(@NotNull LivingEntity entity) {
        return entity.getMaxHealth();
    }

    @Override
    public void setMaxHealth(@NotNull LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }

    @Override
    public <T extends Entity> @NotNull T spawnEntity(@NotNull Location location, @NotNull Class<T> entityClass,
                                                     @NotNull Consumer<T> beforeSpawn) {
        World world = location.getWorld();
        Validate.notNull(world, "location must have a valid world!");

        if (!(world instanceof CraftWorld)) {
            throw new IllegalArgumentException("location must have a valid bukkit world!");
        }

        CraftWorld craftWorld = (CraftWorld) world;
        net.minecraft.server.v1_8_R3.Entity nmsEntity = craftWorld.createEntity(location, entityClass);

        CraftEntity bukkitEntity = nmsEntity.getBukkitEntity();
        T castEntity = entityClass.cast(bukkitEntity);
        beforeSpawn.accept(castEntity);

        craftWorld.addEntity(nmsEntity, SpawnReason.CUSTOM);
        return castEntity;
    }
}
