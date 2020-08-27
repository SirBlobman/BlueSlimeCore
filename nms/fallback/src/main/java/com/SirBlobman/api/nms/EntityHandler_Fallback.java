package com.SirBlobman.api.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHandler_Fallback extends EntityHandler {
    public EntityHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName(Entity entity) {
        return "N/A";
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return 0.0D;
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        // Do Nothing
    }
}