package com.github.sirblobman.api.nms;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHandler_1_14_R1 extends EntityHandler {
    public EntityHandler_1_14_R1(JavaPlugin plugin) {
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
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        return (attribute == null ? 0.0D : attribute.getValue());
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attribute != null) attribute.setBaseValue(maxHealth);
    }
}