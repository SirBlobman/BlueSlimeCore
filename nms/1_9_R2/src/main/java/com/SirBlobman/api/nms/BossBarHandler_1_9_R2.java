package com.SirBlobman.api.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;
import com.SirBlobman.api.nms.boss.bar.BossBarWrapper;
import com.SirBlobman.api.nms.boss.bar.BossBarWrapper_Spigot;

import org.bukkit.entity.Player;

public class BossBarHandler_1_9_R2 extends BossBarHandler {
    private final Map<UUID, BossBarWrapper> playerToBossBarMap = new HashMap<>();
    
    @Override
    public BossBarWrapper getBossBar(Player player) {
        if(player == null) return null;
        
        UUID uuid = player.getUniqueId();
        BossBarWrapper wrapper = this.playerToBossBarMap.getOrDefault(uuid, null);
        if(wrapper != null) return wrapper;
    
        wrapper = new BossBarWrapper_Spigot(player);
        this.playerToBossBarMap.put(uuid, wrapper);
        return wrapper;
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
    
    @Override
    public void removeBossBar(Player player) {
        BossBarWrapper wrapper = getBossBar(player);
        wrapper.removePlayer(player);
        
        wrapper.setVisible(false);
    }
}