package com.github.sirblobman.api.nms.bossbar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BossBarHandler_Spigot extends BossBarHandler {
    private final Map<UUID, BossBarWrapper> bossBarMap;
    public BossBarHandler_Spigot(JavaPlugin plugin) {
        super(plugin);
        this.bossBarMap = new HashMap<>();
    }
    
    @Override
    public BossBarWrapper getBossBar(Player player) {
        if(player == null) return null;
        
        UUID uuid = player.getUniqueId();
        BossBarWrapper wrapper = this.bossBarMap.getOrDefault(uuid, null);
        if(wrapper != null) return wrapper;
        
        BossBarWrapper spigotWrapper = new BossBarWrapper_Spigot(player);
        this.bossBarMap.put(uuid, spigotWrapper);
        return spigotWrapper;
    }
    
    @Override
    public void removeBossBar(Player player) {
        BossBarWrapper wrapper = getBossBar(player);
        wrapper.setVisible(false);
        wrapper.removePlayer(player);
    }
    
    @Override
    public void updateBossBar(Player player, String message, double progress, String color, String style) {
        BossBarWrapper wrapper = getBossBar(player);
        wrapper.addPlayer(player);
        
        wrapper.setTitle(message);
        wrapper.setProgress(progress);
        wrapper.setColor(color);
        wrapper.setStyle(style);
        
        wrapper.setVisible(true);
    }
}