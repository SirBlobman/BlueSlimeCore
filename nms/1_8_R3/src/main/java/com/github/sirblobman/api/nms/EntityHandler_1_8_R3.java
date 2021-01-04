package com.github.sirblobman.api.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHandler_1_8_R3 extends EntityHandler {
    public EntityHandler_1_8_R3(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName(Entity entity) {
        if(entity instanceof Player) {
            Player player = (Player) entity;
            return player.getName();
        }

        String customName = entity.getCustomName();
        return (customName == null ? entity.getName() : customName);
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