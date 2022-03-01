package com.github.sirblobman.bossbar.legacy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.bossbar.legacy.reflection.Reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BossBarAPI {
    private static final Map<UUID, BossBar> barMap;
    
    static {
        barMap = new ConcurrentHashMap<>();
    }
    
    public static void setMessage(Player player, String message) {
        setMessage(player, message, 100.0F);
    }
    
    public static void setMessage(Player player, String message, final float percentage) {
        setMessage(player, message, percentage, 0.0F);
    }
    
    public static void setMessage(Player player, String message, float percentage, float minHealth) {
        UUID playerId = player.getUniqueId();
        if(!barMap.containsKey(playerId)) {
            barMap.put(playerId, new BossBar(player, message, percentage, minHealth));
        }
        
        BossBar bossBar = BossBarAPI.barMap.get(playerId);
        if(!bossBar.message.equals(message)) {
            bossBar.setMessage(message);
        }
        
        float maxHealth = bossBar.getMaxHealth();
        float newHealth = ((percentage / 100.0F) * maxHealth);
        if(bossBar.health != newHealth) {
            bossBar.setHealth(percentage);
        }
        
        if(!bossBar.isVisible()) {
            bossBar.setVisible(true);
        }
    }
    
    public static String getMessage(Player player) {
        BossBar bossBar = getBossBar(player);
        return (bossBar == null ? null : bossBar.getMessage());
    }
    
    public static boolean hasBar(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        return barMap.containsKey(uuid);
    }
    
    public static void removeBar(@NotNull Player player) {
        BossBar bossBar = getBossBar(player);
        if(bossBar == null) return;
        
        UUID uuid = player.getUniqueId();
        bossBar.setVisible(false);
        barMap.remove(uuid);
    }
    
    public static void setHealth(@NotNull Player player, float percentage) {
        BossBar bossBar = getBossBar(player);
        if(bossBar != null) {
            bossBar.setHealth(percentage);
        }
    }
    
    public static float getHealth(@NotNull Player player) {
        BossBar bossBar = getBossBar(player);
        return (bossBar == null ? -1.0F : bossBar.getHealth());
    }
    
    @Nullable
    public static BossBar getBossBar(@Nullable Player player) {
        if(player == null) return null;
        
        UUID uuid = player.getUniqueId();
        return barMap.getOrDefault(uuid, null);
    }
    
    public static Collection<BossBar> getBossBars() {
        Collection<BossBar> values = barMap.values();
        return Collections.unmodifiableCollection(values);
    }
    
    protected static void sendPacket(@NotNull Player player, @NotNull Object packet) {
        Validate.notNull(player, "player must not be null!");
        Validate.notNull(packet, "packet must not be null!");
        
        try {
            Object handle = Reflection.getHandle(player);
            if(handle == null) throw new IllegalStateException("handle is null!");
            
            Class<?> handleClass = handle.getClass();
            Field field_playerConnection = Reflection.getField(handleClass, "playerConnection");
            if(field_playerConnection == null) throw new IllegalStateException("field_playerConnection is null!");
            
            Object playerConnection = field_playerConnection.get(handle);
            Class<?> playerConnectionClass = playerConnection.getClass();
            Class<?> packetClass = Reflection.getNMSClass("Packet");
            
            Method method_sendPacket = Reflection.getMethod(playerConnectionClass, "sendPacket", packetClass);
            if(method_sendPacket == null) throw new IllegalStateException("method_sendPacket is null!");
            
            method_sendPacket.invoke(playerConnection, packet);
        } catch(Exception ignored) {
        }
    }
}
