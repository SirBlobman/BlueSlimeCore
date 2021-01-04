package com.github.sirblobman.api.nms;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHandler_1_7_R4 extends EntityHandler {
    public EntityHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName(Entity entity) {
        if(entity instanceof AnimalTamer) {
            AnimalTamer animalTamer = (AnimalTamer) entity;
            return animalTamer.getName();
        }

        if(entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            String customName = living.getCustomName();
            if(customName != null) return customName;
        }

        EntityType entityType = entity.getType();
        return entityType.name();
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }
}