package com.github.sirblobman.api.nms;

import java.util.function.Consumer;
import java.util.logging.Logger;

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

import net.kyori.adventure.text.Component;

public final class EntityHandler_Paper extends EntityHandler {
    public EntityHandler_Paper(@NotNull JavaPlugin plugin) {
        super(plugin);
        Logger logger = getLogger();
        logger.info("Using non-NMS Paper EntityHandler");
    }

    @Override
    public @NotNull String getName(@NotNull Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            return player.getName();
        }

        String customName = entity.getCustomName();
        return (customName != null ? customName : entity.getName());
    }

    @Override
    public void setCustomNameTextOnly(@NotNull Entity entity, String text, boolean visible) {
        Component paperName = Component.text(text);
        entity.customName(paperName);
    }

    @Override
    public double getMaxHealth(@NotNull LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) {
            return entity.getHealth();
        }

        return attribute.getValue();
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
        Validate.notNull(world, "location must have a valid world.");
        return world.spawn(location, entityClass, beforeSpawn::accept);
    }
}
