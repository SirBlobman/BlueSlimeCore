package com.github.sirblobman.api.nms;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

import net.minecraft.network.chat.Component;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;

public final class EntityHandler_1_20_R4 extends EntityHandler {
    public EntityHandler_1_20_R4(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull String getName(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            return player.getName();
        }

        String customName = entity.getCustomName();
        return (customName != null ? customName : entity.getName());
    }

    @Override
    public void setCustomNameTextOnly(@NotNull Entity entity, String text, boolean visible) {
        if (!(entity instanceof CraftEntity craftEntity)) {
            return;
        }

        net.minecraft.world.entity.Entity nmsEntity = craftEntity.getHandle();
        Component component = Component.literal(text);
        nmsEntity.setCustomName(component);
        nmsEntity.setCustomNameVisible(visible);
    }

    @Override
    public double getMaxHealth(@NotNull LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            return attribute.getValue();
        }

        return 0.0D;
    }

    @Override
    public void setMaxHealth(@NotNull LivingEntity entity, double maxHealth) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(maxHealth);
        }
    }

    @Override
    public <T extends Entity> @NotNull T spawnEntity(@NotNull Location location, @NotNull Class<T> entityClass,
                                                     @NotNull Consumer<T> beforeSpawn) {
        World world = location.getWorld();
        Validate.notNull(world, "location must have a valid world!");
        return world.spawn(location, entityClass, beforeSpawn);
    }
}
