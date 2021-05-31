package com.github.sirblobman.api.nms.bossbar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BossBarHandler_BossBarAPI extends BossBarHandler {
    private final Map<UUID, BossBarWrapper> bossBarMap;
    public BossBarHandler_BossBarAPI(JavaPlugin plugin) {
        super(plugin);
        this.bossBarMap = new HashMap<>();
    }
    
    @Override
    public BossBarWrapper getBossBar(Player player) {
        if(player == null) return null;
        
        UUID uuid = player.getUniqueId();
        BossBarWrapper wrapper = this.bossBarMap.getOrDefault(uuid, null);
        if(wrapper != null) return wrapper;
    
        PluginManager manager = Bukkit.getPluginManager();
        if(manager.isPluginEnabled("BossBarAPI")) {
            BossBarWrapper apiWrapper = new BossBarWrapper_BossBarAPI(player);
            this.bossBarMap.put(uuid, apiWrapper);
            return apiWrapper;
        }
        
        Logger logger = getPlugin().getLogger();
        logger.warning("BossBarAPI is not installed, sending boss bar for '" + player.getName() + "' to chat.");
        
        BossBarWrapper fallbackWrapper = new BossBarWrapper_Fallback(player);
        this.bossBarMap.put(uuid, fallbackWrapper);
        return fallbackWrapper;
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
        wrapper.setTitle(message);
        wrapper.setProgress(progress);
        wrapper.setColor(color);
        wrapper.setStyle(style);
        wrapper.setVisible(true);
    }
}
