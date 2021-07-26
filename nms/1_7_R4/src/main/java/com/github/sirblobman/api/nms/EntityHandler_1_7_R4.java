package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public class EntityHandler_1_7_R4 extends EntityHandler {
    public EntityHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getName(Entity entity) {
        if(entity instanceof AnimalTamer) {
            AnimalTamer animalTamer = (AnimalTamer) entity;
            return animalTamer.getName();
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
    public void setCustomNameTextOnly(Entity entity, String text, boolean visible) {
        if(entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setCustomName(text);
            livingEntity.setCustomNameVisible(visible);
        }
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
        if(world == null) throw new IllegalArgumentException("location must have a valid bukkit world!");

        T entity = world.spawn(location, entityClass);
        beforeSpawn.accept(entity);
        return entity;
    }
}
