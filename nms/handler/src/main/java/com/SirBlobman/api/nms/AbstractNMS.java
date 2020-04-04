package com.SirBlobman.api.nms;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;
import com.SirBlobman.api.nms.boss.bar.BossBarHandler_BossBarAPI;
import com.SirBlobman.api.nms.boss.bar.BossBarHandler_Spigot;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractNMS {
    private final JavaPlugin plugin;
    private BossBarHandler bossBarHandler;
    public AbstractNMS(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public final JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public final BossBarHandler getBossBarHandler() {
        if(this.bossBarHandler != null) return this.bossBarHandler;
        
        JavaPlugin plugin = getPlugin();
        int minorVersion = VersionUtil.getMinorVersion();
        
        this.bossBarHandler = (minorVersion < 9 ? new BossBarHandler_BossBarAPI(plugin) : new BossBarHandler_Spigot(plugin));
        return getBossBarHandler();
    }
    
    public abstract PlayerHandler getPlayerHandler();
    public abstract EntityHandler getEntityHandler();
    public abstract ScoreboardHandler getScoreboardHandler();
    public abstract ItemHandler getItemHandler();
}