package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EntityHandler extends Handler {
    public EntityHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getName(Entity entity);
    public abstract void setCustomNameTextOnly(Entity entity, String text, boolean visible);

    public abstract double getMaxHealth(LivingEntity entity);
    public abstract void setMaxHealth(LivingEntity entity, double maxHealth);

    public abstract <T extends Entity> T spawnEntity(Location location, Class<T> entityClass, Consumer<T> beforeSpawn);
}
