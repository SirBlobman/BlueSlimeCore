package com.SirBlobman.api.nms;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class EntityHandler_1_14_R1 extends EntityHandler {
    @Override
    public double getMaxHealth(LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        return attribute.getValue();
    }
    
    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(maxHealth);
    }
    
    @Override
    public String getName(LivingEntity entity) {
        return entity.getName();
    }
}