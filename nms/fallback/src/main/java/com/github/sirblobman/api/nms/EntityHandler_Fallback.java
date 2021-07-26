package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
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
    public void setCustomNameTextOnly(Entity entity, String text, boolean visible) {
        // Do Nothing
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        return 0.0D;
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        // Do Nothing
    }

    @Override
    public <T extends Entity> T spawnEntity(Location location, Class<T> entityClass, Consumer<T> beforeSpawn) {
        World world = location.getWorld();
        if(world == null) throw new IllegalArgumentException("location must not have a null world!");

        T entity = world.spawn(location, entityClass);
        beforeSpawn.accept(entity);
        return entity;
    }
}
