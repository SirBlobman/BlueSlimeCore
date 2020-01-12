package com.SirBlobman.api.nms.boss.bar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.SirBlobman.api.nms.NMS_Handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class BossBarHandler_1_7_R4 extends BossBarHandler {
    private final Map<UUID, BossBarWrapper> playerToBossBarMap = new HashMap<>();
    
    @Override
    public void updateBossBar(Player player, String message, double progress, String color, String style) {
        PluginManager manager = Bukkit.getPluginManager();
        if(!manager.isPluginEnabled("BossBarAPI")) {
            NMS_Handler handler = NMS_Handler.getFallbackHandler();
            if(handler == null) return;
            
            BossBarHandler bossBarHandler = handler.getBossBarHandler();
            bossBarHandler.updateBossBar(player, message, progress, color, style);
            return;
        }
    
        BossBarWrapper bossBar = getBossBar(player);
        bossBar.setColor(color);
        bossBar.setStyle(style);
        
        bossBar.setVisible(true);
        bossBar.addPlayer(player);
    }
    
    @Override
    public void removeBossBar(Player player) {
        PluginManager manager = Bukkit.getPluginManager();
        if(!manager.isPluginEnabled("BossBarAPI")) {
            NMS_Handler handler = NMS_Handler.getFallbackHandler();
            if(handler == null) return;
        
            BossBarHandler bossBarHandler = handler.getBossBarHandler();
            bossBarHandler.removeBossBar(player);
            return;
        }
    
        BossBarWrapper bossBar = getBossBar(player);
        bossBar.setVisible(false);
        bossBar.removePlayer(player);
    }
    
    @Override
    public BossBarWrapper getBossBar(Player player) {
        if(player == null) return null;
        
        PluginManager manager = Bukkit.getPluginManager();
        if(!manager.isPluginEnabled("BossBarAPI")) {
            NMS_Handler handler = NMS_Handler.getFallbackHandler();
            if(handler == null) return null;
        
            BossBarHandler bossBarHandler = handler.getBossBarHandler();
            return bossBarHandler.getBossBar(player);
        }
        
        UUID uuid = player.getUniqueId();
        BossBarWrapper wrapper = this.playerToBossBarMap.getOrDefault(uuid, null);
        if(wrapper != null) return wrapper;
        
        wrapper = new BossBarWrapper_BossBarAPI(player);
        this.playerToBossBarMap.put(uuid, wrapper);
        return wrapper;
    }
}