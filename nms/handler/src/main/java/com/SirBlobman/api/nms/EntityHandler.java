package com.SirBlobman.api.nms;

import org.bukkit.entity.LivingEntity;

public abstract class EntityHandler {
    public abstract double getMaxHealth(LivingEntity entity);
    public abstract void setMaxHealth(LivingEntity entity, double maxHealth);
    
    public abstract String getName(LivingEntity entity);
}