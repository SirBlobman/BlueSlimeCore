package com.SirBlobman.api.nms;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityHandler_1_7_R4 extends EntityHandler {
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
        String customName = entity.getCustomName();
        if(customName != null) return customName;
        
        EntityType type = entity.getType();
        return type.name();
    }
}