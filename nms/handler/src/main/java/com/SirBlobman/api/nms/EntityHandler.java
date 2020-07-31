package com.SirBlobman.api.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class EntityHandler {
    private final JavaPlugin plugin;
    public EntityHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public final JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public abstract String getName(Entity entity);
    public abstract double getMaxHealth(LivingEntity entity);
    public abstract void setMaxHealth(LivingEntity entity, double maxHealth);
}