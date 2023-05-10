package com.github.sirblobman.api.nms;

import java.util.function.Consumer;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.VersionUtility;

public final class EntityHandler_Fallback extends EntityHandler {
    public EntityHandler_Fallback(@NotNull JavaPlugin plugin) {
        super(plugin);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        Logger logger = getLogger();
        logger.warning("Using fallback EntityHandler.");
        logger.warning("Version '" + minecraftVersion + "' and NMS '" + nmsVersion + "' combo is not supported.");
        logger.warning("Please contact SirBlobman if you believe this is a mistake.");
        logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
    }

    @Override
    public @NotNull String getName(@NotNull Entity entity) {
        return entity.getName();
    }

    @Override
    public void setCustomNameTextOnly(@NotNull Entity entity, String text, boolean visible) {
        entity.setCustomName(text);
        entity.setCustomNameVisible(false);
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
        if (world == null) {
            throw new IllegalArgumentException("location must not have a null world.");
        }

        T entity = world.spawn(location, entityClass);
        beforeSpawn.accept(entity);
        return entity;
    }
}
