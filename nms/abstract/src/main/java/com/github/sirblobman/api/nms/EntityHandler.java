package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract NMS Entity Handler Class
 * @author SirBlobman
 */
public abstract class EntityHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public EntityHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Gets the name of the given entity.
     * If the entity is a {@link Player}, it will show their username.
     * Otherwise, it will get the custom name.
     * If the entity does not have a custom name, it will get the regular name.
     * @param entity the entity to get the name from.
     * @return the name of the entity.
     * @see Player#getName()
     * @see Entity#getCustomName()
     * @see Entity#getName()
     */
    public abstract @NotNull String getName(@NotNull Entity entity);

    /**
     * Sets the custom name of the given entity with a single text variable.
     * Internally, it should be equivalent to the following json:
     * {@code {"text": "name"}}
     * This method exists because the regular setCustomName does some strange text modification due to color codes.
     * @param entity the entity that will be changed.
     * @param text the custom name text to set.
     * @param visible whether the custom name should be visible or not.
     */
    public abstract void setCustomNameTextOnly(@NotNull Entity entity, String text, boolean visible);

    /**
     * Gets the maximum health of the given living entity.
     * This method exists because there is a new attribute system in newer versions.
     * @param entity the living entity to get the maximum health of
     * @return the maximum health of the living entity
     */
    public abstract double getMaxHealth(@NotNull LivingEntity entity);

    /**
     * Sets the maximum health of the given living entity.
     * This method exists because there is a new attribute system in newer versions.
     * @param entity the living entity to modify.
     * @param maxHealth the maximum health to set.
     */
    public abstract void setMaxHealth(@NotNull LivingEntity entity, double maxHealth);

    /**
     * Spawns a new entity of the given class at the specified location, allowing for customization before spawning.
     *
     * @param location the location to spawn the entity at.
     * @param entityClass the class of the entity to spawn.
     * @param beforeSpawn a consumer function to apply any customizations to the entity before it is spawned.
     * @param <T> the type of entity to spawn.
     * @return the spawned entity.
     */
    public abstract <T extends Entity> @NotNull T spawnEntity(@NotNull Location location,
                                                              @NotNull Class<T> entityClass,
                                                              @NotNull Consumer<T> beforeSpawn);
}
