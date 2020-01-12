package com.SirBlobman.api.nms;

import org.bukkit.entity.LivingEntity;

public class FallbackEntityHandler extends EntityHandler {
    @Override
    public double getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }
    
    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }
    
    @Override
    public String getName(LivingEntity entity) {
        return entity.getType().name();
    }
}