package com.SirBlobman.api.nms.boss.bar;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.apache.commons.lang.Validate;

public abstract class BossBarWrapper {
    private final UUID playerId;
    public BossBarWrapper(Player player) {
        Validate.notNull(player, "player must not be null!");
        this.playerId = player.getUniqueId();
    }
    
    public final Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }
    
    public final void removeAllPlayers() {
        List<Player> playerList = getPlayers();
        playerList.forEach(this::removePlayer);
    }
    
    public abstract String getTitle();
    public abstract void setTitle(String title);
    
    public abstract Object getColor();
    public abstract void setColor(String color);
    
    public abstract Object getStyle();
    public abstract void setStyle(String style);
    
    public abstract void removeFlag(String flag);
    public abstract void addFlag(String flag);
    public abstract boolean hasFlag(String flag);
    
    public abstract void setProgress(double progress);
    public abstract double getProgress();
    
    public abstract void addPlayer(Player player);
    public abstract void removePlayer(Player player);
    public abstract List<Player> getPlayers();
    
    public abstract void setVisible(boolean visible);
    public abstract boolean isVisible();
}