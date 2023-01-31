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

import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;

import com.github.sirblobman.api.utility.Validate;

public final class EntityHandler_1_19_R1 extends EntityHandler {
    public EntityHandler_1_19_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getName(Entity entity) {
        if (entity instanceof Player player) {
            return player.getName();
        }

        String entityName = entity.getName();
        String customName = entity.getCustomName();
        return (customName == null ? entityName : customName);
    }

    @Override
    public void setCustomNameTextOnly(Entity entity, String text, boolean visible) {
        if (entity instanceof CraftEntity craftEntity) {
            net.minecraft.world.entity.Entity nmsEntity = craftEntity.getHandle();
            Component textComponent = Component.literal(text);
            nmsEntity.setCustomName(textComponent);
            nmsEntity.setCustomNameVisible(visible);
        }
    }

    @Override
    public double getMaxHealth(LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        return (attribute == null ? 0.0D : attribute.getValue());
    }

    @Override
    public void setMaxHealth(LivingEntity entity, double maxHealth) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(maxHealth);
        }
    }

    @Override
    public <T extends Entity> T spawnEntity(Location location, Class<T> entityClass, Consumer<T> beforeSpawn) {
        Validate.notNull(location, "location must not be null!");
        Validate.notNull(entityClass, "entityClass must not be null!");

        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("location must have a valid bukkit world!");
        }

        return world.spawn(location, entityClass, beforeSpawn::accept);
    }
}
