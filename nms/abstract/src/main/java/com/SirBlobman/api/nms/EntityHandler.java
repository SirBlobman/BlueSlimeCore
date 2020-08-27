package com.SirBlobman.api.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EntityHandler extends Handler {
    public EntityHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getName(Entity entity);
    public abstract double getMaxHealth(LivingEntity entity);
    public abstract void setMaxHealth(LivingEntity entity, double maxHealth);
}