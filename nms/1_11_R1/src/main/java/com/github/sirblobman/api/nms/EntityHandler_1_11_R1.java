package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public class EntityHandler_1_11_R1 extends EntityHandler {
    public EntityHandler_1_11_R1(JavaPlugin plugin) {
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
    public void setCustomNameTextOnly(Entity entity, String text, boolean visible) {
        entity.setCustomName(text);
        entity.setCustomNameVisible(visible);
    }
    
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
    public <T extends Entity> T spawnEntity(Location location, Class<T> entityClass, Consumer<T> beforeSpawn) {
        Validate.notNull(location, "location must not be null!");
        Validate.notNull(entityClass, "entityClass must not be null!");
        
        World world = location.getWorld();
        if(world == null) throw new IllegalArgumentException("location must have a valid bukkit world!");
        
        org.bukkit.util.Consumer<T> bukkitConsumer = beforeSpawn::accept;
        return world.spawn(location, entityClass, bukkitConsumer);
    }
}
