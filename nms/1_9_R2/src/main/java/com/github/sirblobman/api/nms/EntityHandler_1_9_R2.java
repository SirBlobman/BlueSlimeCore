package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;

import com.github.sirblobman.api.utility.Validate;

public class EntityHandler_1_9_R2 extends EntityHandler {
    public EntityHandler_1_9_R2(JavaPlugin plugin) {
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
        return entity.getMaxHealth();
    }
    
    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        entity.setMaxHealth(maxHealth);
    }
    
    @Override
    public <T extends Entity> T spawnEntity(Location location, Class<T> entityClass, Consumer<T> beforeSpawn) {
        Validate.notNull(location, "location must not be null!");
        Validate.notNull(entityClass, "entityClass must not be null!");
        
        World world = location.getWorld();
        if(!(world instanceof CraftWorld))
            throw new IllegalArgumentException("location must have a valid bukkit world!");
        CraftWorld craftWorld = (CraftWorld) world;
        
        net.minecraft.server.v1_9_R2.Entity nmsEntity = craftWorld.createEntity(location, entityClass);
        if(beforeSpawn != null) beforeSpawn.accept(entityClass.cast(nmsEntity.getBukkitEntity()));
        
        craftWorld.addEntity(nmsEntity, SpawnReason.CUSTOM);
        return entityClass.cast(nmsEntity.getBukkitEntity());
    }
}
