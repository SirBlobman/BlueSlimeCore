package com.SirBlobman.api.nms.bossbar;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarWrapper_Spigot extends BossBarWrapper {
    private final BossBar bossBar;
    public BossBarWrapper_Spigot(Player player) {
        super(player);
        this.bossBar = Bukkit.createBossBar("Boss Bar", BarColor.BLUE, BarStyle.SOLID);
    }
    
    @Override
    public String getTitle() {
        return this.bossBar.getTitle();
    }
    
    @Override
    public void setTitle(String title) {
        this.bossBar.setTitle(title);
    }
    
    @Override
    public BarColor getColor() {
        return this.bossBar.getColor();
    }
    
    @Override
    public void setColor(String color) {
        BarColor barColor = parseColor(color);
        this.bossBar.setColor(barColor);
    }
    
    @Override
    public BarStyle getStyle() {
        return this.bossBar.getStyle();
    }
    
    @Override
    public void setStyle(String style) {
        BarStyle barStyle = parseStyle(style);
        this.bossBar.setStyle(barStyle);
    }
    
    @Override
    public void removeFlag(String flag) {
        BarFlag barFlag = parseFlag(flag);
        this.bossBar.removeFlag(barFlag);
    }
    
    @Override
    public void addFlag(String flag) {
        BarFlag barFlag = parseFlag(flag);
        this.bossBar.addFlag(barFlag);
    }
    
    @Override
    public boolean hasFlag(String flag) {
        BarFlag barFlag = parseFlag(flag);
        return this.bossBar.hasFlag(barFlag);
    }
    
    @Override
    public void setProgress(double progress) {
        this.bossBar.setProgress(progress);
    }
    
    @Override
    public double getProgress() {
        return this.bossBar.getProgress();
    }
    
    @Override
    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }
    
    @Override
    public void removePlayer(Player player) {
        this.bossBar.removePlayer(player);
    }
    
    @Override
    public List<Player> getPlayers() {
        return this.bossBar.getPlayers();
    }
    
    @Override
    public void setVisible(boolean visible) {
        this.bossBar.setVisible(visible);
    }
    
    @Override
    public boolean isVisible() {
        return this.bossBar.isVisible();
    }
    
    private BarColor parseColor(String color) {
        try {
            if(color == null) throw new IllegalArgumentException("color must not be null!");
            String upper = color.toUpperCase();
            return BarColor.valueOf(upper);
        } catch(IllegalArgumentException ex) {
            return BarColor.BLUE;
        }
    }
    
    private BarStyle parseStyle(String style) {
        try {
            if(style == null) throw new IllegalArgumentException("style must not be null!");
            String upper = style.toUpperCase();
            return BarStyle.valueOf(upper);
        } catch(IllegalArgumentException ex) {
            return BarStyle.SOLID;
        }
    }
    
    private BarFlag parseFlag(String flag) {
        try {
            if(flag == null) throw new IllegalArgumentException("flag must not be null!");
            String upper = flag.toUpperCase();
            return BarFlag.valueOf(upper);
        } catch(IllegalArgumentException ex) {
            return BarFlag.CREATE_FOG;
        }
    }
}