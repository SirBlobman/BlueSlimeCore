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

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;

import com.github.sirblobman.api.utility.Validate;

import com.google.gson.JsonObject;

public class EntityHandler_1_15_R1 extends EntityHandler {
    public EntityHandler_1_15_R1(JavaPlugin plugin) {
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
        if(entity instanceof CraftEntity) {
            CraftEntity craftEntity = (CraftEntity) entity;
            net.minecraft.server.v1_15_R1.Entity nmsEntity = craftEntity.getHandle();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("text", text);

            IChatBaseComponent chatComponent = ChatSerializer.a(jsonObject);
            nmsEntity.setCustomName(chatComponent);
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
        if(attribute != null) attribute.setBaseValue(maxHealth);
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
