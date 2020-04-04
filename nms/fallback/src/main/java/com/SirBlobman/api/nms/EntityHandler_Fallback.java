package com.SirBlobman.api.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHandler_Fallback extends EntityHandler {
    public EntityHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public String getName(Entity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            return player.getName();
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