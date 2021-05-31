package com.github.sirblobman.api.nms.bossbar;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;
import org.inventivetalent.bossbar.BossBarAPI.Color;
import org.inventivetalent.bossbar.BossBarAPI.Style;

@SuppressWarnings("deprecation")
public class BossBarWrapper_BossBarAPI extends BossBarWrapper {
    private BossBar bossBar;
    public BossBarWrapper_BossBarAPI(Player player) {
        super(player);
        this.bossBar = null;
    }
    
    @Override
    public String getTitle() {
        BossBar bossBar = getBossBar(false);
        return (bossBar == null ? "N/A" : bossBar.getMessage());
    }
    
    @Override
    public void setTitle(String title) {
        BossBar bossBar = getBossBar(true);
        if(bossBar != null) bossBar.setMessage(title);
    }
    
    @Override
    public Color getColor() {
        return Color.BLUE;
    }
    
    @Override
    public void setColor(String color) {
        // Do Nothing
    }
    
    @Override
    public Style getStyle() {
        return Style.PROGRESS;
    }
    
    @Override
    public void setStyle(String style) {
        // Do Nothing
    }
    
    @Override
    public void removeFlag(String flag) {
        // Do Nothing
    }
    
    @Override
    public void addFlag(String flag) {
        // Do Nothing
    }
    
    @Override
    public boolean hasFlag(String flag) {
        return false;
    }
    
    @Override
    public void setProgress(double progress) {
        BossBar bossBar = getBossBar(true);
        if(bossBar != null) {
            float floatProgress = (float) progress;
            bossBar.setProgress(floatProgress);
        }
    }
    
    @Override
    public double getProgress() {
        BossBar bossBar = getBossBar(false);
        return (bossBar == null ? 0.0D : 1.0D);
    }
    
    @Override
    public void addPlayer(Player player) {
        setVisible(true);
    }
    
    @Override
    public void removePlayer(Player player) {
        setVisible(false);
    }
    
    @Override
    public List<Player> getPlayers() {
        Player player = getPlayer();
        return (player == null ? Collections.emptyList() : Collections.singletonList(player));
    }
    
    @Override
    public void setVisible(boolean visible) {
        BossBar bossBar = getBossBar(visible);
        if(bossBar != null) bossBar.setVisible(visible);
    }
    
    @Override
    public boolean isVisible() {
        BossBar bossBar = getBossBar(false);
        return (bossBar != null && bossBar.isVisible());
    }

    private BossBar getBossBar(boolean create) {
        if(this.bossBar != null || !create) {
            return this.bossBar;
        }

        Player player = getPlayer();
        if(player == null) return null;

        BossBarAPI.setMessage(player, "", 1.0F);
        return (this.bossBar = BossBarAPI.getBossBar(player));
    }
}
